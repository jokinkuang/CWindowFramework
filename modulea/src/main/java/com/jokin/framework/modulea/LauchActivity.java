package com.jokin.framework.modulea;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jokin.framework.modulesdk.intent.ServerIntent;
import com.jokin.framework.modulesdk.log.Logger;


public class LauchActivity extends Activity {
    private static final String TAG = "LauchActivity";

    private static final String ACTION = "framework.moduleserver.main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(TAG, "onCreate() called");
        // startActivity(ServerIntent.getServerMainActivityIntent(this));

        // startService(new Intent(this, ModuleBService.class));

        Intent intent = ServerIntent.getServerMainActivityIntent(this);
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(TAG, "onStop() called");
        startService(new Intent(this, ModuleAService.class));
        finish();
    }
}
