package com.jokin.framework.moduleserver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.jokin.framework.modulesdk.iwindow.ILifecycable;

/**
 * Created by jokin on 2018/7/16 19:58.
 */

public class ModuleCenter implements ILifecycable {
    private static final String TAG = ModuleCenter.class.getSimpleName();

    private Context mContext;
    private ModuleCenterService mService;

    public ModuleCenter(Context context) {
        mContext = context;

        Intent intent = new Intent(mContext, ModuleCenterService.class);
        intent.setAction(ModuleCenterService.LOCAL);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
            mService = ((ModuleCenterService.LocalBinder)service).getModuleCenterService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");
            mService = null;
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate() called with:" + mService);
        if (mService != null) {
            mService.notifyOnCreate(bundle);
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() called with:" + mService);
        if (mService != null) {
            mService.notifyOnStart();
        }
    }

    @Override
    public void onRestart() {
        Log.d(TAG, "onRestart() called with:" + mService);
        if (mService != null) {
            mService.notifyOnRestart();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume() called with:" + mService);
        if (mService != null) {
            mService.notifyOnResume();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called with:" + mService);
        if (mService != null) {
            mService.notifyOnPause();
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop() called with:" + mService);
        if (mService != null) {
            mService.notifyOnStop();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called with:" + mService);
        if (mService != null) {
            mService.notifyOnDestroy();
        }
        mContext.unbindService(mServiceConnection);
    }
}
