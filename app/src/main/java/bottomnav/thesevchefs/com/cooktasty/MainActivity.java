package bottomnav.thesevchefs.com.cooktasty;

// FOR MAIN PAGE AFTER LOGIN

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Recipe", R.drawable.ic_restaurant_menu_black_24dp, R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Favourites", R.drawable.ic_star_black_24dp, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Upload", R.drawable.ic_add_black_24dp, R.color.colorAccent);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Activity", R.drawable.ic_notifications_black_24dp, R.color.colorAccent);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Me", R.drawable.ic_person_black_24dp, R.color.colorAccent);

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
                    RecipeListFragment recipeFragment = new RecipeListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentp, recipeFragment).commit();
                } else if (position == 1) {
                    FavRecipeFragment favRecipeFragment = new FavRecipeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentp, favRecipeFragment).commit();
                } else if (position == 2) {
                    dispatchTakePictureIntent();
                } else if (position == 3) {
                    TimelineActivityFragment timelineActivityFragment = new TimelineActivityFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentp, timelineActivityFragment).commit();
                } else if (position == 4) {
                    BlankFragment settingsFragment = new BlankFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentp, settingsFragment).commit();
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

//        load default fragment
        RecipeListFragment recipeFragment = new RecipeListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentp, recipeFragment).commit();

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
