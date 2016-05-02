package in.vineetsirohi.utility;

import android.os.Environment;

import java.io.File;


/**
 * Created by vineet on 04/10/13.
 */
public class FileUtils {



    public static File getWallpapyrusDir() {
        return new File(Environment.getExternalStorageDirectory(), ".wallpapyrus");
    }


}
