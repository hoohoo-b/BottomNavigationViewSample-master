package bottomnav.thesevchefs.com.cooktasty.cooktastyapi;

import android.content.Context;
import android.provider.Settings;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.CooktastyAPI;
import bottomnav.thesevchefs.com.cooktasty.entity.ActivityTimeline;
import bottomnav.thesevchefs.com.cooktasty.entity.Recipe;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeIngredient;

/**
 * Created by Admin on 1/11/2017.
 */

public class UserAPI extends CooktastyAPI {

    public static void getAuthTokenAPI(Context context, String email, String password, final APICallback callback) {

        String apiUrl = endPoint + "login2/";

        JSONObject bodyParam = new JSONObject();
        try {
            bodyParam.put("email", email);
            bodyParam.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, apiUrl, bodyParam, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String authToken = response.getString("token");
                            callback.onSuccess(authToken);
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

    public static void userSignUpAPI(Context context, String email, String password, String username, final APICallback callback) {

        String apiUrl = endPoint + "user/signup/";
        JSONObject bodyParam = new JSONObject();
        try {
            bodyParam.put("email", email);
            bodyParam.put("password", password);
            bodyParam.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, apiUrl, bodyParam, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            callback.onSuccess(success);
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

    public static void userActivityTimeLineAPI(Context context, final String authToken, final int page, final APICallback callback) {

        String apiUrl = endPoint + "this/user/timeline/?page=" + page;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String timelinesJsonString = response.getString("results");
                            List<ActivityTimeline> activityTimelines = Arrays.asList(gson.fromJson(timelinesJsonString, ActivityTimeline[].class));
                            callback.onSuccess(activityTimelines);
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

}
