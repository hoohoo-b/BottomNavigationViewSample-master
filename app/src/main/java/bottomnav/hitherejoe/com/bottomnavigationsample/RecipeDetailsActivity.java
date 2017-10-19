package bottomnav.hitherejoe.com.bottomnavigationsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Allets on 19/10/2017.
 */

public class RecipeDetailsActivity extends AppCompatActivity {

    private String recipeList;
    private TextView mRecipeName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe_details);

        mRecipeName = (TextView) findViewById(R.id.tv_recipe_details_name);

        Intent intentThatStartedThisActivity = getIntent();

        // COMPLETED (2) Display the weather forecast that was passed from MainActivity
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                recipeList = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mRecipeName.setText(recipeList);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_details_menu, menu);
        return true;
    }

}
