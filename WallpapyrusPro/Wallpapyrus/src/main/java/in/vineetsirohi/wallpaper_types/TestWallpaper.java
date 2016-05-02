package in.vineetsirohi.wallpaper_types;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff;
import android.text.TextPaint;
import android.view.MotionEvent;

public class TestWallpaper implements ILiveWallpaper {

	private TextPaint paint;

	private int counter;
	private float offset;
	private int surfaceWidth;
	private int surfaceHeight;
	private int centerX;
	private int centerY;
	private int surfaceDestroyedCounter;

	public TestWallpaper(TextPaint paint) {
		this.paint = paint;
		this.paint.setTextAlign(Align.CENTER);
		this.paint.setTextSize(32);
		counter = 0;
		surfaceDestroyedCounter = 0;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		canvas.drawColor(Color.WHITE);
		
		drawText(canvas, 0, "Wallpapyrus " + counter++);
		drawText(canvas, 1, "Surface " + surfaceWidth + " " + surfaceHeight);
		drawText(canvas, 2, "Offset " + offset);
		drawText(canvas, 3, "surfaceDestroyedCounter " + surfaceDestroyedCounter);
	}

	/**
	 * @param canvas
	 * @param heightOffsetMultiple
	 *            TODO
	 * @param text
	 *            TODO
	 */
	private void drawText(Canvas canvas, int heightOffsetMultiple, String text) {
		if (text == null) {
			return;
		}
		canvas.drawText(text, centerX, centerY + paint.getTextSize()
				* heightOffsetMultiple, paint);
	}

	@Override
	public void onSurfaceChanged(int width, int height) {
		surfaceWidth = width;
		surfaceHeight = height;
		centerX = width / 2;
		centerY = height / 2;
	}

	@Override
	public void onOffsetsChanged(float xOffset, float yOffset,
			float xOffsetStep, float yOffsetStep, int xPixelOffset,
			int yPixelOffset) {
		offset = xOffset;
	}

	@Override
	public void onVisibilityChanged(boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSurfaceDestroyed() {
		surfaceDestroyedCounter++;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public float getNoOfFramesPerSecond() {
		return 30f;
	}

	@Override
	public void onSharedPreferenceChanged(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isScroll() {
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
