package bottomnav.hitherejoe.com.bottomnavigationsample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.JsonReader;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.NetworkUtils;

import static android.R.attr.x;

/**
 * Created by Allets on 21/10/2017.
 */

public class UploadActivity extends AppCompatActivity {

    String[] hours = {"0", "1", "2", "3", "4", "5"};
    String[] minutes = {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
    String[] difficulty = {"easy", "medium", "hard"};
    String[] quantity = {"1", "2", "3", "4", "5"};
    String[] measurement = {"portion", "tbsp", "tsp", "cup", "kg"};
    ArrayList<String> ingredientsList = new ArrayList<String>();

    EditText mName;
    EditText mDescription;
    Spinner mHours;
    Spinner mMinutes;
    Spinner mIngredients;
    TextView mIngredientSelected;
    Spinner mQuantity;
    Spinner mMeasurement;
    Spinner mDifficulty;
    Button btnAdd;
    Button btnSelect;
    Button btnRemove;
    Bitmap recipeImage;
    ImageView mImage;
    String authToken = "";
    String resultOutput = null;
    String[] ingredientListOutput = null;
    Uri imageUri;
    LinearLayout mRecipeUploadPage;

    ArrayAdapter<String> hourAdapter;
    ArrayAdapter<String> minuteAdapter;
    ArrayAdapter<String> ingredientAdapter;
    ArrayAdapter<String> quantityAdapter;
    ArrayAdapter<String> measurementAdapter;
    ArrayAdapter<String> difficultyAdapter;

    Toast mToast;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_upload);

        context = this;

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            imageUri = intentThatStartedThisActivity.getData();
            try {
                recipeImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (MyApplication.getAuthToken() != null) {
            authToken = MyApplication.getAuthToken();
        }

        mRecipeUploadPage = (LinearLayout) findViewById(R.id.ll_recipe_upload_page);

        mName = (EditText) findViewById(R.id.et_recipe_upload_name);
        mDescription = (EditText) findViewById(R.id.et_recipe_upload_description);
        mHours = (Spinner) findViewById(R.id.spinner_recipe_duration_hour);
        mMinutes = (Spinner) findViewById(R.id.spinner_recipe_duration_minute);
        mIngredients = (Spinner) findViewById(R.id.spinner_recipe_ingredients);
        mIngredientSelected = (TextView) findViewById(R.id.tv_recipe_ingredient_selected);
        mQuantity = (Spinner) findViewById(R.id.spinner_recipe_quantity);
        mMeasurement = (Spinner) findViewById(R.id.spinner_recipe_quantity_measurement);
        mDifficulty = (Spinner) findViewById(R.id.spinner_recipe_difficulty);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSelect = (Button) findViewById(R.id.btnSelectIngredient);
        btnRemove = (Button) findViewById(R.id.btnRemoveIngredient);
        mImage = (ImageView) findViewById(R.id.iv_recipe_upload_image);

        mImage.setImageBitmap(recipeImage);

        getIngredientsNameList();

        hourAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, hours);
        minuteAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, minutes);
        System.out.print(ingredientsList);
        quantityAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, quantity);
        measurementAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, measurement);
        difficultyAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficulty);

        viewItemsOnSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upload_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        String recipeId = null;

        switch (itemId) {
            case R.id.action_next:
                String data = getDataInputsPart1();
                new UploadRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/upload/", "POST", data, authToken);
//                try {
//                    recipeId = JsonReader.getRecipeIdFromResult(resultOutput);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                new UploadRecipe();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getIngredientsNameList() {
        new FetchIngredientList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://hidden-springs-80932.herokuapp.com/api/v1.0/ingredient/list/", "GET", authToken);
        if (ingredientListOutput != null) {
//            ArrayList<String> listOfIngredientNames = null;
//            for (int i = 0; i < ingredientListOutput.length; i++) {
//                try {
//                    listOfIngredientNames.add(JsonReader.getRecipeName(ingredientListOutput[i]));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            ingredientsList = listOfIngredientNames;
        }
    }

    private String getDataInputsPart1() {
        String nameInput;
        String descriptionInput;
        String difficultyInput;

        nameInput = mName.getText().toString();
        descriptionInput = mDescription.getText().toString();
        difficultyInput = mDifficulty.getSelectedItem().toString();

        JsonReader.setRecipeName(nameInput);
        JsonReader.setRecipeDescription(descriptionInput);
        JsonReader.setRecipeDifficulty(difficultyInput);
        return JsonReader.getFormattedRecipe();
    }

    public void viewItemsOnSpinner() {

        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHours.setAdapter(hourAdapter);

        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMinutes.setAdapter(minuteAdapter);

//        ingredientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mIngredients.setAdapter(ingredientAdapter);

        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mQuantity.setAdapter(quantityAdapter);

        measurementAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMeasurement.setAdapter(measurementAdapter);

        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDifficulty.setAdapter(difficultyAdapter);
    }

    public void onClick(View view) {
        if (view == btnAdd) {
            EditText txtItem = (EditText) findViewById(R.id.txtItem);
            ingredientsList.add(txtItem.getText().toString());
            txtItem.setText("");
            ingredientAdapter.notifyDataSetChanged();
        }

        if (view == btnSelect) {
            String ingredientSelected = mQuantity.getSelectedItem().toString() + " " + mMeasurement.getSelectedItem().toString() + " of " + mIngredients.getSelectedItem().toString();
            if (mIngredientSelected.getText() == null) {
                mIngredientSelected.setText(ingredientSelected);
            } else {
                mIngredientSelected.append("\n" + ingredientSelected);
            }
        }

        if (view == btnRemove) {
            if (mIngredientSelected.getText() != null) {
                String existingIngredient = mIngredientSelected.getText().toString();
                if (existingIngredient.lastIndexOf("\n") > 0) {
                    mIngredientSelected.setText(existingIngredient.substring(0, existingIngredient.lastIndexOf("\n")));
                } else {
                    mIngredientSelected.setText("");
                }
            }
        }
    }

    public class UploadRecipe extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String requestMethod = params[1];
            String json = params[2];
            String authToken = params[3];

            String output = null;

            try {
                output = NetworkUtils.getResponseFromHttpUrl(urlString, requestMethod, json, authToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mToast.setText(s);
            resultOutput = s;
        }
    }

    public class FetchIngredientList extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {

            String urlString = params[0];
            String requestMethod = params[1];
            String authToken = params[2];

            String output;
            String[] ingredientList = null;

            try {
                output = NetworkUtils.getResponseFromHttpUrl(urlString, requestMethod, authToken);
                ingredientList = JsonReader.retrieveRecipeList(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ingredientList;
        }


        @Override
        protected void onPostExecute(String[] ingredientListData) {

            ingredientListOutput = ingredientListData;
            if (ingredientListOutput != null && ingredientListOutput.length > 0) {
                for (int i = 0; i < ingredientListOutput.length; i++) {
                    try {
                        ingredientsList.add(JsonReader.getRecipeName(ingredientListOutput[i]));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ingredientAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, ingredientsList);
                ingredientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mIngredients.setAdapter(ingredientAdapter);

//            Collections.addAll(ingredientsList, ingredientListOutput);
            }
        }

    }
}

