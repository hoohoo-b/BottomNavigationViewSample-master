package bottomnav.hitherejoe.com.bottomnavigationsample;

// FOR FAVOURITE ACTIVITY... CURRENTLY BLANK

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.JsonReader;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.NetworkUtils;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.RecipeAdapter;


public class FavRecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 100;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private Context appContext;
    private String authToken;

    public static FavRecipeFragment newInstance() {
        FavRecipeFragment fragment = new FavRecipeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        appContext = getActivity().getApplicationContext();
        authToken = MyApplication.getAuthToken();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_favrecipelist);
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) rootView.findViewById(R.id.tv_error_message_display);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipeAdapter = new RecipeAdapter(appContext, NUM_LIST_ITEMS, this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        mRecyclerView.setVisibility(View.VISIBLE);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        showRecipeListDataView();
        getRecipeNameList();
    }

    private void showRecipeListDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void getRecipeNameList() {
        new FetchFavouriteRecipeAsyncTask().execute("https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/favourites/", "GET", authToken);
    }

    public class FetchFavouriteRecipeAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            String urlString = params[0];
            String authToken = params[1];

            String output;
            String[] recipeList = null;

            try {
                // authToken
                output = NetworkUtils.getResponseFromHttpUrl(urlString, "GET", "32ff65c24c42a5efa074ad4e5804f098bc0f8447");
                recipeList = JsonReader.retrieveRecipeList(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return recipeList;
        }

        @Override
        protected void onPostExecute(String[] recipeListData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (recipeListData != null) {
                showRecipeListDataView();
                mRecipeAdapter.setRecipeNameListData(recipeListData);
            } else {
                showErrorMessage();
            }
        }

    }


    public void onListItemClick(String recipeList) {
//        setContentView(R.layout.fragment_recipe_details);

//        RecipeDetailsActivity recipeDetailFragment = new RecipeDetailsActivity();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentp, recipeDetailFragment).commit();

//        Context context = getActivity().getApplicationContext();
//        Class recipeDetailsActivityClass = RecipeDetailsActivity.class;
//        Intent intentToStartDetailActivity = new Intent(context, recipeDetailsActivityClass);
//        intentToStartDetailActivity.putExtra("RecipeDetails", recipeList);
//        startActivity(intentToStartDetailActivity);
    }

}
