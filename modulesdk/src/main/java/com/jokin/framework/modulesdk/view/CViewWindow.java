package com.jokin.framework.modulesdk.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.IWindowManager;
import com.jokin.framework.modulesdk.R;
import com.jokin.framework.modulesdk.delegate.MaximizeDelegate;
import com.jokin.framework.modulesdk.delegate.MinimizeDelegate;
import com.jokin.framework.modulesdk.delegate.MoveDelegate;
import com.jokin.framework.modulesdk.delegate.ScaleDelegate;

/**
 * Created by jokin on 2018/7/16 10:51.
 */

public class CViewWindow extends FrameLayout implements IWindow, View.OnClickListener {
    private static final String TAG = "CWindow";
    protected IWindow.LayoutParams mWindowLayoutParams;
    protected WindowManager.LayoutParams mLastLayoutParams;

    ScaleDelegate mScaleDelegate;
    MinimizeDelegate mMinimizeDelegate;
    MaximizeDelegate mMaximizeDelegate;
    private MoveDelegate mMovableDelegate;

    public CViewWindow(@NonNull Context context) {
        super(context);
        init();
    }

    public CViewWindow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CViewWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CViewWindow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initEvents();
    }

    private void init() {
        /** init()时，children列表是空的！ */
        mWindowLayoutParams = new IWindow.LayoutParams.Builder().build();

        mScaleDelegate = new ScaleDelegate(this);
        mMinimizeDelegate = new MinimizeDelegate(this);
        mMaximizeDelegate = new MaximizeDelegate(this);
        mMovableDelegate = new MoveDelegate(this);

        mLastX = mWindowLayoutParams.x;
        mLastY = mWindowLayoutParams.y;
        mLastWidth = mWindowLayoutParams.width;
        mLastHeight = mWindowLayoutParams.height;
    }

    private void initEvents() {
        Log.e(TAG, "child size:" + getChildCount());
        printChild(this);
        Log.e(TAG, "close id:"+R.id.btn_close);
        Log.e(TAG, "min id:"+R.id.btn_min);
        Log.e(TAG, "max id:"+R.id.btn_max);
        Log.e(TAG, "scale id:"+R.id.btn_scale);

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_min).setOnClickListener(this);
        findViewById(R.id.btn_max).setOnClickListener(this);
        findViewById(R.id.btn_crash).setOnClickListener(this);
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

        TextView textView = findViewById(R.id.text);
        textView.setText(textView.getText()+" Process:"+ Process.myPid());
    }

    private void printChild(ViewGroup parent) {
        Log.e(TAG, "printChild count:" + parent.getChildCount());
        for (int i = 0; i < parent.getChildCount(); ++i) {
            View child = getChildAt(i);
            if (child instanceof ViewGroup) {
                // printChild((ViewGroup) child);
            } else {
                Log.e(TAG, "child id:" + String.valueOf(child.getId()) + "with tag:" + child.getTag());
            }
        }
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

    private int mLastX;
    private int mLastY;
    private int mLastWidth;
    private int mLastHeight;
    private boolean mMovable = true;

    @Override
    public void onMinimizeStart() {
        setWindowLayoutParams(getWindowLayoutParams()
                .X(mLastX).Y(mLastY)
                .Width(mLastWidth).Height(mLastHeight));
    }

    @Override
    public void onMinimizeEnd() {
        mMovable = true;
    }

    @Override
    public void onMaximizeStart() {
        mLastX = getWindowLayoutParams().x;
        mLastY = getWindowLayoutParams().y;
        mLastWidth = getWindowLayoutParams().width;
        mLastHeight = getWindowLayoutParams().height;
        mMovable = false;
    }

    @Override
    public void onMaximizeEnd() {
        // nop
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
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height);
        setX(layoutParams.x);
        setY(layoutParams.y);
        setLayoutParams(params);
    }

    private boolean equals(ViewGroup.LayoutParams params1, ViewGroup.LayoutParams params2) {
        if (params1 != null && params2 == null) {
            Log.d(TAG, "equals: f");
            return false;
        }
        if (params1 == null && params2 != null) {
            Log.d(TAG, "equals: f2");
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



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMovable) {
            mMovableDelegate.handleEvent(event);
        }
        return true;
    }

    //////////////////

    private IWindowManager mWindowManager;
    private IWindow.LayoutParams mLayoutParams;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_close) {
            onClickBtnClose();

        } else if (i == R.id.btn_min) {
            onClickBtnMin();

        } else if (i == R.id.btn_max) {
            onClickBtnMax();

        } else if (i == R.id.btn_crash) {
            View view = null;
            view.getTag();

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
