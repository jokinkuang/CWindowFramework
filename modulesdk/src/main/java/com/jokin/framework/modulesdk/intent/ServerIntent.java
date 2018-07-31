package com.jokin.framework.modulesdk.intent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.List;

/**
 * Created by jokin on 2018/7/17 11:02.
 */

public class ServerIntent {
    private static final String TAG = "ServerIntent";
    private static final String ACTION_SERVER_SERVICE_MAIN = "framework.server.service.main";
    private static final String ACTION_SERVER_ACTIVITY_MAIN = "framework.server.activity.main";

    public static Intent getServerMainServiceIntent(Context context) {
        Intent intent = getExplicitIntent(context, new Intent(ACTION_SERVER_SERVICE_MAIN));
        intent.putExtra("client.package.name", context.getPackageName());
        return intent;
    }

    public static Intent getServerMainActivityIntent(Context context) {
        Intent intent = new Intent(ACTION_SERVER_ACTIVITY_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    //将隐式启动转换为显式启动,兼容编译sdk5.0以后版本
    private static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        PackageManager pm = context.getPackageManager();

        List<ResolveInfo> resolveInfos = pm.queryIntentServices(implicitIntent, 0);
        if (resolveInfos == null || resolveInfos.size() != 1) {
            Log.d(TAG, "getExplicitIntent: ResolveInfos is empty!");
            return null;
        }

        ResolveInfo info = resolveInfos.get(0);
        String packageName = info.serviceInfo.packageName;
        String className = info.serviceInfo.name;

        Intent explicitIntent = new Intent(implicitIntent);
        ComponentName component = new ComponentName(packageName, className);
        explicitIntent.setComponent(component);
        Log.d(TAG, "getExplicitIntent() called with: implicitIntent = " + implicitIntent
                + ", explicitIntent = " + explicitIntent);
        return explicitIntent;
    }
}
