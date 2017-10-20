package bottomnav.hitherejoe.com.bottomnavigationsample;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.JsonReader;

/**
 * Created by Allets on 19/10/2017.
 */

public class RecipeDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private String recipeList;
    private TextView mRecipeName;
    private TextView mRecipeDescription;
    private ImageView mRecipeImageView;
    private TextView mRecipeDifficulty;
    private TextView mRecipeTime;
    private TextView mRecipeIngredients;
    private ImageView mRecipeIsFavourited;

    String recipeName = "";
    String recipeDescription = "";
    String imageURL = "";
    String recipeDifficulty = "";
    String recipeTime = "";
    String recipeIngredients = "";
    Boolean recipeIsFavourite = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe_details);

        mRecipeName = (TextView) findViewById(R.id.tv_recipe_details_name);
        mRecipeDescription = (TextView) findViewById(R.id.tv_recipe_details_description);
        mRecipeImageView = (ImageView) findViewById(R.id.iv_recipe_details_image);
        mRecipeDifficulty = (TextView) findViewById(R.id.tv_recipe_details_difficulty);
        mRecipeTime = (TextView) findViewById(R.id.tv_recipe_details_time);
        mRecipeIngredients = (TextView) findViewById(R.id.tv_recipe_details_ingredients);
        mRecipeIsFavourited = (ImageView) findViewById(R.id.iv_recipe_isfavourite);
        mRecipeIsFavourited.setOnClickListener(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                recipeList = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

                try {
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
                }
                else {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_recipe_isfavourite:
                if (recipeIsFavourite) {
                    mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_false);
                    recipeIsFavourite = false;
                }
                else {
                    mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_true);
                    recipeIsFavourite = true;
                }
        }
    }

}
