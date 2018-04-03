package rating;

import exceptions.UnsupportedFileFormatException;
import matlabcontrol.*;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;
import objects.AHPObject;

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

    private MatlabProxy connect() throws matlabcontrol.MatlabConnectionException{
        MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
                .setUsePreviouslyControlledSession(true)
                .setHidden(true)
                .setMatlabLocation(null).build();
        MatlabProxyFactory factory = new MatlabProxyFactory(options);
        return factory.getProxy();
    }

    private double[][] listToMatixConverter(List<Double> pairwiseMatrix) throws UnsupportedFileFormatException{
        double dimensionDouble = Math.sqrt(pairwiseMatrix.size());
        int dimensionInt = (int) Math.sqrt(pairwiseMatrix.size());
        int colsCounter = 0;
        int rowsCounter = 0;

        if(dimensionDouble==dimensionInt){
            double[] buffer = new double [dimensionInt];
            double [][] formattedPairwiseMatrix = new double [dimensionInt][dimensionInt];
            for (Double value:pairwiseMatrix) {
                buffer[colsCounter]=value;
                colsCounter++;
                if(colsCounter==dimensionInt){
                    colsCounter=0;
                    formattedPairwiseMatrix[rowsCounter]=buffer;
                    rowsCounter++;
                }
            }
            return formattedPairwiseMatrix;

        }else{
            throw new UnsupportedFileFormatException();
        }
    }

    public String listToMatrixString(List<Double> pairwiseMatrix ,String name){
        StringBuilder matrixString = new StringBuilder();
        matrixString.append(name+"=[");
        int dimension = (int) Math.sqrt(pairwiseMatrix.size());
        int dimensionCounter=0;
        for(int i=0;i<pairwiseMatrix.size();i++) {
            matrixString.append(pairwiseMatrix.get(i));
            if((i+1) % dimension==0 && i!=0 && i!=pairwiseMatrix.size()-1){
                matrixString.append(";");
            }else if(i!=pairwiseMatrix.size()-1){
                matrixString.append(",");
            }
        }
        matrixString.append("]");
        return matrixString.toString();
    }

    public double[] priorityVectrorGenerator(List<Double> pairwiseMatrix) throws UnsupportedFileFormatException, MatlabConnectionException, MatlabInvocationException {

        int dimension = (int) Math.sqrt(pairwiseMatrix.size());
        double [][] formattedPairwiseMatrix = listToMatixConverter(pairwiseMatrix);
        MatlabProxy proxy = connect();
        MatlabTypeConverter processor = new MatlabTypeConverter(proxy);
        proxy.eval(listToMatrixString(pairwiseMatrix,"pairwiseMatrix"));
        //processor.setNumericArray("pairwiseMatrix", new MatlabNumericArray(formattedPairwiseMatrix, null));
        proxy.eval("priorityVector = eig(pairwiseMatrix)");
        double [] priorityVector = new double[dimension];
        for(int i=0;i<dimension;i++){
            processor.getNumericArray("priorityVector").getRealValue(i);
        }
        proxy.disconnect();
        return priorityVector;
    }



}
