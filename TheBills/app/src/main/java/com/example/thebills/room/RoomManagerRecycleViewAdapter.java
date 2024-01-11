package com.example.thebills.room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomManagerRecycleViewAdapter extends RecyclerView.Adapter<RoomManagerRecycleViewAdapter.MyViewHolder> {

    private Context context;
    private Map<String,String> roomMap;
    private RoomRecycleViewEvent listener;

    public RoomManagerRecycleViewAdapter(Context context, Map<String, String> roomMap, RoomRecycleViewEvent listener) {
        this.context = context;
        this.roomMap = roomMap;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomManagerRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_row, parent,false);
        return new RoomManagerRecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomManagerRecycleViewAdapter.MyViewHolder holder, int position) {

        List<String> keys = new ArrayList<>(roomMap.keySet());
        String key = keys.get(position);
        String name = roomMap.get(key);

        holder.roomName.setText(name);
        holder.roomKey.setText(key);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return roomMap.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView roomName;
        public TextView roomKey;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.textViewRoomName);
            roomKey = itemView.findViewById(R.id.textViewRoomKey);
            cardView = itemView.findViewById(R.id.cardViewRecycleViewRooms);

        }

    }
}
