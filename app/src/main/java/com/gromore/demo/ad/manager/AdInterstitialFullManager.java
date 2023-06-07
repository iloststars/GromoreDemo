package com.gromore.demo.ad.manager;

import android.app.Activity;
import android.util.Log;

import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.GMMediationAdSdk;
import com.bytedance.msdk.api.v2.GMSettingConfigCallback;
import com.bytedance.msdk.api.v2.ad.interstitialFull.GMInterstitialFullAd;
import com.bytedance.msdk.api.v2.ad.interstitialFull.GMInterstitialFullAdLoadCallback;
import com.bytedance.msdk.api.v2.slot.GMAdOptionUtil;
import com.bytedance.msdk.api.v2.slot.GMAdSlotInterstitialFull;

import java.util.HashMap;
import java.util.Map;

public class AdInterstitialFullManager {
    private static final String TAG = AdInterstitialFullManager.class.getName();

    private GMInterstitialFullAd mGMInterstitialFullAd;
    private Activity mActivity;
    private GMInterstitialFullAdLoadCallback mGMInterstitialFullAdLoadCallback;
    private String mAdUnitId; //广告位

    public AdInterstitialFullManager(Activity activity, GMInterstitialFullAdLoadCallback gmInterstitialFullAdLoadCallback) {
        mActivity = activity;
        mGMInterstitialFullAdLoadCallback = gmInterstitialFullAdLoadCallback;
    }

    public GMInterstitialFullAd getGMInterstitialFullAd() {
        return mGMInterstitialFullAd;
    }


    public void loadAdWithCallback(final String adUnitId) {
        this.mAdUnitId = adUnitId;

        /**
         * 判断当前是否存在config 配置 ，如果存在直接加载广告 ，如果不存在则注册config加载回调
         */
        if (GMMediationAdSdk.configLoadSuccess()) {
            Log.e(TAG, "load ad 当前config配置存在，直接加载广告");
            loadAd(adUnitId);
        } else {
            Log.e(TAG, "load ad 当前config配置不存在，正在请求config配置....");
            GMMediationAdSdk.registerConfigCallback(mSettingConfigCallback); //不用使用内部类，否则在ondestory中无法移除该回调
        }
    }

    private void loadAd(String unitId) {
        /**
         * 注：每次加载插全屏广告的时候需要新建一个GMInterstitialFullAd，否则可能会出现广告填充问题
         * （ 例如：mInterstitialFullAd = new GMInterstitialFullAd(this, adUnitId);）
         */
        //Context 必须传activity
        mGMInterstitialFullAd = new GMInterstitialFullAd(mActivity, unitId);
        Map<String, String> customData = new HashMap<>();
        customData.put(GMAdConstant.CUSTOM_DATA_KEY_GDT, "gdt custom data");//目前仅支持gdt
        // 其他需要透传给adn的数据。

        GMAdSlotInterstitialFull adSlotInterstitialFull = new GMAdSlotInterstitialFull.Builder()
                .setGMAdSlotBaiduOption(GMAdOptionUtil.getGMAdSlotBaiduOption().build())
                .setGMAdSlotGDTOption(GMAdOptionUtil.getGMAdSlotGDTOption().build())
                .setImageAdSize(600, 600)  //设置宽高 （插全屏类型下_插屏广告使用）
                .setVolume(0.5f) //admob 声音配置，与setMuted配合使用
                .setUserID("user123")//用户id,必传参数 (插全屏类型下_全屏广告使用)
                .setCustomData(customData)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setOrientation(GMAdConstant.VERTICAL)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL; (插全屏类型下_全屏广告使用)
                .setBidNotify(true)//开启bidding比价结果通知，默认值为false
                .build();

        //请求广告，调用插屏广告异步请求接口
        mGMInterstitialFullAd.loadAd(adSlotInterstitialFull, mGMInterstitialFullAdLoadCallback);
    }

    public void destroy() {
        if (mGMInterstitialFullAd != null) {
            mGMInterstitialFullAd.destroy();
        }
        mActivity = null;
        mGMInterstitialFullAdLoadCallback = null;
        GMMediationAdSdk.unregisterConfigCallback(mSettingConfigCallback); //注销config回调
    }

    /**
     * config回调
     */
    private GMSettingConfigCallback mSettingConfigCallback = new GMSettingConfigCallback() {
        @Override
        public void configLoad() {
            Log.e(TAG, "load ad 在config 回调中加载广告");
            loadAd(mAdUnitId);
        }
    };

}
