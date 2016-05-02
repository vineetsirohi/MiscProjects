package in.vineetsirohi.wallpaper_types;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.text.TextPaint;
import android.view.MotionEvent;

import in.vineetsirohi.wallpapyrus_lite.pro.R;

public class DefaultWallpaper implements ILiveWallpaper {

	private Context mContext;
	private int mSurfaceWidth;
	private TextPaint mPaint;

	public DefaultWallpaper(Context context, int surfaceWidth, TextPaint paint) {
		super();
		this.mContext = context;
		this.mSurfaceWidth = surfaceWidth;
		mPaint = paint;
	}

	@Override
	public void draw(Canvas canvas) {
		// draw default wallpaper
		canvas.drawColor(Color.WHITE);
		Bitmap bitmapTemp = BitmapFactory.decodeResource(
				mContext.getResources(), R.drawable.ic_launcher);
		canvas.drawBitmap(bitmapTemp,
				-(bitmapTemp.getWidth() - mSurfaceWidth) * 0.5f,
				-bitmapTemp.getHeight() / 2 + canvas.getHeight() / 2, mPaint);

		TextPaint textPaint = new TextPaint();
		textPaint.reset();
		textPaint.setAntiAlias(true);
		textPaint.setDither(true);
		textPaint.setSubpixelText(true);
		textPaint.setColor(Color.BLACK);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(25);
		canvas.drawText(mContext.getString(R.string.select_wallpaper_),
				canvas.getWidth() / 2,
				bitmapTemp.getHeight() / 2 + canvas.getHeight() / 2 + 30,
				textPaint);
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
