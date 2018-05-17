package com.darren.danmulauncher;

import android.app.Activity;
import android.os.Bundle;

import com.darren.danmulauncher.PreferenceFragment.MainSettingFragment;

public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainSettingFragment())
                .commit();
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
