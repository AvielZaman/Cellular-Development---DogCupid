package com.example.dogcupid.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dogcupid.Adapters.DogAdapter;
import com.example.dogcupid.ChatsActivity;
import com.example.dogcupid.HomeActivity;
import com.example.dogcupid.Models.Chat;
import com.example.dogcupid.Models.Dog;
import com.example.dogcupid.R;
import com.example.dogcupid.Utilities.SignalManager;
import com.example.dogcupid.Utilities.SoundPlayer;
import com.example.dogcupid.interfaces.Callback_DogsItemClicked;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DogsFragment extends Fragment {

    private RecyclerView main_LST_dogs;
    private Callback_DogsItemClicked callbackDogsItemClicked;
    private ArrayList<Dog> dogs;
    private ExtendedFloatingActionButton discover_back_BTN;
    private ExtendedFloatingActionButton button_filter_breeds;
    private ArrayList<String> uniqueBreeds;
    private int currentIndex = 0;
    private DogAdapter dogAdapter;
    private SoundPlayer soundPlayer;


    public DogsFragment() {
        dogs = new ArrayList<>();
        uniqueBreeds = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dogs, container, false);

        findViews(v);
        initViews();
        fetchDogsFromFirebase();
        return v;
    }

    private void initViews() {
        soundPlayer = new SoundPlayer(getContext());
        discover_back_BTN.setOnClickListener(v -> transactToHomeActivity());
        button_filter_breeds.setOnClickListener(v -> showBreedFilterDialog());

        SignalManager.init(getContext());
        dogAdapter = new DogAdapter(dogs);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        main_LST_dogs.setLayoutManager(linearLayoutManager);
        main_LST_dogs.setAdapter(dogAdapter);
        dogAdapter.setDogCallback(new Callback_DogsItemClicked() {
            @Override
            public void dogsItemClicked(Dog dog) {
                if (callbackDogsItemClicked != null)
                    callbackDogsItemClicked.dogsItemClicked(dog);

            }
        });
    }


    private void findViews(View v) {
        main_LST_dogs = v.findViewById(R.id.main_LST_dogs);
        discover_back_BTN = v.findViewById(R.id.discover_back_BTN);
        button_filter_breeds = v.findViewById(R.id.button_filter_breeds);
    }

    public void setCallbackDogsItemClicked(Callback_DogsItemClicked callbackDogsItemClicked) {
        this.callbackDogsItemClicked = callbackDogsItemClicked;
    }


    private void transactToHomeActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(getActivity(), HomeActivity.class);
        startActivity(i);
    }

    private void fetchDogsFromFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String currentUserId = user.getUid();

        DatabaseReference likedDogsRef = FirebaseDatabase.getInstance().getReference("liked dogs per user")
                .child(currentUserId);

        likedDogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot likedSnapshot) {
                ArrayList<String> likedDogsList = new ArrayList<>();
                for (DataSnapshot ds : likedSnapshot.getChildren()) {
                    likedDogsList.add(ds.getKey());
                }

                DatabaseReference dogsRef = FirebaseDatabase.getInstance().getReference("dogs");
                dogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dogs.clear();
                        uniqueBreeds.clear();
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String ownerUid = userSnapshot.getKey();
                            if (!Objects.equals(ownerUid, currentUserId)) {
                                Dog dog = userSnapshot.getValue(Dog.class);
                                if (dog != null && !likedDogsList.contains(ownerUid)) {
                                    dogs.add(dog);
                                    // Add unique breeds to the list
                                    if (!uniqueBreeds.contains(dog.getDogBreed())) {
                                        uniqueBreeds.add(dog.getDogBreed());
                                    }
                                }
                            }
                        }
                        dogAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        SignalManager.getInstance().toast("Error retrieving data: " + error.getMessage());
                        SignalManager.getInstance().vibrate(200);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                SignalManager.getInstance().toast("Error retrieving data: " + error.getMessage());
                SignalManager.getInstance().vibrate(200);
            }
        });
    }

    private void showBreedFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Breed");

        // Convert the list of unique breeds to a CharSequence array
        CharSequence[] breedArray = uniqueBreeds.toArray(new CharSequence[0]);

        builder.setItems(breedArray, (dialog, which) -> {
            String selectedBreed = uniqueBreeds.get(which);
            filterDogsByBreed(selectedBreed);
        });

        builder.setNegativeButton("Show All", (dialog, which) -> {
            fetchDogsFromFirebase(); // Reload all dogs
        });

        builder.show();
    }

    private void filterDogsByBreed(String breed) {
        DatabaseReference dogsRef = FirebaseDatabase.getInstance().getReference("dogs");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String currentUserId = user.getUid();

        dogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dogs.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String ownerUid = userSnapshot.getKey();
                    if (!Objects.equals(ownerUid, currentUserId)) {
                        Dog dog = userSnapshot.getValue(Dog.class);
                        if (dog != null && dog.getDogBreed().equals(breed)) {
                            dogs.add(dog);
                        }
                    }
                }
                if (dogs.isEmpty()) {
                    SignalManager.getInstance().toast("No dogs found for the selected breed.");
                    fetchDogsFromFirebase(); // Reload all dogs if no matches
                } else {
                    dogAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                SignalManager.getInstance().toast("Error retrieving data: " + error.getMessage());
                SignalManager.getInstance().vibrate(200);
            }
        });
    }



    public void switchCard(boolean like) {
        SignalManager.getInstance().vibrate(50);
        if (dogs.isEmpty()) return;

        if (currentIndex < 0 || currentIndex >= dogs.size()) {
            // Handle invalid index by resetting to 0
            currentIndex = 0;
        }

        Dog currentDog = dogs.get(currentIndex);

        if (like) {
            addChatToDatabase(currentDog);
            //transactToChatsActivity(currentDog);
        }

        // Move to the next card
        currentIndex++;
        if (currentIndex >= dogs.size()) {
            SignalManager.getInstance().toast("Currently no new dogs to show, starting over!");
            SignalManager.getInstance().vibrate(150);
            currentIndex = 0; // Loop back to the first card if we reach the end
        }

        dogAdapter.notifyDataSetChanged(); // Notify adapter to update the displayed card
        main_LST_dogs.scrollToPosition(currentIndex); // Scroll to the new card
    }



    private void addChatToDatabase(Dog dog) {
        FirebaseUser userSender = FirebaseAuth.getInstance().getCurrentUser();
        assert userSender != null;
        String userSenderId = userSender.getUid();

        // Query the dogs database to find the receiver dog by name
        DatabaseReference dogsRef = FirebaseDatabase.getInstance().getReference("dogs");
        Query query = dogsRef.orderByChild("dogName").equalTo(dog.getDogName());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userReceiverIdKey = null;
                for (DataSnapshot dogSnapshot : snapshot.getChildren()) {
                    userReceiverIdKey = dogSnapshot.getKey();
                }

                if (userReceiverIdKey != null) {
                    // Create a unique key for the chat
                    DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chats");
                    String chatId = chatsRef.push().getKey();
                    String likedDogName =  dog.getDogName();

                    // Create and save the chat object
                    Chat chat = new Chat(chatId, likedDogName, userReceiverIdKey, userSenderId);
                    if (chatId != null) {
                        String finalUserReceiverIdKey = userReceiverIdKey;
                        chatsRef.child(chatId).setValue(chat).addOnSuccessListener(aVoid -> {
                            // Update liked dogs for both users
                            DatabaseReference likedBySenderRef = FirebaseDatabase.getInstance().getReference("liked dogs per user")
                                    .child(userSenderId)
                                    .child(finalUserReceiverIdKey);
                            likedBySenderRef.setValue(true);

                            DatabaseReference likedByReceiverRef = FirebaseDatabase.getInstance().getReference("liked dogs per user")
                                    .child(finalUserReceiverIdKey)
                                    .child(userSenderId);
                            likedByReceiverRef.setValue(true);

                            SignalManager.getInstance().toast("Chat with " + dog.getDogName() + " created!");
                            SignalManager.getInstance().vibrate(200);

                            // Notify the liked dog's app
                            sendLikeNotification(finalUserReceiverIdKey, userSenderId);
                            transactToChatsActivity(dog, chatId);
                        }).addOnFailureListener(e -> {
                            // Handle failure
                            SignalManager.getInstance().toast("Failed to create chat with " + dog.getDogName());
                            SignalManager.getInstance().vibrate(200);
                        });
                    }
                } else {
                    SignalManager.getInstance().toast("Failed to find the user for " + dog.getDogName());
                    SignalManager.getInstance().vibrate(200);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                SignalManager.getInstance().toast("Error retrieving data: " + error.getMessage());
                SignalManager.getInstance().vibrate(200);
            }
        });
    }

    private void sendLikeNotification(String receiverUserId, String senderUserId) {
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifications").child(receiverUserId);
        String notificationId = notificationsRef.push().getKey();

        HashMap<String, Object> notificationData = new HashMap<>();
        notificationData.put("type", "like");
        notificationData.put("senderId", senderUserId);

        if (notificationId != null) {
            notificationsRef.child(notificationId).setValue(notificationData);
        }
    }




    private void transactToChatsActivity(Dog dog, String chatId) {
        Intent intent = new Intent(getActivity(), ChatsActivity.class);
        intent.putExtra("dogName", dog.getDogName());
        intent.putExtra("chatId", chatId);
        startActivity(intent);
    }
}

