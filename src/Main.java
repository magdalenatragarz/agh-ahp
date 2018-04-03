import deserializer.AHPDataReader;
import rating.EigenvalueMethod;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String path ="C:\\Users\\Magda\\Desktop\\t.json";

        //AHPObject object=new AHPObject(null,null);

        System.out.println("\n====================================================\n");
        List<Double> x = new LinkedList<>();
        x.add(5.0);
        x.add(2.0);
        x.add(1.0);
        x.add(0.0);
        x.add(7.5);
        x.add(9.5);
        x.add(0.5);
        x.add(0.0);
        x.add(1.5);
        x.add(1.0);
        x.add(0.0);
        x.add(7.5);
        x.add(9.5);
        x.add(0.5);
        x.add(0.0);
        x.add(1.5);

        AHPDataReader dataReader = new AHPDataReader(path);

        System.out.println(dataReader.ahpObject.toString());

        EigenvalueMethod eig = new EigenvalueMethod(dataReader.ahpObject);
        System.out.println(eig.listToMatrixString(x,"lalala"));
        try{
            eig.priorityVectrorGenerator(x);
        }catch (Exception e){
            e.printStackTrace();
        }
        //Interview x =new Interview();
        //x.interviewMe(path);
        //object.alternatives.add("xxx");
        //object.showAlternatives();
    }




}

