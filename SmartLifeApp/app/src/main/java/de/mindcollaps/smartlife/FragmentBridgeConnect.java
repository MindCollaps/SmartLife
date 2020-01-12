package de.mindcollaps.smartlife;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentBridgeConnect extends Fragment {

    Button buttonDisconnect;
    Button buttonConnect;
    ImageButton buttonSearch;

    EditText txtBridgeName;
    TextView txtBridgeConnectStatus;
    ImageView iconBridgeConnectStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bridge_connect, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPallet();
        setUpListener();

    }

    private void loadPallet(){
        buttonConnect = getActivity().findViewById(R.id.buttonConnect);
        buttonDisconnect = getActivity().findViewById(R.id.buttonConnect);
        buttonSearch = getActivity().findViewById(R.id.buttonSearch);
        txtBridgeName = getActivity().findViewById(R.id.txtBridgeName);
        txtBridgeConnectStatus = getActivity().findViewById(R.id.txtBridgeName);
        iconBridgeConnectStatus = getActivity().findViewById(R.id.iconBridgeConnectStatus);
    }

    private void setUpListener(){
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchClicked();
            }
        });

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDisconnectClicked();
            }
        });

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnectClicked();
            }
        });
    }

    private void onDisconnectClicked(){

    }

    private void onConnectClicked(){

    }

    private void onSearchClicked(){

    }
}
