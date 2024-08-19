package com.example.dogcupid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dogcupid.Utilities.SharePreferencesManager;
import com.example.dogcupid.Utilities.SignalManager;
import com.example.dogcupid.Utilities.SoundPlayer;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial settings_pushNotification_switch;
    private SwitchMaterial settings_sound_switch;
    private SwitchMaterial settings_Vibrate_switch;
    private ExtendedFloatingActionButton settings_profile_BTN;
    private ExtendedFloatingActionButton settings_home_BTN;
    private MaterialButton settings_logout_BTN;
    private SoundPlayer soundPlayer;

    private String[] settings = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharePreferencesManager.init(this);
        SignalManager.init(this);

        findViews();
        initViews();
    }

    private void findViews() {
        settings_pushNotification_switch = findViewById(R.id.settings_pushNotification_switch);
        settings_sound_switch = findViewById(R.id.settings_sound_switch);
        settings_Vibrate_switch = findViewById(R.id.settings_Vibrate_switch);
        settings_profile_BTN = findViewById(R.id.settings_profile_BTN);
        settings_home_BTN = findViewById(R.id.settings_home_BTN);
        settings_logout_BTN = findViewById(R.id.settings_logout_BTN);
    }

    private void initViews() {
        soundPlayer = new SoundPlayer(this);
        SharePreferencesManager prefsManager = SharePreferencesManager.getInstance();
        settings_home_BTN.setOnClickListener(v->transactToHomeActivity());
        settings_profile_BTN.setOnClickListener(v-> transactToProfileActivity());
        settings_logout_BTN.setOnClickListener(v->signOut());

        // Initialize the switch states based on saved preferences
        settings_pushNotification_switch.setChecked(prefsManager.isNotificationEnabled());
        settings_sound_switch.setChecked(prefsManager.isSoundEnabled());
        settings_Vibrate_switch.setChecked(prefsManager.isVibrateEnabled());

        // Push Notifications (Toasts) Switch
        settings_pushNotification_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefsManager.setNotificationEnabled(isChecked);
        });

        // Sound Switch
        settings_sound_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefsManager.setSoundEnabled(isChecked);
        });

        // Vibration Switch
        settings_Vibrate_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefsManager.setVibrateEnabled(isChecked);
        });

    }

    private void transactToHomeActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void transactToProfileActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        finish();
    }
    private void transactToLoginActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        transactToLoginActivity();
                    }
                });
    }
}