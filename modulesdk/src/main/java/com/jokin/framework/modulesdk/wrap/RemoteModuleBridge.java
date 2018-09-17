package com.jokin.framework.modulesdk.wrap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import com.jokin.framework.modulesdk.IClientModule;
import com.jokin.framework.modulesdk.IModuleClient;
import com.jokin.framework.modulesdk.log.Logger;

/**
 * Created by jokin on 2018/7/17 09:40.
 */

public class RemoteModuleBridge extends IModuleClient.Stub {
    private static final String TAG = "RemoteServer";

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IClientModule mModule;

    public RemoteModuleBridge(IClientModule module) {
        if (module == null) {
            throw new NullPointerException("module cannot be null");
        }
        mModule = module;
    }

    @Override
    public String key() throws RemoteException {
        return mModule.key();
    }

    @Override
    public void onCreate(final Bundle bundle) throws RemoteException {
        Logger.i(TAG, "## Receive onCreate");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onCreate(bundle);
            }
        });
    }

    @Override
    public void onStart() throws RemoteException {
        Logger.i(TAG, "## Receive onStart");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onStart();
            }
        });
    }

    @Override
    public void onRestart() throws RemoteException {
        Logger.i(TAG, "## Receive onRestart");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onRestart();
            }
        });
    }

    @Override
    public void onResume() throws RemoteException {
        Logger.i(TAG, "## Receive onResume");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onResume();
            }
        });
    }

    @Override
    public void onPause() throws RemoteException {
        Logger.i(TAG, "## Receive onPause");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onPause();
            }
        });
    }

    @Override
    public void onStop() throws RemoteException {
        Logger.i(TAG, "## Receive onStop");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onStop();
            }
        });
    }

    @Override
    public void onDestroy() throws RemoteException {
        Logger.i(TAG, "## Receive onDestroy");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onDestroy();
            }
        });
    }

    /////////////// Bridge is just a shadow which is equals the target ///////////////

    @Override
    public boolean equals(Object obj) {
        RemoteModuleBridge target = (RemoteModuleBridge) obj;
        return mModule.equals(target.mModule);
    }

    @Override
    public int hashCode() {
        return mModule.hashCode();
    }
}
