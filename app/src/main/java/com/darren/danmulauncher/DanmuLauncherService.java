package com.darren.danmulauncher;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

// TODO: 2018/5/16 无法找到控件 
public class DanmuLauncherService extends AccessibilityService {
    private boolean mIsInit = false;

    private AccessibilityNodeInfo mComplainEdit = null;
    private AccessibilityNodeInfo mSend = null;

    private DanmuLaunchTask mTask;
    public DanmuLauncherService() {
        mTask = new DanmuLaunchTask();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "accessibility service connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> complainEdit = nodeInfo.findAccessibilityNodeInfosByViewId("@id/complain_edit");
            if (!complainEdit.isEmpty()) {//如果是直播界面
                if (!mIsInit) {//如果未初始化
                    List<AccessibilityNodeInfo> send = nodeInfo.findAccessibilityNodeInfosByViewId("@id/send");
                    if (send.isEmpty()) {//如果未显示发送键
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        complainEdit.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    } else {
                        mComplainEdit = complainEdit.get(0);
                        mSend = complainEdit.get(0);
                        mIsInit = true;
                        mTask.execute();
                    }
                } else if (mComplainEdit == null || mSend == null) {//如果已初始化但界面变量丢失
                    mIsInit = false;
                } else {//条件全部满足
                    //run async task
                }
            } else {
                mIsInit = false;
                mTask.cancel(false);
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
            for (;;) {
                if (isCancelled()) {
                    return null;
                }
                if (countdown == 0) {
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "技术主播，欢迎订阅");
                    mComplainEdit.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    mSend.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    countdown += 4;
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
    }
}
