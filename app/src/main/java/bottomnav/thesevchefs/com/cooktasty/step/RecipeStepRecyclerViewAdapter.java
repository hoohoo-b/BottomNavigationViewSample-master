package bottomnav.thesevchefs.com.cooktasty.step;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bottomnav.thesevchefs.com.cooktasty.R;
import bottomnav.thesevchefs.com.cooktasty.entity.RecipeInstruction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepRecyclerViewAdapter extends RecyclerView.Adapter<RecipeStepRecyclerViewAdapter.ViewHolder> {
    ArrayList<RecipeInstruction> steps;
    RecipeStepRecyclerViewAdapter.OnClickHandler onClickHandler;

    public RecipeStepRecyclerViewAdapter() {
        this.steps = new ArrayList<>();
    }

    @Override
    public RecipeStepRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_step, parent, false);

        return new RecipeStepRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.setup(position, steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void setSteps(ArrayList<RecipeInstruction> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public void setOnClickHandler(OnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    public interface OnClickHandler {
        public void onRecipeStepClicked(int position, RecipeInstruction step);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipeStepShortDescription)
        TextView recipeStepShortDescriptionTextView;

        RecipeInstruction step;
        int position;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        public void setup(int position, RecipeInstruction step) {
            this.step = step;
            this.position = position;

            // Position 0 is recipe introduction
            recipeStepShortDescriptionTextView.setText(String.format(
                    "%s. %s",
                    position + 1,
                    step.instruction
            ));

        }

        @Override
        public void onClick(View view) {
        }
    }
}
