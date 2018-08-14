package com.jokin.framework.modulesdk.delegate;

import android.view.MotionEvent;
import android.view.View;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.iwindow.IBaseWindow;

import java.security.InvalidParameterException;

/**
 * Created by jokin on 2018/8/13 15:14.
 *
 * A window wrapper (AOP切面)
 */

public class ResizeDelegate implements IBaseWindow {
    private static final String TAG = ResizeDelegate.class.getSimpleName();

    private ScaleDelegate mScaleDelegate;
    private MinimizeDelegate mMinimizeDelegate;
    private MaximizeDelegate mMaximizeDelegate;
    private MoveDelegate mMovableDelegate;

    private IBaseWindow mWindow;
    private IWindow.LayoutParams mLastParams;

    private boolean mTouchable = true;

    public ResizeDelegate(IBaseWindow target) {
        if (target == null) {
            throw new InvalidParameterException("target cannot be null");
        }
        // target's layout params maybe null if init very earlier.
        mWindow = target;
        mLastParams = target.getWindowLayoutParams();

        mScaleDelegate = new ScaleDelegate(this);
        mMinimizeDelegate = new MinimizeDelegate(this);
        mMaximizeDelegate = new MaximizeDelegate(this);
        mMovableDelegate = new MoveDelegate(this);
    }

    public void minimize() {
        mMinimizeDelegate.minimize();
    }

    public void maximize() {
        mMaximizeDelegate.maximize();
    }

    public void move(MotionEvent event) {
        if (mTouchable) {
            mMovableDelegate.move(event);
        }
    }

    public void scale(MotionEvent event) {
        if (mTouchable) {
            mScaleDelegate.scale(event);
        }
    }

    @Override
    public void onMinimizeStart() {
        // minimize without maximize, lastParams may be null.
        if (mLastParams != null) {
            mWindow.setWindowLayoutParams(mWindow.getWindowLayoutParams().from(mLastParams));
            mWindow.onMinimizeStart();
        }
    }

    @Override
    public void onMinimizeEnd() {
        mTouchable = true;
        mWindow.onMinimizeEnd();
    }

    @Override
    public void onMaximizeStart() {
        mTouchable = false;
        // do a copy to save
        mLastParams = mWindow.getWindowLayoutParams().copy();
        mWindow.onMaximizeStart();
    }

    @Override
    public void onMaximizeEnd() {
        mWindow.onMaximizeEnd();
    }

    @Override
    public void onScaleStart() {
        mWindow.onScaleStart();
    }

    @Override
    public void onScaling() {
        mWindow.onScaling();
    }

    @Override
    public void onScaleEnd() {
        mWindow.onScaleEnd();
    }

    @Override
    public void onMoveStart() {
        mWindow.onMoveStart();
    }

    @Override
    public void onMoving() {
        mWindow.onMoving();
    }

    @Override
    public void onMoveEnd() {
        mWindow.onMoveEnd();
    }

    @Override
    public View getContentView() {
        return mWindow.getContentView();
    }

    @Override
    public void setWindowLayoutParams(IWindow.LayoutParams layoutParams) {
        mWindow.setWindowLayoutParams(layoutParams);
    }

    @Override
    public IWindow.LayoutParams getWindowLayoutParams() {
        return mWindow.getWindowLayoutParams();
    }

}
