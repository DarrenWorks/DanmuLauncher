package com.darren.danmulauncher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";

    private RecyclerView mRvSettings;
    private Button btnSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRvSettings = findViewById(R.id.rvSettings);
        btnSure = findViewById(R.id.btnSure);


        final MainSettingAdapter adapter =
                new MainSettingAdapter(SharedPreferencesUtil.getStringSet(
                        SharedPreferencesUtil.mKeySendContent,
                        SharedPreferencesUtil.mDefSendContentSet
                ));
        mRvSettings.setAdapter(adapter);
        mRvSettings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.btnSure: {
                        SharedPreferencesUtil.putStringSet(
                                SharedPreferencesUtil.mKeySendContent,
                                adapter.getSendContents());
                    }
                }
            }
        });
    }
//    private void openOverLay() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (Settings.canDrawOverlays(MainActivity.this)) {
//                Intent intent = new Intent(MainActivity.this, OverLayService.class);
//                Toast.makeText(MainActivity.this,"已开启Toucher",Toast.LENGTH_SHORT).show();
//                startService(intent);
//                finish();
//            } else {
//                //若没有权限，提示获取.
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                Toast.makeText(MainActivity.this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
//                startActivity(intent);
//            }
//        } else {
//            //SDK在23以下，不用管.
//            Intent intent = new Intent(MainActivity.this, OverLayService.class);
//            startService(intent);
//            finish();
//        }
//    }
}
