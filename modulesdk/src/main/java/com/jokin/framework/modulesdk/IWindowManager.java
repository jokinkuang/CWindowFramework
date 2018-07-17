package com.jokin.framework.modulesdk;

/**
 * Created by jokin on 2018/7/16 10:52.
 */

public interface IWindowManager {
    void addWindow(IWindow window);

    void addWindow(IWindow window, IWindow.LayoutParams layoutParams);

    void updateWindow(IWindow window, IWindow.LayoutParams layoutParams);

    void removeWindow(IWindow window);
}
