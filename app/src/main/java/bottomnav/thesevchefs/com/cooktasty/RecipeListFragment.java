package bottomnav.thesevchefs.com.cooktasty;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.*;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.APICallback;
import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.RecipeAPI;
import bottomnav.thesevchefs.com.cooktasty.entity.Recipe;
import bottomnav.thesevchefs.com.cooktasty.utilities.EndlessRecyclerViewScrollListener;
import bottomnav.thesevchefs.com.cooktasty.utilities.RecipeListAdapter;

import static bottomnav.thesevchefs.com.cooktasty.cooktastyapi.RecipeAPI.getRecipeListAPI;


public class RecipeListFragment extends Fragment implements RecipeListAdapter.ListItemClickListener {

    private Context appContext;
    private String authToken;

    private RecipeListAdapter mRecipeListAdapter;

    @BindView(R.id.rv_recipelist)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.iv_recommended_recipe_image)
    ImageView mRecommendedImage;
    @BindView(R.id.tv_recommended_recipe_name)
    TextView mRecommendedName;

    private Recipe recommendedRecipe;

    private Unbinder unbinder;

    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        appContext = getActivity().getApplicationContext();
        authToken = MyApplication.getAuthToken();
        if (authToken == null) {
            authToken = "";
        }

        mRecipeListAdapter = new RecipeListAdapter(appContext, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadRecipeListToRecyclerView(appContext, authToken, page, mRecipeListAdapter);
            }
        };

        loadRecipeListToRecyclerView(appContext, authToken, 1, mRecipeListAdapter);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecipeListAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.addOnScrollListener(scrollListener);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showRecipeListDataView();
        showRecommendedRecipe();
    }

    private void showRecommendedRecipe() {
        RecipeAPI.getRecommendedRecipeAPI(getContext(), "", new APICallback() {

            @Override
            public void onSuccess(Object result) {
                recommendedRecipe = (Recipe) result;
                Picasso.with(getActivity())
                        .load(recommendedRecipe.image_url)
                        .into(mRecommendedImage);
                mRecommendedName.setText(recommendedRecipe.name);
            }

            @Override
            public void onError(Object result) {
                VolleyError volleyError = (VolleyError) result;
                volleyError.printStackTrace();
            }
        });
    }

    public void loadRecipeListToRecyclerView(Context ctxt, String token, int getPage, final RecipeListAdapter mRecipeListAdapter) {
        RecipeAPI.getRecipeListAPI(ctxt, token, getPage, "", new APICallback() {
            public void onSuccess(Object result) {
                List<Recipe> recipelist = (List<Recipe>) result;
                mRecipeListAdapter.addRecipeListData(recipelist);
            }

            public void onError(Object error) {
                VolleyError volleyError = (VolleyError) error;
                volleyError.printStackTrace();
            }
        });
    }

    private void showRecipeListDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(Recipe recipeDetail) {
        Intent intentToStartRecipeDetailActivity = new Intent(getActivity(), RecipeDetailsActivity.class);
        intentToStartRecipeDetailActivity.putExtra("RecipeId", recipeDetail.id);
        startActivity(intentToStartRecipeDetailActivity);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getRecipeListAPI(getActivity(), authToken, 1, query, new APICallback() {
                    @Override
                    public void onSuccess(Object result) {
                        mRecipeListAdapter.clearList();
                        List<Recipe> recipeList = (List<Recipe>) result;
                        mRecipeListAdapter.addRecipeListData(recipeList);
                    }

                    @Override
                    public void onError(Object result) {
                        VolleyError volleyError = (VolleyError) result;
                        volleyError.printStackTrace();
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    return false;
                } else {
                    getRecipeListAPI(getActivity(), authToken, 1, newText, new APICallback() {
                        @Override
                        public void onSuccess(Object result) {
                            mRecipeListAdapter.clearList();
                            List<Recipe> recipeList = (List<Recipe>) result;
                            mRecipeListAdapter.addRecipeListData(recipeList);
                        }

                        @Override
                        public void onError(Object result) {
                            VolleyError volleyError = (VolleyError) result;
                            volleyError.printStackTrace();
                        }
                    });

                    return true;
                }
            }

            ;
        })
        ;
    }

    ;

    public long getRecommendedRecipeId(){
        return recommendedRecipe.id;
    }
}

