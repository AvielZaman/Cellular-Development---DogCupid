package com.example.dogcupid;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.dogcupid.Utilities.SignalManager;
import com.example.dogcupid.Utilities.SoundPlayer;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    private final int GALLERY_REQ_CODE = 1000;

    private AppCompatImageView edit_profile_image;
    private EditText edit_profile_dog_name;
    private EditText edit_profile_dog_age;
    private EditText edit_profile_dog_breed;
    private ExtendedFloatingActionButton edit_profile_back;
    private String dogName = "";
    private String dogAge = "";
    private String dogBreed = "";
    private byte[] imageByteArray;
    private SoundPlayer soundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findViews();
        initViews();
    }

    private void findViews() {
        edit_profile_image = findViewById(R.id.edit_profile_image);
        edit_profile_dog_name = findViewById(R.id.edit_profile_dog_name);
        edit_profile_dog_age = findViewById(R.id.edit_profile_dog_age);
        edit_profile_dog_breed = findViewById(R.id.edit_profile_dog_breed);
        edit_profile_back = findViewById(R.id.edit_profile_back);
    }


    private void initViews() {
         soundPlayer = new SoundPlayer(this);
        Glide
                .with(this)
                .load(R.drawable.add_photo)
                .centerCrop()
                .placeholder(R.drawable.ic_logo)
                .into(edit_profile_image);

        edit_profile_back.setOnClickListener(v -> saveUserProfile());
        edit_profile_image.setOnClickListener(v -> uploadNewPhoto());

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
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                imageByteArray = inputStreamToByteArray(inputStream);
                edit_profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length));
            } catch (IOException e) {
                e.printStackTrace();
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

    private void saveUserProfile() {
        dogName = edit_profile_dog_name.getText().toString();
        dogAge = edit_profile_dog_age.getText().toString();
        dogBreed = edit_profile_dog_breed.getText().toString();

        // check if the user doesn't entered one of the details, so do nothing.
        if (Objects.equals(dogName, "") || Objects.equals(dogAge, "") || Objects.equals(dogBreed, "")) {
            SignalManager.getInstance().toast("No changes were done.");
            SignalManager.getInstance().vibrate(200);
            transactToProfileActivity();
        }
        else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("dogs").child(userId);

                if (imageByteArray != null) {
                    uploadPhotoToFirebaseStorage(userRef, userId);
                } else {
                    updateUserProfile(userRef, null);
                }
            }
        }
    }

    private void uploadPhotoToFirebaseStorage(DatabaseReference userRef, String userId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + userId + "/dogImage.jpg");

        storageRef.putBytes(imageByteArray)
                .addOnSuccessListener(taskSnapshot ->
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Update the user profile with the new image URL
                            updateUserProfile(userRef, uri.toString());
                        })
                ).addOnFailureListener(Throwable::printStackTrace);
    }

    private void updateUserProfile(DatabaseReference userRef, String imageUrl) {
        HashMap<String, Object> userProfileUpdates = new HashMap<>();
        userProfileUpdates.put("dogName", dogName);
        userProfileUpdates.put("dogAge", dogAge);
        userProfileUpdates.put("dogBreed", dogBreed);
        if (imageUrl != null) {
            userProfileUpdates.put("profileImageUrl", imageUrl);
        }

        userRef.updateChildren(userProfileUpdates)
                .addOnSuccessListener(aVoid -> transactToProfileActivity())
                .addOnFailureListener(Throwable::printStackTrace);
    }

    private void transactToProfileActivity() {
        SignalManager.getInstance().vibrate(50);
        soundPlayer.playSound(R.raw.buttonsound);
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);
        finish();
    }
}