// IRemoteWindowManager.aidl
package com.jokin.framework.modulesdk;

import com.jokin.framework.modulesdk.IModuleClient;

/**
 * Remote Server Module
 **/
interface IModuleServer {

    void registerModule(IModuleClient client);

    void unregisterModule(IModuleClient client);

    /** Nullable */
    IBinder getService(String service);
}
