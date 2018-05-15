package com.darren.danmulauncher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;


public class OverLayService extends Service {
    public static final String TAG = "OverLayService";
    //要引用的布局文件.
    ConstraintLayout overLayLayout;
    //布局参数.
    WindowManager.LayoutParams params;
    //实例化的WindowManager.
    WindowManager windowManager;

    ImageButton imageButton;

    //状态栏高度.（接下来会用到）
    int statusBarHeight = -1;

    public OverLayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        createOverLay();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createOverLay() {
        WindowManager mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        //注意，这里的width和height均使用px而非dp.这里我偷了个懒
        //如果你想完全对应布局设置，需要先获取到机器的dpi
        //px与dp的换算为px = dp * (dpi / 160).
        params.width = 50;
        params.height = 50;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        overLayLayout = (ConstraintLayout) inflater.inflate(R.layout.over_lay_layout,null);
        //添加toucherlayout
        windowManager.addView(overLayLayout,params);

        Log.i(TAG,"toucherlayout-->left:" + overLayLayout.getLeft());
        Log.i(TAG,"toucherlayout-->right:" + overLayLayout.getRight());
        Log.i(TAG,"toucherlayout-->top:" + overLayLayout.getTop());
        Log.i(TAG,"toucherlayout-->bottom:" + overLayLayout.getBottom());

        //主动计算出当前View的宽高信息.
        overLayLayout.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0)
        {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG,"状态栏高度为:" + statusBarHeight);

        //浮动窗口按钮.
        imageButton = (ImageButton) overLayLayout.findViewById(R.id.ImageButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                switch (id) {
                    case R.id.ImageButton:
                        Toast.makeText(OverLayService.this, "onClick", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        setOnClicklistener(listener, imageButton);
    }

    private void setOnClicklistener(View.OnClickListener listener, View ...views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }
}
