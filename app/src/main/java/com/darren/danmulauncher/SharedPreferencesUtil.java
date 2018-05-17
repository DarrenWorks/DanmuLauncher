package com.darren.danmulauncher;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static SharedPreferences sharedPreferences;

    public static final String mKeySendContent = "弹幕";
    public static final String mKeySendIntervals = "间隔";

    public static final String mDefSendContent = "喜欢的朋友别忘记订阅";
    public static final int mDefSendIntervals = 30;//单位：秒

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("Config", Context.MODE_PRIVATE);
    }

    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public static boolean putString(String key, String value) {
        return sharedPreferences.edit().putString(key, value).commit();
    }

    public static  int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public static boolean putInt(String key, int value) {
        return sharedPreferences.edit().putInt(key, value).commit();
    }

    public static void clear() {
        sharedPreferences = null;
    }
}
