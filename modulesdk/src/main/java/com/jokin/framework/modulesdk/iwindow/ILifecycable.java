package com.jokin.framework.modulesdk.iwindow;

/**
 * Created by jokin on 2018/7/16 11:20.
 */

public interface ILifecycable {
    void onCreate(android.os.Bundle bundle);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
