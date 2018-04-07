import deserializer.AHPDataReader;
import questionnaire.Interview;
import rating.EigenvalueMethod;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String path = "C:\\Users\\Magda\\Desktop\\proba.json";

        //Interview interview = new Interview();
        //interview.interviewMe(path);

        System.out.println("\n====================================================\n");

        AHPDataReader dataReader = new AHPDataReader(path);
        System.out.println(dataReader.ahpObject.toString());


        EigenvalueMethod eig = new EigenvalueMethod(dataReader.ahpObject);
        eig.set();
        System.out.println(eig.getRating().toString());


        //x.interviewMe(path);
        //object.alternatives.add("xxx");
        //object.showAlternatives();
    }


}

