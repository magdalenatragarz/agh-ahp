package rating;

import exceptions.UnsupportedFileFormatException;
import matlabcontrol.*;
import objects.AHPObject;

import java.util.LinkedList;
import java.util.List;

abstract public class Method {

    String name;

    List<Double> rating;
    AHPObject ahpObject;

    public Method(AHPObject ahpObject) {
        this.ahpObject = ahpObject;
    }


    abstract List<Double> priorityVectorGenerator(List<Double> pairwiseMatrix) throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException;


    MatlabProxy connect() throws matlabcontrol.MatlabConnectionException {
        MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
                .setUsePreviouslyControlledSession(true)
                .setHidden(true)
                .setMatlabLocation(null).build();
        MatlabProxyFactory factory = new MatlabProxyFactory(options);
        return factory.getProxy();
    }

    List<Double> normalizer(List<java.lang.Double> priorityVector) {
        List<java.lang.Double> normalizedPriorityVector = new LinkedList<>();
        List<java.lang.Double> buffer = new LinkedList<>();
        double sum = 0, doubleBuf;
        for (java.lang.Double value : priorityVector) {
            if (value < 0) {
                buffer.add(value * (-1));
            } else {
                buffer.add(value);
            }
        }
        for (java.lang.Double value : buffer) {
            sum = sum + value;
        }
        for (java.lang.Double value : buffer) {
            doubleBuf = value / sum;
            normalizedPriorityVector.add(doubleBuf);

        }
        return normalizedPriorityVector;
    }


    String listToMatrixString(List<Double> pairwiseMatrix, String name) {
        StringBuilder matrixString = new StringBuilder();
        matrixString.append(name + "=[");
        int dimension = (int) Math.sqrt(pairwiseMatrix.size());
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
}
