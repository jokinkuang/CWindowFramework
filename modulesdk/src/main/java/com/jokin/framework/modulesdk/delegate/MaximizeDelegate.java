package com.jokin.framework.modulesdk.delegate;

import android.view.Gravity;
import android.view.ViewGroup;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.iwindow.IBaseWindow;

import java.security.InvalidParameterException;

/**
 * Created by jokin on 2018/7/19 15:01.
 */

public class MaximizeDelegate {
    private static final String TAG = MaximizeDelegate.class.getSimpleName();

    private IBaseWindow mWindow;
    private IWindow.LayoutParams mParams;

    public MaximizeDelegate(IBaseWindow target) {
        if (target == null) {
            throw new InvalidParameterException("target cannot be null");
        }
        mWindow = target;
    }

    public void maximize() {
        mWindow.onMaximizeStart();

        mParams = mWindow.getWindowLayoutParams();
        mParams.x = 0;
        mParams.y = 0;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mWindow.setWindowLayoutParams(mParams);

        mWindow.onMaximizeEnd();
    }
}
