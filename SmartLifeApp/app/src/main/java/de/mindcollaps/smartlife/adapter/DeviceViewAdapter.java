package de.mindcollaps.smartlife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.mindcollaps.smartlife.R;
import de.mindcollaps.smartlife.UpdateHandler;
import de.mindcollaps.smartlife.files.Device;

public class DeviceViewAdapter extends RecyclerView.Adapter<DeviceViewAdapter.ViewHolder>{

    public static final String TAG = "DeviceViewAdapter";

    private ArrayList<String> deviceNames = new ArrayList<>();
    private ArrayList<Device> device = new ArrayList<>();
    private Context mContext;

    public DeviceViewAdapter(ArrayList<Device> device, Context mContext) {
        this.mContext = mContext;
        this.device = device;

        for (Device r: device) {
            deviceNames.add(r.getName());
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_deviceitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, deviceNames.get(position), Toast.LENGTH_SHORT).show();
                UpdateHandler.setDevice(device.get(position));
            }
        });
        holder.textView.setText(deviceNames.get(position));
    }

    @Override
    public int getItemCount() {
        return deviceNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.deviceName);
            relativeLayout = itemView.findViewById(R.id.parentLayoutD);
        }
    }
}
