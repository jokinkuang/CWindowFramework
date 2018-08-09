package com.jokin.framework.modulesdk.view;

/**
 * Created by jokin on 2018/8/3 11:51.
 */

public interface IRemoteViewManager {

    void destroy();

    void addView(CRemoteView view);

    void updateView(CRemoteView view);

    void removeView(CRemoteView view);
}
