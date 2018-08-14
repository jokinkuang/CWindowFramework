package com.jokin.framework.cwindowframework;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jokin.framework.modulesdk.view.CRemoteView;
import com.jokin.framework.modulesdk.view.CViewWindow;
import com.jokin.framework.moduleserver.IRemoteViewListener;
import com.jokin.framework.moduleserver.ModuleCenter;
import com.jokin.framework.moduleserver.RemoteLayoutInflater;

import java.util.HashMap;

public class FullscreenActivity extends AppCompatActivity {
    private final String TAG = "FullscreenActivity"+this;

    private Context mContext;
    private ModuleCenter mModuleCenter;
    private ViewGroup mRootView;
    private HashMap<String, View> mRemoteViews = new HashMap<>(10);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

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
                Log.i(TAG, "## Add: remoteView" + view);
                if (mRemoteViews.containsKey(view.key())) {
                    Log.w(TAG, "## Add: already add view " + view);
                    return;
                }
                CViewWindow realView = (CViewWindow) RemoteLayoutInflater.from(mContext, view.getPackage()).inflate(view.getLayoutId(), mRootView, false);
                Log.d(TAG, "onAdd: realView" + realView);
                realView.notifyActivated();
                mRootView.addView(realView);
                realView.setTag(view.key());
                mRemoteViews.put(view.key(), realView);
            }

            @Override
            public void onUpdate(CRemoteView view) {
                View realView = mRemoteViews.get(view.key());
                if (realView == null) {
                    Log.e(TAG, "## Update: Not found view for " + view + ", maybe view has been close local");
                    return;
                }
                // Log.e(TAG, "============end============");
                Log.d(TAG, "onUpdate() called with: view = [" + view + "]");
                view.reapply(mContext, realView);
            }

            @Override
            public void onRemove(CRemoteView view) {
                Log.d(TAG, "onRemove() called with: view = [" + view + "]");
                View realView = mRemoteViews.remove(view.key());
                if (realView == null) {
                    Log.e(TAG, "## Remove: Not found view for " + view + ", maybe view has been close local");
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


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        mModuleCenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        mModuleCenter.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");
        mModuleCenter.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        mModuleCenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
        mModuleCenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
        mModuleCenter.onDestroy();
    }
}
