package com.jokin.framework.modulesdk.iwindow;

/**
 * Created by jokin on 2018/7/16 16:08.
 */

public interface IResizable {
    void onMinimize();

    void onMaximize();

    void onTransform(int x, int y, int width, int height);
}
