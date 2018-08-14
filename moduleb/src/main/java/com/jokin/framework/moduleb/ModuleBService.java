package com.jokin.framework.moduleb;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.delegate.MaximizeDelegate;
import com.jokin.framework.modulesdk.delegate.MinimizeDelegate;
import com.jokin.framework.modulesdk.delegate.ScaleDelegate;
import com.jokin.framework.modulesdk.impl.CModule;
import com.jokin.framework.modulesdk.impl.CWindow;
import com.jokin.framework.modulesdk.view.CRemoteView;
import com.jokin.framework.modulesdk.view.IRemoteViewManager;
import com.jokin.framework.modulesdk.view.RemoteWindowManager;
import com.jokin.framework.modulesdk.wrap.LifecycleAdapter;

public class ModuleBService extends Service implements View.OnClickListener {
    private static final String TAG = "ModuleBService";
    private Handler mHandler = new Handler(Looper.getMainLooper());

    CModule mModule;
    CWindow mWindow;
    ScaleDelegate mScaleDelegate;
    MinimizeDelegate mMinimizeDelegate;
    MaximizeDelegate mMaximizeDelegate;

    IRemoteViewManager mRemoteViewManager;
    CRemoteView mCRemoteView;

    public ModuleBService() {
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
                mRemoteViewManager.destroy();
            }
        });

        mWindow = (CWindow) LayoutInflater.from(this).inflate(R.layout.layout_window, null, false);
        mWindow.setWindowLayoutParams(new IWindow.LayoutParams.Builder().x(800).y(300).build());
        mWindow.findViewById(R.id.btn_close).setOnClickListener(this);
        mWindow.findViewById(R.id.btn_min).setOnClickListener(this);
        mWindow.findViewById(R.id.btn_max).setOnClickListener(this);
        mWindow.findViewById(R.id.btn_scale).setOnClickListener(this);
        mWindow.findViewById(R.id.btn_animation).setOnClickListener(this);
        mWindow.findViewById(R.id.btn_crash).setOnClickListener(this);
        mWindow.findViewById(R.id.edit_text).setOnClickListener(this);

        mScaleDelegate = new ScaleDelegate(mWindow);
        mMinimizeDelegate = new MinimizeDelegate(mWindow);
        mMaximizeDelegate = new MaximizeDelegate(mWindow);


        mWindow.findViewById(R.id.btn_scale).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch() called with: v = [" + v + "], event = [" + event + "]");
                if (v.getId() == R.id.btn_scale) {
                    mWindow.getResizeDelegate().scale(event);
                    return true;
                }
                return false;
            }
        });

        TextView textView = mWindow.findViewById(R.id.text);
        textView.setText(textView.getText()+" Process:"+ Process.myPid());
        startForeground(1, new NotificationCompat.Builder(this, "1").build());
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //     startForeground(1,new Notification.Builder(this, ).build());
        // } else {
            startForeground(1, new Notification());
        // }

        //// Remote view
        mRemoteViewManager = new RemoteWindowManager(this);
        mCRemoteView = new CRemoteView(this.getPackageName(), R.layout.layout_view_window);
        mRemoteViewManager.addView(mCRemoteView);
        // startTimer();
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
            case R.id.btn_animation:
                onClickBtnAnimation();
                break;
            case R.id.btn_crash:
                View view = null;
                view.getTag();
                break;
            case R.id.btn_scale:
                onClickBtnDrag();
                break;
            case R.id.edit_text:
                onClickEditText();
                break;
        }
    }

    private void onClickBtnClose() {
        Log.d(TAG, "onClickBtnClose() called");
        stopTimer();
        mModule.destroy();
    }

    private void onClickBtnMin() {
        mWindow.getResizeDelegate().minimize();
    }

    private void onClickBtnMax() {
        mWindow.getResizeDelegate().maximize();
    }

    private boolean mStart = false;
    private void onClickBtnAnimation() {
        mStart = !mStart;
        if (mStart) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    private void onClickBtnDrag() {

    }

    private void onClickEditText() {
        InputMethodManager inputManager = (InputMethodManager) this.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    ////////////////////////////////////

    private void startTimer() {
        mHandler.postDelayed(backgroundRunnable, 33);
    }

    private void stopTimer() {
        mHandler.removeCallbacks(backgroundRunnable);
    }

    private Runnable backgroundRunnable = new Runnable() {
        @Override
        public void run() {
            setBackground();
            startTimer();
        }
    };

    private Resources res;
    private int index = 0;
    private int min = 0;
    private int max = 30;

    private void setBackground() {
        String id = String.format("eraser_sliding_%02d", index);
        if (index >= max) {
            index = min;
        } else {
            index++;
        }
        if (res == null) {
            res = getResources();
        }
        Log.d(TAG, "setBackground: "+id);

        // 服务端内存占有非常大！Bitmap没能释放native的内存
        // Bitmap bmp = BitmapFactory.decodeResource(res, getDrawableId(id));
        // mCRemoteView.setImageViewBitmap(R.id.image, bmp);
        // 坑爹，会一直添加Action到RemoteViews，导致数组越来越大！
        // mCRemoteView.setImageViewResource(R.id.image, getDrawableId(id));
        mCRemoteView = mCRemoteView.clone();
        mCRemoteView.setImageViewResource(R.id.image, getDrawableId(id));
        // Log.e(TAG, "========================");
        mRemoteViewManager.updateView(mCRemoteView);

        ((ImageView)mWindow.findViewById(R.id.image)).setImageResource(getDrawableId(id));
    }
    public int getDrawableId(String id) {
        return getResources().getIdentifier(id, "drawable", getPackageName());
    }

    public static Drawable GetImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }
}
