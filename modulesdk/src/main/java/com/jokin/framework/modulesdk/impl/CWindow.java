package com.jokin.framework.modulesdk.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.IWindowManager;
import com.jokin.framework.modulesdk.delegate.MoveDelegate;
import com.jokin.framework.modulesdk.delegate.ResizeDelegate;
import com.jokin.framework.modulesdk.log.Logger;

/**
 * Created by jokin on 2018/7/16 10:51.
 */

public class CWindow extends FrameLayout implements IWindow {
    private static final String TAG = CWindow.class.getSimpleName();

    protected IWindow.LayoutParams mWindowLayoutParams;
    protected ResizeDelegate mResizeDelegate;

    public CWindow(@NonNull Context context) {
        super(context);
        init();
    }

    public CWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mWindowLayoutParams = new IWindow.LayoutParams.Builder().build();
        mResizeDelegate = new ResizeDelegate(this);
    }

    public ResizeDelegate getResizeDelegate() {
        return mResizeDelegate;
    }

    @Override
    public void onCreate(Bundle bundle) {
        Logger.d(TAG, "onCreate() called with: bundle = [" + bundle + "]");
        setVisibility(INVISIBLE);
    }

    @Override
    public void onStart() {
        Logger.d(TAG, "onStart() called");
    }

    @Override
    public void onRestart() {
        Logger.d(TAG, "onRestart() called");
    }

    @Override
    public void onResume() {
        Logger.d(TAG, "onResume() called");
        setVisibility(VISIBLE);
    }

    @Override
    public void onPause() {
        Logger.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        Logger.d(TAG, "onStop() called");
        setVisibility(INVISIBLE);
    }

    @Override
    public void onDestroy() {
        Logger.d(TAG, "onDestroy() called");
        setVisibility(INVISIBLE);
    }

    @Override
    public void onMoveStart() {
        // nop
    }

    @Override
    public void onMoving() {
        // nop
    }

    @Override
    public void onMoveEnd() {
        // nop
    }

    @Override
    public void onMinimizeStart() {
        // nop
    }

    @Override
    public void onMinimizeEnd() {
        // nop
    }

    @Override
    public void onMaximizeStart() {
       // nop
    }

    @Override
    public void onMaximizeEnd() {
        // nop
    }

    @Override
    public void onScaleStart() {
        // nop
    }

    @Override
    public void onScaling() {
        // nop
    }

    @Override
    public void onScaleEnd() {
        // nop
    }

    @Override
    public View getContentView() {
        return this;
    }

    @Override
    public void setWindowLayoutParams(IWindow.LayoutParams layoutParams) {
        if (layoutParams == null) {
            throw new NullPointerException("Layout parameters cannot be null");
        }
        mWindowLayoutParams = layoutParams;
        if (mWindowManager != null) {
            mWindowManager.updateWindow(this, layoutParams);
        }
    }

    private boolean equals(WindowManager.LayoutParams params1, WindowManager.LayoutParams params2) {
        if (params1 != null && params2 == null) {
            Logger.d(TAG, "equals: f");
            return false;
        }
        if (params1 == null && params2 != null) {
            Logger.d(TAG, "equals: f2");
            return false;
        }
        if (params1.x != params2.x) {
            return false;
        }
        if (params1.y != params2.y) {
            return false;
        }
        if (params1.flags != params2.flags) {
            return false;
        }
        if (params1.gravity != params2.gravity) {
            return false;
        }
        if (params1.width != params2.width) {
            return false;
        }
        if (params1.height != params2.height) {
            return false;
        }
        return true;
    }

    @Override
    public IWindow.LayoutParams getWindowLayoutParams() {
        return mWindowLayoutParams;
    }

    @Override
    public void attachWindowManager(IWindowManager windowManager) {
        mWindowManager = windowManager;
    }

    @Override
    public void detachWindowManager() {
        mWindowManager = null;
    }

    @Override
    public void notifyActivated() {
        // TODO notify server
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    ///////////////////

    private MoveDelegate mMovableDelegate = new MoveDelegate(this);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mResizeDelegate.move(event);
        return true;
    }

    //////////////////

    private IWindowManager mWindowManager;
}
