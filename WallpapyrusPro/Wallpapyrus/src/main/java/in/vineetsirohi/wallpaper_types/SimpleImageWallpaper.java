package in.vineetsirohi.wallpaper_types;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import in.vineetsirohi.utility.AppConstants;
import in.vineetsirohi.utility.MyBitmapUtility;
import in.vineetsirohi.wallpapyrus_lite.pro.R;
import in.vineetsirohi.wallpapyrus_lite.pro.SettingsActivity;

public class SimpleImageWallpaper implements ILiveWallpaper {

    public static final String WALLPAPER_IN_CACHE = "wallpaper";

    private static final int COMPRESSION = 100;

    private TextPaint mPaint;

    private Context mContext;

    private Bitmap mBitmap;

    private Bitmap mOriginalBitmap;

    private int mSurfaceWidth;

    private int mSurfaceHeight;

    private float mXOffset;

    private int mBitmapScaledWidth;

    private int mBitmapScaledHeight;

    private SharedPreferences mPrefs;

    private boolean isScroll;

    public SimpleImageWallpaper(Context context, TextPaint paint, int surfaceWidth,
            int surfaceHeight, float xOffset) {
        mPaint = paint;
        mContext = context;
        mXOffset = xOffset;

        mSurfaceWidth = surfaceWidth;
        mSurfaceHeight = surfaceHeight;

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        setWallpaperScroll();

        mBitmapScaledWidth = 960;
        mBitmapScaledHeight = 1600;
        loadNewWallpaper();

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, -(mBitmap.getWidth() - mSurfaceWidth)
                    * mXOffset, 0, mPaint);
        } else {
            new DefaultWallpaper(mContext, mSurfaceWidth, mPaint).draw(canvas);
        }

    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        setBitmapScale();
        createScaledBitmap();
    }

    @Override
    public void onSurfaceDestroyed() {

    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset,
            float xOffsetStep, float yOffsetStep, int xPixelOffset,
            int yPixelOffset) {
        if (!isScroll) {
            this.mXOffset = 0.5f;
            return;
        }
        this.mXOffset = xOffset;
    }

    @Override
    public void onVisibilityChanged(boolean visible) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public float getNoOfFramesPerSecond() {
        return 0;
    }

    @Override
    public void onSharedPreferenceChanged(String key) {
        if (key.equals(SettingsActivity.PREFS_WALLPAPER_IMAGE)) {
            Log.d(AppConstants.LOG_TAG,
                    "in.vineetsirohi.wallpaper_types.SimpleImageWallpaper.onSharedPreferenceChanged");
            
            loadNewWallpaper();
        } else if (key.equals(SettingsActivity.PREFS_IS_WALLPAPER_SCROLL)) {
            // Log.d(AppConstants.LOG_TAG,
            // "WallpaperWithClock.onSharedPreferenceChanged()" +
            // " : wallpaper scroll");
            setWallpaperScroll();
            if (!isScroll) {
                this.mXOffset = 0.5f;
            }
        }
    }

    /**
     *
     */
    private void setWallpaperScroll() {
        isScroll = mPrefs.getBoolean(
                SettingsActivity.PREFS_IS_WALLPAPER_SCROLL, true);
    }

    /**
     *
     */
    private void loadNewWallpaper() {
        String wallpaperAddress = mPrefs.getString(
                SettingsActivity.PREFS_WALLPAPER_IMAGE, null);

        if (wallpaperAddress == null) {
            readBitmapFromCache();
        } else {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)
                    || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                mOriginalBitmap = MyBitmap.getBitmapFromFile(wallpaperAddress,
                        mBitmapScaledWidth, mBitmapScaledHeight);
            }
            if (mOriginalBitmap != null) {
                saveWallpaperInCache();
            } else {
                readBitmapFromCache();
            }
        }
        setBitmapScale();
        createScaledBitmap();
    }

    private void readBitmapFromCache() {
        File file = new File(mContext.getFilesDir(), WALLPAPER_IN_CACHE);
        if (file.exists()) {
            // Log.d("Wallpapyrus", "WallpaperWithClock.readBitmapFromCache()"
            // + " : ");
            try {
                mOriginalBitmap = MyBitmap.getBitmapFromFile(file.toString(),
                        mBitmapScaledWidth, mBitmapScaledHeight);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                Toast.makeText(mContext, R.string.wallpapyrus_load_error,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveWallpaperInCache() {
        if (mOriginalBitmap == null) {
            return;
        }

        FileOutputStream fos = null;
        try {
            fos = mContext.openFileOutput(WALLPAPER_IN_CACHE,
                    Context.MODE_PRIVATE);
            mOriginalBitmap.compress(CompressFormat.JPEG, COMPRESSION, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *
     */
    private void createScaledBitmap() {
        if (mOriginalBitmap == null
                || (mBitmapScaledWidth <= 0 || mBitmapScaledHeight <= 0)) {
            return;
        }

        mBitmap = Bitmap.createScaledBitmap(mOriginalBitmap,
                mBitmapScaledWidth, mBitmapScaledHeight, true);
    }

    private void setBitmapScale() {
        if (mOriginalBitmap == null || mSurfaceWidth == 0
                || mSurfaceHeight == 0 || mOriginalBitmap.getWidth() == 0
                || mOriginalBitmap.getHeight() == 0) {
            return;
        }

        if ((mSurfaceWidth / mSurfaceHeight) <= (mOriginalBitmap.getWidth() / mOriginalBitmap
                .getHeight())) {
            mBitmapScaledHeight = mSurfaceHeight;
            mBitmapScaledWidth = mBitmapScaledHeight
                    * mOriginalBitmap.getWidth() / mOriginalBitmap.getHeight();
            if (mBitmapScaledWidth < mSurfaceWidth) {
                mBitmapScaledWidth = mSurfaceWidth;
                mBitmapScaledHeight = mBitmapScaledWidth
                        * mOriginalBitmap.getHeight()
                        / mOriginalBitmap.getWidth();
            }
        } else {
            mBitmapScaledWidth = mSurfaceWidth;
            mBitmapScaledHeight = mBitmapScaledWidth
                    * mOriginalBitmap.getHeight() / mOriginalBitmap.getWidth();
            if (mBitmapScaledHeight < mSurfaceHeight) {
                mBitmapScaledHeight = mSurfaceHeight;
                mBitmapScaledWidth = mBitmapScaledHeight
                        * mOriginalBitmap.getWidth()
                        / mOriginalBitmap.getHeight();
            }
        }
    }

    @Override
    public boolean isScroll() {
        return isScroll;
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void recycle() {
        MyBitmapUtility.recycleBitmap(mBitmap);
        MyBitmapUtility.recycleBitmap(mOriginalBitmap);
    }


}
