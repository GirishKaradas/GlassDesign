package com.example.android.glass.glassdesign;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class GlassDesign extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
