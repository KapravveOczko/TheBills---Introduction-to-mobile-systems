package com.example.thebills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomManagerRecycleViewAdapter extends RecyclerView.Adapter<RoomManagerRecycleViewAdapter.MyViewHolder> {

    Context context;
    Map<String,String> roomMap;

    public RoomManagerRecycleViewAdapter(Context context, Map<String, String> roomMap) {
        this.context = context;
        this.roomMap = roomMap;
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
    }

    @Override
    public int getItemCount() {
        return roomMap.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView roomName;
        TextView roomKey;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.textViewRoomName);
            roomKey = itemView.findViewById(R.id.textViewRoomKey);
        }
    }
}
