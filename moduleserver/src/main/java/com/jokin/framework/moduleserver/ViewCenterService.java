package com.jokin.framework.moduleserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jokin.framework.modulesdk.IRemoteView;
import com.jokin.framework.modulesdk.constant.Constants;
import com.jokin.framework.modulesdk.view.CRemoteView;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ViewCenterService extends Service {
    private static final String TAG = ViewCenterService.class.getSimpleName();

    private ConcurrentHashMap<String, CRemoteView> mClientViews = new ConcurrentHashMap<>(15);
    private ConcurrentHashMap<IRemoteViewListener, IRemoteViewListener> mRemoteViewListeners = new ConcurrentHashMap<>(30);
    private Handler mHandler = new Handler();

    public ViewCenterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed with mClientViews size:"+ mClientViews.size());
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent == null) {
            Log.d(TAG, "onBind() called with intent null [" + intent + "]");
            return mViewServer;
        }
        if (Constants.BINDER_LOCAL.equalsIgnoreCase(intent.getAction())) {
            Log.d(TAG, "onBind() called with: local = [" + intent + "]");
            return new LocalBinder();
        }
        Log.d(TAG, "onBind() called with: remote = [" + intent + "]");
        return mViewServer;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO remote all listeners form the package!
        Log.d(TAG, "onUnbind() called with: intent = [" + intent + "]");
        return super.onUnbind(intent);
    }

    ///////////////////

    private final IRemoteView.Stub mViewServer = new IRemoteView.Stub() {
        @Override
        public void add(CRemoteView view) throws RemoteException {
            Log.d(TAG, "add() called with: view = [" + view + "]");
            mClientViews.put(view.key(), view);
            notifyOnAdd(view);
        }

        @Override
        public void update(CRemoteView view) throws RemoteException {
            Log.d(TAG, "update() called with: view = [" + view + "]");
            mClientViews.put(view.key(), view);
            notifyOnUpdate(view);
        }

        @Override
        public void remove(CRemoteView view) throws RemoteException {
            Log.d(TAG, "remove() called with: view = [" + view + "]");
            mClientViews.remove(view.key());
            notifyOnRemove(view);
        }
    };

    public class LocalBinder extends Binder {
        public ViewCenterService getServiceInstance() {
            return ViewCenterService.this;
        }
    };


    ///////////////////

    public void registerRemoteViewListener(IRemoteViewListener listener) {
        mRemoteViewListeners.put(listener, listener);
    }

    public void unregisterRemoteViewListener(IRemoteViewListener listener) {
        mRemoteViewListeners.remove(listener);
    }

    private void notifyOnAdd(final CRemoteView view) {
        Log.d(TAG, "notifyOnAdd() called with: view = [" + view + "]");
        Log.d(TAG, "notifyOnAdd() view size:"+mClientViews.size()+", listener size:"+mRemoteViewListeners.size());
        for (Iterator<IRemoteViewListener> iterator = mRemoteViewListeners.values().iterator(); iterator.hasNext();) {
            final IRemoteViewListener listener = iterator.next();
            try {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onAdd(view);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyOnUpdate(final CRemoteView view) {
        Log.d(TAG, "notifyOnUpdate() called with: view = [" + view + "]");
        Log.d(TAG, "notifyOnUpdate() view size:"+mClientViews.size());
        for (Iterator<IRemoteViewListener> iterator = mRemoteViewListeners.values().iterator(); iterator.hasNext();) {
            final IRemoteViewListener listener = iterator.next();
            try {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onUpdate(view);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyOnRemove(final CRemoteView view) {
        Log.d(TAG, "notifyOnRemove() called with: view = [" + view + "]");
        Log.d(TAG, "notifyOnRemove() view size:"+mClientViews.size());
        for (Iterator<IRemoteViewListener> iterator = mRemoteViewListeners.values().iterator(); iterator.hasNext();) {
            final IRemoteViewListener listener = iterator.next();
            try {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRemove(view);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
