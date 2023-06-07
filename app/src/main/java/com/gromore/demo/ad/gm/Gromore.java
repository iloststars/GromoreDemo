package com.gromore.demo.ad.gm;

import android.content.Context;

import com.gromore.demo.ad.manager.GMAdManagerHolder;

public class Gromore {

    private static Gromore mAfujiaGromore = null;

    private Gromore() {
    }

    public static Gromore getInstance() {
        if (mAfujiaGromore == null) {
            mAfujiaGromore = new Gromore();
        }
        return mAfujiaGromore;
    }


    public void init(Context context) {
        GMAdManagerHolder.init(context);
    }

}
