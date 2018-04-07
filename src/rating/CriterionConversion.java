package rating;

import java.util.List;

public class CriterionConversion {
    private String name;
    private List<Double> priorityVector;
    private List<CriterionConversion> subcriteria;


    public List<Double> getPriorityVector() {
        return priorityVector;
    }

    public CriterionConversion(String name, List<Double> priorityVector, List<CriterionConversion> subcriteria) {
        this.name = name;
        this.priorityVector = priorityVector;
        this.subcriteria = subcriteria;
    }

    public void setPriorityVector(List<Double> priorityVector) {
        this.priorityVector = priorityVector;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<CriterionConversion> getSubcriteria() {
        return subcriteria;

    }

    public String showCriteriaTree(int depth) {
        StringBuilder criteriaTree = new StringBuilder();
        StringBuilder depthTabulators = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            depthTabulators.append("\t");
        }
        criteriaTree.append(name + priorityVector.toString() + "\n");
        if (subcriteria != null) {
            for (CriterionConversion c : subcriteria) {
                criteriaTree.append(depthTabulators.toString() + c.showCriteriaTree(depth + 1));
            }
        }
        return criteriaTree.toString();
    }
}


