package com.gromore.demo;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.msdk.adapter.util.Logger;
import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.reward.RewardItem;
import com.bytedance.msdk.api.v2.ad.interstitialFull.GMInterstitialFullAdListener;
import com.bytedance.msdk.api.v2.ad.interstitialFull.GMInterstitialFullAdLoadCallback;

import java.util.Map;

public class GromoreInterstitialFull {
    private static final String TAG = GromoreInterstitialFull.class.getName();
    public Activity activity;
    private AdInterstitialFullManager mAdInterstitialFullManager; //插全屏管理类

    private boolean mLoadSuccess; //是否加载成功
    private GMInterstitialFullAdListener mGMInterstitialFullAdListener;
    private static GromoreInterstitialFull interstitialFull;

    public static GromoreInterstitialFull getInstance() {
        if (interstitialFull == null) {
            interstitialFull = new GromoreInterstitialFull();
        }
        return interstitialFull;
    }


    private GromoreInterstitialFull() {

    }

    public void load(Activity activity) {
        this.activity = activity;
        initListener();
        initAdLoader();
        loadInterstitialFull();
    }

    private void loadInterstitialFull() {
        mLoadSuccess = false;
        mAdInterstitialFullManager.loadAdWithCallback(Config.INTERSTITIALAD);

    }

    public void showInterstitialFull(Activity activity) {
        showInterFullAd(activity);

    }

    public void initListener() {

        mGMInterstitialFullAdListener = new GMInterstitialFullAdListener() {
            @Override
            public void onInterstitialFullShow() {
//                Toast.makeText(mContext, "插全屏广告show", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onInterstitialFullShow");
                if (mAdInterstitialFullManager != null && mAdInterstitialFullManager.getGMInterstitialFullAd() != null && mAdInterstitialFullManager.getGMInterstitialFullAd().getShowEcpm() != null) {
                    String resultEcpm = mAdInterstitialFullManager.getGMInterstitialFullAd().getShowEcpm().getPreEcpm();
                    if (resultEcpm != null) {

                    }

                }


            }

            /**
             * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
             * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
             * @param adError showFail的具体原因
             */
            @Override
            public void onInterstitialFullShowFail(@NonNull AdError adError) {
                //Toast.makeText(mContext, "插全屏广告展示失败", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onInterstitialFullShowFail");

                // 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载

            }

            @Override
            public void onInterstitialFullClick() {
                //Toast.makeText(mContext, "插全屏广告click", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onInterstitialFullClick");
            }

            @Override
            public void onInterstitialFullClosed() {
                //Toast.makeText(mContext, "插全屏广告close", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onInterstitialFullClosed");
                loadInterstitialFull();
            }

            @Override
            public void onVideoComplete() {
                //TToast.show(mContext, "插全屏播放完成");
                Log.d(TAG, "onVideoComplete");
            }

            @Override
            public void onVideoError() {
                //TToast.show(mContext, "插全屏播放出错");
                Log.d(TAG, "onVideoError");
            }

            @Override
            public void onSkippedVideo() {
                //TToast.show(mContext, "插全屏跳过");
                Log.d(TAG, "onSkippedVideo");
            }

            /**
             * 当广告打开浮层时调用，如打开内置浏览器、内容展示浮层，一般发生在点击之后
             * 常常在onAdLeftApplication之前调用
             */
            @Override
            public void onAdOpened() {
                //Toast.makeText(mContext, "插全屏广告onAdOpened", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onAdOpened");
            }

            /**
             * 此方法会在用户点击打开其他应用（例如 Google Play）时
             * 于 onAdOpened() 之后调用，从而在后台运行当前应用。
             */
            @Override
            public void onAdLeftApplication() {
                //Toast.makeText(mContext, "插全屏广告onAdLeftApplication", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onAdLeftApplication");
            }

            @Override
            public void onRewardVerify(@NonNull RewardItem rewardItem) {
                Map<String, Object> customData = rewardItem.getCustomData();
                if (customData != null) {
                    String adnName = (String) customData.get(RewardItem.KEY_ADN_NAME);
                    switch (adnName) {
                        case RewardItem.KEY_GDT:
                            Logger.d(TAG, "rewardItem gdt: " + customData.get(RewardItem.KEY_GDT_TRANS_ID));
                            break;
                    }
                }
                Log.d(TAG, "onRewardVerify");
                //TToast.show(InterstitialFullActivity.this, "onRewardVerify！");
            }
        };
    }

    public void initAdLoader() {
        mAdInterstitialFullManager = new AdInterstitialFullManager(activity, new GMInterstitialFullAdLoadCallback() {
            @Override
            public void onInterstitialFullLoadFail(@NonNull AdError adError) {
                mLoadSuccess = false;
                Log.e(TAG, "load interaction ad error : " + adError.code + ", " + adError.message);
                //mAdInterstitialFullManager.printLoadFailAdnInfo();// 获取本次waterfall加载中，加载失败的adn错误信息。

            }

            @Override
            public void onInterstitialFullAdLoad() {
                mLoadSuccess = true;
                Log.e(TAG, "load interaction ad success ! ");
                //TToast.show(mContext, "插全屏加载成功！");
                //mAdInterstitialFullManager.printLoadAdInfo(); //展示已经加载广告的信息
                //mAdInterstitialFullManager.printLoadFailAdnInfo();// 获取本次waterfall加载中，加载失败的adn错误信息。
            }

            @Override
            public void onInterstitialFullCached() {
                mLoadSuccess = true;
                Log.d(TAG, "onFullVideoCached....缓存成功！");
                //TToast.show(mContext, "插全屏缓存成功！");
            }
        });
    }

    /**
     * 展示广告
     */
    private void showInterFullAd(Activity activity) {
        if (mLoadSuccess && mAdInterstitialFullManager != null) {
            if (mAdInterstitialFullManager.getGMInterstitialFullAd() != null && mAdInterstitialFullManager.getGMInterstitialFullAd().isReady()) {
                //在获取到广告后展示,强烈建议在onInterstitialFullCached回调后，展示广告，提升播放体验
                //该方法直接展示广告，如果展示失败了（如过期），会回调onVideoError()
                //展示广告，并传入广告展示的场景
                mAdInterstitialFullManager.getGMInterstitialFullAd().setAdInterstitialFullListener(mGMInterstitialFullAdListener);
                mAdInterstitialFullManager.getGMInterstitialFullAd().showAd(activity);
                mLoadSuccess = false;
            } else {
                //TToast.show(this, "当前广告不满足show的条件");
            }
        } else {
            //TToast.show(this, "请先加载广告");
            loadInterstitialFull();
        }
    }

    public void destroy() {
        if (mAdInterstitialFullManager != null) {
            mAdInterstitialFullManager.destroy();
        }


    }

}
