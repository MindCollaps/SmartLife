package de.mindcollaps.smartlife.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.mindcollaps.smartlife.R;
import de.mindcollaps.smartlife.adapter.RoomsViewAdapter;
import de.mindcollaps.smartlife.files.Device;
import de.mindcollaps.smartlife.files.Room;

public class FragmentRoomList extends Fragment {

    public static final String TAG = "FragmentRoomList";

    RecyclerView listView;

    private void loadPalette() {
        listView = getActivity().findViewById(R.id.roomsContent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_roomlist, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPalette();
        updateRooms();
    }

    public void updateRooms(){
        ArrayList<Device> devices = new ArrayList<>();
        Device device = new Device();
        device.setName("hello i bims der device von der Laden");
        Device device1 = new Device();
        device1.setName("ich raste auskalsddlaksdjalkgtjlas");
        devices.add(device);
        devices.add(device1);
        Room room = new Room();
        room.setName("Ich bin der Raum da hinten lol");
        room.setDevices(devices);
        Room room1 = new Room();
        room1.setName("ich bin 2 der rai, asdaksdjl");
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(room);
        rooms.add(room1);
        RoomsViewAdapter adapter = new RoomsViewAdapter(rooms, getActivity());
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
