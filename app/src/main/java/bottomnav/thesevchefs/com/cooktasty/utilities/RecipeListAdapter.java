package bottomnav.thesevchefs.com.cooktasty.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bottomnav.thesevchefs.com.cooktasty.R;
import bottomnav.thesevchefs.com.cooktasty.entity.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 31/10/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private List<Recipe> recipeList;
    private Context mContext;
    private ListItemClickListener mOnClickListener;

    public RecipeListAdapter(Context mContext, ListItemClickListener mOnClickListener) {
        this.mContext = mContext;
        this.mOnClickListener = mOnClickListener;
        this.recipeList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Recipe recipe = recipeList.get(position);
        Picasso.with(mContext)
                .load(recipe.image_url)
                .into(holder.mRecipeListImageView);
        holder.mRecipeListTextView.setText(recipe.name);
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) return 0;
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_list_data) TextView mRecipeListTextView;
        @BindView(R.id.iv_recipe_image) ImageView mRecipeListImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Recipe recipe = recipeList.get(clickedPosition);
            mOnClickListener.onListItemClick(recipe);
        }
    }

    public void addRecipeListData(List<Recipe> newRecipeList) {
        this.recipeList.addAll(newRecipeList);
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(Recipe recipe);
    }

    public void clearList() {
        recipeList.clear();
    }

}
