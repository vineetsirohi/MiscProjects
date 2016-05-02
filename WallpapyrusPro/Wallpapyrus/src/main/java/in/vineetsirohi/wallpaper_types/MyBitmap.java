package in.vineetsirohi.wallpaper_types;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class MyBitmap {

	public static Bitmap getBitmapFromAssets(Context context,
			String assetsAddress, int maxWidth, int maxHeight) {
		if (context == null || assetsAddress == null || maxWidth == 0
				|| maxHeight == 0)
			return null;

		try {
			return getBitmapFromInputStream(
					context.getAssets().open(assetsAddress), maxWidth,
					maxHeight);
		} catch (IOException e) {
			return null;
		}
	}

	public static Bitmap getBitmapFromInputStream(InputStream inputStream,
			int maxWidth, int maxHeight) {
		if (inputStream == null || maxWidth == 0 || maxHeight == 0)
			return null;

		Bitmap bitmap = null;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream, null, opts);
		setupBitmapOptionsWithLimits(opts, maxWidth, maxHeight);
		bitmap = BitmapFactory.decodeStream(inputStream, null, opts);
		// Log.d("Wallpapyrus", "MyBitmap.getBitmapFromInputStream()"
		// + " : ");

		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	private static void setupBitmapOptionsWithLimits(Options opts,
			int maxWidthLimit, int maxHeightLimit) {
		opts.inJustDecodeBounds = false;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opts.inScaled = false;
		opts.inDither = true;

		int productOfDimensions = opts.outWidth * opts.outHeight;
		int maxlimit = maxWidthLimit * maxHeightLimit;

		opts.inSampleSize = (int) (productOfDimensions * 1f / maxlimit);
	}

	public static Bitmap getBitmapFromFile(String fileName, int maxWidth,
			int maxHeight) {
		if (fileName == null)
			return null;

		if (((new File(fileName)).exists())) {
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, opts);
			if(maxWidth == 0 || maxHeight == 0){
				maxWidth = opts.outWidth;
				maxHeight = opts.outHeight;
			}
			setupBitmapOptionsWithLimits(opts, maxWidth, maxHeight);
			return BitmapFactory.decodeFile(fileName, opts);
		}
		return null;
	}
}
