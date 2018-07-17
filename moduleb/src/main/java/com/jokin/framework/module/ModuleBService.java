package com.jokin.framework.module;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.impl.CWindow;
import com.jokin.framework.modulesdk.impl.CWindowManager;

public class ModuleBService extends Service {
    private static final String TAG = "ModuleBService";

    CWindow mWindow;
    CWindowManager mWindowManager;

    public ModuleBService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        mWindowManager.addWindow(mWindow);
        Log.d(TAG, "onStartCommand: window layoutparams" + mWindow.getLayoutParams());
        Log.d(TAG, "onStartCommand: window layoutparams" + mWindow.getWindowLayoutParams());
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
        mWindow = (CWindow) LayoutInflater.from(this).inflate(R.layout.layout_window, null, false);
        mWindow.setWindowLayoutParams(new IWindow.LayoutParams.Builder().build());
        mWindowManager = new CWindowManager(this);
    }
}
