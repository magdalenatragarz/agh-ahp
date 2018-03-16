import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

class Interview {

    static private ArrayList<String> alternatives = new ArrayList<>();

  static void interview(){
      String buffer;


      //buffer = odczyt.nextLine();
      //System.out.println("Witaj "+buffer);

  }

  static void introduction(){
      System.out.println("======Analytic Hierarchy Process======");
  }

  static void askForAlternatives() {
      String alternativeBuffer;
      int numberOfAternatives;
      System.out.println("How many alternatives does solution of your problem have?");
      Scanner scanner = new Scanner(System.in);
      numberOfAternatives = scanner.nextInt();
      if (numberOfAternatives > 0) {
          System.out.println("Enter set of yours alternatives, press ENTER after each.");
          for (int i = 0; i <= numberOfAternatives; i++) {
              alternativeBuffer = scanner.nextLine();
              alternatives.add(alternativeBuffer);
          }
      }
  }
      //=======help==========

    static void show(){
      //String temp="";
        for (String temp:alternatives){
          System.out.println(temp);
      }
    }







}
