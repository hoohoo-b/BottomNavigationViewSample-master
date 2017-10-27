package bottomnav.hitherejoe.com.bottomnavigationsample;

// FOR FAVOURITE ACTIVITY... CURRENTLY BLANK

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.JsonReader;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.NetworkUtils;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.RecipeAdapter;

public class FavouriteActivity extends FragmentActivity {

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    public static FavouriteActivity newInstance() {
        FavouriteActivity fragment = new FavouriteActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.fragment_favourite);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_recipelist);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }



//    public class FetchFavouriteRecipeList extends AsyncTask<String, Void, String[]> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String[] doInBackground(String... params) {
//            String urlString = params[0];
//            String requestMethod = params[1];
//            String authToken = params[2];
//
//            String output;
//            String[] recipeList = null;
//
//            try {
//                output = NetworkUtils.getResponseFromHttpUrl(urlString, requestMethod, authToken);
//                recipeList = JsonReader.retrieveRecipeList(output);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return recipeList;
//        }
//
//
//        @Override
//        protected void onPostExecute(String[] recipeListData) {
//            mLoadingIndicator.setVisibility(View.INVISIBLE);
//            if (recipeListData != null) {
//                showRecipeListDataView();
//                mRecipeAdapter.setRecipeNameListData(recipeListData);
//            } else {
//                showErrorMessage();
//            }
//        }
//
//    }


}
