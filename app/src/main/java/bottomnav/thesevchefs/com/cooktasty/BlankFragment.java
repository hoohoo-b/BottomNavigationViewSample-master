package bottomnav.thesevchefs.com.cooktasty;

// FOR THE ACCOUNTS ME PAGE

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BlankFragment extends Fragment {

    @BindView(R.id.btn_settings) Button btn_settings;
    @BindView(R.id.btn_logout) Button btn_logout;
    private Unbinder unbinder;

    public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.btn_logout)
    public void onClickLogoutButton(View view) {
        MyApplication.setAuthToken(null);
        MyApplication.setEmail(null);
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @OnClick(R.id.btn_settings)
    public void onClickSettingButton(View view) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
