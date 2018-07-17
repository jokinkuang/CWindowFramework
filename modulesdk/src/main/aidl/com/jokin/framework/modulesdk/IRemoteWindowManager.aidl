// IRemoteWindowManager.aidl
package com.jokin.framework.modulesdk;

import com.jokin.framework.modulesdk.IRemoteWindow;

interface IRemoteWindowManager {
    void addWindow(IRemoteWindow remoteWindow);
    void removeWindow(IRemoteWindow remoteWindow);
}
