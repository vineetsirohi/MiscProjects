package in.vineetsirohi.wallpaper_types;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextPaint;

import in.vineetsirohi.wallpapyrus_lite.pro.WallpapyrusPreferences;

public class WallpaperFactory {

    private Context mContext;

    private TextPaint mPaint;

    private SharedPreferences mPrefs;

    private WallpapyrusPreferences mWallpapyrusPreferences;

    public WallpaperFactory(Context context, TextPaint paint) {
        this.mContext = context;
        mPaint = paint;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mWallpapyrusPreferences = new WallpapyrusPreferences(mContext);
    }

    public ILiveWallpaper getUserSelectedWallpaper(TextPaint paint) {
        ILiveWallpaper wallpaper = getWallpaperBasedOnPrefsSet();
        ILiveWallpaper clock = getClockBasedOnPrefsSet();
        return new WallpaperWithClock(mContext, wallpaper, clock, this);
    }

    public ILiveWallpaper getWallpaperBasedOnPrefsSet() {
        if (mWallpapyrusPreferences.isWallpaperTypeImage()) {
            return new SimpleImageWallpaper(mContext, mPaint,
                    mWallpapyrusPreferences.getLiveWallpaperSurfaceWidth(),
                    mWallpapyrusPreferences.getLiveWallpaperSurfaceHeight(),
                    0.5f);
        } else if (mWallpapyrusPreferences.isWallpaperTypeColor()) {
            return new ColorWallpaper(mPrefs);
        }
        return new MockWallpaper();
    }

    private ILiveWallpaper getClockBasedOnPrefsSet() {
        return getClock(mWallpapyrusPreferences.getClock(),
                mWallpapyrusPreferences.getLiveWallpaperSurfaceWidth(),
                mWallpapyrusPreferences.getLiveWallpaperSurfaceHeight());
    }

    public ILiveWallpaper getClock(String clockData, int surfaceWidth,
            int surfaceHeight) {
        IGetClock getClock = new ClockMakerFromPrefs(mContext, mPrefs,
                clockData, surfaceWidth, surfaceHeight);
        return getClock.getClock();
    }

}
