package com.example.pmu_projekat.application;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class PMUProject extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
