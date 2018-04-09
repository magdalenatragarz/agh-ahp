package rating;

import exceptions.UnsupportedFileFormatException;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import objects.AHPObject;
import objects.Criterion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public class GeometricMeanMethod extends Method {

    //final static public String name = "Geometric Mean Method";

    public GeometricMeanMethod(AHPObject ahpObject) {
        super(ahpObject);
        this.name = "Geometric Mean Method";
    }


    public List<Double> priorityVectorGenerator(List<Double> pairwiseMatrix) throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException {
        int dimension = (int) Math.sqrt(pairwiseMatrix.size());
        List<Double> priorityVector = new LinkedList<>();
        double sum = 1;
        for (int i = 0; i < pairwiseMatrix.size(); i++) {
            sum = sum * pairwiseMatrix.get(i);
            if ((i + 1) % dimension == 0) {
                priorityVector.add(Math.pow(sum, (double) 1 / dimension));
                //priorityVector.add(BigDecimal.valueOf(Math.pow(sum, (double) 1 / dimension)).setScale(4, RoundingMode.HALF_UP).doubleValue());
                sum = 1;
            }
        }
        return normalizer(priorityVector);
    }
}
