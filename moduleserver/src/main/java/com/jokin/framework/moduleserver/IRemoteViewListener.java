package com.jokin.framework.moduleserver;

import com.jokin.framework.modulesdk.view.CRemoteView;

/**
 * Created by jokin on 2018/8/7 19:53.
 */

public interface IRemoteViewListener {

    void onAdd(CRemoteView view);

    void onUpdate(CRemoteView view);

    void onRemove(CRemoteView view);
}
