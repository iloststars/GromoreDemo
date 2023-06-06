package com.gromore.demo;

import android.app.Activity;
import android.util.Log;

import com.bytedance.msdk.adapter.util.Logger;
import com.bytedance.msdk.adapter.util.UIUtils;
import com.bytedance.msdk.api.GMAdEcpmInfo;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.GMNetworkRequestInfo;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAd;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdListener;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdLoadCallback;
import com.bytedance.msdk.api.v2.slot.GMAdSlotSplash;

import java.util.List;

public class AdSplashManager {
    private static final String TAG = AdSplashManager.class.getSimpleName();
    private GMSplashAd mSplashAd;
    private Activity mActivity;
    //开屏广告加载超时时间,建议大于1000,这里为了冷启动第一次加载到广告并且展示,示例设置了2000ms
    private static final int AD_TIME_OUT = 4000;
    private GMSplashAdLoadCallback mGMSplashAdLoadCallback;
    private GMSplashAdListener mSplashAdListener;

    public AdSplashManager(Activity activity, GMSplashAdLoadCallback splashAdLoadCallback, GMSplashAdListener splashAdListener) {
        mActivity = activity;
        mGMSplashAdLoadCallback = splashAdLoadCallback;
        mSplashAdListener = splashAdListener;
    }

    public GMSplashAd getSplashAd() {
        return mSplashAd;
    }

    /**
     * 加载开屏广告
     */
    public void loadSplashAd(String unitId) {
        /**
         * 注：每次加载开屏广告的时候需要新建一个TTSplashAd，否则可能会出现广告填充问题
         * （ 例如：mTTSplashAd = new TTSplashAd(this, mAdUnitId);）
         */
        mSplashAd = new GMSplashAd(mActivity, unitId);
        mSplashAd.setAdSplashListener(mSplashAdListener);

        //创建开屏广告请求参数AdSlot,具体参数含义参考文档
        GMAdSlotSplash adSlot = new GMAdSlotSplash.Builder()
                .setImageAdSize(UIUtils.getRealWidth(mActivity), (int)(UIUtils.getRealHeight(mActivity)*0.8f)) // 单位px
                .setSplashPreLoad(true)//开屏gdt开屏广告预加载
                .setMuted(false) //声音开启
                .setVolume(1f)//admob 声音配置，与setMuted配合使用
                .setTimeOut(AD_TIME_OUT)//设置超时
                .setSplashButtonType(GMAdConstant.SPLASH_BUTTON_TYPE_FULL_SCREEN)
                .setDownloadType(GMAdConstant.DOWNLOAD_TYPE_POPUP)
                .setBidNotify(true)//开启bidding比价结果通知，默认值为false
                .setSplashShakeButton(true) //开屏摇一摇开关，默认开启，目前只有gdt支持
                .build();

        //自定义兜底方案 选择使用
        GMNetworkRequestInfo networkRequestInfo = SplashUtils.getGMNetworkRequestInfo();
        //请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mSplashAd.loadAd(adSlot, networkRequestInfo, mGMSplashAdLoadCallback);
    }


    /**
     * 打印其他信息
     */
    public void printInfo() {
        if (mSplashAd != null) {
            /**
             * 获取已经加载的clientBidding ，多阶底价广告的相关信息
             */
            List<GMAdEcpmInfo> gmAdEcpmInfos = mSplashAd.getMultiBiddingEcpm();
            if (gmAdEcpmInfos != null) {
                for (GMAdEcpmInfo info : gmAdEcpmInfos) {
                    Log.e(TAG, "多阶+client相关信息 AdNetworkPlatformId" + info.getAdNetworkPlatformId()
                            + "  AdNetworkRitId:" + info.getAdNetworkRitId()
                            + "  ReqBiddingType:" + info.getReqBiddingType()
                            + "  PreEcpm:" + info.getPreEcpm()
                            + "  LevelTag:" + info.getLevelTag()
                            + "  ErrorMsg:" + info.getErrorMsg()
                            + "  request_id:" + info.getRequestId()
                            + "  SdkName:" + info.getAdNetworkPlatformName()
                            + "  CustomSdkName:" + info.getCustomAdNetworkPlatformName());
                }
            }

            /**
             * 获取实时填充/缓存池中价格最优的代码位信息即相关价格信息，每次只有一个信息
             */
            GMAdEcpmInfo gmAdEcpmInfo = mSplashAd.getBestEcpm();
            if (gmAdEcpmInfo != null) {
                Log.e(TAG, "***实时填充/缓存池中价格最优的代码位信息*** AdNetworkPlatformId" + gmAdEcpmInfo.getAdNetworkPlatformId()
                        + "  AdNetworkRitId:" + gmAdEcpmInfo.getAdNetworkRitId()
                        + "  ReqBiddingType:" + gmAdEcpmInfo.getReqBiddingType()
                        + "  PreEcpm:" + gmAdEcpmInfo.getPreEcpm()
                        + "  LevelTag:" + gmAdEcpmInfo.getLevelTag()
                        + "  ErrorMsg:" + gmAdEcpmInfo.getErrorMsg()
                        + "  request_id:" + gmAdEcpmInfo.getRequestId()
                        + "  SdkName:" + gmAdEcpmInfo.getAdNetworkPlatformName()
                        + "  CustomSdkName:" + gmAdEcpmInfo.getCustomAdNetworkPlatformName());
            }

            /**
             * 获取获取当前缓存池的全部信息
             */
            List<GMAdEcpmInfo> gmCacheInfos = mSplashAd.getCacheList();
            if (gmCacheInfos != null) {
                for (GMAdEcpmInfo info : gmCacheInfos) {
                    Log.e(TAG, "***缓存池的全部信息*** AdNetworkPlatformId" + info.getAdNetworkPlatformId()
                            + "  AdNetworkRitId:" + info.getAdNetworkRitId()
                            + "  ReqBiddingType:" + info.getReqBiddingType()
                            + "  PreEcpm:" + info.getPreEcpm()
                            + "  LevelTag:" + info.getLevelTag()
                            + "  ErrorMsg:" + info.getErrorMsg()
                            + "  request_id:" + info.getRequestId()
                            + "  SdkName:" + info.getAdNetworkPlatformName()
                            + "  CustomSdkName:" + info.getCustomAdNetworkPlatformName());
                }
            }

            /**
             * 获取获展示广告的部信息
             */
            GMAdEcpmInfo showGMAdEcpmInfo = mSplashAd.getShowEcpm();

            if (showGMAdEcpmInfo != null) {
                Logger.e(TAG, "展示的广告信息 ：adNetworkPlatformName: " + gmAdEcpmInfo.getAdNetworkPlatformName()
                        + "   CustomAdNetworkPlatformName: " + gmAdEcpmInfo.getCustomAdNetworkPlatformName()
                        + "   adNetworkRitId: " + gmAdEcpmInfo.getAdNetworkRitId()
                        + "   preEcpm: " + gmAdEcpmInfo.getPreEcpm());
            }
            // 获取本次waterfall加载中，加载失败的adn错误信息。
            if (mSplashAd != null)
                Log.d(TAG, "ad load infos: " + mSplashAd.getAdLoadInfoList());
        }
    }

    public void destroy() {
        if (mSplashAd != null) {
            mSplashAd.destroy();
        }
        mActivity = null;
        mGMSplashAdLoadCallback = null;
        mSplashAdListener = null;
    }

}
