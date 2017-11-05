package bottomnav.thesevchefs.com.cooktasty;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;

import org.jcodec.containers.mp4.boxes.Edit;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.APICallback;
import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.RecipeAPI;
import bottomnav.thesevchefs.com.cooktasty.entity.Recipe;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeInstruction;
import bottomnav.thesevchefs.com.cooktasty.utilities.JsonReader;
import bottomnav.thesevchefs.com.cooktasty.utilities.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Url;

import static bottomnav.thesevchefs.com.cooktasty.MainActivity.REQUEST_TAKE_PHOTO;

/**
 * Created by user on 5/11/2017.
 */

public class UploadRecipeInstructions extends AppCompatActivity {

    private String authToken;
    Recipe thisActivityRecipe;
    RecipeInstruction[] thisActivityRecipeInstructions;
    int currentIndex;
    Bitmap recipeImage;
    Uri selectedImage;
    long recipeId;
    Context context;

    @BindView(R.id.iv_recipe_instruction_image)
    ImageView mImage;

    @BindView(R.id.et_recipeInstructionDescription)
    EditText mInstructionDescription;

    @BindView(R.id.time_minute)
    EditText mMinute;

    @BindView(R.id.time_hour)
    EditText mHour;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_upload_activity);
        authToken = MyApplication.getAuthToken();
        if (authToken == null) {
            authToken = "";
        }

        context = this;

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra("recipeId")) {

            currentIndex = intentThatStartedThisActivity.getIntExtra("currentIndex", 1);
            recipeId = Long.parseLong(intentThatStartedThisActivity.getExtras().getString("recipeId"));
            RecipeAPI.getRecipeDetailAPI(this, authToken, recipeId, new APICallback() {
                public void onSuccess(Object result) {
                    Recipe recipe = (Recipe) result;
                    initRecipeDetailActivityContent(recipe);
                }

                public void onError(Object error) {
                    VolleyError volleyError = (VolleyError) error;
                    volleyError.printStackTrace();
                }
            });

        }
    }

    public void initRecipeDetailActivityContent(Recipe recipe) {

        this.thisActivityRecipe = recipe;
        this.thisActivityRecipeInstructions = recipe.instructions;
        setTitle("Step " + currentIndex);

    }

    public void uploadRecipeInstructionImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            try {
                recipeImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mImage.setImageBitmap(recipeImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_done:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Upload your recipe?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String json = getJsonData();
                                saveInstructionDetails(json);
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToNextInstruction(View view) throws ParseException {
        String json = getJsonData();
        saveInstructionDetails(json);

        Intent intent = new Intent(this, UploadRecipeInstructions.class);
        intent.putExtra("recipeId", ("" + recipeId));
        intent.putExtra("currentIndex", currentIndex + 1);
        startActivity(intent);
    }

    public String getJsonData(){
        JsonReader.setRecipeId((int) thisActivityRecipe.id);
        JsonReader.setStepNum(currentIndex);
        JsonReader.setInstructionDetails(mInstructionDescription.getText().toString());
        if (TextUtils.isEmpty(mHour.getText().toString())) {
            JsonReader.setHour(0);
        } else { JsonReader.setHour(Integer.parseInt(mHour.getText().toString())); }
        if (TextUtils.isEmpty(mMinute.getText().toString())) {
            JsonReader.setMinute(0);
        } else { JsonReader.setMinute(Integer.parseInt(mMinute.getText().toString())); }
        String json = JsonReader.getFormattedInstruction();
        return json;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upload_instruction_menu, menu);
        return true;
    }

    private void saveInstructionDetails(String json) {
        new UploadInstruction().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/instruction/", json, authToken);
    }

    private void saveInstructionImage(String output) {
        String recipeInstructionId = null;
        try {
            recipeInstructionId = JsonReader.getRecipeInstructionIdFromResult(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (selectedImage != null) {
            UploadInstructionImageAsyncTask uploadRecipeImageTask = new UploadInstructionImageAsyncTask(selectedImage, this.getContentResolver());
            uploadRecipeImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/instruction/image/upload/" + recipeInstructionId + "/", authToken);
        }
    }

    public class UploadInstruction extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String json = params[1];
            String authToken = params[2];

            String output = null;

            try {
                output = NetworkUtils.getResponseFromHttpUrl(urlString, "POST", json, authToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            saveInstructionImage(s);
        }
    }

    public static class UploadInstructionImageAsyncTask extends AsyncTask<String, Void, String> {

        private final Uri imageUri;
        private final ContentResolver cr;
        private String imageFileName;

        UploadInstructionImageAsyncTask(Uri imageUri, ContentResolver cr) {
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
