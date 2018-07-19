package com.jokin.framework.modulesdk.wrap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import com.jokin.framework.modulesdk.IClientModule;
import com.jokin.framework.modulesdk.IModuleClient;

/**
 * Created by jokin on 2018/7/17 09:40.
 */

public class RemoteModuleBridge extends IModuleClient.Stub {

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
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onCreate(bundle);
            }
        });
    }

    @Override
    public void onStart() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onStart();
            }
        });
    }

    @Override
    public void onRestart() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onRestart();
            }
        });
    }

    @Override
    public void onResume() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onResume();
            }
        });
    }

    @Override
    public void onPause() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onPause();
            }
        });
    }

    @Override
    public void onStop() throws RemoteException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mModule.onStop();
            }
        });
    }

    @Override
    public void onDestroy() throws RemoteException {
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
