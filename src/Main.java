import deserializer.AHPDataReader;
import rating.EigenvalueMethod;
import rating.GeometricMeanMethod;
import rating.ProblemSolver;

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

        ProblemSolver solverEig = new ProblemSolver(new EigenvalueMethod(dataReader.ahpObject));
        ProblemSolver solverGeo = new ProblemSolver(new GeometricMeanMethod(dataReader.ahpObject));

        solverEig.setRating();
        System.out.println(solverEig.toString());

    }


}

