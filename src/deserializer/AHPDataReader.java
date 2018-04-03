package deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import objects.AHPObject;

import java.io.*;

public class AHPDataReader {

    public AHPObject ahpObject;

    public AHPDataReader(String path) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(AHPObject.class, new OverallDeserializer())
                .create();
        ahpObject = new AHPObject(null,null);
        try {
            ahpObject = gson.fromJson(readFromFile(path), AHPObject.class);
        }catch (com.google.gson.JsonSyntaxException e){
            System.out.println("Unsupported file format");
        }

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
}
