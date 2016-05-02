package in.vineetsirohi.wallpapyrus_lite.pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import in.vineetsirohi.utility.Utility;

public class WallpapyrusPreferences {

    private static final String PREFS_LIVE_WALLPAPER_SURFACE_WIDTH = "live_wallpaper_surface_width";

    private static final String PREFS_LIVE_WALLPAPER_SURFACE_HEIGHT
            = "live_wallpaper_surface_height";

    private final Context mContext;

    private SharedPreferences mPrefs;

    private SharedPreferences.Editor mEditor;

    public WallpapyrusPreferences(Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void setWallpaperImageAddress(String address) {
        String key = SettingsActivity.PREFS_WALLPAPER_IMAGE;
        mEditor.putString(key, address);
        commit(key);
    }

    private void commit(String key) {
        mEditor.commit();
        Utility.sendPrefsUpdateBroadcast(mContext, key);
    }

    public void saveColorInPrefs(int color) {
        String key = SettingsActivity.PREFS_WALLPAPER_COLOR;
        mEditor.putInt(key, color);
        commit(key);
    }

    public void setWallpaperTypeAsImage() {
        String key = SettingsActivity.PREFS_WALLPAPER_TYPE;
        mEditor.putString(key, SettingsActivity.WALLPAPER_TYPE_IMAGE);
        commit(key);
    }

    public boolean isWallpaperTypeImage() {
        return mPrefs.getString(SettingsActivity.PREFS_WALLPAPER_TYPE,
                SettingsActivity.WALLPAPER_TYPE_IMAGE).equals(
                SettingsActivity.WALLPAPER_TYPE_IMAGE);
    }

    public boolean isWallpaperTypeColor() {
        return mPrefs.getString(SettingsActivity.PREFS_WALLPAPER_TYPE,
                SettingsActivity.WALLPAPER_TYPE_IMAGE).equals(
                SettingsActivity.WALLPAPER_TYPE_COLOR);
    }

    public String getClock() {
        return mPrefs.getString(SettingsActivity.PREFS_SELECT_CLOCK,
                SettingsActivity.NO_CLOCK);
    }

    public int getLiveWallpaperSurfaceWidth() {
        return mPrefs.getInt(PREFS_LIVE_WALLPAPER_SURFACE_WIDTH, 0);
    }

    public void setLiveWallpaperSurfaceWidth(int surfaceWidth) {
        String key = PREFS_LIVE_WALLPAPER_SURFACE_WIDTH;
        mEditor.putInt(key, surfaceWidth);
        commit(key);
    }

    public int getLiveWallpaperSurfaceHeight() {
        return mPrefs.getInt(PREFS_LIVE_WALLPAPER_SURFACE_HEIGHT, 0);
    }

    public void setLiveWallpaperSurfaceHeight(int surfaceHeight) {
        String key = PREFS_LIVE_WALLPAPER_SURFACE_HEIGHT;
        mEditor.putInt(key, surfaceHeight);
        commit(key);
    }

    public void setLiveWallpaperSurfaceWidthAndHeight(int width, int height) {
        setLiveWallpaperSurfaceWidth(width);
        setLiveWallpaperSurfaceHeight(height);
    }

    public String getImageWallpaperAddress() {
        return mPrefs.getString(SettingsActivity.PREFS_WALLPAPER_IMAGE,
                null);
    }
}
