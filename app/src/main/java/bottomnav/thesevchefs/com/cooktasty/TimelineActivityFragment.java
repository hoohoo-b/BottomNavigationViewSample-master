package bottomnav.thesevchefs.com.cooktasty;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;

import java.util.List;

import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.APICallback;
import bottomnav.thesevchefs.com.cooktasty.cooktastyapi.UserAPI;
import bottomnav.thesevchefs.com.cooktasty.entity.ActivityTimeline;
import bottomnav.thesevchefs.com.cooktasty.utilities.EndlessScrollListener;
import bottomnav.thesevchefs.com.cooktasty.utilities.TimelineListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TimelineActivityFragment extends Fragment {

    private Context appContext;
    private String authToken;
    private TimelineListAdapter mTimelineListAdapter;

    @BindView(R.id.lv_timelinelist) ListView mListView;

    private Unbinder unbinder;
    private android.support.v7.app.ActionBar mActionBar;

    public static TimelineActivityFragment newInstance() {
        TimelineActivityFragment fragment = new TimelineActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_findchefs:
                System.out.println("----------1---------");
                return true;
            case R.id.action_dashboard:
                System.out.println("----------2---------");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_activity_timeline, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        appContext = getActivity().getApplicationContext();
        authToken = MyApplication.getAuthToken();
        if (authToken == null) { authToken = ""; }

        mTimelineListAdapter = new TimelineListAdapter(appContext);
        EndlessScrollListener scrollListener = new EndlessScrollListener(){
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadTimelinesToListView(appContext, authToken, page, mTimelineListAdapter);
                return true;
            }
        };

        loadTimelinesToListView(appContext, authToken, 1, mTimelineListAdapter);
        mListView.setAdapter(mTimelineListAdapter);
        mListView.setOnScrollListener(scrollListener);

        return rootView;
    }

    public void loadTimelinesToListView(Context ctxt, String token, int page, final TimelineListAdapter timelineListAdapter){
        UserAPI.userActivityTimeLineAPI(ctxt, token, page, new APICallback(){
            @Override
            public void onSuccess(Object result) {
                List<ActivityTimeline> timelines = (List<ActivityTimeline>) result;
                System.out.println(timelines.size() + "------------");
                timelineListAdapter.addActivityTimelineListData(timelines);
            }
            @Override
            public void onError(Object error) {
                VolleyError volleyError = (VolleyError) error;
                volleyError.printStackTrace();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_timeline_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
