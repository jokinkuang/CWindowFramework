package com.jokin.framework.moduleserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jokin.framework.modulesdk.IRemoteWindow;
import com.jokin.framework.modulesdk.IRemoteWindowManager;

import java.util.concurrent.ConcurrentHashMap;

public class ModuleCenterService extends Service {
    private static final String TAG = ModuleCenterService.class.getSimpleName();
    public static final String LOCAL = "FROM_LOCAL";

    private ConcurrentHashMap<IRemoteWindow, IRemoteWindow> mWindows = new ConcurrentHashMap<>(15);


    public ModuleCenterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent == null) {
            Log.d(TAG, "onBind() called with: null = [" + intent + "]");
            return null;
        }
        if (LOCAL.equalsIgnoreCase(intent.getAction())) {
            Log.d(TAG, "onBind() called with: local = [" + intent + "]");
            return new LocalBinder();
        }
        Log.d(TAG, "onBind() called with: remote = [" + intent + "]");
        return mRemoteBinder;
    }

    public void notifyOnCreate(Bundle bundle) {
        for (IRemoteWindow remoteWindow : mWindows.keySet()) {
            try {
                remoteWindow.onCreate(bundle);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnStart() {
        for (IRemoteWindow remoteWindow : mWindows.keySet()) {
            try {
                remoteWindow.onStart();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnResume() {
        for (IRemoteWindow remoteWindow : mWindows.keySet()) {
            try {
                remoteWindow.onResume();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnPause() {
        for (IRemoteWindow remoteWindow : mWindows.keySet()) {
            try {
                remoteWindow.onPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnRestart() {
        for (IRemoteWindow remoteWindow : mWindows.keySet()) {
            try {
                remoteWindow.onRestart();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnStop() {
        for (IRemoteWindow remoteWindow : mWindows.keySet()) {
            try {
                remoteWindow.onStop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnDestroy() {
        for (IRemoteWindow remoteWindow : mWindows.keySet()) {
            try {
                remoteWindow.onDestroy();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    ///////////////////

    private final IRemoteWindowManager.Stub mRemoteBinder = new IRemoteWindowManager.Stub() {

        @Override
        public void addWindow(IRemoteWindow remoteWindow) throws RemoteException {
            Log.d(TAG, "addWindow() called with: remoteWindow = [" + remoteWindow + "]");
            mWindows.put(remoteWindow, remoteWindow);
        }

        @Override
        public void removeWindow(IRemoteWindow remoteWindow) throws RemoteException {
            Log.d(TAG, "addWindow() called with: remoteWindow = [" + remoteWindow + "]");
            mWindows.remove(remoteWindow);
        }

    };

    public class LocalBinder extends Binder {
        public ModuleCenterService getModuleCenterService() {
            return ModuleCenterService.this;
        }
    };
}
