package com.example.dogcupid.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogcupid.Models.Chat;
import com.example.dogcupid.R;
import com.example.dogcupid.interfaces.Callback_chatsItemClicked;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> chatList;
    private Callback_chatsItemClicked chatsItemClicked;


    public ChatAdapter(List<Chat> chats) {
        this.chatList = chats;
    }

    public void setChatCallback(Callback_chatsItemClicked chatCallback) {
        this.chatsItemClicked = chatCallback;
    }


    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new ChatAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference dogsRef = FirebaseDatabase.getInstance().getReference("dogs");
        dogsRef.child(chat.getUserReceiverId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String receiverDogName = dataSnapshot.child("dogName").getValue(String.class);

                if (chat.getUserSenderId().equals(currentUserId)) {
                    // Current user is the sender, so set the title to the receiver's dog name
                    holder.user_row_username.setText(receiverDogName);
                } else {
                    // Current user is the receiver, so set the title to the sender's dog name
                    dogsRef.child(chat.getUserSenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String senderDogName = dataSnapshot.child("dogName").getValue(String.class);
                            holder.user_row_username.setText(senderDogName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle possible errors.
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        holder.user_CARD.setOnClickListener(v -> {
            if (chatsItemClicked != null)
                chatsItemClicked.chatsItemClicked(chat);
        });
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void setChatList(List<Chat> chats) {
        this.chatList = chats;
    }


    private Chat getItem(int position) {
        return chatList.get(position);
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final CardView user_CARD;
        private MaterialTextView user_row_username;


        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            user_CARD = itemView.findViewById(R.id.user_CARD);
            user_row_username= itemView.findViewById(R.id.user_row_username);
        }
    }

}