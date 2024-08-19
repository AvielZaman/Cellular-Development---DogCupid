package com.example.dogcupid.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesManager {
    private static  volatile SharePreferencesManager instance = null;    // (singleton)
    private static final String KEY_SOUND_ENABLED = "KEY_SOUND_ENABLED";
    private static final String KEY_NOTIFICATION_ENABLED = "KEY_NOTIFICATION_ENABLED";
    private static final String KEY_VIBRATE_ENABLED = "KEY_VIBRATE_ENABLED";
    private static final String SP_FILE = "SP_FILE";
    private SharedPreferences sharedPref;

    private SharePreferencesManager(Context context) {
        this.sharedPref = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static SharePreferencesManager init(Context context) {
        if(instance == null) {
            synchronized (SharePreferencesManager.class){
                if(instance == null){
                    instance = new SharePreferencesManager(context);
                }
            }
        }
        return getInstance();
    }

    public static SharePreferencesManager getInstance() {
        return instance;
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString (String key, String defaultValue) {
        return sharedPref.getString(key, defaultValue);
    }

    public void setNotificationEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_NOTIFICATION_ENABLED, enabled);
        editor.apply();
    }

    public boolean isNotificationEnabled() {
        return sharedPref.getBoolean(KEY_NOTIFICATION_ENABLED, true);
    }

    public void setVibrateEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_VIBRATE_ENABLED, enabled);
        editor.apply();
    }

    public boolean isVibrateEnabled() {
        return sharedPref.getBoolean(KEY_VIBRATE_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_SOUND_ENABLED, enabled);
        editor.apply();
    }

    public boolean isSoundEnabled() {
        return sharedPref.getBoolean(KEY_SOUND_ENABLED, true);
    }
}


