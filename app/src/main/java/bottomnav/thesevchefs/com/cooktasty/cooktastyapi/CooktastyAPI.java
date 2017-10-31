package bottomnav.thesevchefs.com.cooktasty.cooktastyapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;

import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.deserializer.TimeJsonDeserializer;
import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.serializer.TimeJsonSerializer;

/**
 * Created by Admin on 31/10/2017.
 */

public abstract class CooktastyAPI {

    public static String endPoint = "https://hidden-springs-80932.herokuapp.com/api/v1.0/";
//    public static String endPoint = "http://10.0.2.2:8000/api/v1.0/";
    public static Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        gsonBuilder.registerTypeAdapter(Time.class, new TimeJsonDeserializer());
        gsonBuilder.registerTypeAdapter(Time.class, new TimeJsonSerializer());
        gson = gsonBuilder.create();
    }

}
