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

import bottomnav.thesevchefs.com.cooktasty.entity.RecipeIngredient;

/**
 * Created by Admin on 31/10/2017.
 */

public class IngredientAPI extends CooktastyAPI {

    public static void getIngredientListAPI(Context context, final APICallback callback) {

        String apiUrl = endPoint + "ingredient/list/";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String ingredientListJsonString = response.getString("results");
                            RecipeIngredient.IngredientDetail[] ingredientList = gson.fromJson(ingredientListJsonString, RecipeIngredient.IngredientDetail[].class);
                            callback.onSuccess(ingredientList);
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
