package com.darren.danmulauncher;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private RecyclerView mRvSettings;
    private Button mBtnSure;
    private FloatingActionButton mFabAdd;
    private Button mBtnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRvSettings = findViewById(R.id.rvSettings);
        mBtnSure = findViewById(R.id.btnSure);
        mFabAdd = findViewById(R.id.fabAdd);
        mBtnClear = findViewById(R.id.btnClear);


        final MainSettingAdapter adapter =
                new MainSettingAdapter(SharedPreferencesUtil.getStringSet(SharedPreferencesUtil.mKeySendContent, SharedPreferencesUtil.mDefSendContentSet)
                        ,SharedPreferencesUtil.getStringSet(SharedPreferencesUtil.mKeyContentSelected, SharedPreferencesUtil.mDefContentSelected)
                        ,SharedPreferencesUtil.getInt(SharedPreferencesUtil.mKeySendIntervals, SharedPreferencesUtil.mDefSendIntervals));
        mRvSettings.setAdapter(adapter);
        mRvSettings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mBtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.btnSure: {
                        SharedPreferencesUtil.putStringSet(
                                SharedPreferencesUtil.mKeySendContent,
                                adapter.getSendContents());
                        SharedPreferencesUtil.putInt(SharedPreferencesUtil.mKeySendIntervals,
                                adapter.getSendIntervals());
                        Set<String> tempSet = new LinkedHashSet<>(adapter.getSelectedList());
                        Iterator<String> iterator = tempSet.iterator();
                        while(iterator.hasNext()) {
                            String s = iterator.next();
                            if (!adapter.getSendContents().contains(s)) {
                                tempSet.remove(s);
                            }
                        }
                        SharedPreferencesUtil.putStringSet(SharedPreferencesUtil.mKeyContentSelected,
                                tempSet);
                    }
                }
            }
        });

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.fabAdd: {
                        adapter.addSendContent();
                        break;
                    }
                }
            }
        });

        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.btnClear: {
                        SharedPreferencesUtil.putStringSet(SharedPreferencesUtil.mKeyContentSelected, adapter.getSendContents());
                        adapter.notifyDataSetChanged();
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
