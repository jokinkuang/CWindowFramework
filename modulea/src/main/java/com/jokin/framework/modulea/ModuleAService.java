package com.jokin.framework.modulea;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.delegate.MaximizeDelegate;
import com.jokin.framework.modulesdk.delegate.MinimizeDelegate;
import com.jokin.framework.modulesdk.delegate.ScaleDelegate;
import com.jokin.framework.modulesdk.impl.CModule;
import com.jokin.framework.modulesdk.impl.CWindow;
import com.jokin.framework.modulesdk.wrap.LifecycleAdapter;

public class ModuleAService extends Service implements View.OnClickListener {
    private static final String TAG = "ModuleAService";

    CModule mModule;
    CWindow mWindow;
    ScaleDelegate mScaleDelegate;
    MinimizeDelegate mMinimizeDelegate;
    MaximizeDelegate mMaximizeDelegate;

    public ModuleAService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
        mModule = new CModule(this);
        mModule.addLifecycleCallback(new LifecycleAdapter() {
            @Override
            public void onDestroy() {
                super.onDestroy();
                stopSelf();
            }
        });

        mWindow = (CWindow) LayoutInflater.from(this).inflate(R.layout.layout_window, null, false);
        mWindow.setWindowLayoutParams(new IWindow.LayoutParams.Builder().build());
        mWindow.findViewById(R.id.btn_close).setOnClickListener(this);
        mWindow.findViewById(R.id.btn_min).setOnClickListener(this);
        mWindow.findViewById(R.id.btn_max).setOnClickListener(this);
        mWindow.findViewById(R.id.btn_scale).setOnClickListener(this);

        mScaleDelegate = new ScaleDelegate(mWindow);
        mMinimizeDelegate = new MinimizeDelegate(mWindow);
        mMaximizeDelegate = new MaximizeDelegate(mWindow);


        mWindow.findViewById(R.id.btn_scale).setOnTouchListener(new View.OnTouchListener() {
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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        mModule.getWindowManagerService().addWindow(mWindow);
        Log.d(TAG, "onStartCommand: window layoutparams" + mWindow.getLayoutParams());
        Log.d(TAG, "onStartCommand: window layoutparams" + mWindow.getWindowLayoutParams());
        return Service.START_STICKY;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                onClickBtnClose();
                break;
            case R.id.btn_min:
                onClickBtnMin();
                break;
            case R.id.btn_max:
                onClickBtnMax();
                break;
            case R.id.btn_scale:
                onClickBtnDrag();
                break;
        }
    }

    private void onClickBtnClose() {
        mModule.destroy();
    }

    private void onClickBtnMin() {
        mMinimizeDelegate.minimize();
    }

    private void onClickBtnMax() {
        mMaximizeDelegate.maximize();
    }

    private void onClickBtnDrag() {

    }
}
