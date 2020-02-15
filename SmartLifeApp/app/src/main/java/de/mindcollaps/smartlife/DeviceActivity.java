package de.mindcollaps.smartlife;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import de.mindcollaps.smartlife.ui.FragmentDevice;
import de.mindcollaps.smartlife.ui.FragmentDeviceConfig;

public class DeviceActivity extends Activity {

    BottomNavigationView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_device);
        super.onCreate(savedInstanceState);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()){
                    case R.id.navigation_device:
                        fragment = new FragmentDevice();
                        break;

                    case R.id.navigation_config:
                        fragment = new FragmentDeviceConfig();
                        break;
                }
                loadFragment(fragment);
                return true;
            }
        });
    }

    @Override
    public void loadPalette() {
        menu = findViewById(R.id.navMenu);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContentD, fragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }
}
