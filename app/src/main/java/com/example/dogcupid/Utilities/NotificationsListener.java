package com.example.dogcupid.Utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationsListener {

    private DatabaseReference notificationsRef;

    public NotificationsListener(String userId) {
        notificationsRef = FirebaseDatabase.getInstance().getReference("notifications").child(userId);
        listenForNotifications();
    }

    private void listenForNotifications() {
        notificationsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                if (dataSnapshot.exists()) {
                    String type = dataSnapshot.child("type").getValue(String.class);
                    String senderId = dataSnapshot.child("senderId").getValue(String.class);

                    if ("like".equals(type)) {
                        String toastMessage = "You have a new like!";
                        SignalManager.getInstance().toast(toastMessage);
                        SignalManager.getInstance().vibrate(200);
                    }

                    // Remove the notification after it's been handled
                    dataSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle child changed if necessary
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Handle child removed if necessary
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle child moved if necessary
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}
