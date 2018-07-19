package com.jokin.framework.cwindowframework;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jokin.framework.moduleserver.ModuleCenter;

public class FullscreenActivity extends AppCompatActivity {
    private final String TAG = "FullscreenActivity"+this;

    private ModuleCenter mModuleCenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen);
        TextView textView = findViewById(R.id.fullscreen_content);
        textView.setText(textView.getText()+"\n"+String.valueOf(this.hashCode()));

        findViewById(R.id.moduleB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.jokin.framework.module", "com.jokin.framework.module.ModuleBService"));
                startService(intent);
            }
        });

        mModuleCenter = new ModuleCenter(this);
        mModuleCenter.onCreate(savedInstanceState);
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
