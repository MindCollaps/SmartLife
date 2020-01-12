package de.mindcollaps.smartlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadPalette();

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;

                switch (menuItem.getItemId()){
                    case R.id.gotoBridge:
                        fragment = new FragmentBridgeConnect();
                        break;
                    case R.id.gotoConfiguration:
                        fragment = new FragmentConfig();
                        break;
                    case R.id.gotoSettings:
                        fragment = new FragmentSettings();
                        break;
                    case R.id.gotoSmartLife:
                        fragment = new FragmentSmartLife();
                        break;
                }

                loadFragment(fragment);
                return true;
            }
        });
    }

    private void loadPalette(){
        menu = findViewById(R.id.navBar);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }
}
