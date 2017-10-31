package bottomnav.thesevchefs.com.cooktasty.cooktastyapi;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.deserializer.TimeJsonDeserializer;
import bottomnav.thesevchefs.com.cooktasty.entity.Recipe;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeIngredient;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class RecipeAPI {

    private static String endPoint = "https://hidden-springs-80932.herokuapp.com/api/v1.0/";
    private static Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        gsonBuilder.registerTypeAdapter(Time.class, new TimeJsonDeserializer());
        gson = gsonBuilder.create();
    }

    public static void getRecipeDetailAPI(Context context, int recipeId, final APICallback callback) {

        String apiUrl = endPoint + "recipe/" + recipeId + "/";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String recipedetailJsonString = response.getString("data");
                            Recipe recipe = gson.fromJson(recipedetailJsonString, Recipe.class);
                            callback.onSuccess(recipe);
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

    public static void getRecipeListAPI(Context context, final String authToken, final APICallback callback) {

        String apiUrl = endPoint + "recipe/list/";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String recipelistJsonString = response.getString("results");
                            Recipe[] recipes = gson.fromJson(recipelistJsonString, Recipe[].class);

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
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  super.getHeaders();
                if(authToken == null || authToken == "") return params;

                if(params==null)params = new HashMap<>();
                params.put("Authorization","Token " + authToken);
                return params;
            }

        };

        Volley.newRequestQueue(context).add(jsonRequest);

    }

    public static void getFavouriteRecipeListAPI(Context context, final String authToken, final APICallback callback) {

        String apiUrl = endPoint + "recipe/favourites/";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String recipelistJsonString = response.getString("results");
                            Recipe[] recipes = gson.fromJson(recipelistJsonString, Recipe[].class);
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
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  super.getHeaders();
                if(authToken == null || authToken == "") return params;

                if(params==null)params = new HashMap<>();
                params.put("Authorization","Token " + authToken);
                return params;
            }

        };

        Volley.newRequestQueue(context).add(jsonRequest);

    }



}
