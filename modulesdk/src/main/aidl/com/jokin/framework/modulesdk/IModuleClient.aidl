// IRemoteWindow.aidl
package com.jokin.framework.modulesdk;

/**
 * Remote Client Module
 **/
interface IModuleClient {
    String key();

    void onCreate(out android.os.Bundle bundle);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
