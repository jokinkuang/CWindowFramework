package com.jokin.framework.moduleserver;

import android.content.Context;
import android.view.LayoutInflater;

import com.jokin.framework.modulesdk.log.Logger;

import static android.content.ContentValues.TAG;

/**
 * Created by jokin on 2018/8/7 20:21.
 */

public class RemoteLayoutInflater {

    private static Context s_c = null;
    public static LayoutInflater from(Context localContext, String remotePackageName) {
        Context c = prepareContext(localContext, remotePackageName);
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.cloneInContext(c);
    }

    private static Context prepareContext(Context context, String pkgName) {
        if (s_c != null) {
            return s_c;
        }
        Context c = null;
        String packageName = pkgName;
        if (packageName != null) {
            try {
                c = context.createPackageContext(packageName, Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
                // 下面是另外一个尝试，暂时未成功。
                // c = context.createPackageContextAsUser(
                //         packageName, Context.CONTEXT_RESTRICTED, mUser);
                // Method method = context.getClass().getMethod("createPackageContextAsUser", String.class, int.class, UserHandle.class);
                // Constructor consturctMethod = UserHandle.class.getConstructor(int.class);
                // UserHandle userHandle = (UserHandle) consturctMethod.newInstance(Process.myUid());
                // method.invoke(context, packageName, Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY, userHandle);
            } catch (Exception e) {
                Logger.e(TAG, "Package name " + packageName + " not found");
                c = context;
            }
        } else {
            c = context;
        }
        s_c = c;
        return c;
    }
}
