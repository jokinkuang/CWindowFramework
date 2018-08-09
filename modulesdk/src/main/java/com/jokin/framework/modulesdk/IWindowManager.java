package com.jokin.framework.modulesdk;

import android.view.ViewGroup;

/**
 * Created by jokin on 2018/7/16 10:52.
 */

public interface IWindowManager {
    void destroy();

    void addWindow(IWindow window);

    void addWindow(IWindow window, IWindow.LayoutParams layoutParams);

    void removeWindow(IWindow window);

    void updateWindow(IWindow window, IWindow.LayoutParams layoutParams);

    void updateWindow(IWindow window, ViewGroup.LayoutParams layoutParams);

    /////////////////////////////////////////////
    // Gravity.Center = #11
    // Gravity.Center_Horizontal = #1
    // Gravity.Center_Vertical = #10
    // Gravity.Left = #3
    // Gravity.Top = #30
    // Gravity.Right = #5
    // Gravity.Bottom = #50
    // Gravity.Left|Top = #33
    // Gravity.Right|Bottom = #55
    /////////////////////////////////////////////
}
