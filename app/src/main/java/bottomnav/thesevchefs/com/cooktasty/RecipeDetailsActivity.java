package bottomnav.thesevchefs.com.cooktasty;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import bottomnav.thesevchefs.com.cooktasty.utilities.JsonReader;
import bottomnav.thesevchefs.com.cooktasty.utilities.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Allets on 19/10/2017.
 */

public class RecipeDetailsActivity extends AppCompatActivity {

    String recipeName = "";
    String recipeDescription = "";
    String imageURL = "";
    String recipeDifficulty = "";
    String recipeTime = "";
    String recipeIngredients = "";
    Boolean recipeIsFavourite = false;

    private String recipedDetailJson;
    private String authToken;
    private String mEmail;
    private String recipeId;

    @BindView(R.id.tv_recipe_details_name) TextView mRecipeName;
    @BindView(R.id.tv_recipe_details_description) TextView mRecipeDescription;
    @BindView(R.id.iv_recipe_details_image) ImageView mRecipeImageView;
    @BindView(R.id.tv_recipe_details_difficulty) TextView mRecipeDifficulty;
    @BindView(R.id.tv_recipe_details_time) TextView mRecipeTime;
    @BindView(R.id.tv_recipe_details_ingredients) TextView mRecipeIngredients;
    @BindView(R.id.iv_recipe_isfavourite) ImageView mRecipeIsFavourited;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        ButterKnife.bind(this);

        authToken = MyApplication.getAuthToken();
        mEmail = MyApplication.getEmail();

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null
                && intentThatStartedThisActivity.hasExtra("RecipeDetails")) {

            recipedDetailJson = intentThatStartedThisActivity.getStringExtra("RecipeDetails");

            try {
                recipeId = JsonReader.getRecipeId(recipedDetailJson);
                recipeName = JsonReader.getRecipeName(recipedDetailJson);
                recipeDescription = JsonReader.getRecipeDescription(recipedDetailJson);
                imageURL = JsonReader.getRecipeImageUrl(recipedDetailJson);
                recipeDifficulty = JsonReader.getRecipeDifficultyLevel(recipedDetailJson);
                recipeTime = JsonReader.getRecipeTimeRequired(recipedDetailJson);
                recipeIngredients = JsonReader.getRecipeIngredients(recipedDetailJson);
                recipeIsFavourite = JsonReader.getRecipeIsFavourite(recipedDetailJson);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_details_menu, menu);
        return true;
    }

    @OnClick(R.id.iv_recipe_isfavourite)
    public void onClickFavouriteImageView(View v) {
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

        @Override
        protected void onPostExecute(String result) {

        }

    }

}
