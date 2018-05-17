package com.darren.danmulauncher;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesUtil.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SharedPreferencesUtil.clear();
    }
}
