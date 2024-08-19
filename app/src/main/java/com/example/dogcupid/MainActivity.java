package com.example.dogcupid;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dogcupid.Data.DataManager;
import com.example.dogcupid.Models.Dog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private MaterialTextView main_LBL_title;
    private MaterialButton main_BTN_save;
    private TextInputEditText main_ET_text;
    private MaterialButton main_BTN_updateTitle;
    private MaterialButton main_BTN_load;
    private MaterialTextView main_LBL_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
    }

//

    private void initViews() {
            main_BTN_save.setOnClickListener(v -> saveDataToFirebase());
//        main_BTN_save.setOnClickListener(v -> updateCar("269-69-402"));
//        main_BTN_updateTitle.setOnClickListener(v -> setLabel(main_ET_text.getText().toString()));
         main_BTN_load.setOnClickListener(v -> loadDataAndShowOnScreen());

    }

    private void loadDataAndShowOnScreen() {
        DatabaseReference dogsRef = FirebaseDatabase.getInstance().getReference("dogs");
        dogsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, Dog>> t = new GenericTypeIndicator<HashMap<String, Dog>>() {};
                HashMap<String, Dog> allDogs = snapshot.getValue(t);
                if (allDogs != null) {
                    StringBuilder dogInfo = new StringBuilder();
                    for (String key : allDogs.keySet()) {
                        Dog dog = allDogs.get(key);
                        dogInfo.append(key).append(": ")
                                .append(dog.getDogName()).append(", ")
                                .append(dog.getDogBreed()).append(", Age: ")
                                .append(dog.getDogAge()).append(", Description-: ")
                                .append(dog.getDescription()).append("\n");
                    }
                    main_LBL_data.setText(dogInfo.toString());
                } else {
                    main_LBL_data.setText("No data available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void saveDataToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dogsRef = database.getReference("dogs");

        HashMap<String, Dog> dogs = DataManager.getDogs();

        for (String key : dogs.keySet()) {
            Dog dog = dogs.get(key);

            if (dog != null && dog.getImageUrl() != null) {
                new Thread(() -> {
                    try {
                        // Download the image from the web
                        URL url = new URL(dog.getImageUrl());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();

                        InputStream input = connection.getInputStream();
                        byte[] imageData = inputStreamToByteArray(input);

                        // Upload the image data to Firebase Storage
                        StorageReference storageRef = FirebaseStorage.getInstance()
                                .getReference().child("images/dogs/" + key + ".jpg");

                        storageRef.putBytes(imageData)
                                .addOnSuccessListener(taskSnapshot ->
                                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                            // Update the Dog object with the download URL
                                            dog.setImageUri(uri.toString());

                                            // Save the Dog object to Firebase Realtime Database
                                            dogsRef.child(key).setValue(dog)
                                                    .addOnSuccessListener(aVoid -> {
                                                        // Success
                                                    }).addOnFailureListener(Throwable::printStackTrace);
                                        })
                                ).addOnFailureListener(Throwable::printStackTrace);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                // No image URL, save the Dog object directly
                dogsRef.child(key).setValue(dog)
                        .addOnSuccessListener(aVoid -> {
                            // Success
                        }).addOnFailureListener(Throwable::printStackTrace);
            }
        }
    }

    private byte[] inputStreamToByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }
    private void findViews() {
        main_LBL_title = findViewById(R.id.main_LBL_title);
        main_BTN_save = findViewById(R.id.main_BTN_save);
        main_ET_text = findViewById(R.id.main_ET_text);
        main_BTN_updateTitle = findViewById(R.id.main_BTN_updateTitle);
        main_BTN_load = findViewById(R.id.main_BTN_load);
        main_LBL_data = findViewById(R.id.main_LBL_data);
    }
}