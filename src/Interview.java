import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

class Interview {
    JSONObject jo = new JSONObject();
    private ArrayList<String> alternatives = new ArrayList<>();
    private ArrayList<String> criterias = new ArrayList<>();

   void interview(){
      String buffer;


      //buffer = odczyt.nextLine();
      //System.out.println("Witaj "+buffer);

  }

  void introduction(){
      System.out.println("======Analytic Hierarchy Process======");
  }

  void askForAlternatives() {
      String alternativeBuffer;
      int numberOfAternatives;
      System.out.println("How many alternatives does solution of your problem have?");
      Scanner scanner = new Scanner(System.in);
      numberOfAternatives = scanner.nextInt();
      if (numberOfAternatives > 0) {
          System.out.println("Enter set of yours alternatives, press ENTER after each.");
          for (int i=0; i<=numberOfAternatives; i++) {
              alternativeBuffer = scanner.nextLine();
              alternatives.add(alternativeBuffer);
          }
      }else{
          System.out.println("Nothing to choose :(");
      }
  }

  void lookforCriterias(){
      String criteriaBuffer;
      int numberOfCriterias;

      System.out.println("How many criterias do you have?");
      Scanner scanner = new Scanner(System.in);
      numberOfCriterias = scanner.nextInt();
      if (numberOfCriterias>0){
          System.out.println("Enter set of criterias, press ENTER after each.");
          for (int i=0;i<=numberOfCriterias;i++){
              criteriaBuffer = scanner.nextLine();
              if(!criteriaBuffer.equals("")) {
                  criterias.add(criteriaBuffer);
                  lookForSubcriterias(criteriaBuffer);
              }
          }
      }
  }

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
                      criterias.add(subcriteriaBuffer);
                  }
              }
      }else{

      }
  }

      //=======help==========

    void show(){
      //String temp="";
        for (String temp:criterias){
          System.out.println(temp);
      }
    }







}
