import java.util.List;

public class Criterion {


    private String name;
    private List<Double> matrix;
    private List<Criterion> subcriteria;

    public void setSubcriteria(List<Criterion> subcriteria) {
        this.subcriteria = subcriteria;
    }

    public Criterion(String name, List<Double> matrix, List<Criterion> subcriteria) {
        this.name = name;
        this.matrix = matrix;
        this.subcriteria = subcriteria;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMatrix(List<Double> matrix) {
        this.matrix = matrix;
    }

    public String showCriteriaTree() {
        StringBuilder criteriaTree = new StringBuilder();
        criteriaTree.append(name  + matrix.toString() + "\n");
        if (subcriteria != null) {
            for (Criterion c : subcriteria) {
                criteriaTree.append("\t"+c.showCriteriaTree());
            }
        }
        return criteriaTree.toString();
    }
}
