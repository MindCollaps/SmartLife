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
import de.mindcollaps.smartlife.files.Room;

public class RoomsViewAdapter extends RecyclerView.Adapter<RoomsViewAdapter.ViewHolder>{

    public static final String TAG = "RoomsViewAdapter";

    private ArrayList<String> roomNames = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private Context mContext;

    public RoomsViewAdapter(ArrayList<Room> rooms, Context mContext) {
        this.mContext = mContext;
        this.rooms = rooms;

        for (Room r:rooms) {
            roomNames.add(r.getName());
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_roomitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, roomNames.get(position), Toast.LENGTH_SHORT).show();
                UpdateHandler.setRoom(rooms.get(position));
            }
        });
        holder.textView.setText(roomNames.get(position));
    }

    @Override
    public int getItemCount() {
        return roomNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.roomName);
            relativeLayout = itemView.findViewById(R.id.parentLayoutR);
        }
    }
}
