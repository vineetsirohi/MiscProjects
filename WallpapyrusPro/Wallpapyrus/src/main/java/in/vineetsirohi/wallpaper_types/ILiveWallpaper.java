package in.vineetsirohi.wallpaper_types;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface ILiveWallpaper {

    public void draw(Canvas canvas);

    public void onSurfaceChanged(int width, int height);

    public void onSurfaceDestroyed();

    public void onOffsetsChanged(float xOffset, float yOffset,
            float xOffsetStep, float yOffsetStep, int xPixelOffset,
            int yPixelOffset);

    public void onVisibilityChanged(boolean visible);

    public void onDestroy();

    public float getNoOfFramesPerSecond();

    public void onSharedPreferenceChanged(String key);

    public boolean isScroll();

    public void onTouchEvent(MotionEvent event);

    public void recycle();
}
