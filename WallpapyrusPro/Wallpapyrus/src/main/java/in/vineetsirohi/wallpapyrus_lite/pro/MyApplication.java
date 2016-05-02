package in.vineetsirohi.wallpapyrus_lite.pro;

import android.app.Application;

import in.vineetsirohi.utility.ColorsCache;


/**
 * Created by vineet on 11/10/13.
 */
public class MyApplication extends Application {

    public static MyApplication mContext;

    public static ColorsCache mColorsCache; // static but requires context

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        mColorsCache = new ColorsCache(this);

    }



}
