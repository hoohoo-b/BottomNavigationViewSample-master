package bottomnav.hitherejoe.com.bottomnavigationsample;

// FOR MAIN PAGE AFTER LOGIN

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.JsonReader;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.NetworkUtils;
import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.RecipeAdapter;

import static android.R.attr.bitmap;
import static bottomnav.hitherejoe.com.bottomnavigationsample.R.id.imageView;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

//    private BottomBar mBottomBar;

    private static final int NUM_LIST_ITEMS = 100;

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private String authToken = "";

    private Bitmap recipeImage;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String token = MyApplication.getAuthToken();
        if (token != null) {
            authToken = token;
        }

        final Context context = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_recipelist);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipeAdapter = new RecipeAdapter(this, NUM_LIST_ITEMS, this);
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
                if (position == 0) {
                    RecipeActivity recipeFragment = new RecipeActivity();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentp, recipeFragment).commit();
                } else if (position == 1) {
                    Class favouriteActivityClass = FavouriteActivity.class;
                    Intent intentToStartDetailActivity = new Intent(context, favouriteActivityClass);
                    startActivity(intentToStartDetailActivity);
                } else if (position == 2) {
                    dispatchTakePictureIntent();
                } else if (position == 4) {
                    BlankFragment documentaryFragment = new BlankFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentp, documentaryFragment).commit();
                }

                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

        loadRecipeListData();
    }

    public void loadRecipeListData() {
        showRecipeListDataView();
        getRecipeNameList();
    }

    private void getRecipeNameList() {
        new FetchRecipeList().execute("https://hidden-springs-80932.herokuapp.com/api/v1.0/recipe/list/", "GET", authToken);
    }

    private void showRecipeListDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void onListItemClick(String recipeList) {
        setContentView(R.layout.fragment_recipe_details);
        Context context = this;
        Class recipeDetailsActivityClass = RecipeDetailsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, recipeDetailsActivityClass);
        intentToStartDetailActivity.putExtra("RecipeDetails", recipeList);
        startActivity(intentToStartDetailActivity);
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
            String authToken = params[2];

            String output;
            String[] recipeList = null;

            try {
                output = NetworkUtils.getResponseFromHttpUrl(urlString, requestMethod, authToken);
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

    public void dispatchTakePictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Intent intent = new Intent(MainActivity.this, UploadActivity.class);
            intent.setData(selectedImage);
            startActivity(intent);
        }
    }

}
