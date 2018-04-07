import deserializer.AHPDataReader;
import questionnaire.Interview;
import rating.EigenvalueMethod;
import rating.GeometricMeanMethod;

import java.lang.reflect.Member;
import rating.Method;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String path = "C:\\Users\\Magda\\Desktop\\proba.json";

        //Interview interview = new Interview();
        //interview.interviewMe(path);
        List<Double> dd = new LinkedList<>();
        dd.add(2.0);
        dd.add(2.0);
        dd.add(2.0);
        dd.add(4.0);
        dd.add(4.0);
        dd.add(4.0);
        dd.add(5.0);
        dd.add(5.0);
        dd.add(5.0);


        System.out.println("\n====================================================\n");

        AHPDataReader dataReader = new AHPDataReader(path);
        System.out.println(dataReader.ahpObject.toString());


        EigenvalueMethod eig = new EigenvalueMethod(dataReader.ahpObject);
        eig.setRating();
        System.out.println(eig.getRating().toString());

        GeometricMeanMethod ggg = new GeometricMeanMethod(dataReader.ahpObject);
        ggg.setRating();
        System.out.println(ggg.getRating());

        //x.interviewMe(path);
        //object.alternatives.add("xxx");
        //object.showAlternatives();
    }


}

