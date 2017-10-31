package bottomnav.thesevchefs.com.cooktasty.cooktastyapi.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Time;

/**
 * Created by Admin on 31/10/2017.
 */

public class TimeJsonSerializer implements JsonSerializer<Time> {
    @Override
    public JsonElement serialize(Time src, Type typeOfSrc, JsonSerializationContext context) {
//        String timeString = src.getHours() + ":" + src.getMinutes() + ":" + src.getSeconds();
        return new JsonPrimitive(src.toString());
    }
}
