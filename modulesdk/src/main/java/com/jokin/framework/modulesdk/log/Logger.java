package com.jokin.framework.modulesdk.log;

/**
 * Created by jokin on 2018/7/3 11:35.
 *
 * Log is special.
 */

public class Logger {
    static volatile ILog mLogHub;

    static ILog log() {
        if (mLogHub == null) {
            mLogHub = Singleton.instance;
        }
        return mLogHub;
    }

    static class Singleton {
        static final ILog instance = new Logcat();
    }

    public static void v(String tag, String msg) {
        log().v(tag, msg);
    }
    public static void d(String tag, String msg) {
        log().d(tag, msg);
    }
    public static void i(String tag, String msg) {
        log().i(tag, msg);
    }
    public static void w(String tag, String msg) {
        log().w(tag, msg);
    }
    public static void w(String tag, String msg, Throwable tr) {
        log().w(tag, msg, tr);
    }
    public static void e(String tag, String msg) {
        log().e(tag, msg);
    }
    public static void e(String tag, String msg, Throwable tr) {
        log().e(tag, msg, tr);
    }
    public static void wtf(String tag, String msg) {
        log().wtf(tag, msg);
    }
    public static void wtf(String tag, String msg, Throwable tr) {
        log().wtf(tag, msg, tr);
    }
}
