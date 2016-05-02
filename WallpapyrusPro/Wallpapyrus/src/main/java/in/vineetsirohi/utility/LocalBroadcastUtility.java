package in.vineetsirohi.utility;

import android.content.Intent;

/**
 * Created by vineet on 12/12/13.
 */
public class LocalBroadcastUtility {

    public static final String WALLPAPER_UPDATED = "wallpaper_updated";
    public static final String PREFERENCE_KEY = "preference_key";

    public static Intent wallpaperUpdatedIntent(){
        return new Intent(WALLPAPER_UPDATED);
    }

}
