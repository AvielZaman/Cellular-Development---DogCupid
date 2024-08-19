package com.example.dogcupid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogcupid.Models.Message;

import com.example.dogcupid.R;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private Context context;
    private List<Message> messageList;

    public MessageAdapter(Context context) {
        this.context = context;
        this.messageList = new ArrayList<>();
    }

    public void add(Message message) {
        messageList.add(message);
    }

    public void addAll(List<Message> messages) {
        this.messageList.addAll(messages);
        notifyDataSetChanged();
    }


    public void clear(){
        messageList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_sent, parent,false);
            return new MyViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_received, parent,false);
            return new MyViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.message_sent.setText(message.getMessage());

        } else {
            holder.message_received.setText(message.getMessage());

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return VIEW_TYPE_SENT;
        }
        else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private final MaterialTextView message_sent, message_received;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message_sent= itemView.findViewById(R.id.message_sent);
            message_received= itemView.findViewById(R.id.message_received);
        }
    }
}
