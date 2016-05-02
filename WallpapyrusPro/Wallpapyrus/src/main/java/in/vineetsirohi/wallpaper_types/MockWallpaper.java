package in.vineetsirohi.wallpaper_types;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class MockWallpaper implements ILiveWallpaper{

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
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
		return 0.01f;
	}

	@Override
	public void onSharedPreferenceChanged(String key) {
		// TODO Auto-generated method stub
		
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
