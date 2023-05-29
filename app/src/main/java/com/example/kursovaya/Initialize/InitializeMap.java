package com.example.kursovaya.Initialize;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class InitializeMap extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("0e9e3ed7-51bb-4622-bed2-86bc88582833");
        MapKitFactory.initialize(this);
    }
}
