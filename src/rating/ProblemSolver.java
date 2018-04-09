package rating;

import exceptions.UnsupportedFileFormatException;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import objects.Criterion;

import java.util.*;

public class ProblemSolver {


    Method method;

    public ProblemSolver(Method method) {
        this.method = method;
    }

    public List<Double> getRating() {
        return method.rating;
    }

    @Override
    public String toString() {
        StringBuilder ratingStringBuilder = new StringBuilder();

        int counter = 0;

        Map<Double, String> rating = new HashMap<>();
        for (int i = 0; i < method.rating.size(); i++) {
            rating.put(method.rating.get(i), method.ahpObject.getAlternatives().get(i));
        }
        Map<Double, String> sortedRating = new TreeMap<>(rating);
        ratingStringBuilder.append("Counted using " + method.name + "\n\n");
        ratingStringBuilder.append("Final Ranking:\n");
        for (Map.Entry<Double, String> entry : sortedRating.entrySet()) {
            ratingStringBuilder.append("\t" + (counter + 1) + ".\t" + entry.getValue() + " - " + entry.getKey() * 100 + "%\n");
            //ratingStringBuilder.append("\t"+(counter+1)+".\t"+entry.getValue()+" - "+BigDecimal.valueOf(entry.getKey()*100).setScale(4,RoundingMode.HALF_UP).doubleValue()+"%\n");
            counter++;
        }
        return ratingStringBuilder.toString();
    }

    public void setRating() {
        try {
            // method.rating = createRating(convertToPriorityVectorForm().getGoal());
            method.rating = createRating(method.ahpObject.getGoal());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Double> getOutputVector(double weight, List<Double> subVector, List<Double> finalVector) {
        // final vector has alternatives size
        for (int i = 0; i < subVector.size(); i++) {
            finalVector.set(i, finalVector.get(i) + (weight * subVector.get(i)));
            System.out.println(finalVector.toString());

        }
        return finalVector;
    }


    List<Double> createRating(Criterion criterion) throws UnsupportedFileFormatException, MatlabInvocationException, MatlabConnectionException {
        List<Double> vector;
        double buffer;
        List<Double> rating = new LinkedList<>();
        if (criterion.getSubcriteria() != null) {
            for (int i = 0; i < criterion.getSubcriteria().size(); i++) {
                vector = createRating(criterion.getSubcriteria().get(i));
                for (int j = 0; j < vector.size(); j++) {
                    buffer = vector.get(j) * method.priorityVectorGenerator(criterion.getMatrix()).get(i);
                    vector.remove(j);
                    vector.add(j, buffer);
                    //vector.add(j, BigDecimal.valueOf(buffer).setScale(4, RoundingMode.HALF_UP).doubleValue());
                }
                if (i == 0) {
                    rating = vector;
                } else {
                    for (int j = 0; j < rating.size(); j++) {
                        buffer = rating.get(j) + vector.get(j);
                        rating.remove(j);
                        rating.add(j, buffer);
                        //rating.add(j, BigDecimal.valueOf(buffer).setScale(4, RoundingMode.HALF_UP).doubleValue());
                    }
                }
            }
        } else {
            return method.priorityVectorGenerator(criterion.getMatrix());
        }
        return rating;
    }


/*

    List<Double> createRating(Criterion criterion) {

        //has children
        if (criterion.getSubcriteria() != null) {

            List<Double> rating = new LinkedList<>();

            System.out.println("SUBCRITERIA SIZE: "+criterion.getSubcriteria().size());
            for (int i = 0; i < criterion.getSubcriteria().size(); i++) rating.add(0.0);


            for (int i = 0; i < criterion.getSubcriteria().size(); i++) {
                try {
                    rating = getOutputVector(method.priorityVectorGenerator(criterion.getMatrix()).get(i), createRating(criterion.getSubcriteria().get(i)), rating);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return rating;
        } else {
            List<Double> x = null;
            try {
                x = method.priorityVectorGenerator(criterion.getMatrix());
            } catch (Exception e) {
                System.out.println("tu");
            }

            return x;

        }
    }*/
    }
