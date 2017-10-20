package bottomnav.hitherejoe.com.bottomnavigationsample.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

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

    public static String getRecipeName(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("name");
    }

    public static String getRecipeImageUrl(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("image_url");
    }

    public static String getRecipeDescription(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("description");
    }

    public static String getRecipeUploadUserId(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("upload_by_user");
    }

    public static String getRecipeDifficultyLevel(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        String recipeDifficulty = recipeListJson.getString("difficulty_level");
        switch (recipeDifficulty) {
            case "0":
                recipeDifficulty = "easy";
                break;
            case "1":
                recipeDifficulty = "medium";
                break;
            case "2":
                recipeDifficulty = "hard";
        }

        return recipeDifficulty;
    }

    public static String getRecipeTimeRequired(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("time_required");
    }

    public static String getRecipeUploadDateTime(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("upload_datetime");
    }

    public static String getRecipeIngredients(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        JSONObject recipeIngredients = new JSONObject(output);
        return recipeIngredients.getString("ingredients");
    }

    public static Boolean getRecipeIsFavourite(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getBoolean("is_favourited");
    }

}
