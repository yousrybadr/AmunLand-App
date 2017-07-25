package com.example.yousry.amunland;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by yousry on 4/26/2017.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }
}
