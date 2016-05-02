package in.vineetsirohi.wallpapyrus_lite.pro;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import in.vineetsirohi.utility.AppConstants;
import in.vineetsirohi.utility.Utility;

public class PickWallpaperActivity extends Activity {

    private WallpapyrusPreferences mWpPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWpPrefs = new WallpapyrusPreferences(this);

        String imagePath = getImagePath(getIntent().getData());
        Log.d(AppConstants.LOG_TAG,
                "in.vineetsirohi.wallpapyrus_lite.pro.PickWallpaperActivity.onCreate" + ": path "
                        + imagePath);

        if (imagePath == null) {
            setContentView(R.layout.pick_wallpaper);
            findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            Utility.checkIfWallpapyrusNeedsToBeSetAsLiveWallpaper(this);
            mWpPrefs.setWallpaperTypeAsImage();
            mWpPrefs.setWallpaperImageAddress(imagePath);

            finish();
        }
    }

    private String getImagePath(Uri uri) {
        Log.d(AppConstants.LOG_TAG,
                "in.vineetsirohi.wallpapyrus_lite.pro.PickWallpaperActivity.getImagePath"
                        + ": uri : " + uri);

        if (uri.getScheme().equals("file")) {
            return uri.getPath();
        } else if (uri.getScheme().equals("content")) {
            return getRealPathFromURI(uri);
        }
        return null;
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
