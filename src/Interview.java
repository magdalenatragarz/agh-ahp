import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Interview {

    JSONObject treeFile = new JSONObject();
    JSONArray alternatives = new JSONArray();

    private ArrayList<String> alternativesList = new ArrayList<>();

    void interviewMe(){
        askForAlternatives();
        build();
    }

    void build(){
        try (FileWriter file = new FileWriter("C:\\Users\\Magda\\IdeaProjects\\AHP\\JSONExample.json")) {
            file.write(treeFile.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    void askForAlternatives() {
      String alternativeBuffer;
      String firstPermission;
      int numberOfAternatives=-1;
      Scanner firstPermissionScanner = new Scanner(System.in);

      System.out.println("How many alternatives does the solution of your problem have?");

      Scanner numberScanner = new Scanner(System.in);
      Scanner alternativesScanner = new Scanner(System.in);
      /*while(!numberScanner.hasNext()){
          numberScanner.next();
          System.out.println("Enter valid data");
          numberOfAternatives = numberScanner.nextInt();
      }*/
      numberOfAternatives = numberScanner.nextInt();
      if (numberOfAternatives > 0) {
          System.out.println("Enter set of your alternatives, press ENTER after each.");
          for (int i=0; i<numberOfAternatives; i++) {
              alternativeBuffer = alternativesScanner.nextLine();
              alternatives.add(alternativeBuffer);
              alternativesList.add(alternativeBuffer);
          }
          treeFile.put("alternatives",alternatives);
          System.out.println("Does your problem have criterias? [y/n]");
          firstPermission = firstPermissionScanner.nextLine();
          while (!firstPermission.equals("y") && !firstPermission.equals("n")) {
              System.out.println("Type y if yes, n if no.");
              firstPermission = firstPermissionScanner.nextLine();
          }
          if(firstPermission.equals("y")) {
              JSONObject goal = new JSONObject();
              treeFile.put("goal",lookforCriterias(goal));
          }else{
              JSONArray compareAlternativesMatrix = createMatrix(alternativesList,0,"null");
              treeFile.put("goal",compareAlternativesMatrix);
          }
      }else{
          System.out.println("Nothing to choose :(");
      }
  }

  JSONObject lookforCriterias(JSONObject parent){
      ArrayList<String> buf = new ArrayList<>();

      JSONObject buffer = new JSONObject();

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
              parent.put("matrix",createMatrix(buf,0,"null"));

              for (int j = 0; j < buf.size(); j++) {
                  if (!buf.get(j).equals("")) {
                      System.out.println("Does criteria " + buf.get(j) + " has any subcriterias? [y/n]");
                      permission = permissionScanner.nextLine();
                      while (!permission.equals("y") && !permission.equals("n")) {
                          System.out.println("Type y if yes, n if no.");
                          permission = permissionScanner.nextLine();
                      }
                      if (permission.equals("n")) {
                          parent.put(buf.get(j)+" "+(j+1), createMatrix(buf,1,buf.get(j)));

                      } else if (permission.equals("y")) {
                          JSONObject newSubrcriteria = new JSONObject();
                          parent.put(buf.get(j)+" "+(j+1),lookforCriterias(newSubrcriteria));
                      }
                  }
              }
      }else{
              JSONArray compareAlternativesMatrix = createMatrix(alternativesList,0,"null");
              treeFile.put("goal",compareAlternativesMatrix);
      }
      return parent;
  }

  JSONArray createMatrix(ArrayList<String> buf,int mode,String nameOfLeaf) {
      JSONArray pairComparsionMatrix = new JSONArray();
      Scanner scanner = new Scanner(System.in);
      double asGoodAs;
      double [][] bufferedTab;
      int size;
      String communicate = " in terms of ";
      if (mode==0) {
          size=buf.size();
          bufferedTab = new double[buf.size()][buf.size()];
      }else {
          size = alternativesList.size();
          bufferedTab = new double[alternativesList.size()][alternativesList.size()];
      }

      for (int i = 0; i < size; i++) {
          for (int j = 0; j < size; j++) {
              if (i <= j) {
                  if (i == j) bufferedTab[j][j] = 1;
              } else {
                  if(mode==0) {
                      System.out.println("How many times do you find " + buf.get(i) + " more important than " + buf.get(j) + "?");
                  }else{
                      System.out.println("How many times do you find " + alternativesList.get(i) + " better than " + alternativesList.get(j)+communicate+nameOfLeaf+" ?");
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
