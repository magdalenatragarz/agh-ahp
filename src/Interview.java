import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

class Interview {

    String lolololo;
    //test test test

    LinkedHashMap<String, Object> jsonOrderedMap = new LinkedHashMap<>();
    JSONObject treeFile = new JSONObject(); ///LinkedHashMap???
    JSONArray alternatives = new JSONArray();

    private ArrayList<String> alternativesList = new ArrayList<>();

    void interviewMe() {
        askForAlternatives();
        build();
    }

    void build() {
        //Gson gson = new Gson();

        try (FileWriter file = new FileWriter("C:\\Users\\Magda\\IdeaProjects\\AHP\\JSONExample.json")) {
            //treeFile = new JSONObject(jsonOrderedMap);
            file.write(treeFile.toJSONString());
            //file.write(gson.toJson(jsonOrderedMap));
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void askForAlternatives() {
        String alternativeBuffer;
        String firstPermission;
        int numberOfAternatives = -1;
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
            //jsonOrderedMap.put("alternatives",alternatives);
            treeFile.put("alternatives", alternatives);
            System.out.println("Does your problem have criterias? [y/n]");
            firstPermission = firstPermissionScanner.nextLine();
            while (!firstPermission.equals("y") && !firstPermission.equals("n")) {
                System.out.println("Type y if yes, n if no.");
                firstPermission = firstPermissionScanner.nextLine();
            }
            if (firstPermission.equals("y")) {
                JSONObject goal = new JSONObject();
                //jsonOrderedMap.put("Goal",lookforCriterias(goal));
                //jsonOrderedMap.put("Goal",lookforCriterias());
                treeFile.put("Goal", lookforCriterias(goal));
            } else {
                JSONArray compareAlternativesMatrix = createMatrix(alternativesList, 0, "null");
                //jsonOrderedMap.put("Goal",compareAlternativesMatrix);
                treeFile.put("Goal", compareAlternativesMatrix);
            }
        } else {
            System.out.println("Nothing to choose :(");
        }
    }


    JSONObject lookforCriterias(JSONObject parent) {
        ArrayList<String> buf = new ArrayList<>();

        //ArrayList<String> order=new ArrayList<>();
        //ArrayList<Object> objects = new ArrayList<>();

        LinkedHashMap<String, Object> parentBuffer = new LinkedHashMap<>();
        //parentBuffer.
        Scanner numberScanner = new Scanner(System.in);
        Scanner criteriaScanner = new Scanner(System.in);
        Scanner permissionScanner = new Scanner(System.in);

        String criteriaBuffer;
        int numberOfCriterias;
        String permission;

        System.out.println("How many criterias do you have?");
        numberOfCriterias = numberScanner.nextInt();
        if (numberOfCriterias > 0) {
            System.out.println("Enter all of the possible criterias, press ENTER after each.");
            for (int i = 0; i < numberOfCriterias; i++) {
                criteriaBuffer = criteriaScanner.nextLine();
                buf.add(criteriaBuffer);
            }
            parent.put("matrix", createMatrix(buf, 0, "null"));
            //order.add(0,"matrix");
            //objects.add(0,createMatrix(buf, 0, "null"));
            //parentBuffer.put("matrix", createMatrix(buf, 0, "null"));

            for (int j = 0; j < buf.size(); j++) {
                if (!buf.get(j).equals("")) {
                    System.out.println("Does criteria " + buf.get(j) + " has any subcriterias? [y/n]");
                    permission = permissionScanner.nextLine();
                    while (!permission.equals("y") && !permission.equals("n")) {
                        System.out.println("Type y if yes, n if no.");
                        permission = permissionScanner.nextLine();
                    }
                    if (permission.equals("n")) {
                        parent.put(buf.get(j) + " " + (j + 1), createMatrix(buf, 1, buf.get(j)));
                        //order.add(j+1,buf.get(j));
                        //objects.add(j+1,createMatrix(buf, 1, buf.get(j));
                        //parentBuffer.put(buf.get(j) + " " + (j + 1), createMatrix(buf, 1, buf.get(j)));

                    } else if (permission.equals("y")) {
                        JSONObject newSubrcriteria = new JSONObject();
                        parent.put(buf.get(j) + " " + (j + 1), lookforCriterias(newSubrcriteria));
                        //parentBuffer.put(buf.get(j) + " " + (j + 1), lookforCriterias());
                        //order.add(j+1,buf.get(j)+" " + (j + 1));
                        //objects.add(j+1,lookforCriterias());

                    }
                }
            }
        } else {
            JSONArray compareAlternativesMatrix = createMatrix(alternativesList, 0, "null");
            jsonOrderedMap.put("Goal", compareAlternativesMatrix);
            treeFile.put("goal", compareAlternativesMatrix);
        }
        //return new JSONObject(objects,order);
        return parent;
    }


    JSONArray createMatrix(ArrayList<String> buf, int mode, String nameOfLeaf) {
        JSONArray pairComparsionMatrix = new JSONArray();
        Scanner scanner = new Scanner(System.in);
        double asGoodAs;
        double[][] bufferedTab;
        int size;
        String communicate = " in terms of ";
        if (mode == 0) {
            size = buf.size();
            bufferedTab = new double[buf.size()][buf.size()];
        } else {
            size = alternativesList.size();
            bufferedTab = new double[alternativesList.size()][alternativesList.size()];
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i <= j) {
                    if (i == j) bufferedTab[j][j] = 1;
                } else {
                    if (mode == 0) {
                        System.out.println("How many times do you find " + buf.get(i) + " more important than " + buf.get(j) + "?");
                    } else {
                        System.out.println("How many times do you find " + alternativesList.get(i) + " better than " + alternativesList.get(j) + communicate + nameOfLeaf + " ?");
                    }
                    asGoodAs = scanner.nextDouble();
                    bufferedTab[i][j] = asGoodAs;
                    bufferedTab[j][i] = (1 / asGoodAs);
                }
            }
        }
        for (int k = 0; k < size; k++) {
            for (int l = 0; l < size; l++) {
                pairComparsionMatrix.add(bufferedTab[k][l]);
            }
        }


        return pairComparsionMatrix;
    }

}
