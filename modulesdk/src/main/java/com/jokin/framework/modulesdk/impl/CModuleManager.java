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

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Created by jokin on 2018/7/16 10:53.
 *
 * Module Register
 */

public final class CModuleManager implements IModuleManager {
    private static final String TAG = CModuleManager.class.getSimpleName();

    private Context mContext;
    private IModuleServer mModuleServer;
    private HashMap<Integer, IClientModule> mClientModules = new HashMap(5);
    private HashMap<ConnectionListener, ConnectionListener> mListeners = new HashMap<>(5);

    public CModuleManager(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        Log.i(TAG, "## Init()");
        Intent intentService = ServerIntent.getServerMainServiceIntent(mContext);
        mContext.bindService(intentService, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
            Log.i(TAG, "## ServiceConnected");

            mModuleServer = IModuleServer.Stub.asInterface(service);
            notifyConnected();

            try {
                mModuleServer.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        Log.e(TAG, "dead!!!");
                    }
                }, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            for (IClientModule module : mClientModules.values()) {
                try {
                    Log.d(TAG, "onServiceConnected: "+ module);
                    RemoteModuleBridge bridge = new RemoteModuleBridge(module);
                    mModuleServer.registerModule(bridge);
                     bridge = new RemoteModuleBridge(module);
                    mModuleServer.registerModule(bridge);
                     bridge = new RemoteModuleBridge(module);
                    mModuleServer.registerModule(bridge);
                     bridge = new RemoteModuleBridge(module);
                    mModuleServer.registerModule(bridge);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");
            Log.i(TAG, "## ServiceDisconnected");

            /** When disconnected, no more unregister through IPC */

            // 1. unbind and mark IPC has broken
            mContext.unbindService(mServiceConnection);
            mModuleServer = null;
            // 2. notify upwards
            notifyDisconnected();
            IClientModule[] modules = mClientModules.values().toArray(new IClientModule[mClientModules.size()]);
            for (IClientModule module : modules) {
                module.onDestroy();
            }
            // 3. clear
            mClientModules.clear();
        }
    };

    /**
     * Try recycle the module manager
     * @return return true when destroyed. others false;
     */
    public boolean tryDestroy() {
        if (mClientModules.size() == 0) {
            Log.i(TAG, "## TryDestroy: destroy() now");
            destroy();
            return true;
        }
        return false;
    }

    /**
     * Destroy from local, not from IPC remote
     **/
    @Override
    public void destroy() {
        Log.i(TAG, "## Destroy()");
        Log.i(TAG, "## modules size: "+mClientModules.size());
        if (mModuleServer != null) {
            // 1. unregister by IPC
            IClientModule[] modules = mClientModules.values().toArray(new IClientModule[mClientModules.size()]);
            for (IClientModule module : modules) {
                unregisterModule(module);
            }
            // 2. real broken the IPC and mark IPC has broken
            mContext.unbindService(mServiceConnection);
            mModuleServer = null;
            // 4. clear
            mClientModules.clear();
            Log.i(TAG, "## ##");
        }
    }

    @Override
    public void addConnectionListener(ConnectionListener listener) {
        mListeners.put(listener, listener);
    }

    @Override
    public void removeConnectionListener(ConnectionListener listener) {
        mListeners.remove(listener);
    }

    private void notifyConnected() {
        for (ConnectionListener listener : mListeners.values()) {
            listener.onConnected();
        }
    }

    private void notifyDisconnected() {
        for (ConnectionListener listener : mListeners.values()) {
            listener.onDisconnected();
        }
    }

    @Override
    public void registerModule(IClientModule module) {
        if (module == null) {
            throw new NullPointerException("module cannot be null");
        }
        if (mModuleServer != null) {
            try {
                Log.i(TAG, "## RegisterModule:"+module);
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
        if (! mClientModules.containsKey(module.hashCode())) {
            Log.w(TAG, "## UnregisterModule not found module:"+module);
            return;
        }
        if (mModuleServer != null) {
            try {
                Log.i(TAG, "## UnregisterModule:"+module);
                mModuleServer.unregisterModule(new RemoteModuleBridge(module));
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        }
        mClientModules.remove(module.hashCode());
    }

    /**
     * @param service
     * @return return null if not found
     */
    @Override
    public IBinder getService(String service) {
        if (service == null) {
            throw new InvalidParameterException("service cannot be null");
        }
        if (mModuleServer != null) {
            try {
                Log.i(TAG, "## GetService:"+service);
                return mModuleServer.getService(service);
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        }
        return null;
    }
}
