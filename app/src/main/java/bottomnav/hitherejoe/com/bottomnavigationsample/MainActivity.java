package bottomnav.hitherejoe.com.bottomnavigationsample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static Button btn_settings;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_recipe:
                                fragment = new Frag1();
                                FragmentManager fm = getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentp, fragment);
                                ft.commit();
                                break;
                            case R.id.action_favourite:fragment = new Frag2();
                                fm = getSupportFragmentManager();
                                ft = fm.beginTransaction();
                                ft.replace(R.id.fragmentp, fragment);
                                ft.commit();
                                break;
                            case R.id.action_me:fragment = new BlankFragment();
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


    }

}
