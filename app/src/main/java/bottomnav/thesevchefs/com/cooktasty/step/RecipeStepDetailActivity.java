package bottomnav.thesevchefs.com.cooktasty.step;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import org.parceler.Parcels;

import java.util.ArrayList;

import bottomnav.thesevchefs.com.cooktasty.R;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeInstruction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailActivity extends AppCompatActivity {
    @BindView(R.id.recipeStepDetailFragmentContainer)
    View recipeStepDetailFragmentContainer;

    @BindView(R.id.toPreviousRecipeStepButton)
    Button toPreviousRecipeStepButton;

    @BindView(R.id.toNextRecipeStepButton)
    Button toNextRecipeStepButton;

    @BindView(R.id.recipeStepScrollView)
    ScrollView recipeStepScrollView;

    int currentIndex;
    ArrayList<RecipeInstruction> steps;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_detail_activity);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            steps = intent.getExtras().getParcelableArrayList("recipe_instructions");
            currentIndex = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
            setup(steps.get(currentIndex));
        }

        toPreviousRecipeStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex = Math.max(0, currentIndex - 1);
                RecipeInstruction step = steps.get(currentIndex);
                setup(step);
            }
        });

        toNextRecipeStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex = Math.min(steps.size() - 1, currentIndex + 1);

                RecipeInstruction step = steps.get(currentIndex);
                setup(step);
            }
        });
    }

    private void setup(RecipeInstruction step) {
        recipeStepScrollView.scrollTo(0, 0);

        if (currentIndex > 0) {
            toPreviousRecipeStepButton.setVisibility(View.VISIBLE);
        } else {
            toPreviousRecipeStepButton.setVisibility(View.INVISIBLE);
        }

        if (currentIndex < (steps.size() - 1)) {
            toNextRecipeStepButton.setVisibility(View.VISIBLE);
        } else {
            toNextRecipeStepButton.setVisibility(View.INVISIBLE);
        }

        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        recipeStepDetailFragment.setRecipeStep(step);

        setTitle(String.format("%s. %s", currentIndex, step.instruction));

        getFragmentManager()
            .beginTransaction()
            .replace(R.id.recipeStepDetailFragmentContainer, recipeStepDetailFragment)
            .commit();

    }
}
