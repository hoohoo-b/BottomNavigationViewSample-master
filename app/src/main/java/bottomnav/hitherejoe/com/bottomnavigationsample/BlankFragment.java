package bottomnav.hitherejoe.com.bottomnavigationsample;

// FOR THE ACCOUNTS ME PAGE

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class BlankFragment extends Fragment implements View.OnClickListener {
//    Button Setting = (Button)findViewById(R.id.open_activity_button);

    private static Button btn_settings;
    private static Button btn_logout;

    public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
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
        View myView = inflater.inflate(R.layout.fragment_settings, container, false);
        btn_settings = (Button) myView.findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this);
        btn_logout = (Button) myView.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);

        return myView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.btn_logout:
                startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}
