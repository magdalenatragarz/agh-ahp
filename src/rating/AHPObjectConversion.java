package rating;

import java.util.List;

class AHPObjectConversion {

    private List<String> alternatives;
    private CriterionConversion goal;


    public CriterionConversion getGoal() {

        return goal;
    }

    public void setGoal(CriterionConversion goal) {
        this.goal = goal;
    }


    public void setAlternatives(List<String> alternatives) {

        this.alternatives = alternatives;
    }

    @Override
    public String toString() {
        StringBuilder tree = new StringBuilder();
        if ((alternatives != null && goal != null)) {
            tree.append("Alternatives: ");
            for (String a : alternatives) {
                tree.append(a + ", ");
            }
            tree.append("\n" + goal.showCriteriaTree(1));
        }
        return tree.toString();
    }

}

