import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        String path = "C:\\Users\\Magda\\IdeaProjects\\AHP\\JSONExample.json";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(AHPObject.class, new OverallDeserializer())
                .create();

        AHPObject object;
        object = gson.fromJson(readFromFile(path), AHPObject.class);
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
        return jsonBuilder.toString();
    }


}

