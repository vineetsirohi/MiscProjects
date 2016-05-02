package in.vineetsirohi.utility;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

import in.vineetsirohi.wallpapyrus_lite.pro.R;

public class Utility {

    public static final int PICK_IMAGE = 1;

    public static final int GALLERY_KITKAT_INTENT_CALLED = 2;

    public static String WALLPAPER_KITKAT = "kitkat_wallpapyrus_pro.jpg";

    public static void checkIfWallpapyrusNeedsToBeSetAsLiveWallpaper(
            Context context) {
        if (context == null) {
            return;
        }

        // Check for app used as live wallpaper
        String wallpapyrusService = null;
        WallpaperInfo wallpaperInfo = WallpaperManager.getInstance(context)
                .getWallpaperInfo();
        if (wallpaperInfo != null) {
            wallpapyrusService = wallpaperInfo.getServiceName();
        }

        if (wallpapyrusService == null
                || !wallpapyrusService
                .equals("in.vineetsirohi.wallpapyrus_lite.pro.MyWallpaperService")) {
            Toast.makeText(context,
                    R.string.set_wallpapyrus_as_live_wallpaper_,
                    Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(
                    "android.service.wallpaper.LIVE_WALLPAPER_CHOOSER"));
        }
    }

    public static Bitmap getScaledBitmap(Bitmap bitmap, float scale) {
        if (bitmap == null) {
            return null;
        }

        return Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * scale),
                (int) (bitmap.getHeight() * scale), true);
    }

    public static TextPaint getPaintObject() {
        TextPaint paint = new TextPaint();
        paint.reset();
        paint.setAntiAlias(true);
        paint.setDither(false);
        paint.setFilterBitmap(true);
        paint.setSubpixelText(true);
        paint.setColor(Color.BLACK);
        return paint;
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, File file) {
        if (bitmap == null) {
            Log.d(AppConstants.LOG_TAG,
                    "in.vineetsirohi.utility.Utility.saveBitmapToFile" + ": bitmap is null");
            return false;
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void chooseWallpaper(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            activity.startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver()
                .openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor
                .getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

//    public static void setWallpaperAddress(String address, Context context) {
//        if (address == null) {
//            return;
//        }
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        editor.putString(SettingsActivity.PREFS_WALLPAPER_IMAGE, address);
//        editor.commit();
//
//        LocalBroadcastManager.getInstance(context).sendBroadcast(
//                LocalBroadcastUtility.wallpaperUpdatedIntent());
//    }


    public static void sendPrefsUpdateBroadcast(Context context, String key){
        Intent intent = LocalBroadcastUtility.wallpaperUpdatedIntent();
        intent.putExtra(LocalBroadcastUtility.PREFERENCE_KEY, key);
        LocalBroadcastManager.getInstance(context).sendBroadcast(
                intent);
    }

}
