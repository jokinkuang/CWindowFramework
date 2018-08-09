package com.jokin.framework.modulesdk.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.jokin.framework.modulesdk.IClientModule;
import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.IWindowManager;

import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;
import java.util.HashMap;

import static com.jokin.framework.modulesdk.IWindow.LayoutParams.INT_INVALID;

/**
 * Created by jokin on 2018/7/16 10:53.
 */

public final class CWindowManager implements IWindowManager {
    private static final String TAG = CWindowManager.class.getSimpleName();

    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mDefaultLayoutParams;

    private IClientModule mModule;
    private HashMap<Integer, IWindow> mWindows = new HashMap<>(5);

    public CWindowManager(IClientModule module) {
        mModule = module;
        mContext = module.getContext();
        init();
    }

    private void init() {
        Log.d(TAG, "init() called");
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        int WINDOW_TYPE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WINDOW_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            WINDOW_TYPE = WindowManager.LayoutParams.TYPE_PHONE;
        }

        mDefaultLayoutParams = new WindowManager.LayoutParams();
        mDefaultLayoutParams.type = WINDOW_TYPE;
        mDefaultLayoutParams.format = PixelFormat.RGBA_8888;
        mDefaultLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
        mDefaultLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mDefaultLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mDefaultLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDefaultLayoutParams.x = 0;
        mDefaultLayoutParams.y = 0;
    }

    @Override
    public void destroy() {
        IWindow[] windows = mWindows.values().toArray(new IWindow[mWindows.size()]);
        for (IWindow window : windows) {
            removeWindow(window);
        }
        mWindows.clear();
    }

    @Override
    public void addWindow(IWindow window) {
        if (window == null) {
            throw new NullPointerException("window cannot be null");
        }
        addWindow(window, window.getWindowLayoutParams());
    }

    @Override
    public void addWindow(IWindow window, IWindow.LayoutParams layoutParams) {
        if (window == null) {
            throw new NullPointerException("window cannot be null");
        }
        View contentView = window.getContentView();
        if (contentView == null) {
            throw new NullPointerException("window::getContentView() cannot be null");
        }
        if (layoutParams == null) {
            layoutParams = new IWindow.LayoutParams.Builder().build();
        }
        if (! checkPermission()) {
            return;
        }
        if (hasAdded(contentView)) {
            removeView(contentView);
        }
        // make sure window is pure before add
        window.detachWindowManager();
        window.setWindowLayoutParams(layoutParams);
        window.getContentView().setLayoutParams(toWindowLayoutParams(layoutParams));

        addView(contentView, layoutParams);
        window.attachWindowManager(this);

        mModule.addLifecycleCallback(window);
        mWindows.put(window.hashCode(), window);
    }

    @Override
    public void removeWindow(IWindow window) {
        View contentView = window.getContentView();
        if (contentView == null) {
            // no need to throw Exception
            return;
        }
        removeView(contentView);
        window.detachWindowManager();

        mModule.removeLifecycleCallback(window);
        mWindows.remove(window.hashCode());
    }

    @Override
    public void updateWindow(IWindow window, IWindow.LayoutParams layoutParams) {
        View contentView = window.getContentView();
        if (contentView == null) {
            throw new NullPointerException("window::getContentView() cannot be null");
        }
        if (! checkPermission()) {
            return;
        }
        if (! hasAdded(contentView)) {
            throw new InvalidParameterException("Window::updateWindow() window has not been added");
        }
        updateView(contentView, layoutParams);

        // Careful dead loop!! window.setWindowLayoutParams() may cause updateWindow() again !!
        if (layoutParams != window.getWindowLayoutParams()) {
            Log.d(TAG, "updateWindow: save layoutParams to window");
            window.setWindowLayoutParams(toLayoutParams((WindowManager.LayoutParams) contentView.getLayoutParams()));
        }
    }

    @Override
    public void updateWindow(IWindow window, ViewGroup.LayoutParams layoutParams) {
        View contentView = window.getContentView();
        if (contentView == null) {
            throw new NullPointerException("window::getContentView() cannot be null");
        }
        if (! checkPermission()) {
            return;
        }
        if (! hasAdded(contentView)) {
            throw new InvalidParameterException("Window::updateWindow() window has not been added");
        }
        contentView.requestLayout();
        mWindowManager.updateViewLayout(contentView, layoutParams);

        // Careful dead loop!! window.setWindowLayoutParams() may cause updateWindow() again !!
        // if (layoutParams != window.getWindowLayoutParams()) {
        //     Log.d(TAG, "updateWindow: save layoutParams to window");
        //     window.setWindowLayoutParams(toLayoutParams((WindowManager.LayoutParams) contentView.getLayoutParams()));
        // }
    }

    ///////// Android's WindowManager ///////////

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(mContext)) {
                //有悬浮窗权限开启服务绑定 绑定权限
                return true;
            } else {
                //没有悬浮窗权限m,去开启悬浮窗权限
                try {
                    Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    mContext.startActivity(intent2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        } else {
            //默认有悬浮窗权限  但是 华为, 小米,oppo等手机会有自己的一套Android6.0以下  会有自己的一套悬浮窗权限管理 也需要做适配
            return true;
        }
    }

    private boolean hasAdded(View window) {
        if (window.getWindowToken() != null || window.getParent() != null) {
            // parent is not null would be more Accurate（准确）！
            return true;
        }
        return false;
    }

    private void addView(View view, IWindow.LayoutParams layoutParams) {
        mWindowManager.addView(view, toWindowLayoutParams(layoutParams));
        Log.d(TAG, "addView: added end with layoutParams:"+view.getLayoutParams());
    }

    private void updateView(View view, IWindow.LayoutParams layoutParams) {
        mWindowManager.updateViewLayout(view, toWindowLayoutParams(layoutParams));
        Log.d(TAG, "updateView: update end with layoutParams:"+view.getLayoutParams());
    }

    private void removeView(View view) {
        mWindowManager.removeViewImmediate(view);
    }

    ////////////////////////////////

    private static class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private WeakReference<View> mTarget;

        public GlobalLayoutListener(View view) {
            mTarget = new WeakReference<View>(view);
        }

        @Override
        public void onGlobalLayout() {
            if (mTarget.get() == null) {
                return;
            }
            mTarget.get().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    ////////////////////////////////

    private WindowManager.LayoutParams toWindowLayoutParams(IWindow.LayoutParams params) {
        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.copyFrom(mDefaultLayoutParams);

        if (params.flags != INT_INVALID) {
            windowParams.flags = params.flags;
        }
        if (params.gravity != INT_INVALID) {
            windowParams.gravity = params.gravity;
        }
        if (params.x != INT_INVALID) {
            windowParams.x = params.x;
        }
        if (params.y != INT_INVALID) {
            windowParams.y = params.y;
        }
        if (params.width != INT_INVALID) {
            windowParams.width = params.width;
        }
        if (params.height != INT_INVALID) {
            windowParams.height = params.height;
        }
        return windowParams;
    }

    private IWindow.LayoutParams toLayoutParams(WindowManager.LayoutParams params) {
        return new IWindow.LayoutParams.Builder()
                .gravity(params.gravity)
                .flags(params.flags)
                .width(params.width)
                .height(params.height)
                .x(params.x)
                .y(params.y)
                .build();
    }
}
