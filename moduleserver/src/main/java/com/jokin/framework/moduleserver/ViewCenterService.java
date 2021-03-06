package com.jokin.framework.moduleserver;

import android.os.Handler;
import android.os.RemoteException;

import com.jokin.framework.modulesdk.IViewServer;
import com.jokin.framework.modulesdk.log.Logger;
import com.jokin.framework.modulesdk.view.CRemoteView;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ViewCenterService extends IViewServer.Stub {
    private static final String TAG = ViewCenterService.class.getSimpleName();

    private ConcurrentHashMap<String, CRemoteView> mClientViews = new ConcurrentHashMap<>(15);
    private ConcurrentHashMap<IRemoteViewListener, IRemoteViewListener> mRemoteViewListeners = new ConcurrentHashMap<>(30);
    private Handler mHandler = new Handler();

    public ViewCenterService() {
    }

    ///////// Remote //////////

    @Override
    public void add(CRemoteView view) throws RemoteException {
        Logger.d(TAG, "add() called with: view = [" + view + "]");
        mClientViews.put(view.key(), view);
        notifyOnAdd(view);
    }

    @Override
    public void update(CRemoteView view) throws RemoteException {
        Logger.d(TAG, "update() called with: view = [" + view + "]");
        mClientViews.put(view.key(), view);
        notifyOnUpdate(view);
    }

    @Override
    public void remove(CRemoteView view) throws RemoteException {
        Logger.d(TAG, "remove() called with: view = [" + view + "]");
        mClientViews.remove(view.key());
        notifyOnRemove(view);
    }

    ///////// Local //////////

    public void registerRemoteViewListener(IRemoteViewListener listener) {
        mRemoteViewListeners.put(listener, listener);
    }

    public void unregisterRemoteViewListener(IRemoteViewListener listener) {
        mRemoteViewListeners.remove(listener);
    }

    private void notifyOnAdd(final CRemoteView view) {
        Logger.d(TAG, "notifyOnAdd() called with: view = [" + view + "]");
        Logger.d(TAG, "notifyOnAdd() view size:"+mClientViews.size()+", listener size:"+mRemoteViewListeners.size());
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
        Logger.d(TAG, "notifyOnUpdate() called with: view = [" + view + "]");
        Logger.d(TAG, "notifyOnUpdate() view size:"+mClientViews.size());
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
        Logger.d(TAG, "notifyOnRemove() called with: view = [" + view + "]");
        Logger.d(TAG, "notifyOnRemove() view size:"+mClientViews.size());
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
