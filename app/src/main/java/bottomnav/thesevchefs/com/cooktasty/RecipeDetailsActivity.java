package bottomnav.thesevchefs.com.cooktasty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bottomnav.thesevchefs.com.cooktasty.entity.RecipeInstruction;
import bottomnav.thesevchefs.com.cooktasty.step.RecipeStepDetailActivity;
import bottomnav.thesevchefs.com.cooktasty.step.RecipeStepDetailFragment;
import bottomnav.thesevchefs.com.cooktasty.step.RecipeStepListFragment;
import bottomnav.thesevchefs.com.cooktasty.step.RecipeStepRecyclerViewAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.APICallback;
import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.RecipeAPI;
import bottomnav.thesevchefs.com.cooktasty.entity.Recipe;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeIngredient;

/**
 * Created by Allets on 19/10/2017.
 */

public class RecipeDetailsActivity extends AppCompatActivity {

    Recipe thisActivityRecipe;
    RecipeInstruction[] thisActivityRecipeInstructions;
    RecipeStepDetailFragment recipeStepDetailFragment;
    ArrayList<RecipeInstruction> steps;

    private String authToken;

    @BindView(R.id.tv_recipe_details_name)
    TextView mRecipeName;
    @BindView(R.id.tv_recipe_details_description)
    TextView mRecipeDescription;
    @BindView(R.id.iv_recipe_details_image)
    ImageView mRecipeImageView;
    @BindView(R.id.tv_recipe_details_difficulty)
    TextView mRecipeDifficulty;
    @BindView(R.id.tv_recipe_details_time)
    TextView mRecipeTime;
    @BindView(R.id.tv_recipe_details_ingredients)
    TextView mRecipeIngredients;
    @BindView(R.id.iv_recipe_isfavourite)
    ImageView mRecipeIsFavourited;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);
        authToken = MyApplication.getAuthToken();
        if (authToken == null) {
            authToken = "";
        }

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra("RecipeId")) {

            long recipeId = intentThatStartedThisActivity.getLongExtra("RecipeId", 0);
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

        String recipeDifficulty;
        switch (recipe.difficulty_level) {
            case 0:
                recipeDifficulty = "easy";
                break;
            case 1:
                recipeDifficulty = "medium";
                break;
            case 2:
                recipeDifficulty = "hard";
                break;
            default:
                recipeDifficulty = "easy";
        }

        Picasso.with(this)
                .load(recipe.image_url)
                .into(mRecipeImageView);
        mRecipeName.setText(recipe.name);
        mRecipeDescription.setText(recipe.description);
        mRecipeDifficulty.setText(recipeDifficulty);
        mRecipeTime.setText(recipe.time_required.toString());

        System.out.println("---------------------");
        System.out.println(recipe.is_favourited);
        System.out.println("---------------------");

        if (recipe.is_favourited) {
            mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_true);
        } else {
            mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_false);
        }

        String ingredientText = "";
        for (RecipeIngredient ri : recipe.ingredients) {
            ingredientText = ingredientText + ri.serving_size + " of " + ri.ingredient.name + " " + "\n\r";
        }
        mRecipeIngredients.setText(ingredientText);

        // Setup recipe step list fragment
        steps = new ArrayList<RecipeInstruction>(Arrays.asList(recipe.instructions));
        RecipeStepListFragment recipeStepListFragment = new RecipeStepListFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.recipeStepListFragmentContainer, recipeStepListFragment)
                .commit();

        recipeStepListFragment.setSteps(steps);

        if (findViewById(R.id.recipeStepDetailFragmentContainer) != null) {
            setupRecipeStepDetailFragment(steps.get(0));
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
        long recipeId = thisActivityRecipe.id;
        boolean setToStatus = !thisActivityRecipe.is_favourited;
        RecipeAPI.setFavouriteRecipeAPI(this, authToken, recipeId, setToStatus,
                new APICallback() {
                    @Override
                    public void onSuccess(Object result) {
                        thisActivityRecipe.is_favourited = !thisActivityRecipe.is_favourited;
                        if (thisActivityRecipe.is_favourited) {
                            mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_true);
                        } else {
                            mRecipeIsFavourited.setImageResource(R.drawable.ic_recipe_isfavourite_false);
                        }
                    }

                    @Override
                    public void onError(Object error) {
                        VolleyError volleyError = (VolleyError) error;
                        volleyError.printStackTrace();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_recipe_steps:
                Intent intent = new Intent(RecipeDetailsActivity.this, RecipeStepDetailActivity.class);
                intent.putParcelableArrayListExtra("recipe_instructions", steps);
                intent.putExtra(Intent.EXTRA_INDEX, 0);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecipeStepDetailFragment(RecipeInstruction step) {
        recipeStepDetailFragment = new RecipeStepDetailFragment();
        recipeStepDetailFragment.setRecipeStep(step);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.recipeStepDetailFragmentContainer, recipeStepDetailFragment)
                .commit();
    }
}
