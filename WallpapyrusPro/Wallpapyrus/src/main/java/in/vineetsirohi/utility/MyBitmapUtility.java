package in.vineetsirohi.utility;

import android.graphics.Bitmap;

public class MyBitmapUtility {

	/**
	 * Recycles a bitmap and sets it to null
	 * 
	 * @param bitmap
	 */
	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null)
			bitmap.recycle();
		bitmap = null;
	}
}
