package com.example.dogcupid;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dogcupid.Utilities.SignalManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            signIn();
        } else
            transactToLottieActivity();
    }

    private void transactToLottieActivity() {
        Intent intent = new Intent(this, LottieSplashActivity.class);
        intent.putExtra("shouldLoop", false); // Set loop to false for one-time animation
        startActivity(intent);
        finish();
    }


    private void signIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_logo)
                .build();
        signInLauncher.launch(signInIntent);
    }


    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                // Check if the user is new
                boolean isNewUser = user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp();

                if (isNewUser) {
                    // Redirect to a new activity to collect additional information
                    Intent intent = new Intent(this, CollectUserInfoActivity.class);
                    startActivity(intent);
                } else {
                    // Existing user, proceed as usual
                    transactToLottieActivity();
                }
            }
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed the back button
                SignalManager.getInstance().toast("Sign in canceled.");
            } else {
                // Handle other errors (e.g., network issues)
                SignalManager.getInstance().toast("Unsuccessful login, please try again!");
            }
        }
    }

}