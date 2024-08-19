package com.example.dogcupid.Utilities;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoundPlayer {

    private Context context;
    private Executor executor;
    private MediaPlayer mediaPlayer;
    private int resID;

    public SoundPlayer(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void playSound(int resID) {
        SharePreferencesManager prefsManager = SharePreferencesManager.getInstance();
        if (prefsManager.isSoundEnabled()) {
            if (mediaPlayer == null) {
                executor.execute(() -> {
                    mediaPlayer = MediaPlayer.create(context, resID);
                    mediaPlayer.setVolume(0.7f, 0.7f);
                    mediaPlayer.start();
                });
            }
        }
    }

    public void stopSound() {
        if (mediaPlayer != null)
            executor.execute(() -> {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            });


    }

}


