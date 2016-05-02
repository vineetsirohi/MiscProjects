package in.vineetsirohi.wallpaper_types;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;

import in.vineetsirohi.wallpapyrus_lite.pro.SettingsActivity;

public class ClockMakerFromPrefs implements IGetClock {

    private Context mContext;

    private SharedPreferences mPrefs;

    private String clockData;

    private int surfaceWidth;

    private int surfaceHeight;

    ClockMakerFromPrefs(Context context, SharedPreferences prefs,
            String clockData, int surfaceWidth, int surfaceHeight) {
        this.mContext = context;
        mPrefs = prefs;
        this.clockData = clockData;
        this.surfaceWidth = surfaceWidth;
        this.surfaceHeight = surfaceHeight;
    }

    @Override
    public ILiveWallpaper getClock() {
        String[] data = clockData.split(SettingsActivity.SEPARATOR);
        String type = null;
        String packageName = null;

        if (data != null) {
            type = data[0];
            if (data.length > 1) {
                packageName = data[1];
            }
        }

//		if (type != null) {
//			if (type.equals(SettingsActivity.NO_CLOCK)) {
//				return new MockWallpaper();
//			} else if (type.equals(SettingsActivity.ANALOG_CLOCK)) {
//				// String packageName = "in.vineetsirohi.wallpapyrus_lite.pro";
//				return new WallpaperFactory(mContext, paint).getAnalogClock(
//						packageName, surfaceWidth, surfaceHeight);
//			} else if (type.equals(SettingsActivity.MARKET_CLOCK)) {
//				// String packageName =
//				// "in.vineetsirohi.wallpapyrus_pro.clock_1";
//				return new WallpaperFactory(mContext, paint).getAnalogClock(
//						packageName, surfaceWidth, surfaceHeight);
//			} else {
//				return new MockWallpaper();
//			}
//		} else {
//			return new MockWallpaper();
//		}
//

        if (type != null) {
            if (type.equals(SettingsActivity.NO_CLOCK)) {
                return new MockWallpaper();
            } else if (type.equals(SettingsActivity.ANALOG_CLOCK) || type
                    .equals(SettingsActivity.MARKET_CLOCK)) {
                return getAnalogClock(
                        packageName, surfaceWidth, surfaceHeight);
            } else {
                return new MockWallpaper();
            }
        } else {
            return new MockWallpaper();
        }


    }


    public ILiveWallpaper getAnalogClock(String packageName, int surfaceWidth,
            int surfaceHeight) {
        float scale = 0.5f;
        try {
            scale = Integer.valueOf(mPrefs.getString(
                    SettingsActivity.PREFS_CLOCK_SCALE, "50")) / 100f;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        float relativeX = 0.5f;
        try {
            relativeX = Integer.valueOf(mPrefs.getString(
                    SettingsActivity.PREFS_X_AS_PERCENT_OF_SCREEN_WIDTH, "50")) / 100f;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        float relativeY = .35f;
        try {
            relativeY = Integer.valueOf(mPrefs.getString(
                    SettingsActivity.PREFS_Y_AS_PERCENT_OF_SCREEN_HEIGHT, "50")) / 100f;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Resources resources = null;
        try {
            resources = mContext.getPackageManager()
                    .getResourcesForApplication(packageName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return new AnalogClock(mContext, packageName, surfaceWidth,
                surfaceHeight, scale, relativeX, relativeY);
    }


}
