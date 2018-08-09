package com.jokin.framework.modulesdk.delegate;

import com.jokin.framework.modulesdk.IWindow;

import java.security.InvalidParameterException;

/**
 * Created by jokin on 2018/7/19 15:01.
 */

public class MinimizeDelegate {
    private static final String TAG = "MinimizeDelegate";

    private IWindow mWindow;
    private IWindow.LayoutParams mParams;

    public MinimizeDelegate(IWindow target) {
        if (target == null) {
            throw new InvalidParameterException("target cannot be null");
        }
        mWindow = target;
    }

    public void minimize() {
        mWindow.onMinimizeStart();
        // nop
        mWindow.onMinimizeEnd();
    }
}
