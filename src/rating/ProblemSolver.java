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


    @Override
    public String toString() {
        StringBuilder ratingStringBuilder = new StringBuilder();
        
        ratingStringBuilder.append("Counted using " + method.name + "\n\n");
        ratingStringBuilder.append("Final Ranking:\n");

        for (int i = 0; i < method.rating.size(); i++) {
            ratingStringBuilder.append("\t" + method.ahpObject.getAlternatives().get(i) + " - " + method.rating.get(i)+"\n");
        }

        return ratingStringBuilder.toString();
    }

    public void setRating() {
        try {
            method.rating = createRating(method.ahpObject.getGoal());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                }
                if (i == 0) {
                    rating = vector;
                } else {
                    for (int j = 0; j < rating.size(); j++) {
                        buffer = rating.get(j) + vector.get(j);
                        rating.remove(j);
                        rating.add(j, buffer);
                    }
                }
            }
        } else {
            return method.priorityVectorGenerator(criterion.getMatrix());
        }
        return rating;
    }
}
