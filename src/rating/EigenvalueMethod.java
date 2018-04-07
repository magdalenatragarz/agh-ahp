package rating;

import exceptions.UnsupportedFileFormatException;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.extensions.MatlabTypeConverter;
import objects.AHPObject;
import objects.Criterion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class EigenvalueMethod extends Method {


    public EigenvalueMethod(AHPObject ahpObject) {
        super(ahpObject);
    }


    public void setRating() {
        try {
            this.rating = createRating(convertToPriorityVectorForm().getGoal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<Double> createRating(CriterionConversion criterion) {
        List<Double> vector;
        double buffer;
        List<Double> rating = new LinkedList<>();
        if (criterion.getSubcriteria().size() != 0) {
            for (int i = 0; i < criterion.getSubcriteria().size(); i++) {
                vector = createRating(criterion.getSubcriteria().get(i));
                for (int j = 0; j < vector.size(); j++) {
                    buffer = vector.get(j) * criterion.getPriorityVector().get(i);
                    vector.remove(j);
                    vector.add(j, BigDecimal.valueOf(buffer).setScale(4, RoundingMode.HALF_UP).doubleValue());
                }
                if (i == 0) {
                    rating = vector;
                } else {
                    for (int j = 0; j < rating.size(); j++) {
                        buffer = rating.get(j) + vector.get(j);
                        rating.remove(j);
                        rating.add(j, BigDecimal.valueOf(buffer).setScale(4, RoundingMode.HALF_UP).doubleValue());
                    }
                }
            }
        } else {
            return criterion.getPriorityVector();
        }
        return normalizer(rating);
    }


    private List<Double> priorityVectorGenerator(List<Double> pairwiseMatrix) throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException {
        int dimension = (int) Math.sqrt(pairwiseMatrix.size());
        MatlabProxy proxy = connect();
        MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
        proxy.eval(listToMatrixString(pairwiseMatrix, "pairwiseMatrix"));
        proxy.eval("[priorityVectorMatrix,eigenvalueMatrix] = eig(pairwiseMatrix)");
        List<Double> priorityVector = new LinkedList<>();
        double[][] priorityVectorMatrix = new double[dimension][dimension];
        List<Double> eigenvalues = new LinkedList<>();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                priorityVectorMatrix[i][j] = processor.getNumericArray("priorityVectorMatrix").getRealValue(i, j);
            }

            eigenvalues.add(BigDecimal.valueOf(processor.getNumericArray("eigenvalueMatrix").getRealValue(i, i)).setScale(4, RoundingMode.HALF_UP).doubleValue());
        }
        for (int i = 0; i < dimension; i++) {
            if (Collections.max(eigenvalues).equals(eigenvalues.get(i))) {
                for (int j = 0; j < dimension; j++) {
                    priorityVector.add(priorityVectorMatrix[j][i]);
                }
            }
        }
        proxy.disconnect();
        return normalizer(priorityVector);
    }


    private AHPObjectConversion convertToPriorityVectorForm() throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException {
        AHPObjectConversion ahpConverted = new AHPObjectConversion();
        ahpConverted.setAlternatives(ahpObject.getAlternatives());
        Criterion goal = ahpObject.getGoal();
        ahpConverted.setGoal(convertMatrices(goal));
        return ahpConverted;
    }


    private CriterionConversion convertMatrices(Criterion criterion) throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException {
        CriterionConversion convertedCriterion = new CriterionConversion(criterion.getName(), priorityVectorGenerator(criterion.getMatrix()), new LinkedList<>());
        convertedCriterion.setName(criterion.getName());
        convertedCriterion.setPriorityVector(priorityVectorGenerator(criterion.getMatrix()));
        if (criterion.getSubcriteria() != null) {
            for (int i = 0; i < criterion.getSubcriteria().size(); i++) {
                convertedCriterion.getSubcriteria().add(convertMatrices(criterion.getSubcriteria().get(i)));
            }
        }
        return convertedCriterion;
    }


}
