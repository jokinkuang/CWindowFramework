package com.jokin.framework.modulesdk.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jokin.framework.modulesdk.IClientModule;
import com.jokin.framework.modulesdk.IModuleManager;
import com.jokin.framework.modulesdk.IViewServer;
import com.jokin.framework.modulesdk.constant.Server;
import com.jokin.framework.modulesdk.impl.CModuleManager;

import java.util.HashMap;

/**
 * Created by jokin on 2018/8/3 11:52.
 */

public class RemoteWindowManager implements IRemoteViewManager {
    private static final String TAG = RemoteWindowManager.class.getSimpleName();


    private IClientModule mModule;
    private CModuleManager mModuleManager;

    private Context mContext;
    private IViewServer mViewServer;
    private HashMap<String, CRemoteView> mClientViews = new HashMap(5);

    public RemoteWindowManager(IClientModule module, CModuleManager moduleManager) {
        mModule = module;
        mContext = module.getContext();
        mModuleManager = moduleManager;
        mModuleManager.addConnectionListener(mConnectionListener);
    }

    private void init() {
        Log.i(TAG, "## Init()");
        mViewServer = IViewServer.Stub.asInterface(mModuleManager.getService(Server.VIEW_SERVICE));
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
            mViewServer = IViewServer.Stub.asInterface(service);

            for (CRemoteView view : mClientViews.values()) {
                try {
                    Log.d(TAG, "onServiceConnected: "+ view);
                    mViewServer.add(view);
                    mViewServer.add(view);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");

            /** When disconnected, no more unregister through IPC */

            // 1. unbind and mark IPC has broken
            try {
                mContext.unbindService(mServiceConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mViewServer = null;
            // 2. notify upwards
            // 3. clear
            mClientViews.clear();
        }
    };

    private IModuleManager.ConnectionListener mConnectionListener = new IModuleManager.ConnectionListener() {
        @Override
        public void onConnected() {
            Log.i(TAG, "## onConnected");
            mViewServer = IViewServer.Stub.asInterface(mModuleManager.getService(Server.VIEW_SERVICE));
            for (CRemoteView view : mClientViews.values()) {
                try {
                    Log.d(TAG, "onServiceConnected: "+ view);
                    mViewServer.add(view);
                    mViewServer.add(view);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onDisconnected() {
            Log.i(TAG, "onDisconnected");
            mViewServer = null;
            mClientViews.clear();
        }
    };

    @Override
    public void destroy() {
        Log.i(TAG, "## Destroy()");
        Log.i(TAG, "## view size: "+mClientViews.size());
        if (mViewServer != null) {
            // 1. unregister by IPC
            mModuleManager.removeConnectionListener(mConnectionListener);
            // 2. loop with a copy to delete
            CRemoteView[] views = mClientViews.values().toArray(new CRemoteView[mClientViews.size()]);
            for (CRemoteView view : views) {
                removeView(view);
            }
            mViewServer = null;
            // 3. clear
            mClientViews.clear();
            Log.i(TAG, "## ##");
        }
    }

    @Override
    public void addView(CRemoteView view) {
        if (view == null) {
            throw new NullPointerException("view cannot be null");
        }
        if (mViewServer != null) {
            try {
                mViewServer.add(view);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mClientViews.put(view.key(), view);
    }

    @Override
    public void updateView(CRemoteView view) {
        if (! mClientViews.containsKey(view.key())) {
            throw new IllegalStateException("view has not been added");
        }
        if (mViewServer != null) {
            try {
                mViewServer.update(view);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mClientViews.put(view.key(), view);
    }

    @Override
    public void removeView(CRemoteView view) {
        if (! mClientViews.containsKey(view.key())) {
            return;
        }
        if (mViewServer != null) {
            try {
                mViewServer.remove(view);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mClientViews.remove(view.key());
    }
}
