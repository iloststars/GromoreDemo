package com.gromore.demo.utils;

import com.bytedance.msdk.adapter.pangle.PangleNetworkRequestInfo;
import com.bytedance.msdk.api.v2.GMNetworkRequestInfo;
import com.gromore.demo.ad.config.Config;

public class SplashUtils {
    public static GMNetworkRequestInfo getGMNetworkRequestInfo() {
        GMNetworkRequestInfo networkRequestInfo;
//        //穿山甲兜底，参数分别是appId和adn代码位。注意第二个参数是代码位，而不是广告位。
        networkRequestInfo = new PangleNetworkRequestInfo(Config.FIRST_APPID, Config.FIRST_SPLASH);
        return networkRequestInfo;
    }
}
