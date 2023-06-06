package com.gromore.demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.gromore.demo.R;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    GromoreBanner banner;
    GromoreInterstitialFull mGromoreInterstitialFull;

    GromoreReward mGromoreReward;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv1).setOnClickListener(this);
        findViewById(R.id.tv2).setOnClickListener(this);
        findViewById(R.id.tv3).setOnClickListener(this);


        ViewGroup bannerContainer = findViewById(R.id.fl4);
        banner = new GromoreBanner(bannerContainer, this);
        banner.showBanner();

        mGromoreInterstitialFull = GromoreInterstitialFull.getInstance();
        mGromoreInterstitialFull.load(this);

        mGromoreReward = GromoreReward.getInstance();
        mGromoreReward.loadReward(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                Log.d(TAG, "开屏");
                startActivity(new Intent(this, SplashActivity.class));
                break;

            case R.id.tv2:
                Log.d(TAG, "显示插屏");
                mGromoreInterstitialFull.showInterstitialFull(this);
                break;

            case R.id.tv3:
                Log.d(TAG, "显示激励");
                mGromoreReward.showRewardAd(this, new GromoreReward.GromoreRewardListener() {
                    @Override
                    public void onRewardVerify() {
                        Toast.makeText(MainActivity.this, "激励回掉成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRewardFail() {
                        Toast.makeText(MainActivity.this, "激励回掉失败", Toast.LENGTH_LONG).show();

                    }
                });

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        banner.destroy();
        mGromoreInterstitialFull.destroy();
    }
}
