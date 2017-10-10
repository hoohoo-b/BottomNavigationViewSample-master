package bottomnav.hitherejoe.com.bottomnavigationsample;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.NetworkUtils;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.RecipeAdapter;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {


    private static Button btn_settings;
    private Fragment fragment;

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_recipelist);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipeAdapter = new RecipeAdapter();
        mRecyclerView.setAdapter(mRecipeAdapter);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_recipe:
                                fragment = new RecipeActivity();
                                FragmentManager fm = getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentp, fragment);
                                ft.commit();
                                break;
                            case R.id.action_favourite:
                                fragment = new FavouriteActivity();
                                fm = getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentp, fragment);
                                ft.commit();
                                break;
                            case R.id.action_me:
                                fragment = new BlankFragment();
                                fm = getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentp, fragment);
                                ft.commit();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });

        loadRecipeListData();
    }

    private void loadRecipeListData() {
        showRecipeListDataView();
        callApi();
    }

    private void callApi() {
        new FetchRecipeList().execute("https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/list/", "GET", "");
    }

    private void showRecipeListDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchRecipeList extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            String urlString = params[0];
            String requestMethod = params[1];
            String json = params[2];

            String output;
            String[] recipeListData = null;

            try {
                output = NetworkUtils.getResponseFromHttpUrl(urlString, requestMethod, json);
                recipeListData = retrieveRecipeNameList(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return recipeListData;
        }

        private String[] retrieveRecipeNameList(String output) throws JSONException {
            ArrayList<String> listData = new ArrayList<String>();
            JSONObject recipeListJson = new JSONObject(output);
            String[] recipeListData = null;

            JSONArray recipeData = recipeListJson.getJSONArray("data");
            if (recipeData != null) {
                for (int i = 0; i < recipeData.length(); i++) {
                    JSONObject recipeJson = recipeData.getJSONObject(i);
                    listData.add(recipeJson.getString("name"));
                }
                recipeListData = listData.toArray(new String[listData.size()]);
            }
            return recipeListData;
        }


        @Override
        protected void onPostExecute(String[] recipeListData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (recipeListData != null) {
                showRecipeListDataView();
                                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                mRecipeAdapter.setRecipeListData(recipeListData);
            } else {
                showErrorMessage();
            }
        }

    }

}
