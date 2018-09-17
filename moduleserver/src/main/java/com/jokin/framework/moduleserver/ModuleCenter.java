package com.jokin.framework.moduleserver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.jokin.framework.modulesdk.constant.Constants;
import com.jokin.framework.modulesdk.constant.Server;
import com.jokin.framework.modulesdk.iwindow.ILifecycable;

import java.util.HashMap;

/**
 * Created by jokin on 2018/7/16 19:58.
 */

public class ModuleCenter implements ILifecycable {
    private static final String TAG = ModuleCenter.class.getSimpleName();

    private Context mContext;
    private volatile ModuleCenterService mModuleCenterService;
    private volatile ViewCenterService mViewCenterService;
    private HashMap<IRemoteViewListener, IRemoteViewListener> mRemoteViewListeners = new HashMap<>(20);

    public ModuleCenter(Context context) {
        mContext = context;
        initModuleCenter();
    }

    private void initModuleCenter() {
        Intent intent = new Intent(mContext, ModuleCenterService.class);
        intent.setAction(Constants.BINDER_LOCAL);
        mContext.bindService(intent, mModuleCenterServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mModuleCenterServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
            ModuleCenterService.LocalBinder localBinder = (ModuleCenterService.LocalBinder)service;
            if (localBinder == null) {
                throw new NullPointerException("Fatal error!");
            }
            mModuleCenterService = ((ModuleCenterService.LocalBinder)service).getServiceInstance();

            // init view server
            mViewCenterService = (ViewCenterService) localBinder.getService(Server.VIEW_SERVICE);
            if (mRemoteViewListeners.size() > 0) {
                Log.d(TAG, "onServiceConnected: mRemoteViewListeners.size="+mRemoteViewListeners.size());
                for (IRemoteViewListener listener : mRemoteViewListeners.values()) {
                    mViewCenterService.registerRemoteViewListener(listener);
                }
                mRemoteViewListeners.clear();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");
            mModuleCenterService = null;
            mViewCenterService = null;
        }
    };

    public void registerRemoteViewListener(IRemoteViewListener listener) {
        if (mViewCenterService != null) {
            Log.d(TAG, "registerRemoteViewListener: add now " + listener);
            mViewCenterService.registerRemoteViewListener(listener);
        } else {
            Log.d(TAG, "registerRemoteViewListener: add delay " + listener);
            mRemoteViewListeners.put(listener, listener);
        }
    }

    public void unregisterRemoteViewListener(IRemoteViewListener listener) {
        if (mViewCenterService != null) {
            Log.d(TAG, "registerRemoteViewListener: remove now " + listener);
            mViewCenterService.unregisterRemoteViewListener(listener);
        } else {
            Log.d(TAG, "registerRemoteViewListener: remove delay " + listener);
            mRemoteViewListeners.remove(listener);
        }
    }

    //////////// Lifecycle /////////////

    @Override
    public void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate() called with:" + mModuleCenterService);
        if (mModuleCenterService != null) {
            mModuleCenterService.notifyOnCreate(bundle);
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() called with:" + mModuleCenterService);
        if (mModuleCenterService != null) {
            mModuleCenterService.notifyOnStart();
        }
    }

    @Override
    public void onRestart() {
        Log.d(TAG, "onRestart() called with:" + mModuleCenterService);
        if (mModuleCenterService != null) {
            mModuleCenterService.notifyOnRestart();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume() called with:" + mModuleCenterService);
        if (mModuleCenterService != null) {
            mModuleCenterService.notifyOnResume();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called with:" + mModuleCenterService);
        if (mModuleCenterService != null) {
            mModuleCenterService.notifyOnPause();
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop() called with:" + mModuleCenterService);
        if (mModuleCenterService != null) {
            mModuleCenterService.notifyOnStop();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called with:" + mModuleCenterService);
        if (mModuleCenterService != null) {
            mModuleCenterService.notifyOnDestroy();
        }
        mContext.unbindService(mModuleCenterServiceConnection);
    }
}
