package bottomnav.thesevchefs.com.cooktasty;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.TextView;

import bottomnav.thesevchefs.com.cooktasty.utilities.JsonReader;
import bottomnav.thesevchefs.com.cooktasty.utilities.NetworkUtils;
import bottomnav.thesevchefs.com.cooktasty.utilities.RecipeAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeListFragment extends Fragment implements RecipeAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 100;

    private Context appContext;
    private String authToken;

    private RecipeAdapter mRecipeAdapter;

    @BindView(R.id.rv_favrecipelist) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    private Unbinder unbinder;

    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        appContext = getActivity().getApplicationContext();
        authToken = MyApplication.getAuthToken();
        if (authToken == null) { authToken = ""; }

        mRecipeAdapter = new RecipeAdapter(appContext, NUM_LIST_ITEMS, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
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
        new FetchRecipeListAsyncTask().execute("https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/list/", authToken);
    }

    public class FetchRecipeListAsyncTask extends AsyncTask<String, Void, String[]> {

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
                output = NetworkUtils.getResponseFromHttpUrl(urlString, "GET", authToken);
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

    @Override
    public void onListItemClick(String recipeDetails) {
        Intent intentToStartRecipeDetailActivity = new Intent(getActivity(), RecipeDetailsActivity.class);
        intentToStartRecipeDetailActivity.putExtra("RecipeDetails", recipeDetails);
        startActivity(intentToStartRecipeDetailActivity);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
