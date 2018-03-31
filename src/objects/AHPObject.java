package objects;

import java.io.*;
import java.util.List;

public class AHPObject {

    private List<String> alternatives;

    private Criterion goal;


    public AHPObject(List<String> alternatives, Criterion goal) {
        this.alternatives = alternatives;
        this.goal = goal;
    }



    private static String readFromFile(String path) {
        StringBuilder jsonBuilder = new StringBuilder();
        String jsonBuffer;

        File jsonFile = new File(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            while ((jsonBuffer = br.readLine()) != null) {
                jsonBuilder.append(jsonBuffer);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonBuilder.toString().replace(" ","").replace("\t","").replace("\n","");
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