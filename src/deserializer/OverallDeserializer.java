package deserializer;

import com.google.gson.*;
import objects.AHPObject;
import objects.Criterion;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class OverallDeserializer implements JsonDeserializer<AHPObject> {


    @Override
    public AHPObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Criterion.class, new CriterionDeserializer())
                .create();
        List<String> alternatives = null;
        List<Criterion> subcriteria = new LinkedList<>();
        List<Double> matrix = new LinkedList<>();
        boolean isGoalArray;
        try {
            alternatives = jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("alternatives").getAsJsonArray(), List.class);
            isGoalArray = jsonElement.getAsJsonObject().get("Goal").isJsonArray();
            if (!isGoalArray) {
                for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().get("Goal").getAsJsonObject().entrySet()) {

                    if (entry.getValue().isJsonObject()) {
                        Criterion criterion;
                        criterion = gson.fromJson(entry.getValue(), Criterion.class);
                        criterion.setName(entry.getKey());
                        subcriteria.add(criterion);

                    } else if (entry.getValue().isJsonArray() && entry.getKey().equals("matrix")) {
                        matrix = jsonDeserializationContext.deserialize(entry.getValue(), List.class);

                    } else if (entry.getValue().isJsonArray()) {
                        String name = entry.getKey();
                        matrix = jsonDeserializationContext.deserialize(entry.getValue(), List.class);
                        subcriteria.add(new Criterion(name, matrix, null));
                    }
                }
            } else {
                JsonArray goalArray = jsonElement.getAsJsonObject().get("Goal").getAsJsonArray();
                subcriteria = null;
                matrix = jsonDeserializationContext.deserialize(goalArray, List.class);
            }
        } catch (NullPointerException e) {
            System.out.println("Unsupported file format");
        }
        return new AHPObject(alternatives, new Criterion("Goal", matrix, subcriteria));
    }




}


