package com.jokin.framework.modulesdk.delegate;

import android.view.ViewGroup;

import com.jokin.framework.modulesdk.IWindow;

/**
 * Created by jokin on 2018/7/19 15:01.
 */

public class MinimizeDelegate {
    private static final String TAG = "MinimizeDelegate";

    private IWindow mWindow;
    private IWindow.LayoutParams mParams;

    public MinimizeDelegate(IWindow target) {
        mWindow = target;
    }

    public void minimize() {
        mParams = mWindow.getWindowLayoutParams();
        mParams.width = mWindow.getContentView().getWidth();
        mParams.height = mWindow.getContentView().getHeight();
        mParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mWindow.setWindowLayoutParams(mParams);
    }
}
