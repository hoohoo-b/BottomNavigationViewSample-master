package bottomnav.thesevchefs.com.cooktasty;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import bottomnav.thesevchefs.com.cooktasty.utilities.JsonReader;
import bottomnav.thesevchefs.com.cooktasty.utilities.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.et_recipe_upload_name) EditText mName;
    @BindView(R.id.et_recipe_upload_description) EditText mDescription;
    @BindView(R.id.spinner_recipe_duration_hour) Spinner mHours;
    @BindView(R.id.spinner_recipe_duration_minute) Spinner mMinutes;
    @BindView(R.id.spinner_recipe_ingredients) Spinner mIngredients;
    @BindView(R.id.tv_recipe_ingredient_selected) TextView mIngredientSelected;
    @BindView(R.id.spinner_recipe_quantity) Spinner mQuantity;
    @BindView(R.id.spinner_recipe_quantity_measurement) Spinner mMeasurement;
    @BindView(R.id.spinner_recipe_difficulty) Spinner mDifficulty;
    @BindView(R.id.btnAdd) Button btnAdd;
    @BindView(R.id.btnSelectIngredient) Button btnSelect;
    @BindView(R.id.btnRemoveIngredient) Button btnRemove;
    @BindView(R.id.iv_recipe_upload_image) ImageView mImage;

    @BindView(R.id.ll_recipe_upload_page) LinearLayout mRecipeUploadPage;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    Bitmap recipeImage;
    String authToken = "";
    String resultOutput = null;
    String[] ingredientListOutput = null;
    Uri imageUri;

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

        ButterKnife.bind(this);

        mImage.setImageBitmap(recipeImage);

        getIngredientsNameList();

        hourAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hours);
        minuteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutes);
        quantityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, quantity);
        measurementAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measurement);
        difficultyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, difficulty);

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

        switch (itemId) {
            case R.id.action_next:
                String data = getDataInputsPart1();
                new UploadRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/upload/", "POST", data, authToken);
                 return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getIngredientsNameList() {
        showIngredientListDataView();
        new FetchIngredientList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://hidden-springs-80932.herokuapp.com/api/v1.0/ingredient/list/", "GET", authToken);
    }

    private void showIngredientListDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecipeUploadPage.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecipeUploadPage.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
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
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
            uploadRecipe(s);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            showIngredientListDataView();

            // todo: toast not set causing error
            mToast.setText(s);
            resultOutput = s;
        }
    }

    private void uploadRecipe(String output) {
        String recipeId = null;
        try {
            recipeId = JsonReader.getRecipeIdFromResult(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UploadRecipeImageAsyncTask uploadRecipeImageTask = new UploadRecipeImageAsyncTask(imageUri, this.getContentResolver());
        uploadRecipeImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/image/upload/" + recipeId + "/", "32ff65c24c42a5efa074ad4e5804f098bc0f8447");
    }


    public class FetchIngredientList extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            ingredientListOutput = ingredientListData;
            if (ingredientListOutput != null && ingredientListOutput.length > 0) {
                showIngredientListDataView();
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
            } else {
                showErrorMessage();
            }
        }

    }

    public class UploadRecipeImageAsyncTask extends AsyncTask<String, Void, String> {

        private final Uri imageUri;
        private final ContentResolver cr;
        private String imageFileName;

        UploadRecipeImageAsyncTask(Uri imageUri, ContentResolver cr) {
            this.imageUri = imageUri;
            this.cr = cr;
            this.imageFileName = "filename.jpg";

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = cr.query(imageUri, filePathColumn, null, null, null);

            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                this.imageFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String authToken = params[1];

            String output = null;
            try {
                output = NetworkUtils.imageUploadPost(urlString, authToken, imageUri, this.cr, this.imageFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }

    }


}

