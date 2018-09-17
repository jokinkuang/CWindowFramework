package com.jokin.framework.moduleserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.jokin.framework.modulesdk.IModuleClient;
import com.jokin.framework.modulesdk.IModuleServer;
import com.jokin.framework.modulesdk.IViewServer;
import com.jokin.framework.modulesdk.constant.Constants;
import com.jokin.framework.modulesdk.constant.Server;
import com.jokin.framework.modulesdk.log.Logger;

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
        Logger.d(TAG, "onCreate() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "Service destroyed with mClientModules size:"+mClientModules.size());
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent == null) {
            Logger.d(TAG, "onBind() called with intent null [" + intent + "]");
            return mModuleServer;
        }
        if (Constants.BINDER_LOCAL.equalsIgnoreCase(intent.getAction())) {
            Logger.d(TAG, "onBind() called with: local = [" + intent + "]");
            return new LocalBinder();
        }
        Logger.d(TAG, "onBind() called with: remote = [" + intent + "]");
        return mModuleServer;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO remote all listeners form the package!
        Logger.d(TAG, "onUnbind() called with: intent = [" + intent + "]");
        return super.onUnbind(intent);
    }

    public void notifyOnCreate(Bundle bundle) {
        Logger.d(TAG, "notifyOnCreate() called with size: "+mClientModules.size());
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
        Logger.d(TAG, "notifyOnStart() called with size: "+mClientModules.size());
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
        Logger.d(TAG, "notifyOnResume() called with size: "+mClientModules.size());
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
        Logger.d(TAG, "notifyOnPause() called with size: "+mClientModules.size());
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
        Logger.d(TAG, "notifyOnRestart() called with size: "+mClientModules.size());
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
        Logger.d(TAG, "notifyOnStop() called with size: "+mClientModules.size());
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
        Logger.d(TAG, "notifyOnDestroy() called with size: "+mClientModules.size());
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

    private final IViewServer.Stub mViewServer = new ViewCenterService();

    private final IModuleServer.Stub mModuleServer = new IModuleServer.Stub() {
        @Override
        public void registerModule(IModuleClient client) throws RemoteException {
            Logger.d(TAG, "## registerModule client = [" + client.key() + "]");
            if (mClientModules.contains(client.key())) {
                Logger.w(TAG, "## registerModule client already exist update it:"+client.key());
            }
            mClientModules.put(client.key(), client);
        }

        @Override
        public void unregisterModule(IModuleClient client) throws RemoteException {
            Logger.d(TAG, "unregisterModule() called with: client = [" + client.key() + "]");
            mClientModules.remove(client.key());
        }

        @Override
        public IBinder getService(String service) throws RemoteException {
            return getServiceInner(service);
        }
    };

    public class LocalBinder extends Binder {
        public ModuleCenterService getServiceInstance() {
            return ModuleCenterService.this;
        }
        public IBinder getService(String service) {
            return getServiceInner(service);
        }
    };

    private IBinder getServiceInner(String service) {
        Logger.i(TAG, "## getServiceInner: "+service);
        if (Server.VIEW_SERVICE.equalsIgnoreCase(service)) {
            return mViewServer;
        } else if (Server.WINDOW_SERVICE.equalsIgnoreCase(service)) {
            // TODO
        }
        return null;
    }
}
