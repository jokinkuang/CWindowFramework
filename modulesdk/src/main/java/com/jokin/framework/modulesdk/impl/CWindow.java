package com.jokin.framework.modulesdk.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.IWindowManager;
import com.jokin.framework.modulesdk.delegate.MoveDelegate;

/**
 * Created by jokin on 2018/7/16 10:51.
 */

public class CWindow extends FrameLayout implements IWindow, View.OnTouchListener {
    private static final String TAG = "CWindow";
    protected IWindow.LayoutParams mWindowLayoutParams;

    public CWindow(@NonNull Context context) {
        super(context);
        init();
    }

    public CWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate() called with: bundle = [" + bundle + "]");
        setVisibility(INVISIBLE);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onRestart() {
        Log.d(TAG, "onRestart() called");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume() called");
        setVisibility(VISIBLE);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop() called");
        setVisibility(INVISIBLE);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        setVisibility(INVISIBLE);
    }

    @Override
    public void onMove(int x, int y) {

    }

    @Override
    public void onMinimize() {

    }

    @Override
    public void onMaximize() {

    }

    @Override
    public void onTransform(int x, int y, int width, int height) {

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
    public boolean onTouch(View v, MotionEvent event) {
        mMovableDelegate.handleEvent(event);
        return true;
    }

    //////////////////

    private IWindowManager mWindowManager;
    private IWindow.LayoutParams mLayoutParams;



}