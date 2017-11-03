package bottomnav.thesevchefs.com.cooktasty.cooktastyapi;

import android.content.Context;
import android.provider.Settings;

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

import static android.R.attr.key;

/**
 * Created by Jun Jie on 31/10/2017.
 */

public class RecipeAPI extends CooktastyAPI {

    public static void getRecommendedRecipeAPI(Context context, final String authToken, final APICallback callback) {

        String apiUrl = endPoint + "recipe/recommend/";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String recommendedRecipeJsonString = response.getString("data");
                            Recipe recipe = gson.fromJson(recommendedRecipeJsonString, Recipe.class);
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
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(authToken == null || authToken == "") return super.getHeaders();
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Token " + authToken);
                return params;
            }

        };

        Volley.newRequestQueue(context).add(jsonRequest);
    }

    public static void getRecipeDetailAPI(Context context, final String authToken, long recipeId, final APICallback callback) {

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
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(authToken == null || authToken == "") return super.getHeaders();
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Token " + authToken);
                return params;
            }

        };

        Volley.newRequestQueue(context).add(jsonRequest);

    }

    public static void getRecipeListAPI(Context context, final String authToken, int pageNumber, final APICallback callback) {

        String apiUrl = endPoint + "recipe/list/?page=" + pageNumber;

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
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(authToken == null || authToken == "") return super.getHeaders();

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Token " + authToken);
                return params;
            }

        };

        Volley.newRequestQueue(context).add(jsonRequest);

    }

    public static void getFavouriteRecipeListAPI(Context context, final String authToken, int pageNumber, final APICallback callback) {

        String apiUrl = endPoint + "recipe/favourites/?page=" + pageNumber;
        System.out.println(apiUrl);

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
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(authToken == null || authToken == "") return super.getHeaders();

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Token " + authToken);
                return params;
            }

        };

        Volley.newRequestQueue(context).add(jsonRequest);

    }

    public static void setFavouriteRecipeAPI(Context context, final String authToken, long recipeId, boolean toFavouriteStatus, final APICallback callback) {
        String apiUrl = endPoint + "recipe/favourite/" + recipeId + "/";
        int requestMethod;

        if (toFavouriteStatus)
            requestMethod = Request.Method.POST;
        else
            requestMethod = Request.Method.DELETE;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (requestMethod, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(authToken == null || authToken == "") return super.getHeaders();

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Token " + authToken);
                return params;
            }

        };

        Volley.newRequestQueue(context).add(jsonRequest);
    }


}
