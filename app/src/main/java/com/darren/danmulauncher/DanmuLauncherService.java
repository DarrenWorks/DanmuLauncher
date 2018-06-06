package com.darren.danmulauncher;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.icu.util.UniversalTimeScale;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DanmuLauncherService extends AccessibilityService {
    public static final String TAG = "DanmuLauncherService";

    private boolean mIsInit = false;

    private AccessibilityNodeInfo mComplainEdit = null;
    private AccessibilityNodeInfo mSend = null;

    private DanmuLaunchTask mTask;

    private List<String> mSelectedSendContents;

    public DanmuLauncherService() {
        mTask = new DanmuLaunchTask();
        mTask.execute();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> complainEdit = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.qgame:id/complain_edit");
            if (!complainEdit.isEmpty()) {//如果是直播界面
                Log.d(TAG, "onAccessibilityEvent: is the live layout");
                if (!mIsInit) {//如果未初始化
                    mComplainEdit = complainEdit.get(0);
                    mIsInit = true;

                } else if (mComplainEdit == null) {//如果已初始化但界面变量丢失
                    mIsInit = false;
                } else {//条件全部满足

                }
            } else {
                mIsInit = false;
                mComplainEdit = null;
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    class DanmuLaunchTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            int countdown = 0;
            for (; ; ) {
                if (!mIsInit) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                if (countdown == 0) {
                    Bundle arguments = new Bundle();
                    mSelectedSendContents = new ArrayList<>(SharedPreferencesUtil.getStringSet(SharedPreferencesUtil.mKeyContentSelected, SharedPreferencesUtil.mDefContentSelected));
                    Random random = new Random();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, mSelectedSendContents.get(random.nextInt(mSelectedSendContents.size())));
                    mComplainEdit.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                    if (nodeInfo != null) {
                        List<AccessibilityNodeInfo> send = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.qgame:id/send");
                        if (!send.isEmpty()) {
                            send.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                    countdown += SharedPreferencesUtil.getInt(SharedPreferencesUtil.mKeySendIntervals, SharedPreferencesUtil.mDefSendIntervals) * 2;
                } else {
                    countdown--;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "onCancelled: task canceled");
        }
    }
}
