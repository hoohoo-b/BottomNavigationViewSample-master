package bottomnav.hitherejoe.com.bottomnavigationsample.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import bottomnav.hitherejoe.com.bottomnavigationsample.MainActivity;
import bottomnav.hitherejoe.com.bottomnavigationsample.R;

import static bottomnav.hitherejoe.com.bottomnavigationsample.R.id.imageView;

/**
 * Created by Allets on 9/10/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private String[] mRecipeListData;
    private String[] mRecipeImageListData;
    private int mNumberOfItems;
    private static int viewHolderCount;
    final private ListItemClickListener mOnClickListener;
    private Context context;

    public RecipeAdapter(Context context, int numberOfItems, ListItemClickListener listener) {
        mNumberOfItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
        this.context = context;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mRecipeListTextView;
        public final ImageView mRecipeListImageView;

        public RecipeAdapterViewHolder(View view) {
            super(view);
            mRecipeListTextView = (TextView) view.findViewById(R.id.tv_recipe_list_data);
            mRecipeListImageView = (ImageView) view.findViewById(R.id.iv_recipe_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
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
        String recipeName = "";
        String imageURL = "";
        Bitmap imageBitmap = null;

        try {
            recipeName = JsonReader.retrieveRecipeName(recipeList);
            imageURL = JsonReader.retrieveRecipeImageUrl(recipeList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        new ImageUrlToBitmap().execute(imageURL);

        Picasso.with(context)
                .load(imageURL)
                .into(holder.mRecipeListImageView);

        holder.mRecipeListTextView.setText(recipeName);
//        holder.mRecipeListImageView.setImageBitmap(imageBitmap);
//        Picasso.with(context).load(imageURL).into(imageView);

    }

    @Override
    public int getItemCount() {
        if (mRecipeListData == null) {
            return 0;
        }
        return mRecipeListData.length;
    }

    public void setRecipeNameListData(String[] recipeListData) {
        mRecipeListData = recipeListData;
        notifyDataSetChanged();
    }


    public class ImageUrlToBitmap extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... args) {

            try {

                return BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap imageBitmap) {

        }
    }
}