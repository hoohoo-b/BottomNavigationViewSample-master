package bottomnav.hitherejoe.com.bottomnavigationsample;

// FOR MAIN PAGE AFTER LOGIN

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.JsonReader;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.NetworkUtils;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.RecipeAdapter;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

//    private BottomBar mBottomBar;

    private static final int NUM_LIST_ITEMS = 100;
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
        mRecipeAdapter = new RecipeAdapter(this ,NUM_LIST_ITEMS, this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Recipe", R.drawable.ic_free_breakfast_black_24dp, R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Favourites", R.drawable.ic_star_black_24dp, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Upload", R.drawable.ic_add_black_24dp, R.color.colorAccent);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Activity", R.drawable.ic_notifications_black_24dp, R.color.colorAccent);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Me", R.drawable.ic_person_black_24dp, R.color.colorAccent);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });


/*        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (resId == R.id.bb_menu_recents) {
                    // The user selected the "Recents" tab.
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (resId == R.id.bb_menu_recents) {
                    // The user reselected the "Recents" tab. React accordingly.
                }
            }
        });*/

/*        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);*/

/*        bottomNavigationView.setOnNavigationItemSelectedListener(
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
                });*/

        loadRecipeListData();
    }

    private void loadRecipeListData() {
        showRecipeListDataView();
        getRecipeNameList();
    }

    private void getRecipeNameList() {
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
            String[] recipeList = null;

            try {
                output = NetworkUtils.getResponseFromHttpUrl(urlString, requestMethod, json);
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

    public void onListItemClick(int clickedItemIndex) {
        setContentView(R.layout.fragment_recipe_details);
    }

}
