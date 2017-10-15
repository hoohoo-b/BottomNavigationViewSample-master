package bottomnav.hitherejoe.com.bottomnavigationsample.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Allets on 15/10/2017.
 */

public class JsonReader {

    public static String[] retrieveRecipeList(String output) throws JSONException {
        ArrayList<String> list = new ArrayList<String>();
        JSONObject recipeListJson = new JSONObject(output);
        String[] recipeListData = null;

        JSONArray recipeData = recipeListJson.getJSONArray("data");
        if (recipeData != null) {
            for (int i = 0; i < recipeData.length(); i++) {
                list.add(recipeData.get(i).toString());
            }
            recipeListData = list.toArray(new String[list.size()]);
        }
        return recipeListData;
    }

    public static String retrieveRecipeName(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("name");
    }

    public static String retrieveRecipeImageUrl(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("image_url");
    }

    public static String retrieveRecipeDescription(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("description");
    }

    public static String retrieveRecipeUploadUserId(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("upload_by_user");
    }

    public static String retrieveRecipeDifficultyLevel(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("difficulty_level");
    }

    public static String retrieveRecipeTimeRequired(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("time_required");
    }

    public static String retrieveRecipeUploadDateTime(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("upload_datetime");
    }

//    public static String retrieveRecipeIngredients(String output) throws JSONException {
//        JSONObject recipeListJson = new JSONObject(output);
//        return recipeListJson.getString("ingredients");
//    }

}
