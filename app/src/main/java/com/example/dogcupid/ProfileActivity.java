package com.example.dogcupid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dogcupid.Models.Dog;
import com.example.dogcupid.Utilities.SignalManager;
import com.example.dogcupid.Utilities.SoundPlayer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private AppCompatImageView profile_IMG_image;
    private MaterialTextView profile_LBL_dog_name;
    private MaterialTextView profile_LBL_age;
    private MaterialTextView profile_LBL_breed;
    private MaterialTextView profile_LBL_owner_name;
    private MaterialTextView profile_LBL_phone;
    private MaterialButton profile_Edit_BTN;
    private ExtendedFloatingActionButton home_profile_BTN;
    private ExtendedFloatingActionButton home_settings_BTN;

    private DatabaseReference dogsRef = FirebaseDatabase.getInstance().getReference("dogs");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userImageUrl = "";
    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViews();
        initViews();
        fetchUserDataFromDB();
    }

    private void findViews() {
        profile_IMG_image = findViewById(R.id.profile_IMG_image);
        profile_LBL_dog_name = findViewById(R.id.profile_LBL_dog_name);
        profile_LBL_age = findViewById(R.id.profile_LBL_age);
        profile_LBL_breed = findViewById(R.id.profile_LBL_breed);
        profile_LBL_owner_name = findViewById(R.id.profile_LBL_owner_name);
        profile_LBL_phone = findViewById(R.id.profile_LBL_phone);
        profile_Edit_BTN = findViewById(R.id.profile_Edit_BTN);
        home_profile_BTN = findViewById(R.id.home_profile_BTN);
        home_settings_BTN = findViewById(R.id.home_settings_BTN);
    }

    private void initViews() {
        soundPlayer = new SoundPlayer(this);
        profile_Edit_BTN.setOnClickListener(v -> transactToEditProfileActivity());
        home_profile_BTN.setOnClickListener(v -> transactToHomeActivity());
        home_settings_BTN.setOnClickListener(v -> transactToSettingsActivity());
    }

    private void fetchUserDataFromDB() {
        if (user == null) return; // Ensure user is not null
        String currentUserId = user.getUid();

        dogsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String ownerUid = userSnapshot.getKey(); // UID of the dog owner
                    if (Objects.equals(ownerUid, currentUserId)) {
                        Dog dog = userSnapshot.getValue(Dog.class);
                        if (dog != null) {
                            profile_LBL_dog_name.setText("Dog's Name : " + dog.getDogName());
                            profile_LBL_age.setText("Dog's Age : " + dog.getDogAge());
                            profile_LBL_breed.setText("Dog's Breed : " + dog.getDogBreed());
                            profile_LBL_owner_name.setText("Owner's Name : " + dog.getOwnerName());
                            profile_LBL_phone.setText("Owner's Phone : " + dog.getOwnerPhone());
                            userImageUrl = dog.getImageUrl();

                            // Update the image view with Glide
                            Glide.with(ProfileActivity.this)
                                    .load(userImageUrl)
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_logo)
                                    .error(R.drawable.ic_logo)
                                    .skipMemoryCache(true) // Skip memory cache
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk caching
                                    .into(profile_IMG_image);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void transactToEditProfileActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, EditProfileActivity.class);
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
