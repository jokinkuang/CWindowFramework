package com.jokin.framework.modulesdk.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jokin.framework.modulesdk.IRemoteView;
import com.jokin.framework.modulesdk.intent.ServerIntent;

import java.util.HashMap;

/**
 * Created by jokin on 2018/8/3 11:52.
 */

public class RemoteWindowManager implements IRemoteViewManager {
    private static final String TAG = RemoteWindowManager.class.getSimpleName();

    private Context mContext;
    private IRemoteView mViewServer;
    private HashMap<String, CRemoteView> mClientViews = new HashMap(5);

    public RemoteWindowManager(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        Log.i(TAG, "## Init()");
        try {
            Intent intentService = ServerIntent.getServerViewServiceIntent(mContext);
            mContext.bindService(intentService, mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
            mViewServer = IRemoteView.Stub.asInterface(service);

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

    @Override
    public void destroy() {
        Log.i(TAG, "## Destroy()");
        Log.i(TAG, "## view size: "+mClientViews.size());
        if (mViewServer != null) {
            // 1. unregister by IPC
            // loop with a copy to delete
            CRemoteView[] views = mClientViews.values().toArray(new CRemoteView[mClientViews.size()]);
            for (CRemoteView view : views) {
                removeView(view);
            }
            // 2. real broken the IPC and mark IPC has broken
            try {
                mContext.unbindService(mServiceConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mViewServer = null;
            // 4. clear
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
