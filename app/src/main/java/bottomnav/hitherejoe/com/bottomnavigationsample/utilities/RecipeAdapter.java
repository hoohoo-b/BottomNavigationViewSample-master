package bottomnav.hitherejoe.com.bottomnavigationsample.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bottomnav.hitherejoe.com.bottomnavigationsample.R;

/**
 * Created by Allets on 9/10/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private String[] mRecipeListData;

    public RecipeAdapter() {
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mRecipeListTextView;

        public RecipeAdapterViewHolder(View view) {
            super(view);
            mRecipeListTextView = (TextView) view.findViewById(R.id.recipe_list_data);
        }
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        String recipeList = mRecipeListData[position];
        holder.mRecipeListTextView.setText(recipeList);
    }

    @Override
    public int getItemCount() {
        if (mRecipeListData == null) {
            return 0;
        }
        return mRecipeListData.length;
    }

    public void setRecipeListData(String[] recipeListData) {
        mRecipeListData = recipeListData;
        notifyDataSetChanged();
    }
}