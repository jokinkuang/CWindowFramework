package com.jokin.framework.modulesdk.impl;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.jokin.framework.modulesdk.IClientModule;
import com.jokin.framework.modulesdk.IWindowManager;
import com.jokin.framework.modulesdk.constant.Constants;
import com.jokin.framework.modulesdk.iwindow.ILifecycable;
import com.jokin.framework.modulesdk.view.IRemoteViewManager;
import com.jokin.framework.modulesdk.view.RemoteWindowManager;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jokin on 2018/7/16 10:57.
 *
 * Module
 */

public class CModule implements IClientModule {
    private static final String TAG = "CModule";
    private final String mKey;

    private ConcurrentHashMap<ILifecycable, ILifecycable> mLifecycables = new ConcurrentHashMap<>(10);
    private Context mContext;
    private IWindowManager mWindowManager;
    private IRemoteViewManager mRemoteViewManager;

    private static volatile CModuleManager sModuleManager;

    public CModule(Context context) {
        mContext = context.getApplicationContext();
        mKey = mContext.getPackageName()+ Constants.KEY_SEPARATOR+this.hashCode();

        if (sModuleManager == null) {
            sModuleManager = new CModuleManager(mContext);
        }
        sModuleManager.registerModule(this);
        sModuleManager.registerModule(this);
        sModuleManager.registerModule(this);
        sModuleManager.registerModule(this);

        mWindowManager = new CWindowManager(this);
        mRemoteViewManager = new RemoteWindowManager(this, sModuleManager);
    }

    @Override
    public void onCreate(Bundle bundle) {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onCreate(bundle);
        }
    }

    @Override
    public void onStart() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onStart();
        }
    }

    @Override
    public void onRestart() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onRestart();
        }
    }

    @Override
    public void onResume() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onResume();
        }
    }

    @Override
    public void onPause() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onPause();
        }
    }

    @Override
    public void onStop() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onStop();
        }
    }

    @Override
    public void onDestroy() {
        // Passive destroy(被动）
        Log.i(TAG, "## Passive onDestroy()");
        destroy();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public String key() {
        return String.valueOf(mKey);
    }

    // Active destroy(主动)
    @Override
    public void destroy() {
        Log.d(TAG, "destroy() called");
        sModuleManager.unregisterModule(this);
        if (sModuleManager.tryDestroy()) {
            sModuleManager = null;
        }

        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onDestroy();
        }
        mLifecycables.clear();

        mWindowManager.destroy();
    }

    @Override
    public void addLifecycleCallback(ILifecycable lifecycable) {
        mLifecycables.put(lifecycable, lifecycable);
    }

    @Override
    public void removeLifecycleCallback(ILifecycable lifecycable) {
        mLifecycables.remove(lifecycable);
    }

    ////////////// Window Manager ////////////////

    @Override
    public IWindowManager getWindowManagerService() {
        return mWindowManager;
    }

    @Override
    public IRemoteViewManager getRemoteViewManagerService() {
        return mRemoteViewManager;
    }
}
