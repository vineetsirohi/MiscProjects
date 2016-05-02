package in.vineetsirohi.wallpaper_types;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import in.vineetsirohi.wallpapyrus_lite.pro.SettingsActivity;
import in.vineetsirohi.wallpapyrus_lite.pro.WallpapyrusPreferences;

public class WallpaperWithClock implements ILiveWallpaper {

    private Context mContext;

    private ILiveWallpaper mWallpaper;

    private ILiveWallpaper mClock;

    private WallpaperFactory mWallpaperFactory;

    private int mSurfaceWidth;

    private int mSurfaceHeight;

    WallpaperWithClock(Context context, ILiveWallpaper imageWallpaper,
            ILiveWallpaper clock, WallpaperFactory wallpaperFactory) {
        super();
        mContext = context;
        mWallpaper = imageWallpaper;
        mClock = clock;
        mWallpaperFactory = wallpaperFactory;
    }

    @Override
    public void draw(Canvas canvas) {
        mWallpaper.draw(canvas);
        mClock.draw(canvas);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        mWallpaper.onSurfaceChanged(width, height);
        mClock.onSurfaceChanged(width, height);
    }

    @Override
    public void onSurfaceDestroyed() {
        mWallpaper.onSurfaceDestroyed();
        mClock.onSurfaceDestroyed();
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset,
            float xOffsetStep, float yOffsetStep, int xPixelOffset,
            int yPixelOffset) {
        mWallpaper.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
                xPixelOffset, yPixelOffset);
        mClock.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
                xPixelOffset, yPixelOffset);
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        mWallpaper.onVisibilityChanged(visible);
        mClock.onVisibilityChanged(visible);
    }

    @Override
    public void onDestroy() {
        mWallpaper.onDestroy();
        mClock.onDestroy();
    }

    @Override
    public float getNoOfFramesPerSecond() {
        return mClock.getNoOfFramesPerSecond();
    }

    @Override
    public void onSharedPreferenceChanged(String key) {
        WallpapyrusPreferences wpPrefs = new WallpapyrusPreferences(
                mContext);

        if (key.equals(SettingsActivity.PREFS_WALLPAPER_TYPE)) {
            setWallpaperBasedOnPrefs();
            return;
        }
        if (key.equals(SettingsActivity.PREFS_SELECT_CLOCK)) {
            mClock = mWallpaperFactory.getClock(wpPrefs.getClock(), mSurfaceWidth, mSurfaceHeight);
            return;
        }
        if (key.equals(SettingsActivity.PREFS_WALLPAPER_IMAGE)) {
            if (!(wpPrefs.isWallpaperTypeImage())) {
                wpPrefs.setWallpaperTypeAsImage();
                return;
            }
        }
        mWallpaper.onSharedPreferenceChanged(key);
        mClock.onSharedPreferenceChanged(key);
    }

    /**
     *
     */
    private void setWallpaperBasedOnPrefs() {
        mWallpaper.recycle();
        mWallpaper = null;
        mWallpaper = mWallpaperFactory.getWallpaperBasedOnPrefsSet();
    }

    @Override
    public boolean isScroll() {
        return mWallpaper.isScroll();
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        mWallpaper.onTouchEvent(event);
        mClock.onTouchEvent(event);
    }

    @Override
    public void recycle() {
        mWallpaper.recycle();
        mClock.recycle();
    }

}
