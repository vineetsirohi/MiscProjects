package in.vineetsirohi.wallpaper_types;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.preference.PreferenceManager;
import android.text.TextPaint;
import android.view.MotionEvent;

import java.util.Calendar;

import in.vineetsirohi.utility.MyBitmapUtility;
import in.vineetsirohi.utility.Utility;
import in.vineetsirohi.wallpapyrus_lite.pro.SettingsActivity;

public class AnalogClock implements ILiveWallpaper {

    private final Context mContext;

    // instance members
    private TextPaint mTextPaint;

    private boolean mIsTouchEvent;

    private Bitmap base;

    private Bitmap hourBitmap;

    private Bitmap minuteBitmap;

    private Bitmap secondBitmap;

    private Bitmap mask;

    private Matrix transformBase;

    private Matrix transformHour;

    private Matrix transformMinute;

    private Matrix transformSecond;

    private Matrix transformMask;

    private String mPackageName;

//    private Resources resources;

    private float scale;

    private float relativeX;

    private float relativeY;

    private int x;

    private int y;

    private int baseX;

    private int baseY;

    private int hourX;

    private int hourY;

    private int minuteX;

    private int minuteY;

    private int secondX;

    private int secondY;

    private int maskX;

    private int maskY;

    private int hourBitmapHalfWidth;

    private int hourBitmapHalfHeight;

    private int minuteBitmapHalfWidth;

    private int minuteBitmapHalfHeight;

    private int secondBitmapHalfWidth;

    private int secondBitmapHalfHeight;

    private int surfaceWidth;

    private int surfaceHeight;

    AnalogClock(Context context, String packageName, int surfaceWidth,
            int surfaceHeight, float scale, float relativeX, float relativeY) {
        mContext = context;

        this.mPackageName = packageName;

        this.surfaceWidth = surfaceWidth;
        this.surfaceHeight = surfaceHeight;
        this.scale = scale;

        mTextPaint = Utility.getPaintObject();
        mTextPaint.setColor(Color.WHITE);

        loadBitmaps(scale);

        transformBase = new Matrix();
        transformHour = new Matrix();
        transformMinute = new Matrix();
        transformSecond = new Matrix();
        transformMask = new Matrix();

        this.setRelativeX(relativeX);
        this.setRelativeY(relativeY);
    }

    /**
     * @param scale
     */
    private void loadBitmaps(float scale) {
        Resources resources = null;
        try {
            resources = mContext.getPackageManager().getResourcesForApplication(mPackageName);
        } catch (PackageManager.NameNotFoundException e) {
            return;
        }
        if (resources == null || mPackageName == null) {
            return;
        }

        // base = BitmapFactory.decodeResource(resources, R.raw.clock_base);
        base = BitmapFactory.decodeResource(resources,
                resources.getIdentifier("clock_base", "raw", mPackageName));
        base = Utility.getScaledBitmap(base, scale);

        // hourBitmap = BitmapFactory.decodeResource(resources,
        // R.raw.hour_hand);
        hourBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier("hour_hand", "raw", mPackageName));
        hourBitmap = Utility.getScaledBitmap(hourBitmap, scale);
        if (hourBitmap != null) {
            hourBitmapHalfWidth = hourBitmap.getWidth() / 2;
            hourBitmapHalfHeight = hourBitmap.getHeight() / 2;
        }

