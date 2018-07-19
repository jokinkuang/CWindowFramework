package com.jokin.framework.moduleserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jokin.framework.modulesdk.IModuleClient;
import com.jokin.framework.modulesdk.IModuleServer;

import java.util.concurrent.ConcurrentHashMap;

public class ModuleCenterService extends Service {
    private static final String TAG = ModuleCenterService.class.getSimpleName();
    public static final String LOCAL = "FROM_LOCAL";

    private ConcurrentHashMap<String, IModuleClient> mClientModules = new ConcurrentHashMap<>(15);

    public ModuleCenterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed with mClientModules size:"+mClientModules.size());
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent == null) {
            Log.d(TAG, "onBind() called with: remote = [" + intent + "]");
            return mRemoteBinder;
        }
        if (LOCAL.equalsIgnoreCase(intent.getAction())) {
            Log.d(TAG, "onBind() called with: local = [" + intent + "]");
            return new LocalBinder();
        }
        Log.d(TAG, "onBind() called with: remote = [" + intent + "]");
        return mRemoteBinder;
    }

    public void notifyOnCreate(Bundle bundle) {
        Log.d(TAG, "notifyOnCreate() called with size: "+mClientModules.size());
        for (IModuleClient remoteClient : mClientModules.values()) {
            try {
                remoteClient.onCreate(bundle);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnStart() {
        Log.d(TAG, "notifyOnStart() called with size: "+mClientModules.size());
        for (IModuleClient remoteClient : mClientModules.values()) {
            try {
                remoteClient.onStart();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnResume() {
        Log.d(TAG, "notifyOnResume() called with size: "+mClientModules.size());
        for (IModuleClient remoteClient : mClientModules.values()) {
            try {
                remoteClient.onResume();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnPause() {
        Log.d(TAG, "notifyOnPause() called with size: "+mClientModules.size());
        for (IModuleClient remoteClient : mClientModules.values()) {
            try {
                remoteClient.onPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnRestart() {
        Log.d(TAG, "notifyOnRestart() called with size: "+mClientModules.size());
        for (IModuleClient remoteClient : mClientModules.values()) {
            try {
                remoteClient.onRestart();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnStop() {
        Log.d(TAG, "notifyOnStop() called with size: "+mClientModules.size());
        for (IModuleClient remoteClient : mClientModules.values()) {
            try {
                remoteClient.onStop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnDestroy() {
        Log.d(TAG, "notifyOnDestroy() called with size: "+mClientModules.size());
        for (IModuleClient remoteClient : mClientModules.values()) {
            try {
                remoteClient.onDestroy();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    ///////////////////

    private final IModuleServer.Stub mRemoteBinder = new IModuleServer.Stub() {
        @Override
        public void registerModule(IModuleClient client) throws RemoteException {
            Log.d(TAG, "registerModule() called with: client = [" + client.key() + "]");
            mClientModules.put(client.key(), client);
        }

        @Override
        public void unregisterModule(IModuleClient client) throws RemoteException {
            Log.d(TAG, "unregisterModule() called with: client = [" + client.key() + "]");
            mClientModules.remove(client.key());
        }
    };

    public class LocalBinder extends Binder {
        public ModuleCenterService getModuleCenterService() {
            return ModuleCenterService.this;
        }
    };
}
