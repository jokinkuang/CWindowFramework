package com.jokin.framework.moduleserver;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;

import static android.content.ContentValues.TAG;

/**
 * Created by jokin on 2018/8/7 20:21.
 */

public class RemoteLayoutInflater {

    private static Context s_c;
    public static LayoutInflater from(Context localContext, String remotePackageName) {
        Context c = prepareContext(localContext, remotePackageName);
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.cloneInContext(c);
    }

    private static Context prepareContext(Context context, String pkgName) {
        if (s_c != null) {
            return s_c;
        }
        Context c;
        String packageName = pkgName;
        if (packageName != null) {
            try {
                c = context.createPackageContext(packageName, Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
                // c = context.createPackageContextAsUser(
                //         packageName, Context.CONTEXT_RESTRICTED, mUser);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Package name " + packageName + " not found");
                c = context;
            }
        } else {
            c = context;
        }
        s_c = c;
        return c;
    }
}
