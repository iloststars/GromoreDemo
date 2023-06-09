package com.gromore.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.gromore.demo.R;
import com.gromore.demo.ad.gm.GromoreSplash;

public class SplashActivity extends Activity {
    private GromoreSplash mGromoreSplash;
    private ViewGroup viewAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        viewAd = findViewById(R.id.frame_ad);
        mGromoreSplash = new GromoreSplash(this, viewAd, this::finish);
        mGromoreSplash.showSplash();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGromoreSplash != null) {
            mGromoreSplash.destroy();
        }
    }


}
