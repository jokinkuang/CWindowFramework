package com.jokin.framework.modulesdk.log;

/**
 * Created by jokin on 2018/7/2 16:36.
 */

public interface ILog {
    void v(String tag, String msg);
    void d(String tag, String msg);
    void i(String tag, String msg);
    void w(String tag, String msg);
    void w(String tag, String msg, Throwable tr);
    void e(String tag, String msg);
    void e(String tag, String msg, Throwable tr);
    void wtf(String tag, String msg);
    void wtf(String tag, String msg, Throwable tr);
}
