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

    //final static public String name = "Eigenvector Method";

    public EigenvalueMethod(AHPObject ahpObject) {
        super(ahpObject);
        this.name = "Eigenvector Method";
    }




    public List<Double> priorityVectorGenerator(List<Double> pairwiseMatrix) throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException {
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
            eigenvalues.add(processor.getNumericArray("eigenvalueMatrix").getRealValue(i, i));
            //eigenvalues.add(BigDecimal.valueOf(processor.getNumericArray("eigenvalueMatrix").getRealValue(i, i)).setScale(4, RoundingMode.HALF_UP).doubleValue());
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








}
