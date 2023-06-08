package com.gromore.demo;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdListener;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdLoadCallback;
import com.gromore.demo.activity.MainActivity;
import com.gromore.demo.ad.config.Config;
import com.gromore.demo.ad.manager.AdSplashManager;

public class GromoreSplash {
    public static final String EXTRA_FORCE_LOAD_BOTTOM = "extra_force_load_bottom";
    private static final String TAG = GromoreSplash.class.getSimpleName();
    private ViewGroup mSplashContainer;
    private AdSplashManager mAdSplashManager;
    private boolean mForceLoadBottom;
    private GMSplashAdListener mSplashAdListener;
    private AfujiaSplashListener listener;
    private Activity context;

    public GromoreSplash(Activity context, ViewGroup contentView, AfujiaSplashListener listener) {
        this.context = context;
        this.mSplashContainer = contentView;
        this.listener = listener;
    }


    public void showSplash() {
        initListener();
        initAdLoader();
        //加载开屏广告
        if (mAdSplashManager != null) {
            mAdSplashManager.loadSplashAd(Config.SPLASH);
        }
    }

    public void initListener() {
        mSplashAdListener = new GMSplashAdListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked");
            }

            @Override
            public void onAdShow() {
                Log.d(TAG, "onAdShow");
                if (mAdSplashManager != null && mAdSplashManager.getSplashAd() != null && mAdSplashManager.getSplashAd().getShowEcpm() != null) {
                    String resultEcpm = mAdSplashManager.getSplashAd().getShowEcpm().getPreEcpm();
                    if (resultEcpm != null) {
                        double ecpm = Float.parseFloat(resultEcpm);

                    }
                }


            }

            /**
             * show失败回调。如果show时发现无可用广告（比如广告过期），会触发该回调。
             * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
             * @param adError showFail的具体原因
             */
            @Override
            public void onAdShowFail(AdError adError) {
                Log.d(TAG, "onAdShowFail:" + adError.message + " errorCode:" + adError.code);
                // 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载
                if (listener != null) {
                    listener.next();
                    listener = null;
                }
                goToMainActivity();

            }

            @Override
            public void onAdSkip() {
                Log.d(TAG, "onAdSkip");
                if (listener != null) {
                    listener.next();
                    listener = null;
                }
                goToMainActivity();
            }

            @Override
            public void onAdDismiss() {
                Log.d(TAG, "onAdDismiss");
                if (listener != null) {
                    listener.next();
                    listener = null;
                }
                goToMainActivity();
            }
        };
    }


    public void initAdLoader() {
        mAdSplashManager = new AdSplashManager(context, new GMSplashAdLoadCallback() {
            @Override
            public void onSplashAdLoadFail(AdError adError) {
                //Toast.show(SplashActivity.this, "广告加载失败");
                Log.d(TAG, adError.message);
                Log.e(TAG, "load splash ad error : " + adError.code + ", " + adError.message);
                // 获取本次waterfall加载中，加载失败的adn错误信息。
                if (mAdSplashManager.getSplashAd() != null)
                    Log.d(TAG, "ad load infos: " + mAdSplashManager.getSplashAd().getAdLoadInfoList());
                if (listener != null) {
                    listener.next();
                    listener = null;
                }
            }

            @Override
            public void onSplashAdLoadSuccess() {
                //TToast.show(SplashActivity.this, "广告加载成功");
                Log.e(TAG, "load splash ad success ");
//                mAdSplashManager.printInfo();
                // 根据需要选择调用isReady()
//                if (mAdSplashManager.getSplashAd().isReady()) {
//                    mAdSplashManager.getSplashAd().showAd(mSplashContainer);
//                }
                mAdSplashManager.getSplashAd().showAd(mSplashContainer);

            }

            // 注意：***** 开屏广告加载超时回调已废弃，统一走onSplashAdLoadFail，GroMore作为聚合不存在SplashTimeout情况。*****
            @Override
            public void onAdLoadTimeout() {

            }
        }, mSplashAdListener);
    }

    public static interface AfujiaSplashListener {
        public abstract void next();
    }

    public void destroy() {
        if (mAdSplashManager != null) {
            mAdSplashManager.destroy();
        }


    }

    private void goToMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        mSplashContainer.removeAllViews();
        context.finish();
    }

}
