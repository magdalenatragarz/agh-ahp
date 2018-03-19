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
      int numberOfAternatives;
      Scanner firstPermissionScanner = new Scanner(System.in);


      System.out.println("How many alternatives does solution of your problem have?");

      Scanner numberScanner = new Scanner(System.in);
      Scanner alternativesScanner = new Scanner(System.in);
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
              //treeFile.put()
          }else{
              JSONArray compareAlternativesMatrix = createMatrix(alternativesList,0);
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
              System.out.println("Enter set of criterias, press ENTER after each.");
              for (int i = 0; i < numberOfCriterias; i++) {
                  criteriaBuffer = criteriaScanner.nextLine();
                  buf.add(criteriaBuffer);
              }
              parent.put("matrix",createMatrix(buf,0));

              for (int j = 0; j < buf.size(); j++) {
                  if (!buf.get(j).equals("")) {
                      System.out.println("Does criteria " + buf.get(j) + " has subcriteria? [y/n]");
                      permission = permissionScanner.nextLine();
                      while (!permission.equals("y") && !permission.equals("n")) {
                          System.out.println("Type y if yes, n if no.");
                          permission = permissionScanner.nextLine();
                      }
                      if (permission.equals("n")) {
                          parent.put(buf.get(j), createMatrix(buf,1));

                      } else if (permission.equals("y")) {
                          JSONObject newSubrcriteria = new JSONObject();
                          parent.put(buf.get(j),lookforCriterias(newSubrcriteria));

                      }
                  }
              }
      }else{

      }
      return parent;
  }

  /*JSONArray createLeafMatrix(){
      JSONArray leafMatrix = new JSONArray();
      Scanner scanner = new Scanner(System.in);
      double asGoodAs;
      double[][] bufferedTab = new double[alternativesList.size()][alternativesList.size()];



      return leafMatrix;
  }*/

  JSONArray createMatrix(ArrayList<String> buf,int mode) {
      JSONArray pairComparsionMatrix = new JSONArray();
      Scanner scanner = new Scanner(System.in);
      double asGoodAs;
      double [][] bufferedTab;
      int size;
      String communicate = " based on ";
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
                      System.out.println("How many times do you find " + alternativesList.get(i) + " more important than " + alternativesList.get(j)+communicate+" ?");
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

/*
  void lookForSubcriterias(String criteria){

      String permission;
      int numberOfSubcriteria;
      String subcriteriaBuffer;
      Scanner scanner = new Scanner(System.in);
      System.out.println("Does criteria "+criteria+" have subcriteria? [y/n]");
      permission = scanner.nextLine();
      if(permission.equals("y")){
          System.out.println("How many subcriterias of "+criteria+" do you have?");
              numberOfSubcriteria=scanner.nextInt();
              if (numberOfSubcriteria>0) {
                  System.out.println("Enter set of subcriterias of "+criteria+", press ENTER after each.");
                  for (int i = 0; i < numberOfSubcriteria; i++) {
                      subcriteriaBuffer = scanner.nextLine();
                      //criterias.add(subcriteriaBuffer);
                  }
              }
      }else{

      }
  }*/







}
