package com.jokin.framework.modulesdk.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RemoteViews;

import com.jokin.framework.modulesdk.constant.Constants;


/**
 * Created by jokin on 2018/8/3 16:32.
 */

public class CRemoteView extends RemoteViews implements Parcelable {
    // public CRemoteView(Context context, int layoutId) {
    //     super(context.getApplicationInfo(), layoutId);
    //     init();
    // }

    public CRemoteView(String packageName, int layoutId) {
        super(packageName, layoutId);
        init();
    }

    public CRemoteView(RemoteViews landscape, RemoteViews portrait) {
        super(landscape, portrait);
        init();
    }

    public CRemoteView(Parcel parcel) {
        super(parcel);
        mKey = parcel.readString();
    }

    public static final Parcelable.Creator<CRemoteView> CREATOR = new Parcelable.Creator<CRemoteView>() {
        public CRemoteView createFromParcel(Parcel in) {
            return new CRemoteView(in);
        }

        public CRemoteView[] newArray(int size) {
            return new CRemoteView[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mKey);
    }

    //////////////////////////////////

    private String mKey;

    private void init() {
        mKey = getPackage() + Constants.KEY_SEPARATOR + hashCode();
    }

    public String key() {
        return mKey;
    }

    public CRemoteView clone() {
         CRemoteView remoteView = new CRemoteView(getPackage(), getLayoutId());
         remoteView.mKey = mKey;
         return remoteView;
    }

    // @Override
    // public String toString() {
    //     return "CRemoteView{" +
    //             "mKey='" + mKey + '\'' +
    //             ", mApplication=" + mApplication +
    //             ", mLayoutId=" + mLayoutId +
    //             ", mActions=" + mActions +
    //             ", mLandscape=" + mLandscape +
    //             ", mPortrait=" + mPortrait +
    //             '}';
    // }
}
