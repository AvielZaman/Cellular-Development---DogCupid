package com.example.dogcupid;

import android.app.Application;

import com.example.dogcupid.Utilities.SharePreferencesManager;
import com.example.dogcupid.Utilities.SignalManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SignalManager.init(this);
        SharePreferencesManager.init(this);
    }
}
