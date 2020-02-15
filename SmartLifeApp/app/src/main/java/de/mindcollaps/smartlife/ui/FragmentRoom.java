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

import de.mindcollaps.smartlife.adapter.DeviceViewAdapter;
import de.mindcollaps.smartlife.R;
import de.mindcollaps.smartlife.UpdateHandler;
import de.mindcollaps.smartlife.files.Room;

public class FragmentRoom extends Fragment {


    public static final String TAG = "FragmentRoom";

    RecyclerView listView;
    Room room;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPalette();
        room = UpdateHandler.getRoom();
        updateDevice();
    }

    private void loadPalette() {
        listView = getActivity().findViewById(R.id.deviceContent);
    }

    private void updateDevice(){
        DeviceViewAdapter deviceViewAdapter = new DeviceViewAdapter(room.getDevices(), getActivity());
        listView.setAdapter(deviceViewAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