        // minuteBitmap = BitmapFactory.decodeResource(resources,
        // R.raw.minute_hand);
        minuteBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier("minute_hand", "raw", mPackageName));
        minuteBitmap = Utility.getScaledBitmap(minuteBitmap, scale);
        if (minuteBitmap != null) {
            minuteBitmapHalfWidth = minuteBitmap.getWidth() / 2;
            minuteBitmapHalfHeight = minuteBitmap.getHeight() / 2;
        }

        // secondBitmap = BitmapFactory.decodeResource(resources,
        // R.raw.second_hand);
        secondBitmap = BitmapFactory.decodeResource(resources,
                resources.getIdentifier("second_hand", "raw", mPackageName));
        secondBitmap = Utility.getScaledBitmap(secondBitmap, scale);

        if (secondBitmap != null) {
            secondBitmapHalfWidth = secondBitmap.getWidth() / 2;
            secondBitmapHalfHeight = secondBitmap.getHeight() / 2;
        }

        // mask = BitmapFactory.decodeResource(resources, R.raw.mask);
        mask = BitmapFactory.decodeResource(resources,
                resources.getIdentifier("mask", "raw", mPackageName));
        mask = Utility.getScaledBitmap(mask, scale);

        updateBitmapLocations();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mIsTouchEvent) {
            mIsTouchEvent = false;
        } else {
            updateClockData();
        }

        if (base != null) {
            canvas.drawBitmap(base, transformBase, mTextPaint);
        }
        if (hourBitmap != null) {
            canvas.drawBitmap(hourBitmap, transformHour, mTextPaint);
        }
        if (minuteBitmap != null) {
            canvas.drawBitmap(minuteBitmap, transformMinute, mTextPaint);
        }
        if (secondBitmap != null) {
            canvas.drawBitmap(secondBitmap, transformSecond, mTextPaint);
        }
        if (mask != null) {
            canvas.drawBitmap(mask, transformMask, mTextPaint);
        }
    }

    /**
     *
     */
    private void updateClockData() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        updateBaseTransform();
        updateHourTransform(hour, minute);
        updateMinuteTransform(minute);
        updateSecondTransform(second);
        updateMaskTransform();
    }

    /**
     *
     */
    private void updateMaskTransform() {
        transformMask.reset();
        // transformMask.preScale(scale, scale);
        transformMask.postTranslate(maskX, maskY);
    }

    /**
     * @param second
     */
    private void updateSecondTransform(int second) {
        transformSecond.reset();
        // transformSecond.preScale(scale, scale);
        transformSecond.preRotate(getMinuteOrSecondAngle(second),
                secondBitmapHalfWidth, secondBitmapHalfHeight);
        transformSecond.postTranslate(secondX, secondY);
    }

    /**
     * @param minute
     */
    private void updateMinuteTransform(int minute) {
        transformMinute.reset();
        // transformMinute.preScale(scale, scale);
        transformMinute.preRotate(getMinuteOrSecondAngle(minute),
                minuteBitmapHalfWidth, minuteBitmapHalfHeight);
        transformMinute.postTranslate(minuteX, minuteY);
    }

    /**
     * @param hour
     * @param minute
     */
    private void updateHourTransform(int hour, int minute) {
        transformHour.reset();
        // transformHour.preScale(scale, scale);
        transformHour.preRotate(getHourAngle(hour, minute),
                hourBitmapHalfWidth, hourBitmapHalfHeight);
        transformHour.postTranslate(hourX, hourY);
    }

    /**
     *
     */
    private void updateBaseTransform() {
        transformBase.reset();
        // transformBase.preScale(scale, scale);
        transformBase.postTranslate(baseX, baseY);
    }

    private int getHourAngle(int hour, int minute) {
        return 30 * hour + (minute / 2) - 90;
    }

    private int getMinuteOrSecondAngle(int value) {
        return 6 * value - 90;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        surfaceWidth = width;
        surfaceHeight = height;
        updateBitmapLocations();
    }

    /**
     *
     */
    private void updateBitmapLocations() {
        x = (int) (surfaceWidth * getRelativeX());
        y = (int) (surfaceHeight * getRelativeY());

        try {
            int referenceX = 0;
            int referenceY = 0;

            // to draw clock with base top left reference
            // referenceX = base.getWidth() / 2;
            // referenceY = base.getHeight() / 2;

            baseX = (-base.getWidth() / 2 + x + referenceX);
            baseY = (-base.getHeight() / 2 + y + referenceY);

            hourX = (-hourBitmapHalfWidth + x + referenceX);
            hourY = (-hourBitmapHalfHeight + y + referenceY);

            minuteX = (-minuteBitmapHalfWidth + x + referenceX);
            minuteY = (-minuteBitmapHalfHeight + y + referenceY);

            secondX = (-secondBitmap.getWidth() / 2 + x + referenceX);
            secondY = (-secondBitmap.getHeight() / 2 + y + referenceY);

            maskX = (-mask.getWidth() / 2 + x + referenceX);
            maskY = (-mask.getHeight() / 2 + y + referenceY);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceDestroyed() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset,
            float xOffsetStep, float yOffsetStep, int xPixelOffset,
            int yPixelOffset) {

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
        return 1f;
    }

    @Override
    public void onSharedPreferenceChanged(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (key.equals(SettingsActivity.PREFS_X_AS_PERCENT_OF_SCREEN_WIDTH)) {
            setRelativeXFromPrefs(prefs);
        } else if (key
                .equals(SettingsActivity.PREFS_Y_AS_PERCENT_OF_SCREEN_HEIGHT)) {
            setRelativeYFromPrefs(prefs);
        } else if (key.equals(SettingsActivity.PREFS_CLOCK_SCALE)) {
            setClockScaleFromPrefs(prefs);
        }
    }

    private void setClockScaleFromPrefs(SharedPreferences prefs) {
        try {
            setScale(Integer.valueOf(prefs.getString(
                    SettingsActivity.PREFS_CLOCK_SCALE, "50")) / 100f);
            loadBitmaps(scale);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param prefs
     */
    private void setRelativeYFromPrefs(SharedPreferences prefs) {
        try {
            setRelativeY(Integer.valueOf(prefs.getString(
                    SettingsActivity.PREFS_Y_AS_PERCENT_OF_SCREEN_HEIGHT,
                    "35")) / 100f);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param prefs
     */
    private void setRelativeXFromPrefs(SharedPreferences prefs) {
        try {
            setRelativeX(Integer.valueOf(prefs.getString(
                    SettingsActivity.PREFS_X_AS_PERCENT_OF_SCREEN_WIDTH,
                    "50")) / 100f);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isScroll() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        mIsTouchEvent = true;
    }

    private float getRelativeX() {
        return relativeX;
    }

    private void setRelativeX(float relativeX) {
        if (relativeX < 0) {
            relativeX = 0;
        }
        if (relativeX > 1) {
            relativeX = 1;
        }
        this.relativeX = relativeX;
        updateBitmapLocations();
    }

    private float getRelativeY() {
        return relativeY;
    }

    private void setRelativeY(float relativeY) {
        if (relativeY < 0) {
            relativeY = 0;
        }
        if (relativeY > 1) {
            relativeY = 1;
        }
        this.relativeY = relativeY;
        updateBitmapLocations();
    }

    private float getScale() {
        return scale;
    }

    private void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public void recycle() {
        MyBitmapUtility.recycleBitmap(base);
        MyBitmapUtility.recycleBitmap(hourBitmap);
        MyBitmapUtility.recycleBitmap(minuteBitmap);
        MyBitmapUtility.recycleBitmap(secondBitmap);
        MyBitmapUtility.recycleBitmap(mask);
    }

}
