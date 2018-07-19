package com.jokin.framework.modulesdk.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jokin.framework.modulesdk.IClientModule;
import com.jokin.framework.modulesdk.IModuleManager;
import com.jokin.framework.modulesdk.IModuleServer;
import com.jokin.framework.modulesdk.intent.ServerIntent;
import com.jokin.framework.modulesdk.wrap.RemoteModuleBridge;

import java.util.HashMap;

/**
 * Created by jokin on 2018/7/16 10:53.
 */

public final class CModuleManager implements IModuleManager {
    private static final String TAG = CModuleManager.class.getSimpleName();

    private Context mContext;
    private IModuleServer mModuleServer;
    private HashMap<Integer, IClientModule> mClientModules = new HashMap(5);

    public CModuleManager(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        Log.d(TAG, "init() called");
        Intent intentService = ServerIntent.getServerMainServiceIntent(mContext);
        mContext.bindService(intentService, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
            mModuleServer = IModuleServer.Stub.asInterface(service);

            for (IClientModule module : mClientModules.values()) {
                try {
                    Log.d(TAG, "onServiceConnected: "+ module);
                    RemoteModuleBridge bridge = new RemoteModuleBridge(module);
                    mModuleServer.registerModule(bridge);
                    mModuleServer.registerModule(bridge);
                    mModuleServer.registerModule(bridge);
                    mModuleServer.registerModule(bridge);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mModuleServer = null;
        }
    };

    /**
     * Try recycle the module manager
     * @return return true when destroyed. others false;
     */
    public boolean tryDestroy() {
        if (mClientModules.size() == 0) {
            destroy();
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
        Log.d(TAG, "destroy() called");
        // Careful re entrant
        if (mModuleServer != null) {
            IClientModule[] modules = mClientModules.values().toArray(new IClientModule[mClientModules.size()]);
            for (IClientModule module : modules) {
                unregisterModule(module);
            }
            mClientModules.clear();

            mContext.unbindService(mServiceConnection);
            mModuleServer = null;
        }
    }

    @Override
    public void registerModule(IClientModule module) {
        if (module == null) {
            throw new NullPointerException("module cannot be null");
        }
        if (mModuleServer != null) {
            try {
                mModuleServer.registerModule(new RemoteModuleBridge(module));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mClientModules.put(module.hashCode(), module);
    }

    @Override
    public void unregisterModule(IClientModule module) {
        if (module == null) {
            return;
        }
        if (mModuleServer != null) {
            try {
                mModuleServer.unregisterModule(new RemoteModuleBridge(module));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mClientModules.remove(module.hashCode());
    }
}
