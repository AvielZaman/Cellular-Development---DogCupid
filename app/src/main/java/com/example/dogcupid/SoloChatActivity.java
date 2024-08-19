package com.example.dogcupid;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dogcupid.Adapters.MessageAdapter;
import com.example.dogcupid.Models.Chat;
import com.example.dogcupid.Models.Message;
import com.example.dogcupid.Utilities.SignalManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SoloChatActivity extends AppCompatActivity {

    private String receiverId= "", chatId = "", senderId= "";
    private DatabaseReference dbReference;

    private ShapeableImageView chat_send_message_BTN;
    private EditText chat_message_edit;
    private RecyclerView chat_recycler;

    private MessageAdapter messageAdapter;
    private Toolbar chat_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_chat);

        findViews();
        initViews();
    }

    private void findViews() {
        chat_toolbar = findViewById(R.id.chat_toolbar);
        chat_send_message_BTN = findViewById(R.id.chat_send_message_BTN);
        chat_recycler = findViewById(R.id.chat_recycler);
        chat_message_edit = findViewById(R.id.chat_message_edit);
    }

    private void initViews() {
        setSupportActionBar(chat_toolbar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        receiverId = getIntent().getStringExtra("receiverId");
        senderId = getIntent().getStringExtra("senderId");
        chatId = getIntent().getStringExtra("chatId");

        // Get the liking and liked dog names from the intent
        String likingDogName = getIntent().getStringExtra("likingDogName");
        String likedDogName = getIntent().getStringExtra("likedDogName");

        // Initialize chat ID
        if (receiverId != null && chatId == null) {
            fetchChatIdFromDatabase(receiverId, senderId);
        }else if (chatId != null) {
            // Only call loadMessages if chatId is not null
            loadMessages();
        }

        // Set the chat title based on the app's context
        if (receiverId.equals(currentUserId)) {
            // If it is the liked dog's app, set the title to the name of the liking dog
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(likingDogName);
            }
        } else {
            // If it is the liking dog's app, set the title to the name of the liked dog
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(likedDogName);
            }
        }

        // Initialize the RecyclerView and Adapter
        messageAdapter = new MessageAdapter(this);
        chat_recycler.setAdapter(messageAdapter);
        chat_recycler.setLayoutManager(new LinearLayoutManager(this));



        // Set up the send button
        chat_send_message_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = chat_message_edit.getText().toString();
                if (!messageText.trim().isEmpty()) {
                    sendMessage(messageText);
                } else {
                    SignalManager.getInstance().toast("Message cannot be empty");
                    SignalManager.getInstance().vibrate(200);
                }
            }
        });
    }

    private void loadMessages() {
        // Set up the database reference
        dbReference = FirebaseDatabase.getInstance().getReference("Texts per chat").child(chatId);
        dbReference.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                messageAdapter.clear();
                messageAdapter.addAll(messages);
                chat_recycler.scrollToPosition(messageAdapter.getItemCount() - 1);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }


    private void sendMessage(String messageText) {
        String messageId = UUID.randomUUID().toString();
        String senderId = FirebaseAuth.getInstance().getUid();
        long timestamp = System.currentTimeMillis(); // Get current timestamp

        Message message = new Message(messageId, senderId, messageText, timestamp);

        // Add the message to the chat room
        dbReference.child(messageId).setValue(message)
                .addOnSuccessListener(aVoid -> {
                    // Optionally handle success
                })
                .addOnFailureListener(e -> {
                    SignalManager.getInstance().toast("Failed to send message");
                    SignalManager.getInstance().vibrate(200);
                });

        // Update the UI
        messageAdapter.add(message);
        chat_recycler.scrollToPosition(messageAdapter.getItemCount() - 1);
        chat_message_edit.setText("");
    }

    private void fetchChatIdFromDatabase(String receiverId, String senderId) {
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chats");

        // Separate queries for clarity
        Query querySender = chatsRef.orderByChild("userSenderId").equalTo(senderId);
        Query queryReceiver = chatsRef.orderByChild("userReceiverId").equalTo(senderId);

        // Combine queries using a listener for each
        querySender.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                        Chat chat = chatSnapshot.getValue(Chat.class);
                        if (chat != null && chat.getUserReceiverId().equals(receiverId)) {
                            chatId = chatSnapshot.getKey();
                            // Ensure chatId is set before calling loadMessages
                            loadMessages();
                            return;
                        }
                    }
                }

                // If not found in sender query, check receiver query
                queryReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                                Chat chat = chatSnapshot.getValue(Chat.class);
                                if (chat != null && chat.getUserSenderId().equals(receiverId)) {
                                    chatId = chatSnapshot.getKey();
                                    // Ensure chatId is set before calling loadMessages
                                    loadMessages();
                                    return;
                                }
                            }
                        }

                        // Handle case where chat is not found
                        SignalManager.getInstance().toast("Chat not found");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        SignalManager.getInstance().toast("Error accessing database");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                SignalManager.getInstance().toast("Error accessing database");
            }
        });
    }

}
