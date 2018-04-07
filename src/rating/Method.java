package rating;

import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import objects.AHPObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

abstract public class Method {
    List<Double> rating;
    AHPObject ahpObject;

    public Method(AHPObject ahpObject) {
        this.ahpObject = ahpObject;
    }

    abstract List<Double> createRating(CriterionConversion criterion);

    public List<Double> getRating() {
        return rating;
    }

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
            normalizedPriorityVector.add(BigDecimal.valueOf(doubleBuf).setScale(4, RoundingMode.HALF_UP).doubleValue());
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
