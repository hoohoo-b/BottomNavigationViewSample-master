package bottomnav.hitherejoe.com.bottomnavigationsample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.JsonReader;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.NetworkUtils;

/**
 * Created by Allets on 19/10/2017.
 */

public class RecipeDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    String recipeName = "";
    String recipeDescription = "";
    String imageURL = "";
    String recipeDifficulty = "";
    String recipeTime = "";
    String recipeIngredients = "";
    Boolean recipeIsFavourite = false;
    private String recipeList;
    private String authToken;
    private String mEmail;
    private String recipeId;
    private TextView mRecipeName;
    private TextView mRecipeDescription;
    private ImageView mRecipeImageView;
    private TextView mRecipeDifficulty;
    private TextView mRecipeTime;
    private TextView mRecipeIngredients;
    private ImageView mRecipeIsFavourited;

    //Progress Bar
    private ProgressBar mProgressBar;
    private TextView mLoadingText;

    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe_details);

        //Set Progress Bar
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mLoadingText = (TextView) findViewById(R.id.LoadingCompleteTextView);


        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mLoadingText = (TextView) findViewById(R.id.LoadingCompleteTextView);

        mRecipeName = (TextView) findViewById(R.id.tv_recipe_details_name);
        mRecipeDescription = (TextView) findViewById(R.id.tv_recipe_details_description);
        mRecipeImageView = (ImageView) findViewById(R.id.iv_recipe_details_image);
        mRecipeDifficulty = (TextView) findViewById(R.id.tv_recipe_details_difficulty);
        mRecipeTime = (TextView) findViewById(R.id.tv_recipe_details_time);
        mRecipeIngredients = (TextView) findViewById(R.id.tv_recipe_details_ingredients);
        mRecipeIsFavourited = (ImageView) findViewById(R.id.iv_recipe_isfavourite);
        mRecipeIsFavourited.setOnClickListener(this);
        authToken = MyApplication.getAuthToken();
        mEmail = MyApplication.getEmail();

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("RecipeDetails")) {
                recipeList = intentThatStartedThisActivity.getStringExtra("RecipeDetails");

                try {
                    recipeId = JsonReader.getRecipeId(recipeList);
                    recipeName = JsonReader.getRecipeName(recipeList);
                    recipeDescription = JsonReader.getRecipeDescription(recipeList);
                    imageURL = JsonReader.getRecipeImageUrl(recipeList);
                    recipeDifficulty = JsonReader.getRecipeDifficultyLevel(recipeList);
                    recipeTime = JsonReader.getRecipeTimeRequired(recipeList);
                    recipeIngredients = JsonReader.getRecipeIngredients(recipeList);
                    recipeIsFavourite = JsonReader.getRecipeIsFavourite(recipeList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Picasso.with(this)
                        .load(imageURL)
                        .into(mRecipeImageView);
                mRecipeName.setText(recipeName);
                mRecipeDescription.setText(recipeDescription);
                mRecipeDifficulty.setText(recipeDifficulty);
                mRecipeTime.setText(recipeTime);
                mRecipeIngredients.setText(recipeIngredients);

                if (recipeIsFavourite) {
                    mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_true);
                } else {
                    mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_false);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_details_menu, menu);
        return true;
    }


    public void doThis(MenuItem item){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < 100){
                    mProgressStatus++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingText.setVisibility(View.VISIBLE);
                    }
                });
//                if(mProgressStatus == 100)
//                {
//                    onFinish();
//                }
            }
        }).start();
    }

//    public boolean onKey (View v, int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_ENTER) {
//            fuckayou.setVisibility(View.VISIBLE);
//            return true;
//        }
//        return false;
//    }

//    public void onFinish()
//    {
//        //Unable to Go for next progress bar
//
//    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_recipe_isfavourite:
                if (recipeIsFavourite) {
                    mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_false);
                    recipeIsFavourite = false;
                    new EditRecipeFavourite().execute("https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/favourite/" + recipeId + "/", "DELETE", authToken);
                } else {
                    mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_true);
                    recipeIsFavourite = true;
                    new EditRecipeFavourite().execute("https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/favourite/" + recipeId + "/", "POST", authToken);
                }
        }
    }

    public class EditRecipeFavourite extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String requestMethod = params[1];
            String authToken = params[2];

            try {
                NetworkUtils.getResponseFromHttpUrl(urlString, requestMethod, authToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
