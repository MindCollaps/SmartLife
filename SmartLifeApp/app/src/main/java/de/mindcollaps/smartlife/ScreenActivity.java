package de.mindcollaps.smartlife;

import android.os.Bundle;
import android.os.Debug;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class ScreenActivity extends Activity {


    @Override
    public void loadPalette() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_activity);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        loadFragment(new FragmentRooms());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rooms_menu, menu);
        return true;
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContent, fragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }
}
