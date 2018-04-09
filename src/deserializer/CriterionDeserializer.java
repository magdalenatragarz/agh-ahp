package deserializer;

import com.google.gson.*;
import objects.Criterion;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class CriterionDeserializer implements JsonDeserializer<Criterion> {


    @Override
    public Criterion deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {


        JsonObject parentJsonObject = jsonElement.getAsJsonObject();

        List<Double> parentMatrix = new LinkedList<>();
        List<Double> matrix;
        List<Criterion> subcriteria = new LinkedList<>();
        String name = "";

        if (jsonElement.isJsonObject()) {

            for (Map.Entry<String, JsonElement> entry : parentJsonObject.entrySet()) {
                if (entry.getKey().equals("matrix")) {
                    parentMatrix = jsonDeserializationContext.deserialize(entry.getValue(), List.class);

                } else if (entry.getValue().isJsonObject()) {
                    Criterion criterion = jsonDeserializationContext.deserialize(entry.getValue(), Criterion.class);
                    criterion.setName(entry.getKey());
                    subcriteria.add(criterion);

                } else if (entry.getValue().isJsonArray()) {
                    name = entry.getKey();
                    matrix = jsonDeserializationContext.deserialize(entry.getValue(), List.class);
                    subcriteria.add(new Criterion(name, matrix, null));
                }
            }

            return new Criterion(name, parentMatrix, subcriteria);
        } else if (jsonElement.isJsonArray()) {
            parentMatrix = jsonDeserializationContext.deserialize(jsonElement.getAsJsonArray(), List.class);
            name = "";
            subcriteria = null;
        }
        return new Criterion(name, parentMatrix, subcriteria);
    }


}
