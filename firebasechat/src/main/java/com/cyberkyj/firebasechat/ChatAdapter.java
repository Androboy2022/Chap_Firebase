package com.cyberkyj.firebasechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Chat> dataList = new ArrayList<>();

    public ChatAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cell_chat, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = dataList.get(position);
        ChatViewHolder h = (ChatViewHolder)holder;
        h.txtUserName.setText(chat.getUserName());
        h.txtContents.setText(chat.getContents());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName;
        TextView txtContents;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtName);
            txtContents = itemView.findViewById(R.id.txtContends);
        }
    }

    public void addItem(Chat chat){
        dataList.add(chat);
    }
}
