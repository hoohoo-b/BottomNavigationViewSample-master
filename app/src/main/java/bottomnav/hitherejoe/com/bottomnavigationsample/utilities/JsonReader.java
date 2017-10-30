package bottomnav.hitherejoe.com.bottomnavigationsample.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by Allets on 15/10/2017.
 */

public class JsonReader {
    private static String recipeName;
    private static String recipeDescription;
    private static int recipeDifficulty;


    public static void setRecipeName(String name) {
        recipeName = name;
    }

    public static void setRecipeDescription(String description) {
        recipeDescription = description;
    }

    public static void setRecipeDifficulty(String difficulty) {
        switch(difficulty){
            case "easy":
                recipeDifficulty = 0;
                break;
            case "medium":
                recipeDifficulty = 1;
                break;
            case "hard":
                recipeDifficulty = 2;
        }
    }

    public static String[] retrieveRecipeList(String output) throws JSONException {
        ArrayList<String> list = new ArrayList<String>();
        JSONObject recipeListJson = new JSONObject(output);
        String[] recipeListData = null;

        JSONArray recipeData = recipeListJson.getJSONArray("results");
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

    public static String getRecipeId(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        return recipeListJson.getString("id");
    }

    public static String getFormattedRecipe(){
        String data;
        JSONObject json = new JSONObject();

        try {
            json.put("name", recipeName);
            json.put("description", recipeDescription);
            json.put("difficulty", recipeDifficulty);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        data = json.toString();
        return data;

    }

    public static String getRecipeIdFromResult(String result) throws JSONException{
        JSONObject resultOutput = new JSONObject(result);
        int recipeId = resultOutput.getInt("recipe_id");
        return Integer.toString(recipeId);
    }


}
