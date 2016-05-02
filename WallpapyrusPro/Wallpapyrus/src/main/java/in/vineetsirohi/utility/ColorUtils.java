package in.vineetsirohi.utility;

import android.graphics.Color;

/**
 * Created by vineet on 26/10/13.
 */
public class ColorUtils {

    public static int getColorWithAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int getColorWithoutAlpha(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(red, green, blue);
    }
}
