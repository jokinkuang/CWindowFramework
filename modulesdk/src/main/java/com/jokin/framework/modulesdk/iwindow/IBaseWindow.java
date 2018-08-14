package com.jokin.framework.modulesdk.iwindow;

import android.view.View;

import com.jokin.framework.modulesdk.IWindow;

/**
 * Created by jokin on 2018/8/13 16:53.
 */

public interface IBaseWindow extends IResizable {
    /**
     * @return the view of the window
     */
    View getContentView();

    void setWindowLayoutParams(IWindow.LayoutParams layoutParams);

    IWindow.LayoutParams getWindowLayoutParams();
}
