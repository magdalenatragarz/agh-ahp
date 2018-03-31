import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deserializer.OverallDeserializer;
import objects.AHPObject;
import questionnaire.Interview;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        //String path = "D:\\Pobrane\\Holidays.json";
        String path ="C:\\Users\\Magda\\Desktop\\t.json";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(AHPObject.class, new OverallDeserializer())
                .create();
        //Interview x = new Interview();
        //x.interviewMe(path);
        AHPObject object=new AHPObject(null,null);
        //System.out.println(readFromFile(path));
        try {
            object = gson.fromJson(readFromFile(path), AHPObject.class);
        }catch (com.google.gson.JsonSyntaxException e){
            System.out.println("Unsupported file format");
        }

        System.out.println("\n====================================================\n");


        System.out.println(object.toString());


        //Interview x =new Interview();
        //x.interviewMe(path);
        //object.alternatives.add("xxx");
        //object.showAlternatives();
    }

    public static String readFromFile(String path) {
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


}

