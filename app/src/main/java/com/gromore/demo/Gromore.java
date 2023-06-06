package com.gromore.demo;

import android.content.Context;

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
