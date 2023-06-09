package com.gromore.demo;

import android.app.Application;

import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.gromore.demo.ad.gm.Gromore;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Gromore.getInstance().init(this);
        DeviceIdentifier.register(this);
    }
}
