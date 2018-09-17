package com.jokin.framework.modulesdk;

import android.content.Context;

import com.jokin.framework.modulesdk.iwindow.ILifecycable;
import com.jokin.framework.modulesdk.view.IRemoteViewManager;

/**
 * Created by jokin on 2018/7/16 10:57.
 */

public interface IClientModule extends ILifecycable {
    Context getContext();
    String key();

    void destroy();

    void addLifecycleCallback(ILifecycable lifecycable);
    void removeLifecycleCallback(ILifecycable lifecycable);

    IWindowManager getWindowManagerService();
    IRemoteViewManager getRemoteViewManagerService();
}
