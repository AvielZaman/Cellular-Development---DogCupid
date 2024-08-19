package com.example.dogcupid.Utilities;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class SignalManager {

    private static volatile SignalManager instance = null;
    private Context context;
    private static Vibrator vibrator;
    private SharePreferencesManager prefsManager;

    private SignalManager(Context context) {
        this.context = context;
        this.prefsManager = SharePreferencesManager.init(context);
    }
    public static SignalManager init(Context context){
        if (instance == null) {
            synchronized (SignalManager.class){
                if (instance == null){
                    instance = new SignalManager(context);
                    vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                }
            }
        }
        return getInstance();
    }

    public static SignalManager getInstance() {
        return instance;
    }

    public void toast(String message) {
        if (prefsManager.isNotificationEnabled()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void vibrate(long milliseconds) {
        if (prefsManager.isVibrateEnabled()) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

}

