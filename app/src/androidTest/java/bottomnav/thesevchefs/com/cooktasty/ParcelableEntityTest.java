package bottomnav.thesevchefs.com.cooktasty;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Time;
import java.util.Date;

import bottomnav.thesevchefs.com.cooktasty.entity.Recipe;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeIngredient;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeInstruction;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class ParcelableEntityTest {

    @Test
    public void test_recipe_instruction_is_parcelable() {
        int stepNum = 1;
        String instructionText = "test instruction";
        Time time_required = new Time(1000);
        String imageUrl = "image/test.jpg";
        RecipeInstruction ri = new RecipeInstruction(stepNum, instructionText, time_required, imageUrl);

        Parcel riParcel = Parcel.obtain();
        ri.writeToParcel(riParcel, ri.describeContents());
        riParcel.setDataPosition(0);

        RecipeInstruction createdFromParcel = RecipeInstruction.CREATOR.createFromParcel(riParcel);

        assertThat(createdFromParcel.step_num, is(stepNum));
        assertThat(createdFromParcel.instruction, is(instructionText));
        assertThat(createdFromParcel.time_required, is(time_required));
        assertThat(createdFromParcel.image_url, is(imageUrl));
    }

    @Test
    public void test_recipe_ingredient_is_parcelable() {

        long ingredientId = 1;
        String name = "onion";
        String imageUrl = "image/test.jpg";
        String servingSize = "10 gram";
        RecipeIngredient ri = new RecipeIngredient(ingredientId, name, imageUrl, servingSize);

        Parcel riParcel = Parcel.obtain();
        ri.writeToParcel(riParcel, ri.describeContents());
        riParcel.setDataPosition(0);

        RecipeIngredient createdFromParcel = RecipeIngredient.CREATOR.createFromParcel(riParcel);

        assertThat(createdFromParcel.serving_size, is(servingSize));
        assertThat(createdFromParcel.ingredient.id, is(ingredientId));
        assertThat(createdFromParcel.ingredient.name, is(name));
        assertThat(createdFromParcel.ingredient.image_url, is(imageUrl));
    }

    @Test
    public void test_recipe_is_parcelable() {

        long repId = 1;
        String repName = "recipe1";
        String repDesc = "recipe desc test";
        long repUploadUser = 1;
        int repDifficultyLevel = 1;
        Time repDuration = new Time(10);
        Date repUploadDatetime = new Date(100);
        String repImageUrl = "test/image.jpg";
        Boolean repFavourited = true;

        int instructionStepNum = 1;
        String instructionText = "test instruction";
        Time instructionTimeRequired = new Time(10);
        String instructionImageUrl = "image/test.jpg";
        RecipeInstruction[] repInstruction = {new RecipeInstruction(instructionStepNum, instructionText, instructionTimeRequired, instructionImageUrl)};

        long ingredientId = 1;
        String ingredientName = "onion";
        String ingredientImageUrl = "image/test.jpg";
        String ingredientServingSize = "10 gram";
        RecipeIngredient[] repIngredients = {new RecipeIngredient(ingredientId, ingredientName, ingredientImageUrl, ingredientServingSize)};

        Recipe recipe = new Recipe(repId, repName, repDesc, repUploadUser, repDifficultyLevel, repDuration, repUploadDatetime, repImageUrl, repFavourited, repIngredients, repInstruction);

        Parcel recipeParcel = Parcel.obtain();
        recipe.writeToParcel(recipeParcel, recipe.describeContents());
        recipeParcel.setDataPosition(0);

        Recipe createdFromParcel = Recipe.CREATOR.createFromParcel(recipeParcel);

        assertThat(createdFromParcel.id, is(repId));
        assertThat(createdFromParcel.name, is(repName));
        assertThat(createdFromParcel.description, is(repDesc));
        assertThat(createdFromParcel.upload_by_user, is(repUploadUser));
        assertThat(createdFromParcel.difficulty_level, is(repDifficultyLevel));
        assertThat(createdFromParcel.time_required, is(repDuration));
        assertThat(createdFromParcel.upload_datetime, is(repUploadDatetime));
        assertThat(createdFromParcel.image_url, is(repImageUrl));
        assertThat(createdFromParcel.is_favourited, is(repFavourited));

        assertThat(createdFromParcel.instructions[0].step_num, is(instructionStepNum));
        assertThat(createdFromParcel.instructions[0].instruction, is(instructionText));
        assertThat(createdFromParcel.instructions[0].time_required, is(instructionTimeRequired));
        assertThat(createdFromParcel.instructions[0].image_url, is(instructionImageUrl));

        assertThat(createdFromParcel.ingredients[0].ingredient.id, is(ingredientId));
        assertThat(createdFromParcel.ingredients[0].ingredient.name, is(ingredientName));
        assertThat(createdFromParcel.ingredients[0].ingredient.image_url, is(ingredientImageUrl));
        assertThat(createdFromParcel.ingredients[0].serving_size, is(ingredientServingSize));

    }


}
