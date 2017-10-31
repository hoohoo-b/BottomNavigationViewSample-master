package bottomnav.thesevchefs.com.cooktasty.cooktastyapi.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Time;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class TimeJsonDeserializer implements JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Time.valueOf(json.getAsString());
    }

}
