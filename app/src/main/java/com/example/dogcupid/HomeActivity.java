package com.example.dogcupid;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dogcupid.Utilities.NotificationsListener;
import com.example.dogcupid.Utilities.SignalManager;
import com.example.dogcupid.Utilities.SoundPlayer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    MaterialTextView home_name_text;
    MaterialButton home_Discover_BTN;
    MaterialButton home_Chats_BTN;
    ExtendedFloatingActionButton home_profile_BTN;
    ExtendedFloatingActionButton home_settings_BTN;

    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViews();
        initViews();

    }

    private void initViews() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        home_name_text.setText("Hey ❤️");
        home_profile_BTN.setOnClickListener(v -> transactToProfileActivity());
        home_settings_BTN.setOnClickListener(v -> transactToSettingsActivity());
        home_Discover_BTN.setOnClickListener(v -> transactToDiscoverActivity());
        home_Chats_BTN.setOnClickListener(v -> transactToChatsActivity());
        soundPlayer = new SoundPlayer(this);

        String currentUserId = user.getUid();
        new NotificationsListener(currentUserId);
    }


    private void findViews() {
        home_name_text= findViewById(R.id.home_name_text);
        home_Discover_BTN= findViewById(R.id.home_Discover_BTN);
        home_Chats_BTN= findViewById(R.id.home_Chats_BTN);
        home_profile_BTN= findViewById(R.id.home_profile_BTN);
        home_settings_BTN= findViewById(R.id.home_settings_BTN);

    }

    private void transactToProfileActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, ProfileActivity.class);
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
    private void transactToDiscoverActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, DiscoverActivity.class);
        startActivity(i);
        finish();
    }
    private void transactToChatsActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, ChatsActivity.class);
        startActivity(i);
        finish();
    }

}