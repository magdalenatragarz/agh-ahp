import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OverallDeserializer implements JsonDeserializer<AHPObject> {


    @Override
    public AHPObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Criterion.class, new CriterionDeserializer())
                .create();
        List<String> alternatives;
        List<Criterion> subcriteria = new LinkedList<>();
        List<Double> matrix = new LinkedList<>();

        alternatives = jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("alternatives").getAsJsonArray(), List.class);

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
        return new AHPObject(alternatives, new Criterion("Goal", matrix, subcriteria));
    }


}


