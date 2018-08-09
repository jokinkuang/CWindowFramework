package com.jokin.framework.modulesdk.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.IWindowManager;
import com.jokin.framework.modulesdk.R;
import com.jokin.framework.modulesdk.delegate.MaximizeDelegate;
import com.jokin.framework.modulesdk.delegate.MinimizeDelegate;
import com.jokin.framework.modulesdk.delegate.MoveDelegate;
import com.jokin.framework.modulesdk.delegate.ScaleDelegate;

/**
 * Created by jokin on 2018/8/3 10:57.
 */

public class CRemoteWindow extends FrameLayout implements IWindow, View.OnClickListener {
    private static final String TAG = "CRemoteView";

    ScaleDelegate mScaleDelegate;
    MinimizeDelegate mMinimizeDelegate;
    MaximizeDelegate mMaximizeDelegate;

    private MoveDelegate mMoveDelegate = new MoveDelegate(this);

    public CRemoteWindow(@NonNull Context context) {
        super(context);
        init();
    }

    public CRemoteWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CRemoteWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CRemoteWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mScaleDelegate = new ScaleDelegate(this);
        mMinimizeDelegate = new MinimizeDelegate(this);
        mMaximizeDelegate = new MaximizeDelegate(this);

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_min).setOnClickListener(this);
        findViewById(R.id.btn_max).setOnClickListener(this);
        findViewById(R.id.btn_scale).setOnClickListener(this);
        findViewById(R.id.edit_text).setOnClickListener(this);

        findViewById(R.id.btn_scale).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch() called with: v = [" + v + "], event = [" + event + "]");
                if (v.getId() == R.id.btn_scale) {
                    mScaleDelegate.handleEvent(event);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent() called with: event = [" + event + "]");
        mMoveDelegate.handleEvent(event);
        return true;
    }

    @Override
    public void onMove(int x, int y) {

    }


    @Override
    public void onMinimizeStart() {

    }

    @Override
    public void onMinimizeEnd() {

    }

    @Override
    public void onMaximizeStart() {

    }

    @Override
    public void onMaximizeEnd() {

    }

    @Override
    public void onTransform(int x, int y, int width, int height) {

    }

    @Override
    public void onCreate(Bundle bundle) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public View getContentView() {
        return this;
    }

    @Override
    public void setWindowLayoutParams(IWindow.LayoutParams layoutParams) {
        // nop
    }

    @Override
    public IWindow.LayoutParams getWindowLayoutParams() {
        return null;
    }

    @Override
    public void attachWindowManager(IWindowManager windowManager) {

    }

    @Override
    public void detachWindowManager() {

    }

    @Override
    public void notifyActivated() {

    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_close) {
            onClickBtnClose();

        } else if (i == R.id.btn_min) {
            onClickBtnMin();

        } else if (i == R.id.btn_max) {
            onClickBtnMax();

        } else if (i == R.id.btn_scale) {
            onClickBtnDrag();

        } else if (i == R.id.edit_text) {
            onClickEditText();

        }
    }


    private void onClickBtnClose() {
        Log.d(TAG, "onClickBtnClose() called");
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
    }

    private void onClickBtnMin() {
        mMinimizeDelegate.minimize();
    }

    private void onClickBtnMax() {
        mMaximizeDelegate.maximize();
    }

    private void onClickBtnDrag() {
    }

    private void onClickEditText() {
        InputMethodManager inputManager = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}
