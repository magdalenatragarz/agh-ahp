package objects;

import java.util.List;

public class AHPObject {

    private List<String> alternatives;

    private Criterion goal;


    public Criterion getGoal() {
        return goal;
    }

    public List<String> getAlternatives() {

        return alternatives;
    }

    public AHPObject(List<String> alternatives, Criterion goal) {
        this.alternatives = alternatives;
        this.goal = goal;
    }



    @Override
    public String toString() {
        StringBuilder tree = new StringBuilder();
        if((alternatives!=null && goal!=null)) {
            tree.append("Alternatives: ");
            for (String a : alternatives) {
                tree.append(a + ", ");
            }
            tree.append("\n" + goal.showCriteriaTree(1));
        }
        return tree.toString();
    }

}