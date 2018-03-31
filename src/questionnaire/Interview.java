package questionnaire;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Interview {

    private JsonObject treeFile = new JsonObject();
    private ArrayList<String> alternatives = new ArrayList<>();


    public void interviewMe(String path) {
        askForAlternatives();
        build(path);
    }

    private void build(String path) {
        Gson jsonBuilder = new Gson();

        try (FileWriter file = new FileWriter(path)) {
            file.write(jsonBuilder.toJson(treeFile));
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void askForAlternatives() {
        String alternativeBuffer;
        String firstPermission;
        int numberOfAternatives;
        JsonArray alternativesList = new JsonArray();
        Scanner firstPermissionScanner = new Scanner(System.in);

        System.out.println("How many alternatives does the solution of your problem have?");

        Scanner numberScanner = new Scanner(System.in);
        Scanner alternativesScanner = new Scanner(System.in);
        numberOfAternatives = numberScanner.nextInt();
        if (numberOfAternatives > 0) {
            System.out.println("Enter set of your alternatives, press ENTER after each.");
            for (int i = 0; i < numberOfAternatives; i++) {
                alternativeBuffer = alternativesScanner.nextLine();
                alternatives.add(alternativeBuffer);
                alternativesList.add(alternativeBuffer);
            }
            treeFile.add("alternatives", alternativesList);
            System.out.println("Does your problem have criteria? [y/n]");
            firstPermission = firstPermissionScanner.nextLine();
            while (!firstPermission.equals("y") && !firstPermission.equals("n")) {
                System.out.println("Type y if yes, n if no.");
                firstPermission = firstPermissionScanner.nextLine();
            }
            if (firstPermission.equals("y")) {
                treeFile.add("Goal", lookForCriteria(new JsonObject()));
            } else {
                treeFile.add("Goal", createMatrix(alternatives, 0, "null"));
            }
        } else {
            System.out.println("Nothing to choose :(");
            treeFile.add("alternatives",new JsonArray());
            treeFile.add("Goal",new JsonObject() );
        }
    }


    private JsonObject lookForCriteria(JsonObject parent) {
        ArrayList<String> buf = new ArrayList<>();

        Scanner numberScanner = new Scanner(System.in);
        Scanner criterionScanner = new Scanner(System.in);
        Scanner permissionScanner = new Scanner(System.in);

        String criterionBuffer;
        int numberOfCriteria;
        String permission;

        System.out.println("How many criteria do you have?");
        numberOfCriteria = numberScanner.nextInt();
        if (numberOfCriteria > 0) {
            System.out.println("Enter all of the possible criteria, press ENTER after each.");
            for (int i = 0; i < numberOfCriteria; i++) {
                criterionBuffer = criterionScanner.nextLine();
                buf.add(criterionBuffer);
            }
            parent.add("matrix", createMatrix(buf, 0, "null"));

            for (int j = 0; j < buf.size(); j++) {
                if (!buf.get(j).equals("")) {
                    System.out.println("Does criteria " + buf.get(j) + " has any subcriteria? [y/n]");
                    permission = permissionScanner.nextLine();
                    while (!permission.equals("y") && !permission.equals("n")) {
                        System.out.println("Type y if yes, n if no.");
                        permission = permissionScanner.nextLine();
                    }
                    if (permission.equals("n")) {
                        parent.add(buf.get(j), createMatrix(buf, 1, buf.get(j)));

                    } else if (permission.equals("y")) {
                        parent.add(buf.get(j), lookForCriteria(new JsonObject()));
                    }
                }
            }
        } else {
            treeFile.add("Goal", createMatrix(alternatives, 0, "null"));
        }
        return parent;
    }


    private JsonArray createMatrix(ArrayList<String> buf, int mode, String nameOfLeaf) {
        JsonArray pairwiseComparsionMatrix = new JsonArray();
        Scanner scanner = new Scanner(System.in);
        double asGoodAs;
        double[][] bufferedTab;
        int size;
        String communicate = " in terms of ";
        if (mode == 0) {
            size = buf.size();
            bufferedTab = new double[buf.size()][buf.size()];
        } else {
            size = alternatives.size();
            bufferedTab = new double[alternatives.size()][alternatives.size()];
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i <= j) {
                    if (i == j) bufferedTab[j][j] = 1;
                } else {
                    if (mode == 0) {
                        System.out.println("How many times do you find " + buf.get(i) + " more important than " + buf.get(j) + "?");
                    } else {
                        System.out.println("How many times do you find " + alternatives.get(i) + " better than " + alternatives.get(j) + communicate + nameOfLeaf + " ?");
                    }
                    asGoodAs = scanner.nextDouble();
                    bufferedTab[i][j] = asGoodAs;
                    bufferedTab[j][i] = (1 / asGoodAs);
                }
            }
        }
        for (int k = 0; k < size; k++) {
            for (int l = 0; l < size; l++) {
                pairwiseComparsionMatrix.add(bufferedTab[k][l]);
            }
        }
        return pairwiseComparsionMatrix;
    }

}
