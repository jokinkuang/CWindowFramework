package com.jokin.framework.moduleb;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.jokin.framework.modulesdk.intent.ServerIntent;
import com.jokin.framework.modulesdk.log.Logger;


public class LauchActivity extends Activity {
    private static final String TAG = "LauchActivity";

    private static final String ACTION = "framework.moduleserver.main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch);
        verifyStoragePermissions(this);

        // Looper.myLooper().setMessageLoggerging(new LoggerPrinter(Logger.VERBOSE, TAG));

        Logger.d(TAG, "onCreate() called");
        // startActivity(ServerIntent.getServerMainActivityIntent(this));

        // startService(new Intent(this, ModuleBService.class));

        Intent intent = ServerIntent.getServerMainActivityIntent(this);
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(TAG, "onStop() called");
        startService(new Intent(this, ModuleBService.class));
        finish();
    }
}
