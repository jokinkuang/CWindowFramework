package com.jokin.framework.modulesdk.wrap;

import android.os.Bundle;

import com.jokin.framework.modulesdk.iwindow.ILifecycable;

import java.util.HashMap;

/**
 * Created by jokin on 2018/7/17 20:19.
 */

public class LifecycleBridge implements ILifecycable {
    private HashMap<ILifecycable, ILifecycable> mLifecycables = new HashMap<>(5);

    public LifecycleBridge() {}

    public LifecycleBridge(ILifecycable lifecycable) {
        mLifecycables.put(lifecycable, lifecycable);
    }

    public void addLifecycable(ILifecycable lifecycable) {
        mLifecycables.put(lifecycable, lifecycable);
    }

    public void removeLifecycable(ILifecycable lifecycable) {
        mLifecycables.remove(lifecycable);
    }

    @Override
    public void onCreate(Bundle bundle) {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onCreate(bundle);
        }
    }

    @Override
    public void onStart() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onStart();
        }
    }

    @Override
    public void onRestart() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onRestart();
        }
    }

    @Override
    public void onResume() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onResume();
        }
    }

    @Override
    public void onPause() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onPause();
        }
    }

    @Override
    public void onStop() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onStop();
        }
    }

    @Override
    public void onDestroy() {
        for (ILifecycable lifecycable : mLifecycables.values()) {
            lifecycable.onDestroy();
        }
    }
}
