package com.jokin.framework.modulesdk.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.jokin.framework.modulesdk.IRemoteWindowManager;
import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.IWindowManager;
import com.jokin.framework.modulesdk.intent.ServerIntent;
import com.jokin.framework.modulesdk.wrap.RemoteWindowBridge;

import java.security.InvalidParameterException;

import static com.jokin.framework.modulesdk.IWindow.LayoutParams.INT_INVALID;

/**
 * Created by jokin on 2018/7/16 10:53.
 */

public final class CWindowManager implements IWindowManager {
    private static final String TAG = CWindowManager.class.getSimpleName();

    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mDefaultLayoutParams;

    private IRemoteWindowManager mRemoteWindowManager;
    private SparseArray<IWindow> mWindows = new SparseArray<>(5);

    public CWindowManager(Context context) {
        mContext = context;
        init();
        initRemote();
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
        mDefaultLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mDefaultLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mDefaultLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mDefaultLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDefaultLayoutParams.x = 0;
        mDefaultLayoutParams.y = 0;
    }

    private void initRemote() {
        Log.d(TAG, "initRemote() called");
        Intent intentService = ServerIntent.getServerMainServiceIntent(mContext);
        mContext.bindService(intentService, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
                mRemoteWindowManager = IRemoteWindowManager.Stub.asInterface(service);

                for (int i = 0; i < mWindows.size(); ++i) {
                    try {
                        IWindow window = mWindows.get(mWindows.keyAt(i));
                        Log.d(TAG, "onServiceConnected: "+ window);
                        mRemoteWindowManager.addWindow(new RemoteWindowBridge(window));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mRemoteWindowManager = null;
            }
        }, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void addWindow(IWindow window) {
        addWindow(window, null);
    }

    @Override
    public void addWindow(IWindow window, IWindow.LayoutParams layoutParams) {
        View contentView = window.getContentView();
        if (contentView == null) {
            throw new InvalidParameterException("Window::getContentView() should not be null.");
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
        mWindowManager.addView(contentView, toWindowLayoutParams(layoutParams));
        window.setWindowLayoutParams(layoutParams);
        window.attachWindowManager(this);

        // TODO notify server
        if (mRemoteWindowManager != null) {
            try {
                mRemoteWindowManager.addWindow(new RemoteWindowBridge(window));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            mWindows.put(window.hashCode(), window);
        }
    }

    @Override
    public void updateWindow(IWindow window, IWindow.LayoutParams layoutParams) {
        View contentView = window.getContentView();
        if (contentView == null) {
            throw new InvalidParameterException("Window::getContentView() should not be null.");
        }
        if (! checkPermission()) {
            return;
        }
        if (! hasAdded(contentView)) {
            throw new InvalidParameterException("Window::updateWindow() window has not been added.");
        }
        mWindowManager.updateViewLayout(contentView, toWindowLayoutParams(layoutParams));
        // careful dead loop
        if (layoutParams != window.getWindowLayoutParams()) {
            window.setWindowLayoutParams(layoutParams);
        }
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

        // TODO notify server
        if (mRemoteWindowManager != null) {
            try {
                mRemoteWindowManager.removeWindow(new RemoteWindowBridge(window));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    ////////////////////

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

    private void removeView(View window) {
        try {
            mWindowManager.removeViewImmediate(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///////////////////

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
}
