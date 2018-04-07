package rating;

import exceptions.UnsupportedFileFormatException;
import matlabcontrol.*;
import matlabcontrol.extensions.MatlabTypeConverter;
import objects.AHPObject;
import objects.Criterion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class EigenvalueMethod {

    private List<Double> rating;
    private AHPObject ahpObject;

    public EigenvalueMethod(AHPObject ahpObject) {
        this.ahpObject = ahpObject;
    }

    public List<Double> getRating() {
        return rating;
    }

    public void show() {
        try {
            System.out.println(convertToPriorityVectorForm().toString());
        } catch (UnsupportedFileFormatException e) {
            System.out.println("UnsupportedFileFormatException");
        } catch (MatlabConnectionException e) {
            System.out.println("MatlabConnectionException");
        } catch (MatlabInvocationException e) {
            //System.out.println("MatlabInvocationException");
            e.printStackTrace();
        }

    }


    private MatlabProxy connect() throws matlabcontrol.MatlabConnectionException {
        MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
                .setUsePreviouslyControlledSession(true)
                .setHidden(true)
                .setMatlabLocation(null).build();
        MatlabProxyFactory factory = new MatlabProxyFactory(options);
        return factory.getProxy();
    }

    private CriterionConversion convertMatrices(Criterion criterion) throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException {
        CriterionConversion convertedCriterion = new CriterionConversion(criterion.getName(), priorityVectrorGenerator(criterion.getMatrix()), new LinkedList<CriterionConversion>());
        convertedCriterion.setName(criterion.getName());
        convertedCriterion.setPriorityVector(priorityVectrorGenerator(criterion.getMatrix()));
        if (criterion.getSubcriteria() != null) {
            for (int i = 0; i < criterion.getSubcriteria().size(); i++) {
                convertedCriterion.getSubcriteria().add(convertMatrices(criterion.getSubcriteria().get(i)));
            }
        }
        return convertedCriterion;
    }

    AHPObjectConversion convertToPriorityVectorForm() throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException {
        AHPObjectConversion ahpConverted = new AHPObjectConversion();
        ahpConverted.setAlternatives(ahpObject.getAlternatives());
        Criterion goal = ahpObject.getGoal();
        ahpConverted.setGoal(convertMatrices(goal));
        return ahpConverted;
    }

    private double[][] listToMatixConverter(List<Double> pairwiseMatrix) throws UnsupportedFileFormatException {
        double dimensionDouble = Math.sqrt(pairwiseMatrix.size());
        int dimensionInt = (int) Math.sqrt(pairwiseMatrix.size());
        int colsCounter = 0;
        int rowsCounter = 0;

        if (dimensionDouble == dimensionInt) {
            double[] buffer = new double[dimensionInt];
            double[][] formattedPairwiseMatrix = new double[dimensionInt][dimensionInt];
            for (Double value : pairwiseMatrix) {
                buffer[colsCounter] = value;
                colsCounter++;
                if (colsCounter == dimensionInt) {
                    colsCounter = 0;
                    formattedPairwiseMatrix[rowsCounter] = buffer;
                    rowsCounter++;
                }
            }
            return formattedPairwiseMatrix;

        } else {
            throw new UnsupportedFileFormatException();
        }
    }

    public String listToMatrixString(List<Double> pairwiseMatrix, String name) {
        StringBuilder matrixString = new StringBuilder();
        matrixString.append(name + "=[");
        int dimension = (int) Math.sqrt(pairwiseMatrix.size());
        int dimensionCounter = 0;
        for (int i = 0; i < pairwiseMatrix.size(); i++) {
            matrixString.append(pairwiseMatrix.get(i));
            if ((i + 1) % dimension == 0 && i != 0 && i != pairwiseMatrix.size() - 1) {
                matrixString.append(";");
            } else if (i != pairwiseMatrix.size() - 1) {
                matrixString.append(",");
            }
        }
        matrixString.append("]");
        return matrixString.toString();
    }

    public List<String> createMatlabCondition(int dimension) {
        StringBuilder conditionBuilder = new StringBuilder();
        List<String> commands = new LinkedList<>();
        for (int j = 1; j <= dimension; j++) {
            conditionBuilder.append("if(max([");
            for (int i = 1; i <= dimension; i++) {
                conditionBuilder.append("eigenvalueMatrix(" + i + "," + i + ")");
                if (i < dimension) {
                    conditionBuilder.append(",");
                } else {
                    conditionBuilder.append("])");
                }
            }
            conditionBuilder.append("==eigenvalueMatrix(" + j + "," + j + ")) priorityVector=priorityVectorMatrix(:," + j + ")");
            commands.add(conditionBuilder.toString());
            conditionBuilder.setLength(0);
            if (j < dimension) {
                commands.add("else");
            }
        }
        for (int k = 0; k < dimension; k++) {
            commands.add("end");
        }
        return commands;
    }

    List<Double> priorityVectrorGenerator(List<Double> pairwiseMatrix) throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException {

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
                priorityVectorMatrix[i][j] = BigDecimal.valueOf(processor.getNumericArray("priorityVectorMatrix").getRealValue(i, j)).setScale(4, RoundingMode.HALF_UP).doubleValue();
            }
            eigenvalues.add(BigDecimal.valueOf(processor.getNumericArray("eigenvalueMatrix").getRealValue(i, i)).setScale(4, RoundingMode.HALF_UP).doubleValue());
        }
        for (int i = 0; i < dimension; i++) {
            if (Collections.max(eigenvalues) == eigenvalues.get(i)) {
                for (int j = 0; j < dimension; j++) {
                    priorityVector.add(priorityVectorMatrix[j][i]);
                }
            }
        }
        proxy.disconnect();
        return priorityVector;
    }


}
