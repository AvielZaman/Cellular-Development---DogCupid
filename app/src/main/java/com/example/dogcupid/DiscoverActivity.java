package com.example.dogcupid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dogcupid.Fragments.DogsFragment;
import com.example.dogcupid.Fragments.MapFragment;
import com.example.dogcupid.Models.Dog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class DiscoverActivity extends AppCompatActivity {

    private Dog dog;
    private FrameLayout main_FRAME_dogs;
    private FrameLayout main_FRAME_map;

    private DogsFragment dogsFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        findViews();
        initViews();
    }

    private void findViews() {
        main_FRAME_dogs = findViewById(R.id.main_FRAME_dogs);
        main_FRAME_map = findViewById(R.id.main_FRAME_map);
    }

    private void initViews() {
        dogsFragment = new DogsFragment();
        mapFragment = new MapFragment();

        dogsFragment.setCallbackDogsItemClicked(dog -> {
            if (mapFragment != null) {
                mapFragment.changeCameraPosition(dog.getLatitude(), dog.getLongitude());
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_dogs, dogsFragment).commit();
        mapFragment.setDogsFragment(dogsFragment);


        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();
    }

}