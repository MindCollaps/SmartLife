package de.mindcollaps.smartlife;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import de.mindcollaps.smartlife.files.Device;
import de.mindcollaps.smartlife.files.Room;
import de.mindcollaps.smartlife.ui.FragmentRoom;
import de.mindcollaps.smartlife.ui.FragmentRoomList;

public class ScreenActivity extends Activity {

    public static final String TAG = "ScreenActivity";


    @Override
    public void loadPalette() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_activity);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        loadFragment(new FragmentRoomList());
        setupUpdateHandler();

    }

    private void setupUpdateHandler(){
        new RoomUpdateHandler();
        new DeviceUpdateHandler();
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

    private ScreenActivity getOuterClass(){
        return this;
    }

    public class RoomUpdateHandler {

        public RoomUpdateHandler() {
            new Thread(new UpdateRunner()).start();
        }

        public void update(Room room){
            loadFragment(new FragmentRoom());
        }

        private class UpdateRunner implements Runnable {

            @Override
            public void run() {
                while (true){
                    while (!UpdateHandler.roomChanged){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    update(UpdateHandler.getRoom());
                }
            }
        }
    }

    public class DeviceUpdateHandler {

        public DeviceUpdateHandler() {
            new Thread(new UpdateRunner()).start();
        }

        public void update(Device device){
            startActivity(new Intent(getOuterClass(), DeviceActivity.class));
        }

        private class UpdateRunner implements Runnable {

            @Override
            public void run() {
                while (true){
                    while (!UpdateHandler.deviceChanged){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    update(UpdateHandler.getDevice());
                }
            }
        }
    }
}
