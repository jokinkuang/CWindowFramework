package com.jokin.framework.modulesdk;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jokin.framework.modulesdk.iwindow.ILifecycable;
import com.jokin.framework.modulesdk.iwindow.IMovable;
import com.jokin.framework.modulesdk.iwindow.IResizable;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jokin on 2018/7/16 10:51.
 */

public interface IWindow extends IResizable, IMovable, ILifecycable {

    /**
     * @return the view of the window
     */
    View getContentView();

    void setWindowLayoutParams(IWindow.LayoutParams layoutParams);
    IWindow.LayoutParams getWindowLayoutParams();

    void attachWindowManager(IWindowManager windowManager);
    void detachWindowManager();

    /**
     * 通知：窗口激活
     */
    void notifyActivated();


    /**
     * 通知：触摸事件
     */
    boolean onTouch(MotionEvent event);


    //////////////////////

    class LayoutParams extends ViewGroup.LayoutParams {
        public static final int INT_INVALID = -99999;
        public int flags = INT_INVALID;
        public int gravity = Gravity.LEFT|Gravity.TOP;
        public int x = 0;
        public int y = 0;
        public int width = WRAP_CONTENT;
        public int height = WRAP_CONTENT;

        public LayoutParams() {
            super(INT_INVALID, INT_INVALID);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams X(int x) {
            this.x = x;
            return this;
        }

        public LayoutParams Y(int y) {
            this.y = y;
            return this;
        }

        public LayoutParams Width(int width) {
            this.width = width;
            return this;
        }

        public LayoutParams Height(int height) {
            this.height = height;
            return this;
        }

        public LayoutParams Gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public LayoutParams Flags(int flags) {
            this.flags = flags;
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            LayoutParams layoutParams = (LayoutParams) obj;
            if (layoutParams == null) {
                return false;
            }
            if (flags != layoutParams.flags) {
                return false;
            }
            if (gravity != layoutParams.gravity) {
                return false;
            }
            if (x != layoutParams.x) {
                return false;
            }
            if (y != layoutParams.y) {
                return false;
            }
            if (width != layoutParams.width) {
                return false;
            }
            if (height != layoutParams.height) {
                return false;
            }
            return true;
        }

        public static class Builder {
            private LayoutParams mLayoutParams = new LayoutParams();

            public Builder width(int width) {
                mLayoutParams.width = width;
                return this;
            }

            public Builder height(int height) {
                mLayoutParams.height = height;
                return this;
            }

            public Builder x(int x) {
                mLayoutParams.x = x;
                return this;
            }

            public Builder y(int y) {
                mLayoutParams.y = y;
                return this;
            }

            public Builder gravity(int gravity) {
                mLayoutParams.gravity = gravity;
                return this;
            }

            public Builder flags(int flags) {
                mLayoutParams.flags = flags;
                return this;
            }

            public LayoutParams build() {
                return mLayoutParams;
            }
        }
    }

    ///////////////////////

    class Dump {
        private static final String TAG = "Dump";
        private static Map<String, Integer> sGravitys = new LinkedHashMap<>(10);
        static {
            sGravitys.put("Gravity.Center", Gravity.CENTER);
            sGravitys.put("Gravity.Center_Horizontal", Gravity.CENTER_HORIZONTAL);
            sGravitys.put("Gravity.Center_Vertical", Gravity.CENTER_VERTICAL);
            sGravitys.put("Gravity.Left", Gravity.LEFT);
            sGravitys.put("Gravity.Top", Gravity.TOP);
            sGravitys.put("Gravity.Right", Gravity.RIGHT);
            sGravitys.put("Gravity.Bottom", Gravity.BOTTOM);
            sGravitys.put("Gravity.Left|Top", Gravity.LEFT|Gravity.TOP);
            sGravitys.put("Gravity.Right|Bottom", Gravity.RIGHT|Gravity.BOTTOM);
            // Gravity.Center = #11
            // Gravity.Center_Horizontal = #1
            // Gravity.Center_Vertical = #10
            // Gravity.Left = #3
            // Gravity.Top = #30
            // Gravity.Right = #5
            // Gravity.Bottom = #50
            // Gravity.Left|Top = #33
            // Gravity.Right|Bottom = #55
        }

        public static void dumpGravity() {
            for (Iterator<Map.Entry<String, Integer>> iterator = sGravitys.entrySet().iterator();
                    iterator.hasNext();) {
                Map.Entry<String, Integer> entry = iterator.next();
                Log.d(TAG, "dumpGravity: " + entry.getKey() + " = #" + Integer.toHexString(entry.getValue()));
            }
        }
    }
}
