package com.jokin.framework.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jokin.framework.modulesdk.intent.ServerIntent;


public class LauchActivity extends Activity {
    private static final String TAG = "LauchActivity";

    private static final String ACTION = "framework.moduleserver.main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch);

        Log.d(TAG, "onCreate() called");
        // startActivity(ServerIntent.getServerMainActivityIntent(this));

        // startService(new Intent(this, ModuleBService.class));

        Intent intent = ServerIntent.getServerMainActivityIntent(this);
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
        startService(new Intent(this, ModuleBService.class));
        finish();
    }
}
