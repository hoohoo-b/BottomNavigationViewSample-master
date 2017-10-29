package bottomnav.hitherejoe.com.bottomnavigationsample;

// FOR FAVOURITE ACTIVITY... CURRENTLY BLANK

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bottomnav.hitherejoe.com.bottomnavigationsample.utilities.RecipeAdapter;


public class FavRecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 100;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private Context appContext;

    public static FavRecipeFragment newInstance() {
        FavRecipeFragment fragment = new FavRecipeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        appContext = getActivity().getApplicationContext();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_favrecipelist);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipeAdapter = new RecipeAdapter(appContext, NUM_LIST_ITEMS, this);
        mRecyclerView.setAdapter(mRecipeAdapter);
//
//        mRecyclerView.setVisibility(View.VISIBLE);

        return rootView;
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
