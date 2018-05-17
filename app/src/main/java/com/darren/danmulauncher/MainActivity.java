package com.darren.danmulauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";

    private EditText mEtSendContent;
    private EditText mEtSendIntervals;
    private Button mBtnSendContent;
    private Button mBtnSendIntervals;

    private SharedPreferences mSharedPreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtSendContent = findViewById(R.id.etSendContent);
        mEtSendIntervals = findViewById(R.id.etSendIntervals);
        mBtnSendContent = findViewById(R.id.btnSendContent);
        mBtnSendIntervals = findViewById(R.id.btnSendIntervals);


        View.OnClickListener listener = new MyOnClickListener();

        setOnClicklistener(listener, mBtnSendContent, mBtnSendIntervals);

        mEtSendContent.setText(SharedPreferencesUtil.getString(SharedPreferencesUtil.mKeySendContent, SharedPreferencesUtil.mDefSendContent));
        mEtSendIntervals.setText(String.format(Locale.getDefault(), "%d", SharedPreferencesUtil.getInt( SharedPreferencesUtil.mKeySendIntervals, SharedPreferencesUtil.mDefSendIntervals)));



    }

    private void openOverLay() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(MainActivity.this, OverLayService.class);
                Toast.makeText(MainActivity.this,"已开启Toucher",Toast.LENGTH_SHORT).show();
                startService(intent);
                finish();
            } else {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Toast.makeText(MainActivity.this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        } else {
            //SDK在23以下，不用管.
            Intent intent = new Intent(MainActivity.this, OverLayService.class);
            startService(intent);
            finish();
        }
    }

    private void setOnClicklistener(View.OnClickListener listener, View ...views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btnSendContent: {
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.mKeySendContent, mEtSendContent.getText().toString());
                    Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.btnSendIntervals: {
                    SharedPreferencesUtil.putInt(SharedPreferencesUtil.mKeySendIntervals, Integer.valueOf(mEtSendIntervals.getText().toString()));
                    Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                }

            }
        }
    }

}
