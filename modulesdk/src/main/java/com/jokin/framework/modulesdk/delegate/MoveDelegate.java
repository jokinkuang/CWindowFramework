package com.jokin.framework.modulesdk.delegate;

import android.util.Log;
import android.view.MotionEvent;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.iwindow.IBaseWindow;

import java.security.InvalidParameterException;

/**
 * Created by jokin on 2018/7/16 16:37.
 */

public class MoveDelegate {
    private static final String TAG = MoveDelegate.class.getSimpleName();

    private final int WINDOW_WIDTH = 1920;
    private final int WINDOW_HEIGHT = 1080;

    private IBaseWindow mWindow;
    private IWindow.LayoutParams mParams;
    private int mTouchLastX;
    private int mTouchLastY;

    public MoveDelegate(IBaseWindow target) {
        if (target == null) {
            throw new InvalidParameterException("target cannot be null");
        }
        mWindow = target;
    }

    public void move(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchLastX = x;
                mTouchLastY = y;
                onStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - mTouchLastX;
                int offsetY = y - mTouchLastY;
                onContinue(offsetX, offsetY);
                mTouchLastX = x;
                mTouchLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                onEnd(x, y);
                // do not break;
            default:
                break;
        }
    }

    public void onStart(int x, int y) {
        Log.d(TAG, "onStart() called with: x = [" + x + "], y = [" + y + "]");
        mParams = mWindow.getWindowLayoutParams();
        updateLimitsRect();
        mWindow.onMoveStart();
    }

    public void onContinue(int x, int y) {
        Log.d(TAG, "onContinue() called with: x = [" + x + "], y = [" + y + "]");
        if (x == 0 && y == 0) {
            return;
        }
        transformParams(x, y);
        mWindow.setWindowLayoutParams(mParams);
        mWindow.onMoving();
    }

    private void transformParams(int x, int y) {
        mParams.x += x;
        mParams.y += y;
        // mParams.x = mParams.x < mLeftLimitX ? mLeftLimitX : mParams.x;
        // mParams.x = mParams.x > mRightLimitX ? mRightLimitX : mParams.x;
        // mParams.y = mParams.y < mTopLimitY ? mTopLimitY : mParams.y;
        // mParams.y = mParams.y > mBottomLimitY ? mBottomLimitY : mParams.y;
    }

    public void onEnd(int x, int y) {
        Log.d(TAG, "onEnd() called with: x = [" + x + "], y = [" + y + "]");
        mWindow.setWindowLayoutParams(mParams);
        mWindow.onMoveEnd();
    }


    private int mLeftLimitX;
    private int mRightLimitX;
    private int mTopLimitY;
    private int mBottomLimitY;

    private void updateLimitsRect() {
        mLeftLimitX = 0;
        mRightLimitX = WINDOW_WIDTH - mParams.width;
        mTopLimitY = 0;
        mBottomLimitY = WINDOW_HEIGHT - mParams.height;
    }
}
