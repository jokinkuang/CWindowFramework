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
import com.jokin.framework.modulesdk.constant.Constants;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleCenterService extends Service {
    private static final String TAG = ModuleCenterService.class.getSimpleName();

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
            Log.d(TAG, "onBind() called with intent null [" + intent + "]");
            return mModuleServer;
        }
        if (Constants.BINDER_LOCAL.equalsIgnoreCase(intent.getAction())) {
            Log.d(TAG, "onBind() called with: local = [" + intent + "]");
            return new LocalBinder();
        }
        Log.d(TAG, "onBind() called with: remote = [" + intent + "]");
        return mModuleServer;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO remote all listeners form the package!
        Log.d(TAG, "onUnbind() called with: intent = [" + intent + "]");
        return super.onUnbind(intent);
    }

    public void notifyOnCreate(Bundle bundle) {
        Log.d(TAG, "notifyOnCreate() called with size: "+mClientModules.size());
        for (Iterator<IModuleClient> iterator = mClientModules.values().iterator(); iterator.hasNext();) {
            IModuleClient remoteClient = iterator.next();
            try {
                remoteClient.onCreate(bundle);
            } catch (Exception e) {
                e.printStackTrace();
                iterator.remove();
            }
        }
    }

    public void notifyOnStart() {
        Log.d(TAG, "notifyOnStart() called with size: "+mClientModules.size());
        for (Iterator<IModuleClient> iterator = mClientModules.values().iterator(); iterator.hasNext();) {
            IModuleClient remoteClient = iterator.next();
            try {
                remoteClient.onStart();
            } catch (Exception e) {
                e.printStackTrace();
                iterator.remove();
            }
        }
    }

    public void notifyOnResume() {
        Log.d(TAG, "notifyOnResume() called with size: "+mClientModules.size());
        for (Iterator<IModuleClient> iterator = mClientModules.values().iterator(); iterator.hasNext();) {
            IModuleClient remoteClient = iterator.next();
            try {
                remoteClient.onResume();
            } catch (Exception e) {
                e.printStackTrace();
                iterator.remove();
            }
        }
    }

    public void notifyOnPause() {
        Log.d(TAG, "notifyOnPause() called with size: "+mClientModules.size());
        for (Iterator<IModuleClient> iterator = mClientModules.values().iterator(); iterator.hasNext();) {
            IModuleClient remoteClient = iterator.next();
            try {
                remoteClient.onPause();
            } catch (Exception e) {
                e.printStackTrace();
                iterator.remove();
            }
        }
    }

    public void notifyOnRestart() {
        Log.d(TAG, "notifyOnRestart() called with size: "+mClientModules.size());
        for (Iterator<IModuleClient> iterator = mClientModules.values().iterator(); iterator.hasNext();) {
            IModuleClient remoteClient = iterator.next();
            try {
                remoteClient.onRestart();
            } catch (Exception e) {
                e.printStackTrace();
                iterator.remove();
            }
        }
    }

    public void notifyOnStop() {
        Log.d(TAG, "notifyOnStop() called with size: "+mClientModules.size());
        for (Iterator<IModuleClient> iterator = mClientModules.values().iterator(); iterator.hasNext();) {
            IModuleClient remoteClient = iterator.next();
            try {
                remoteClient.onStop();
            } catch (Exception e) {
                e.printStackTrace();
                iterator.remove();
            }
        }
    }

    public void notifyOnDestroy() {
        Log.d(TAG, "notifyOnDestroy() called with size: "+mClientModules.size());
        for (Iterator<IModuleClient> iterator = mClientModules.values().iterator(); iterator.hasNext();) {
            IModuleClient remoteClient = iterator.next();
            try {
                remoteClient.onDestroy();
            } catch (Exception e) {
                e.printStackTrace();
                iterator.remove();
            }
        }
    }

    ///////////////////

    private final IModuleServer.Stub mModuleServer = new IModuleServer.Stub() {
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
        public ModuleCenterService getServiceInstance() {
            return ModuleCenterService.this;
        }
    };
}
