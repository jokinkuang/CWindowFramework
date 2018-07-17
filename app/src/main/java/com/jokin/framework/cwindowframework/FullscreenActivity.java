package com.jokin.framework.cwindowframework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.jokin.framework.moduleserver.ModuleCenter;

public class FullscreenActivity extends AppCompatActivity {

    private ModuleCenter mModuleCenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen);

        mModuleCenter = new ModuleCenter(this);
        mModuleCenter.onCreate(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mModuleCenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mModuleCenter.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mModuleCenter.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mModuleCenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mModuleCenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mModuleCenter.onDestroy();
    }
}
