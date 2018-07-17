package com.jokin.framework.modulesdk.wrap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import com.jokin.framework.modulesdk.IRemoteWindow;
import com.jokin.framework.modulesdk.IWindow;

/**
 * Created by jokin on 2018/7/17 09:40.
 */

public class RemoteWindowBridge extends IRemoteWindow.Stub {

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IWindow mWindow;

    public RemoteWindowBridge(IWindow window) {
        if (window == null) {
            throw new NullPointerException("window cannot be null");
        }
        mWindow = window;
    }

    @Override
    public void onCreate(final Bundle bundle) throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWindow.onCreate(bundle);
            }
        });
    }

    @Override
    public void onStart() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWindow.onStart();
            }
        });
    }

    @Override
    public void onRestart() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWindow.onRestart();
            }
        });
    }

    @Override
    public void onResume() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWindow.onResume();
            }
        });
    }

    @Override
    public void onPause() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWindow.onPause();
            }
        });
    }

    @Override
    public void onStop() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWindow.onStop();
            }
        });
    }

    @Override
    public void onDestroy() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWindow.onDestroy();
            }
        });
    }

    @Override
    public void notifyActivated() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWindow.notifyActivated();
            }
        });
    }
}
