package com.example.dogcupid;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class LottieSplashActivity extends AppCompatActivity {
    private LottieAnimationView lottie_LOTTIE_lottie;
    private boolean shouldLoop; // Flag to control animation looping

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_splash);

        findViews();
        shouldLoop = getIntent().getBooleanExtra("shouldLoop", false); // Default to no loop

        if (shouldLoop) {
            lottie_LOTTIE_lottie.setRepeatCount(LottieDrawable.INFINITE);
        } else {
            lottie_LOTTIE_lottie.setRepeatCount(0);
        }

        lottie_LOTTIE_lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                // Check if the animation should loop
                if (shouldLoop) {
                    lottie_LOTTIE_lottie.resumeAnimation();
                }
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                if (!shouldLoop) {
                    transactToHomeActivity();
                }
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
                //pass
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
                //pass
            }
        });

        lottie_LOTTIE_lottie.playAnimation();
    }

    public void setShouldLoop(boolean shouldLoop) {
        this.shouldLoop = shouldLoop;
        if (shouldLoop) {
            lottie_LOTTIE_lottie.loop(true);
        }
    }


    private void transactToHomeActivity() {
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }


    private void findViews() {
        lottie_LOTTIE_lottie = findViewById(R.id.lottie_LOTTIE_lottie);
    }
}