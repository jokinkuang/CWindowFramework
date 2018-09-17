package com.jokin.framework.modulesdk;

import android.os.IBinder;

/**
 * Created by jokin on 2018/7/16 10:52.
 */

public interface IModuleManager {
    void destroy();

    void addConnectionListener(ConnectionListener listener);

    void removeConnectionListener(ConnectionListener listener);

    void registerModule(IClientModule module);

    void unregisterModule(IClientModule module);

    IBinder getService(String service);

    interface ConnectionListener {
        void onConnected();
        void onDisconnected();
    }
}
