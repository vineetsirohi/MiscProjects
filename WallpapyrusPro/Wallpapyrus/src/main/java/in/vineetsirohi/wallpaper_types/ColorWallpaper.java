package in.vineetsirohi.wallpaper_types;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import in.vineetsirohi.utility.AppConstants;
import in.vineetsirohi.wallpapyrus_lite.pro.SettingsActivity;

public class ColorWallpaper implements ILiveWallpaper {

    private int mWallpaperColor;

    private Paint mPaint;

    private SharedPreferences mPrefs;

    public ColorWallpaper(SharedPreferences prefs) {
        mPrefs = prefs;
        mPaint = new Paint();
        setColor();
    }

    private void setColor() {
        mWallpaperColor = mPrefs.getInt(SettingsActivity.PREFS_WALLPAPER_COLOR,
                SettingsActivity.DEFAULT_WALLPAPER_COLOR);
        Log.d(AppConstants.LOG_TAG,
                "in.vineetsirohi.wallpaper_types.ColorWallpaper.setColor " + mWallpaperColor);

        mPaint.setColor(mWallpaperColor);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(AppConstants.LOG_TAG, "in.vineetsirohi.wallpaper_types.ColorWallpaper.draw");

        canvas.drawPaint(mPaint);
    }


    @Override
    public void onSharedPreferenceChanged(String key) {
        if (key.equals(SettingsActivity.PREFS_WALLPAPER_COLOR)) {
            setColor();
        }

    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSurfaceDestroyed() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset,
            float xOffsetStep, float yOffsetStep, int xPixelOffset,
            int yPixelOffset) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public float getNoOfFramesPerSecond() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isScroll() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void recycle() {
        // TODO Auto-generated method stub

    }

}
