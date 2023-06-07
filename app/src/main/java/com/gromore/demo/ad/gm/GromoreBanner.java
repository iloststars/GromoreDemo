package com.gromore.demo.ad.gm;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdListener;
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdLoadCallback;
import com.gromore.demo.ad.config.Config;
import com.gromore.demo.ad.manager.AdBannerManager;

public class GromoreBanner {
    private static final String TAG = GromoreBanner.class.getSimpleName();

    private ViewGroup mBannerContainer;
    private GMBannerAdListener mAdBannerListener;
    //广告管理类
    private AdBannerManager mAdBannerManager;
    private Activity activity;

    public GromoreBanner(ViewGroup mBannerContainer, Activity activity) {
        this.mBannerContainer = mBannerContainer;
        this.activity = activity;
    }

    public void showBanner() {
        initListener();
        initAdLoader();
        if (mAdBannerManager != null) {
            mAdBannerManager.loadAdWithCallback(Config.BANNER);
        }
    }


    public void initAdLoader() {
        mAdBannerManager = new AdBannerManager(activity, new GMBannerAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(AdError adError) {
                Log.e(TAG, "load banner ad error : " + adError.code + ", " + adError.message);
                mBannerContainer.removeAllViews();
                //mAdBannerManager.printLoadFailAdnInfo();// 获取本次waterfall加载中，加载失败的adn错误信息。


            }

            @Override
            public void onAdLoaded() {
                //mBannerContainer.setVisibility(View.GONE);
                Log.i(TAG, "banner load success ");
                showBannerAd();
            }
        }, mAdBannerListener);
    }

    public void initListener() {
        mAdBannerListener = new GMBannerAdListener() {

            @Override
            public void onAdOpened() {
                Log.d(TAG, "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.d(TAG, "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.d(TAG, "onAdClosed");
            }

            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked");
            }

            @Override
            public void onAdShow() {
                Log.d(TAG, "onAdShow");
                if (mAdBannerManager != null && mAdBannerManager.getBannerAd() != null && mAdBannerManager.getBannerAd().getShowEcpm() != null) {
                    String resultEcpm = mAdBannerManager.getBannerAd().getShowEcpm().getPreEcpm();
                    if (resultEcpm != null) {

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
                Log.d(TAG, "onAdShowFail");
            }
        };
    }


    /**
     * 展示广告
     */
    private void showBannerAd() {
        /**
         * 加载成功才能展示
         */
        if (mAdBannerManager != null) {
            /**
             * 在添加banner的View前需要清空父容器
             */
            mBannerContainer.removeAllViews();
            if (mAdBannerManager.getBannerAd() != null) {
                // 在调用getBannerView之前，可以选择使用isReady进行判断，当前是否有可用广告。
                if (!mAdBannerManager.getBannerAd().isReady()) {
                    //TToast.show(this, "广告已经无效，建议重新请求");
                    return;
                }
                //横幅广告容器的尺寸必须至少与横幅广告一样大。如果您的容器留有内边距，实际上将会减小容器大小。如果容器无法容纳横幅广告，则横幅广告不会展示
                /**
                 * mBannerViewAd.getBannerView()一个广告对象只能调用一次，第二次为null
                 */
                View view = mAdBannerManager.getBannerAd().getBannerView();
                if (view != null) {
                    mBannerContainer.addView(view);
                } else {
                    //TToast.show(this, "请重新加载广告");
                }
            }
        } else {
            //TToast.show(this, "请先加载广告");
        }
    }

    public void destroy() {
        if (mAdBannerManager != null) {
            mAdBannerManager.destroy();
        }
    }


}
