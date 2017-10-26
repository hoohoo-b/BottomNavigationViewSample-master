package bottomnav.hitherejoe.com.bottomnavigationsample;

// FOR RECIPE TAB

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.TextView;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.RecipeAdapter;
import bottomnav.hitherejoe.com.bottomnavigationsample.R;


public class RecipeActivity extends Fragment {

    private static final int NUM_LIST_ITEMS = 100;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    public static RecipeActivity newInstance() {
        RecipeActivity fragment = new RecipeActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

}
