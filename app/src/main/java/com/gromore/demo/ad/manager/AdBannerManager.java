package com.gromore.demo.ad.manager;

import android.app.Activity;
import android.util.Log;

import com.bytedance.msdk.api.v2.GMAdSize;
import com.bytedance.msdk.api.v2.GMMediationAdSdk;
import com.bytedance.msdk.api.v2.GMSettingConfigCallback;
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAd;
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdListener;
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdLoadCallback;
import com.bytedance.msdk.api.v2.slot.GMAdSlotBanner;

public class AdBannerManager {
    private static final String TAG = AdBannerManager.class.getSimpleName();
    private GMBannerAd mBannerViewAd;
    private Activity mActivity;
    private GMBannerAdLoadCallback mBannerAdLoadCallback;
    private GMBannerAdListener mAdBannerListener;
    private String mAdUnitId; //广告位

    public AdBannerManager(Activity activity, GMBannerAdLoadCallback bannerAdLoadCallback, GMBannerAdListener adBannerListener) {
        //如果接入了Unity sdk 建议提前初始化
        mActivity = activity;
        mBannerAdLoadCallback = bannerAdLoadCallback;
        mAdBannerListener = adBannerListener;
    }

    public GMBannerAd getBannerAd() {
        return mBannerViewAd;
    }

    public void loadAdWithCallback(final String adUnitId) {
        this.mAdUnitId = adUnitId;

        /**
         * 判断当前是否存在config 配置 ，如果存在直接加载广告 ，如果不存在则注册config加载回调
         */
        if (GMMediationAdSdk.configLoadSuccess()) {
            Log.e(TAG, "load ad 当前config配置存在，直接加载广告");
            loadBannerAd(adUnitId);
        } else {
            Log.e(TAG, "load ad 当前config配置不存在，正在请求config配置....");
            GMMediationAdSdk.registerConfigCallback(mSettingConfigCallback); //不用使用内部类，否则在ondestory中无法移除该回调
        }
    }

    private void loadBannerAd(String unitId) {
        /**
         * 注：每次加载banner的时候需要新建一个GMBannerAd，一个广告对象只能load一次，banner广告对象getBannerView只能一次，第二次调用会返回空
         */
        if (mBannerViewAd != null) {
            mBannerViewAd.destroy();
        }
        mBannerViewAd = new GMBannerAd(mActivity, unitId);
        //设置广告事件监听
        mBannerViewAd.setAdBannerListener(mAdBannerListener);

        //设置广告配置
        GMAdSlotBanner slotBanner = new GMAdSlotBanner.Builder()
                .setBannerSize(GMAdSize.BANNER_CUSTOME)
                //.setImageAdSize(320, 150)// GMAdSize.BANNER_CUSTOME可以调用setImageAdSize设置大小
                .setBannerSize(GMAdSize.BANNER_320_50)
//                .setRefreshTime(30) // 从v3100版本开始，不支持sdk端设置banner轮播时间，只能从GroMore平台进行配置。sdk端设置无效。
                .setAllowShowCloseBtn(true)//如果广告本身允许展示关闭按钮，这里设置为true就是展示。注：目前只有mintegral支持。
                .setBidNotify(true)//开启bidding比价结果通知，默认值为false
                .setMuted(true) // 控制视频是否静音
                .build();
        //请求广告，对请求回调的广告作渲染处理
        mBannerViewAd.loadAd(slotBanner, mBannerAdLoadCallback);
    }

    public void destroy() {
        if (mBannerViewAd != null) {
            mBannerViewAd.destroy();
        }
        mActivity = null;
        mBannerViewAd = null;
        mBannerAdLoadCallback = null;
        mAdBannerListener = null;
        GMMediationAdSdk.unregisterConfigCallback(mSettingConfigCallback); //注销config回调
    }


    /**
     * config回调
     */
    private GMSettingConfigCallback mSettingConfigCallback = new GMSettingConfigCallback() {
        @Override
        public void configLoad() {
            Log.e(TAG, "load ad 在config 回调中加载广告");
            loadAdWithCallback(mAdUnitId);
        }
    };
}
