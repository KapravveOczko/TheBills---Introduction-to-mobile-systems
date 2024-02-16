package com.example.thebills.room;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Adapter class for RecyclerView to manage room data
public class RoomManagerRecycleViewAdapter extends RecyclerView.Adapter<RoomManagerRecycleViewAdapter.MyViewHolder> {

    private Context context;
    private Map<String,String> roomMap;
    private RoomRecycleViewEvent listener;

    // Constructor to initialize adapter with data and listener
    public RoomManagerRecycleViewAdapter(Context context, Map<String, String> roomMap, RoomRecycleViewEvent listener) {
        this.context = context;
        this.roomMap = roomMap;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RoomManagerRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_row, parent,false);
        return new RoomManagerRecycleViewAdapter.MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager), allows to copy roomId
    @Override
    public void onBindViewHolder(@NonNull RoomManagerRecycleViewAdapter.MyViewHolder holder, int position) {
        List<String> keys = new ArrayList<>(roomMap.keySet());
        String key = keys.get(position);
        String name = roomMap.get(key);

        holder.roomName.setText(name);
        holder.roomKey.setText(key);

        holder.cardView.setOnClickListener(v -> listener.onItemClick(holder.getAdapterPosition()));

        holder.roomKey.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("text", key);
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context,"room Id copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return roomMap.size();
    }

    // ViewHolder class to hold the views of a single row in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView roomName;
        public Button roomKey;
        public CardView cardView;

        // Constructor to initialize views of the ViewHolder
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.textViewRoomName);
            roomKey = itemView.findViewById(R.id.buttonCopyRoomId);
            cardView = itemView.findViewById(R.id.cardViewRecycleViewRooms);

        }
    }
}
