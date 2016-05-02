package in.vineetsirohi.utility;


import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class MyColor {

	/**
	 * @param color
	 * @return
	 */
	public static int getColorWithoutAlpha(int color) {
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		color = Color.rgb(red, green, blue);
		return color;
	}

	/**
	 * @param color
	 * @param alpha
	 * @return
	 */
	public static int getColorWithAlpha(int color, int alpha) {
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return Color.argb(alpha, red, green, blue);
	}

	public static List<Integer> getRecentColors(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String colors = prefs.getString(AppConstants.RECENT_COLORS_LIST, "");
		String[] colorsArray = colors.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for(String s: colorsArray){
			try {
				list.add(Integer.valueOf(s));
			} catch (NumberFormatException e) {
//				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	public static void saveColorToRecentsList(Integer color, Context context){
		List<Integer> recentColors = getRecentColors(context);
		if (recentColors.contains(color)) {
			return;
		}
		recentColors.add(0, color);
		if (recentColors.size() > 5) {
			for (int i = 5; i < recentColors.size(); i++)
				recentColors.remove(i);
		}
		saveRecentColors(recentColors, context);
	}
	
	public static void saveRecentColors(List<Integer> colors, Context context){
		saveRecentColors(colors, PreferenceManager.getDefaultSharedPreferences(context).edit());
	}
	
	public static void saveRecentColors(List<Integer> colors, Editor editor){
		StringBuilder colorsString = new StringBuilder();
		for(int c: colors){
			colorsString.append(String.valueOf(c));
			colorsString.append(",");
		}
		editor.putString(AppConstants.RECENT_COLORS_LIST, colorsString.toString());
		editor.commit();
	}
}
