package com.gromore.demo;

import android.app.Activity;
import android.util.Log;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.reward.RewardItem;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.ad.reward.GMRewardedAdListener;
import com.bytedance.msdk.api.v2.ad.reward.GMRewardedAdLoadCallback;

public class GromoreReward {
    private static final String TAG = GromoreReward.class.getName();
    private Activity activity;
    private AdRewardManager mAdRewardManager; //激励视频管理类
    private GromoreRewardListener listener;

    private boolean mLoadSuccess; //是否加载成功
    private GMRewardedAdListener mGMRewardedAdListener;
    private static GromoreReward mAfujiaReward = null;

    public static GromoreReward getInstance() {
        if (mAfujiaReward == null) {
            mAfujiaReward = new GromoreReward();
        }
        return mAfujiaReward;
    }


    private GromoreReward() {

    }

    public void loadReward(Activity activity) {
        this.activity = activity;
        mLoadSuccess = false;
        initListener();
        initAdLoader();
        mAdRewardManager.laodAdWithCallback(Config.REWARD, GMAdConstant.VERTICAL);
    }


    public void showReward(Activity activity, GromoreRewardListener listener) {
        this.activity = activity;
        this.listener = listener;
        showRewardAd(activity, listener);
    }

    public void initListener() {
        mGMRewardedAdListener = new GMRewardedAdListener() {

            /**
             * 广告的展示回调 每个广告仅回调一次
             */
            public void onRewardedAdShow() {
//                TToast.show(RewardVideoActivity.this, "激励onRewardedAdShow！");
                Log.d(TAG, "onRewardedAdShow");
                if (mAdRewardManager != null && mAdRewardManager.getGMRewardAd() != null && mAdRewardManager.getGMRewardAd().getShowEcpm() != null) {
                    String resultEcpm = mAdRewardManager.getGMRewardAd().getShowEcpm().getPreEcpm();
                    if (resultEcpm != null) {
                        double ecpm = Float.parseFloat(resultEcpm);
                    }

                }


            }

            /**
             * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
             * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
             * @param adError showFail的具体原因
             */
            @Override
            public void onRewardedAdShowFail(AdError adError) {
                if (adError == null) {
                    return;
                }
                if (listener != null) {
                    listener.onRewardFail();
                }
                // TToast.show(RewardVideoActivity.this, "激励onRewardedAdShowFail！ errCode: " + adError.code + ", errMsg: " + adError.message);
                //Log.d(TAG, "onRewardedAdShowFail, errCode: " + adError.code + ", errMsg: " + adError.message);
                // 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载
            }

            /**
             * 注意Admob的激励视频不会回调该方法
             */
            @Override
            public void onRewardClick() {
                //Log.d(TAG, "onRewardClick");
                //TToast.show(RewardVideoActivity.this, "激励onRewardClick！");

            }

            /**
             * 广告关闭的回调
             */
            public void onRewardedAdClosed() {
                Log.d(TAG, "onRewardedAdClosed");
                //TToast.show(RewardVideoActivity.this, "激励onRewardedAdClosed！");
                loadReward(activity);

            }

            /**
             * 视频播放完毕的回调 Admob广告不存在该回调
             */
            public void onVideoComplete() {
                Log.d(TAG, "onVideoComplete");
                //TToast.show(RewardVideoActivity.this, "激励onVideoComplete！");

            }

            /**
             * 1、视频播放失败的回调
             */
            public void onVideoError() {
                Log.d(TAG, "onVideoError");
                if (listener != null) {
                    listener.onRewardFail();
                }
//                TToast.show(RewardVideoActivity.this, "激励onVideoError！");

            }

            /**
             * 激励视频播放完毕，验证是否有效发放奖励的回调
             */
            public void onRewardVerify(RewardItem rewardItem) {
                Log.d(TAG, "onRewardVerify");
                if (listener != null) {
                    listener.onRewardVerify();
                }

            }

            /**
             * - Mintegral GDT Admob广告不存在该回调
             */
            @Override
            public void onSkippedVideo() {

            }

        };
    }

    public void initAdLoader() {
        mAdRewardManager = new AdRewardManager(activity, new GMRewardedAdLoadCallback() {
            @Override
            public void onRewardVideoLoadFail(AdError adError) {
                mLoadSuccess = false;
                Log.e(TAG, "load RewardVideo ad error : " + adError.code + ", " + adError.message);
                mAdRewardManager.printLoadFailAdnInfo();
            }

            @Override
            public void onRewardVideoAdLoad() {
                mLoadSuccess = true;
                Log.e(TAG, "load RewardVideo ad success !");
                // 获取本次waterfall加载中，加载失败的adn错误信息。
                mAdRewardManager.printLoadAdInfo(); //打印已经加载广告的信息
                mAdRewardManager.printLoadFailAdnInfo();//打印加载失败的adn错误信息
            }

            @Override
            public void onRewardVideoCached() {
                mLoadSuccess = true;
                Log.d(TAG, "onRewardVideoCached....缓存成功");

            }
        });
    }

    /**
     * 展示广告
     */
    public void showRewardAd(Activity activity, GromoreRewardListener listener) {
        if (mLoadSuccess && mAdRewardManager != null) {
            if (mAdRewardManager.getGMRewardAd() != null && mAdRewardManager.getGMRewardAd().isReady()) {
                //在获取到广告后展示,强烈建议在onRewardVideoCached回调后，展示广告，提升播放体验
                //该方法直接展示广告，如果展示失败了（如过期），会回调onVideoError()
                //展示广告，并传入广告展示的场景
                mAdRewardManager.getGMRewardAd().setRewardAdListener(mGMRewardedAdListener);
                mAdRewardManager.getGMRewardAd().showRewardAd(activity);
                mAdRewardManager.printSHowAdInfo();//打印已经展示的广告信息
                mLoadSuccess = false;
            } else {
                if (listener != null) {
                    listener.onRewardFail();
                }
                //TToast.show(this, "当前广告不满足show的条件");
            }
        } else {

            if (listener != null) {
                listener.onRewardFail();
            }
            //TToast.show(this, "请先加载广告");
            loadReward(activity);
        }
    }

    public static interface GromoreRewardListener {
        public abstract void onRewardVerify();

        public abstract void onRewardFail();

    }

    public void destroy() {
        if (mAdRewardManager != null) {
            mAdRewardManager.destroy();
        }


    }


}
