package objects;

import java.util.List;

public class Criterion {


    private String name;

    public List<Double> getMatrix() {
        return matrix;
    }

    public String getName() {

        return name;
    }

    public List<Criterion> getSubcriteria() {
        return subcriteria;
    }

    private List<Double> matrix;
    private List<Criterion> subcriteria;

    public Criterion(String name, List<Double> matrix, List<Criterion> subcriteria) {
        this.name = name;
        this.matrix = matrix;
        this.subcriteria = subcriteria;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String showCriteriaTree(int depth) {
        StringBuilder criteriaTree = new StringBuilder();
        StringBuilder depthTabulators = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            depthTabulators.append("\t");
        }
        criteriaTree.append(name + matrix.toString() + "\n");
        if (subcriteria != null) {
            for (Criterion c : subcriteria) {
                criteriaTree.append(depthTabulators.toString() + c.showCriteriaTree(depth + 1));
            }
        }
        return criteriaTree.toString();
    }
}
