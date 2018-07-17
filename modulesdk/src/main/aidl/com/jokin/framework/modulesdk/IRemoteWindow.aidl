// IRemoteWindow.aidl
package com.jokin.framework.modulesdk;

// Declare any non-default types here with import statements

interface IRemoteWindow {

    void onCreate(out android.os.Bundle bundle);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    /**
     * 通知：窗口激活
     */
    void notifyActivated();

}
