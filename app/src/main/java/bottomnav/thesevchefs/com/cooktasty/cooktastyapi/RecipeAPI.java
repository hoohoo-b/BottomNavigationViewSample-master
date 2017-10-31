package bottomnav.thesevchefs.com.cooktasty.cooktastyapi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import bottomnav.thesevchefs.com.cooktasty.entity.Recipe;

/**
 * Created by Admin on 31/10/2017.
 */

public class RecipeAPI {

    private static String endPoint = "https://hidden-springs-80932.herokuapp.com/api/v1.0/";
    private static Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        gson = gsonBuilder.create();
    }

    public static void getRecipeListAPI(Context context, final APICallback callback) {

        String apiUrl = endPoint + "recipe/list/";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String recipelistJsonString = response.getString("results");
                            List<Recipe> recipes = Arrays.asList(gson.fromJson(recipelistJsonString, Recipe[].class));
                            callback.onSuccess(recipes);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                });

        Volley.newRequestQueue(context).add(jsonRequest);

    }

}
