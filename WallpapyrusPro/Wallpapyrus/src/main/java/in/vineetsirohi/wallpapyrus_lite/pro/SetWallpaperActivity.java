package in.vineetsirohi.wallpapyrus_lite.pro;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

import in.vineetsirohi.utility.Utility;

public class SetWallpaperActivity extends Activity {

    private WallpapyrusPreferences mSetPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSetPreferences = new WallpapyrusPreferences(this);

        Utility.chooseWallpaper(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Utility.PICK_IMAGE
                && data != null) {
            Uri uri = data.getData();
            if (uri == null) {
                return;
            }

            String filePath = "";
            try {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                if (cursor != null) {
                    cursor.close();
                }
            } catch (NullPointerException e) {
                try {
                    filePath = uri.getPath();
                } catch (NullPointerException e1) {

                }
            }
            mSetPreferences.setWallpaperTypeAsImage();
            mSetPreferences.setWallpaperImageAddress(filePath);
        } else if (resultCode == RESULT_OK
                && requestCode == Utility.GALLERY_KITKAT_INTENT_CALLED && data != null) {
            Uri originalUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = Utility.getBitmapFromUri(originalUri, this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(Environment.getExternalStorageDirectory(),
                    Utility.WALLPAPER_KITKAT);
            boolean isSuccess = Utility.saveBitmapToFile(bitmap, file);
            if (isSuccess) {
                mSetPreferences.setWallpaperTypeAsImage();
                mSetPreferences.setWallpaperImageAddress(file.getAbsolutePath());
            } else {

            }
        }

        Utility.checkIfWallpapyrusNeedsToBeSetAsLiveWallpaper(this);
        finish();
    }

}
