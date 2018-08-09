// IRemoteView.aidl
package com.jokin.framework.modulesdk;

import com.jokin.framework.modulesdk.view.CRemoteView;

// Declare any non-default types here with import statements

interface IRemoteView {

    void add(in CRemoteView view);

    void update(in CRemoteView view);

    void remove(in CRemoteView view);
}
