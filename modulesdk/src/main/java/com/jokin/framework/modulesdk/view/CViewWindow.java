package com.jokin.framework.modulesdk.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jokin.framework.modulesdk.IWindow;
import com.jokin.framework.modulesdk.IWindowManager;
import com.jokin.framework.modulesdk.R;
import com.jokin.framework.modulesdk.delegate.ResizeDelegate;
import com.jokin.framework.modulesdk.log.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

/**
 * Created by jokin on 2018/7/16 10:51.
 */

public class CViewWindow extends FrameLayout implements IWindow, View.OnClickListener {
    private static final String TAG = CViewWindow.class.getSimpleName();

    protected IWindow.LayoutParams mWindowLayoutParams;
    protected ResizeDelegate mResizeDelegate;

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
        /**
         * init()时，children列表是空的！放到onAttachedToWindow访问children
         **/
        mWindowLayoutParams = new IWindow.LayoutParams.Builder().build();
        mResizeDelegate = new ResizeDelegate(this);
    }

    private void initEvents() {
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_min).setOnClickListener(this);
        findViewById(R.id.btn_max).setOnClickListener(this);
        findViewById(R.id.btn_crash).setOnClickListener(this);
        findViewById(R.id.edit_text).setOnClickListener(this);

        findViewById(R.id.btn_scale).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Logger.d(TAG, "onTouch() called with: v = [" + v + "], event = [" + event + "]");
                if (v.getId() == R.id.btn_scale) {
                    mResizeDelegate.scale(event);
                    return true;
                }
                return false;
            }
        });

        TextView textView = findViewById(R.id.text);
        textView.setText(textView.getText()+" Process:"+ Process.myPid());
    }

    private String space = "-";
    private String SEPARATOR = "--";
    private void printTree(ViewGroup parent) {
        Logger.e(TAG, space+"child count:" + parent.getChildCount());
        for (int i = 0; i < parent.getChildCount(); ++i) {
            View child = parent.getChildAt(i);
            Logger.e(TAG, space+String.format("child(%d): %s", i, child.toString()));
            if (child instanceof ViewGroup) {
                space += SEPARATOR;
                printTree((ViewGroup) child);
                space = space.substring(SEPARATOR.length());
            }
        }
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

    }

    @Override
    public void onMoving() {

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
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height);
        setX(layoutParams.x);
        setY(layoutParams.y);
        setLayoutParams(params);
    }

    private boolean equals(ViewGroup.LayoutParams params1, ViewGroup.LayoutParams params2) {
        if (params1 != null && params2 == null) {
            Logger.d(TAG, "equals: f");
            return false;
        }
        if (params1 == null && params2 != null) {
            Logger.d(TAG, "equals: f2");
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
        Logger.e(TAG, "notifyActivated!");
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }

    ///////////////////



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mResizeDelegate.move(event);
        return true;
    }

    //////////////////

    private IWindowManager mWindowManager;
    private IWindow.LayoutParams mLayoutParams;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        } else if (i == R.id.btn_crash) {
            // View view = null;
            // view.getTag();
            // try {
            //     Thread.sleep(10*1000);
            // } catch (InterruptedException e) {
            //     Logger.e(TAG, "", e);
            // }

            // Intent intent = getContext().getPackageManager().buildRequestPermissionsIntent(permissions);
            PackageManager packageManager = getContext().getPackageManager();
            try {
                Logger.e(TAG, "onClick: " + packageManager);
                // Intent intent = new Intent("android.content.pm.action.REQUEST_PERMISSIONS");
                // intent.putExtra("android.content.pm.extra.REQUEST_PERMISSIONS_NAMES", PERMISSIONS_STORAGE);
                // intent.setPackage(getContext().getPackageManager().getPermissionControllerPackageName());

                // Method method = packageManager.getClass().getMethod("getPermissionControllerPackageName");
                // String pkg = (String) method.invoke(packageManager);
                // Logger.e(TAG, "pkg:"+pkg);
                // intent.setPackage(pkg);

                int result = getContext().checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", android.os.Process.myPid(), Process.myUid());
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Logger.e(TAG, "Not granted permission!");
                } else {
                    Logger.e(TAG, "Granted permission!");
                }

                Method method = packageManager.getClass().getMethod("buildRequestPermissionsIntent", new Class<?>[]{String[].class});
                Logger.e(TAG, "onClick: "+method);
                Intent intent = (Intent) method.invoke(packageManager, new Object[]{PERMISSIONS_STORAGE});
                Logger.e(TAG, "onClick: "+intent);
                getContext().startActivity(intent);
            } catch (Exception e) {
                Logger.e(TAG, "", e);
            }

            post(new Runnable() {
                @Override
                public void run() {
                    Logger.e(TAG, "start:"+System.currentTimeMillis());
                    Bitmap bitmap = Bitmap.createBitmap(5000, 5000, Bitmap.Config.ARGB_8888);
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(getContext().getCacheDir().getAbsolutePath()+"/a"));
                    } catch (FileNotFoundException e) {
                        Logger.e(TAG, "", e);
                    }
                    Logger.e(TAG, "end:"+System.currentTimeMillis());
                }
            });
        } else if (i == R.id.btn_scale) {
            onClickBtnDrag();

        } else if (i == R.id.edit_text) {
            onClickEditText();

        }
    }


    private void onClickBtnClose() {
        Logger.d(TAG, "onClickBtnClose() called");
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
    }

    private void onClickBtnMin() {
        mResizeDelegate.minimize();
    }

    private void onClickBtnMax() {
        mResizeDelegate.maximize();
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
