package com.example.dogcupid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.dogcupid.Models.Dog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CollectUserInfoActivity extends AppCompatActivity {

    private static final int GALLERY_REQ_CODE = 1000;
    private static final int REQUEST_CODE = 100;

    private AppCompatImageView new_user_image;
    private TextInputEditText new_user_dog_name;
    private TextInputEditText new_user_dog_age;
    private TextInputEditText new_user_dog_breed;
    private TextInputEditText new_user_dog_description;
    private TextInputEditText new_user_dog_gender;
    private TextInputEditText new_user_owner_name;
    private TextInputEditText new_user_owner_phone;
    private ExtendedFloatingActionButton new_user_continue_BTN;
    private ImageView scroll_down_arrow;
    private ScrollView scroll_view;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Uri imageUri;  // Store the selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_user_info);

        findViews();
        initViews();
        initLocationProvider();
    }

    private void findViews() {
        new_user_image = findViewById(R.id.new_user_image);
        new_user_dog_name = findViewById(R.id.new_user_dog_name);
        new_user_dog_age = findViewById(R.id.new_user_dog_age);
        new_user_dog_breed = findViewById(R.id.new_user_dog_breed);
        new_user_dog_description = findViewById(R.id.new_user_dog_description);
        new_user_dog_gender = findViewById(R.id.new_user_dog_gender);
        new_user_owner_name = findViewById(R.id.new_user_owner_name);
        new_user_owner_phone = findViewById(R.id.new_user_owner_phone);
        new_user_continue_BTN = findViewById(R.id.new_user_continue_BTN);
        scroll_down_arrow = findViewById(R.id.scroll_down_arrow);
        scroll_view = findViewById(R.id.scroll_view);
    }

    private void initViews() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Glide
                .with(this)
                .load(R.drawable.add_photo)
                .centerCrop()
                .placeholder(R.drawable.ic_logo)
                .into(new_user_image);

        new_user_continue_BTN.setOnClickListener(v -> uploadImageAndSaveData());
        new_user_image.setOnClickListener(v -> uploadNewPhoto());

        scroll_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scroll_view.getChildAt(0).getHeight() == scroll_view.getHeight() + scrollY) {
                    // Reached the bottom of the ScrollView
                    scroll_down_arrow.setVisibility(View.GONE);
                } else {
                    // Not at the bottom
                    scroll_down_arrow.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void initLocationProvider() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void uploadNewPhoto() {
        Intent iGallery = new Intent(Intent.ACTION_PICK);
        iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(iGallery, GALLERY_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQ_CODE && data != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .into(new_user_image);
        }
    }

    private void uploadImageAndSaveData() {
        if (imageUri != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/dogs/" + user.getUid() + " " + user.getUid()+".jpg");

            storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the dog data with the image URL
                        saveDogData(uri.toString());
                    })
            ).addOnFailureListener(e -> {
                // Handle any errors
            });
        } else {
            // No image selected, save the dog data without an image
            saveDogData(null);
        }
    }

    private void saveDogData(String imageUri) {
        startLottieSplashForSaving();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dogsRef = database.getReference("dogs");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        Dog newDog = new Dog()
                .setDogName(new_user_dog_name.getText().toString())
                .setDogAge(Integer.parseInt(new_user_dog_age.getText().toString()))
                .setDogBreed(new_user_dog_breed.getText().toString())
                .setGender(new_user_dog_gender.getText().toString())
                .setDescription(new_user_dog_description.getText().toString())
                .setOwnerName(new_user_owner_name.getText().toString())
                .setOwnerPhone(new_user_owner_phone.getText().toString());

        if (imageUri != null) {
            newDog.setImageUri(imageUri);
        }

        // Get the user's current location before saving
        getLastLocation(location -> {
            newDog.setLatitude(latitude);
            newDog.setLongitude(longitude);

            // Add the new dog entry to the database under the user's ID
            dogsRef.child(user.getUid()).setValue(newDog).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    notifyDataSaved(); // Notify data is saved
                }
            });
        });
    }
    private void notifyDataSaved() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLottieSplashForSaving() {
        Intent intent = new Intent(this, LottieSplashActivity.class);
        intent.putExtra("shouldLoop", true); // Enable looping for data saving process
        startActivity(intent);
    }

    private void getLastLocation(final OnSuccessListener<Location> locationListener) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Create a location request to get fresh data
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000); // 5 seconds interval
            locationRequest.setFastestInterval(2000); // 2 seconds
            locationRequest.setNumUpdates(1); // Request only one location update

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(CollectUserInfoActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();
                            // Call the success listener with the retrieved location
                            locationListener.onSuccess(location);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, getMainLooper());
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(CollectUserInfoActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(location -> {
                    // Do nothing here, the location will be used later
                });
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
