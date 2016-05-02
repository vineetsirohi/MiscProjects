package in.vineetsirohi.wallpapyrus_lite.pro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import in.vineetsirohi.utility.AppConstants;
import in.vineetsirohi.utility.LocalBroadcastUtility;
import in.vineetsirohi.utility.Utility;
import in.vineetsirohi.wallpaper_types.ILiveWallpaper;
import in.vineetsirohi.wallpaper_types.WallpaperFactory;

public class MyWallpaperService extends WallpaperService {

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    public class MyWallpaperEngine extends Engine {

        private final TextPaint mPaint = Utility.getPaintObject();

        private final Handler mHandler = new Handler();

        private final Runnable mDrawRunner = new Runnable() {
            @Override
            public void run() {
                drawFrame();
            }
        };

        private boolean mVisible;

        private ILiveWallpaper mWallpaper;

        private WallpaperFactory mWallpaperFactory;

        private WallpapyrusPreferences mWallpapyrusPreferences;

        public MyWallpaperEngine() {

            mWallpaperFactory = new WallpaperFactory(mContext, mPaint);
            mWallpaper = mWallpaperFactory.getUserSelectedWallpaper(mPaint);

            mWallpapyrusPreferences = new WallpapyrusPreferences(mContext);

            mHandler.removeCallbacks(mDrawRunner);
            mHandler.post(mDrawRunner);

            LocalBroadcastManager.getInstance(mContext).registerReceiver(mNewWallpaperReceiver,
                    new IntentFilter(LocalBroadcastUtility.WALLPAPER_UPDATED));

        }

        private void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            if (holder.getSurface().isValid()) {
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        mWallpaper.draw(canvas);
                    }
                } catch (Exception e) {

                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawRunner);
            if (mVisible) {
                mHandler.postDelayed(mDrawRunner, getFrameDrawDelay());
            }
        }

        /**
         * @return
         */
        private long getFrameDrawDelay() {
            if (mWallpaper.getNoOfFramesPerSecond() == 0) {
                return Long.MAX_VALUE;
            }
            return (long) (1000 / mWallpaper.getNoOfFramesPerSecond());
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            mWallpapyrusPreferences.setLiveWallpaperSurfaceWidthAndHeight(width, height);
            mWallpaper.onSurfaceChanged(width, height);
            drawFrame();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawRunner);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xOffsetStep, float yOffsetStep, int xPixelOffset,
                int yPixelOffset) {
            if (mWallpaper.isScroll()) {
                mWallpaper.onOffsetsChanged(xOffset, yOffset, xOffsetStep,
                        yOffsetStep, xPixelOffset, yPixelOffset);
                drawFrame();
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            mWallpaper.onTouchEvent(event);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawRunner);
            }
        }


        @Override
        public void onDestroy() {
            mHandler.removeCallbacks(mDrawRunner);
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mNewWallpaperReceiver);
            super.onDestroy();
        }

        private BroadcastReceiver mNewWallpaperReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String key = intent.getStringExtra(LocalBroadcastUtility.PREFERENCE_KEY);
                Log.d(AppConstants.LOG_TAG,
                        "in.vineetsirohi.wallpapyrus_lite.pro.MyWallpaperService.MyWallpaperEngine.onReceive"
                                + ": key " + key);

                mWallpaper.onSharedPreferenceChanged(key);
                drawFrame();
            }
        };

    }

}
