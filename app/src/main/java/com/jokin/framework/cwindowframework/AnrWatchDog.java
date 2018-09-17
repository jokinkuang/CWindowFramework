package com.jokin.framework.cwindowframework;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import com.jokin.framework.modulesdk.log.Logger;

/**
 * Created by jokin on 2018/8/24 10:44.
 */

public class AnrWatchDog {
    private static final String TAG = "AnrWatchDog";

    private HandlerThread mHandlerThread;
    private Handler mCheckHandler;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    private volatile Long mLast;
    private volatile Long mNow;
    private volatile boolean mRunning = true;

    public AnrWatchDog() {
        startMainThread();
        initCheckThread();
        startCheckThread();
    }

    private void startMainThread() {
        mLast = System.currentTimeMillis();
        mMainHandler.post(mMainRunnable);
    }

    private Runnable mMainRunnable = new Runnable() {
        @Override
        public void run() {
            mLast = System.currentTimeMillis();
            mMainHandler.postDelayed(mMainRunnable, 100L);
        }
    };


    private void initCheckThread() {
        mHandlerThread = new HandlerThread("Anr-bg-thread", Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mCheckHandler = new Handler(mHandlerThread.getLooper());
    }

    private void startCheckThread() {
        mCheckHandler.post(mCheckRunnable);
    }

    private void stopCheckThread() {
        mCheckHandler.removeCallbacksAndMessages(null);
    }

    private Runnable mCheckRunnable = new Runnable() {
        @Override
        public void run() {
            mNow = System.currentTimeMillis();
            // Logger.e(TAG, "run time:" + (mNow - mLast));
            if (mNow - mLast > 2 * 1000) {
                Logger.e(TAG, "ANR!!");
                Looper.getMainLooper().getThread().interrupt();
                return;
            }
            mCheckHandler.postDelayed(mCheckRunnable, 100);
        }
    };
}
