package com.jokin.framework.cwindowframework;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jokin.framework.modulesdk.log.Logger;
import com.jokin.framework.modulesdk.view.CRemoteView;
import com.jokin.framework.moduleserver.IRemoteViewListener;
import com.jokin.framework.moduleserver.ModuleCenter;
import com.jokin.framework.moduleserver.RemoteLayoutInflater;

import java.lang.reflect.Method;
import java.util.HashMap;

public class FullscreenActivity extends AppCompatActivity {
    private final String TAG = "FullscreenActivity"+this;

    private Context mContext;
    private ModuleCenter mModuleCenter;
    private AnrWatchDog mAnrWatchDog;
    private ViewGroup mRootView;
    private HashMap<String, View> mRemoteViews = new HashMap<>(10);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        INSTANCE = this;
        mAnrWatchDog = new AnrWatchDog();

        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        verifyStoragePermissions(this);

        Logger.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen);
        TextView textView = findViewById(R.id.fullscreen_content);
        textView.setText(textView.getText()+"\nActivity:"+String.valueOf(this.hashCode()));
        textView.setText(textView.getText()+"\nProcess:"+String.valueOf(Process.myPid()));

        mRootView = findViewById(R.id.rootView);

        findViewById(R.id.moduleA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.jokin.framework.modulea", "com.jokin.framework.modulea.ModuleAService"));
                startService(intent);
            }
        });
        findViewById(R.id.moduleB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.jokin.framework.moduleb", "com.jokin.framework.moduleb.ModuleBService"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent);
                }
            }
        });

        mModuleCenter = new ModuleCenter(this);
        mModuleCenter.onCreate(savedInstanceState);
        mModuleCenter.registerRemoteViewListener(new IRemoteViewListener() {
            @Override
            public void onAdd(CRemoteView view) {
                Logger.i(TAG, "## Add: remoteView" + view);
                if (mRemoteViews.containsKey(view.key())) {
                    Logger.w(TAG, "## Add: already add view " + view);
                    return;
                }
                View realView = RemoteLayoutInflater.from(mContext, view.getPackage()).inflate(view.getLayoutId(), mRootView, false);
                // View realView = view.apply(mContext, mRootView);
                Logger.d(TAG, "onAdd: realView" + realView);
                try {
                    Method method = realView.getClass().getMethod("notifyActivated", new Class[0]);
                    method.invoke(realView);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mRootView.addView(realView);
                realView.setTag(view.key());
                mRemoteViews.put(view.key(), realView);
            }

            @Override
            public void onUpdate(CRemoteView view) {
                View realView = mRemoteViews.get(view.key());
                if (realView == null) {
                    Logger.e(TAG, "## Update: Not found view for " + view + ", maybe view has been close local");
                    return;
                }
                // Logger.e(TAG, "============end============");
                Logger.d(TAG, "onUpdate() called with: view = [" + view + "]");
                view.reapply(mContext, realView);
            }

            @Override
            public void onRemove(CRemoteView view) {
                Logger.d(TAG, "onRemove() called with: view = [" + view + "]");
                View realView = mRemoteViews.remove(view.key());
                if (realView == null) {
                    Logger.e(TAG, "## Remove: Not found view for " + view + ", maybe view has been close local");
                    return;
                }
                mRootView.removeView(realView);
            }
        });

        mRootView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {

            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                if (mRemoteViews.containsValue(child)) {
                    mRemoteViews.remove(child.getTag());
                    // TODO notify client if close at server side
                    // But no need if client side close.
                }
            }
        });
    }

    public static FullscreenActivity INSTANCE;

    private boolean blSandBox;
    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(TAG, "onStart() called");
        mModuleCenter.onStart();
        mRootView.post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!blSandBox) {
                        try {
                            blSandBox = true;
                            Logger.e(TAG, "Looping !!!");
                            Looper.loop();
                        } catch (Exception e) {
                            Logger.e(TAG, "Plugin Dead!", e);
                        } catch (Error e) {
                            Logger.e("UncaughtException", "Got an uncaught exception: "+e.toString());
                            if(e.getClass().equals(OutOfMemoryError.class)) {
                                Logger.e(TAG, "######## OOM ######");
                                // Dump or not
                                // try {
                                //     android.os.Debug.dumpHprofData("/sdcard/dump.hprof");
                                // } catch (IOException ex) {
                                //     e.printStackTrace();
                                // }
                            }
                            Logger.e(TAG, "Error:", e);
                        }
                    }
                }
            }
        });
        Logger.e(TAG, "on start");
    }


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
    protected void onResume() {
        super.onResume();
        Logger.d(TAG, "onResume() called");
        mModuleCenter.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.d(TAG, "onRestart() called");
        mModuleCenter.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(TAG, "onPause() called");
        mModuleCenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(TAG, "onStop() called");
        mModuleCenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy() called");
        mModuleCenter.onDestroy();
    }
}
