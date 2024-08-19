package com.example.dogcupid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogcupid.Adapters.ChatAdapter;
import com.example.dogcupid.Models.Chat;
import com.example.dogcupid.Models.Dog;
import com.example.dogcupid.Utilities.SignalManager;
import com.example.dogcupid.Utilities.SoundPlayer;
import com.example.dogcupid.interfaces.Callback_chatsItemClicked;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsActivity extends AppCompatActivity {

    private Toolbar chats_toolbar;
    private RecyclerView chats_recycler;
    private List<Chat> chatList;
    private ChatAdapter chatAdapter;
    private Callback_chatsItemClicked callbackChatsItemClicked;
    private ExtendedFloatingActionButton chats_profile_BTN;
    private ExtendedFloatingActionButton chats_home_BTN;
    private ExtendedFloatingActionButton chats_settings_BTN;
    private SoundPlayer soundPlayer;
    private String chatId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        findViews();
        initViews();
        loadChats();
    }

    private void findViews() {
        chats_toolbar = findViewById(R.id.chats_toolbar);
        chats_recycler = findViewById(R.id.chats_recycler);
        setSupportActionBar(chats_toolbar);

        chats_profile_BTN = findViewById(R.id.chats_profile_BTN);
        chats_home_BTN = findViewById(R.id.chats_home_BTN);
        chats_settings_BTN = findViewById(R.id.chats_settings_BTN);
    }

    private void initViews() {
        soundPlayer = new SoundPlayer(this);
        chats_profile_BTN.setOnClickListener(v -> transactToProfileActivity());
        chats_home_BTN.setOnClickListener(v -> transactToHomeActivity());
        chats_settings_BTN.setOnClickListener(v -> transactToSettingsActivity());
        getSupportActionBar().setTitle("Chats");
        chatId= getIntent().getStringExtra("chatId");

        chatList = new ArrayList<>();

        chatAdapter = new ChatAdapter(chatList);
        chats_recycler.setAdapter(chatAdapter);
        chats_recycler.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter.setChatCallback(new Callback_chatsItemClicked() {
            @Override
            public void chatsItemClicked(Chat chat) {
                if (callbackChatsItemClicked != null)
                    callbackChatsItemClicked.chatsItemClicked(chat);

                Intent intent = new Intent(ChatsActivity.this, SoloChatActivity.class);
                intent.putExtra("receiverId", chat.getUserReceiverId());
                intent.putExtra("senderId", chat.getUserSenderId());
                intent.putExtra("chatId", chatId);

                // Retrieve dog names and send them to SoloChatActivity
                fetchAndSendDogNames(chat, intent);
            }
        });
    }

    private void loadChats() {
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("chats");
        DatabaseReference dogsRef = FirebaseDatabase.getInstance().getReference("dogs");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    Chat chat = chatSnapshot.getValue(Chat.class);
                    if (chat != null) {
                        chatList.add(chat);
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    private void fetchAndSendDogNames(Chat chat, Intent intent) {
        DatabaseReference dogsRef = FirebaseDatabase.getInstance().getReference("dogs");
        String likingUserId = chat.getUserSenderId();
        String receiverId = chat.getUserReceiverId();

        dogsRef.child(likingUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String likingDogName = dataSnapshot.child("dogName").getValue(String.class);

                dogsRef.child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String likedDogName = dataSnapshot.child("dogName").getValue(String.class);

                        // Put dog names into intent
                        intent.putExtra("likingDogName", likingDogName);
                        intent.putExtra("likedDogName", likedDogName);

                        // Start SoloChatActivity
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }


    private void transactToProfileActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        finish();
    }

    private void transactToHomeActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void transactToSettingsActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
        finish();
    }
}
