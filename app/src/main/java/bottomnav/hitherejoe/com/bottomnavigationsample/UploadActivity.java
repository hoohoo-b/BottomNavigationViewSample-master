package bottomnav.hitherejoe.com.bottomnavigationsample;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.NetworkUtils;

import static android.R.id.list;
import static bottomnav.hitherejoe.com.bottomnavigationsample.R.id.btnAdd;
import static bottomnav.hitherejoe.com.bottomnavigationsample.R.string.hour;

/**
 * Created by Allets on 21/10/2017.
 */

public class UploadActivity extends AppCompatActivity {

    String[] hours = {"1", "2", "3", "4", "5"};
    String[] minutes = {"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
    String[] difficulty = {"easy", "medium", "hard"};
    String[] quantity = {"1", "2", "3", "4", "5"};
    String[] measurement = {"portion", "tbsp", "tsp", "cup", "kg"};
    ArrayList<String> ingredientsList = new ArrayList<String>();

    Spinner mHours;
    Spinner mMinutes;
    Spinner mIngredients;
    Spinner mQuantity;
    Spinner mMeasurement;
    Spinner mDifficulty;
    Button btnAdd;
    Bitmap recipeImage;
    ImageView mImage;

    ArrayAdapter<String> hourAdapter;
    ArrayAdapter<String> minuteAdapter;
    ArrayAdapter<String> ingredientAdapter;
    ArrayAdapter<String> quantityAdapter;
    ArrayAdapter<String> measurementAdapter;
    ArrayAdapter<String> difficultyAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_upload);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Uri imageUri = intentThatStartedThisActivity.getData();
            try {
                recipeImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        mHours = (Spinner) findViewById(R.id.spinner_recipe_duration_hour);
        mMinutes = (Spinner) findViewById(R.id.spinner_recipe_duration_minute);
        mIngredients = (Spinner) findViewById(R.id.spinner_recipe_ingredients);
        mQuantity = (Spinner) findViewById(R.id.spinner_recipe_quantity);
        mMeasurement = (Spinner) findViewById(R.id.spinner_recipe_quantity_measurement);
        mDifficulty = (Spinner) findViewById(R.id.spinner_recipe_difficulty);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        mImage = (ImageView) findViewById(R.id.iv_recipe_upload_image);

        mImage.setImageBitmap(recipeImage);

        hourAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, hours);
        minuteAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, minutes);
        ingredientAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ingredientsList);
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int itemId = item.getItemId();
//
//        switch (itemId) {
//            /*
//             * When you click the reset menu item, we want to start all over
//             * and display the pretty gradient again. There are a few similar
//             * ways of doing this, with this one being the simplest of those
//             * ways. (in our humble opinion)
//             */
//            case R.id.action_next:
//                new UploadActivity().execute()
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void viewItemsOnSpinner() {
        ingredientsList.add("fish");
        ingredientsList.add("chicken");
        ingredientsList.add("pork");
        ingredientsList.add("beef");

        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHours.setAdapter(hourAdapter);

        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMinutes.setAdapter(minuteAdapter);

        ingredientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIngredients.setAdapter(ingredientAdapter);

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
    }

//    public class UploadRecipe extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String urlString = params[0];
//            String requestMethod = params[1];
//            String authToken = params[2];
//
//            try {
//                NetworkUtils.getResponseFromHttpUrl(urlString, requestMethod, authToken);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//    }

}

