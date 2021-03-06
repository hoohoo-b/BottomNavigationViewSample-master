package bottomnav.thesevchefs.com.cooktasty.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Allets on 15/10/2017.
 */

public class JsonReader {
    private static String recipeName;
    private static String recipeDescription;
    private static int recipeDifficulty;
    private static int recipeId;
    private static int stepNum;
    private static String instructionDetails;
    private static int minute;
    private static int hour;


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
        JSONObject recipeJson = new JSONObject(output);
        return recipeJson.getString("name");
    }

    public static String getRecipeImageUrl(String output) throws JSONException {
        JSONObject recipeJson = new JSONObject(output);
        return recipeJson.getString("image_url");
    }

    public static String getRecipeDescription(String output) throws JSONException {
        JSONObject recipeJson = new JSONObject(output);
        return recipeJson.getString("description");
    }

    public static String getRecipeUploadUserId(String output) throws JSONException {
        JSONObject recipeJson = new JSONObject(output);
        return recipeJson.getString("upload_by_user");
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
        JSONObject recipeJson = new JSONObject(output);
        return recipeJson.getString("time_required");
    }

    public static String getRecipeUploadDateTime(String output) throws JSONException {
        JSONObject recipeJson = new JSONObject(output);
        return recipeJson.getString("upload_datetime");
    }

    public static String getRecipeIngredients(String output) throws JSONException {
        JSONObject recipeListJson = new JSONObject(output);
        JSONObject recipeIngredients = new JSONObject(output);
        return recipeIngredients.getString("ingredients");
    }

    public static Boolean getRecipeIsFavourite(String output) throws JSONException {
        JSONObject recipeJson = new JSONObject(output);
        return recipeJson.getBoolean("is_favourited");
    }

    public static String getRecipeId(String output) throws JSONException {
        JSONObject recipeJson = new JSONObject(output);
        return recipeJson.getString("id");
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

    public static String getRecipeInstructionIdFromResult(String result) throws JSONException{
        JSONObject resultOutput = new JSONObject(result);
        int recipeInstructionId = resultOutput.getInt("instruction_id");
        return Integer.toString(recipeInstructionId);
    }

    public static String getFormattedInstruction(){
        String instruction;
        JSONObject json = new JSONObject();

        try {
            json.put("recipe_id", recipeId);
            json.put("step_num", stepNum);
            json.put("instruction", instructionDetails);
            json.put("duration_minute", minute);
            json.put("duration_hour", hour);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        instruction = json.toString();
        return instruction;
    }


    public static void setHour(int hour) {
        JsonReader.hour = hour;
    }

    public static void setMinute(int minute) {
        JsonReader.minute = minute;
    }

    public static void setInstructionDetails(String instructionDetails) {
        JsonReader.instructionDetails = instructionDetails;
    }

    public static void setStepNum(int stepNum) {
        JsonReader.stepNum = stepNum;
    }

    public static void setRecipeId(int recipeId) {
        JsonReader.recipeId = recipeId;
    }
}
