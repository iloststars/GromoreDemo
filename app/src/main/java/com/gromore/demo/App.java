package com.gromore.demo;

import android.app.Application;

import com.gromore.demo.ad.gm.Gromore;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Gromore.getInstance().init(this);
    }
}
