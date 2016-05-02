package in.vineetsirohi.wallpapyrus_lite.pro;

import com.larswerkman.holocolorpicker.ColorPicker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.vineetsirohi.utility.AppConstants;
import in.vineetsirohi.utility.ColorPickerAlertDialog;
import in.vineetsirohi.utility.MyColor;
import in.vineetsirohi.utility.Utility;

public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String WALLPAPYRUS_PRO_CLOCK_TYPE = "wallpapyrus_pro.clock.type";

    private static final String WALLPAPYRUS_PRO_CLOCK_PLUGIN = "wallpapyrus_pro.clock_plugin";

    public static final int PICK_IMAGE = 1;

    // mPrefs
    public static final String PREFS_WALLPAPER_TYPE = "selectWallpaperType";

    public static final String PREFS_WALLPAPER_IMAGE = "selectWallpaper";

    public static final String PREFS_WALLPAPER_COLOR = "selectWallpaperColor";

    public static final String PREFS_IS_WALLPAPER_SCROLL = "isWallpaperScroll";

    public static final String PREFS_SELECT_CLOCK = "selectClock";

    public static final String PREFS_CLOCK_TYPE = "clockType";

    public static final String PREFS_X_AS_PERCENT_OF_SCREEN_WIDTH = "xAsPercentOfScreenWidth";

    public static final String PREFS_Y_AS_PERCENT_OF_SCREEN_HEIGHT = "yAsPercentOfScreenHeight";

    public static final String PREFS_CLOCK_SCALE = "clockScale";

    // wallpaper types
    public static final String WALLPAPER_TYPE_IMAGE = "image";

    public static final String WALLPAPER_TYPE_COLOR = "color";

    // clock types
    public static final String NO_CLOCK = "no_clock";

    public static final String ANALOG_CLOCK = "analog_clock";

    public static final String MARKET_CLOCK = "market_clock";

    public static final String SEPARATOR = ";";

    public static final int DEFAULT_WALLPAPER_COLOR = 0xf43a3a;

    public static final String IS_PROMPT_FOR_SET_LIVEWALLPAPER = "is_prompt_for_set_livewallpaper";

    private ListPreference selectWallpaperTypesPrefs;

    private Preference selectWallpaperPrefs;

    private ListPreference selectClockPrefs;

    private SharedPreferences mPrefs;

    private WallpapyrusPreferences mSetPreferences;

    private Context mContext;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mContext = this;

        addPreferencesFromResource(R.xml.wallpapyrus_settings);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefs.registerOnSharedPreferenceChangeListener(this);

        mSetPreferences = new WallpapyrusPreferences(this);

        initializeSetWallpaperTypesPrefs();
        initializeSetWallpaperPrefs();
        initializeClockTypesPrefs();

    }

    /**
     *
     */
    private void initializeSetWallpaperTypesPrefs() {
        selectWallpaperTypesPrefs = (ListPreference) findPreference(PREFS_WALLPAPER_TYPE);
    }

    /**
     * @throws android.content.res.Resources.NotFoundException
     */
    private void initializeClockTypesPrefs() throws NotFoundException {
        List<WallpapyrusClockAppsInfo> wallpayrusClocksInfoList = listClockTypes();
        ClockAppsInfo clockAppsInfo = new ClockAppsInfo(
                wallpayrusClocksInfoList);
        selectClockPrefs = (ListPreference) findPreference(PREFS_SELECT_CLOCK);
        selectClockPrefs.setEntries(clockAppsInfo.getEntries());
        selectClockPrefs.setEntryValues(clockAppsInfo.getEntryValues());
    }

    /**
     * @return
     * @throws android.content.res.Resources.NotFoundException
     */
    private List<WallpapyrusClockAppsInfo> listClockTypes()
            throws NotFoundException {
        List<WallpapyrusClockAppsInfo> wallpayrusClockAppsInfoList
                = new ArrayList<WallpapyrusClockAppsInfo>();

        // add no clock
        wallpayrusClockAppsInfoList.add(new WallpapyrusClockAppsInfo(NO_CLOCK,
                "", getApplicationContext().getResources().getString(
                R.string.no_clock)));

        // add default clock
        wallpayrusClockAppsInfoList.add(new WallpapyrusClockAppsInfo(
                ANALOG_CLOCK, getApplicationContext().getPackageName(),
                getApplicationContext().getResources().getString(
                        R.string.default_clock)));

        // add third-party clocks
        List<ApplicationInfo> wallpapyrusClockAppsInfo = getThemePackages(getApplicationContext()
                .getPackageManager());
        for (ApplicationInfo appInfo : wallpapyrusClockAppsInfo) {
            try {
                wallpayrusClockAppsInfoList.add(new WallpapyrusClockAppsInfo(
                        (String) appInfo.metaData
                                .get(WALLPAPYRUS_PRO_CLOCK_TYPE),
                        appInfo.packageName, (String) appInfo
                        .loadLabel(getApplicationContext()
                                .createPackageContext(
                                        appInfo.packageName, 0)
                                .getPackageManager())));
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return wallpayrusClockAppsInfoList;
    }

    /**
     *
     */
    private void initializeSetWallpaperPrefs() {
        selectWallpaperPrefs = findPreference(PREFS_WALLPAPER_IMAGE);
        selectWallpaperPrefs
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                    public boolean onPreferenceClick(Preference preference) {
                        if (selectWallpaperTypesPrefs.getValue().equals(
                                WALLPAPER_TYPE_COLOR)) {
                            showColorSelector();
                        } else {
                            showImageSelector();
                        }
                        return true;
                    }
                });
    }

    private void showImageSelector() {
//                        Intent intent = new Intent();
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(
//                                Intent.createChooser(intent, ""), PICK_IMAGE);
        Utility.chooseWallpaper(this);
    }

    /**
     *
     */
    private void showColorSelector() {
        ColorPickerAlertDialog.create(mContext, mPrefs.getInt(PREFS_WALLPAPER_COLOR,
                DEFAULT_WALLPAPER_COLOR), new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                color = MyColor
                        .getColorWithoutAlpha(color);
                mSetPreferences.saveColorInPrefs(color);
            }
        }).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        String filePath = "";
//        if (resultCode == RESULT_OK && data != null) {
//            Uri selectedImage = data.getData();
//            if (selectedImage == null) {
//                return;
//            }
//            try {
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                filePath = cursor.getString(columnIndex);
//                if (cursor != null) {
//                    cursor.close();
//                }
//            } catch (NullPointerException e) {
//                try {
//                    filePath = selectedImage.getPath();
//                } catch (NullPointerException e1) {
//
//                }
//            }
//
//            mSetPreferences.setWallpaperImageAddress(filePath);
//            // Log.d("Wallpapyrus", "Data Recieved! " + filePath);
//        }

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
//            Utility.setWallpaperAddress(filePath, this);
            mSetPreferences.setWallpaperTypeAsImage();
            mSetPreferences.setWallpaperImageAddress(filePath);
        } else if (resultCode == RESULT_OK
                && requestCode == Utility.GALLERY_KITKAT_INTENT_CALLED && data != null) {
            Uri originalUri = data.getData();

            try {
                Bitmap bitmap = Utility.getBitmapFromUri(originalUri, this);
                File file = new File(Environment.getExternalStorageDirectory(),
                        Utility.WALLPAPER_KITKAT);
                boolean isSuccess = Utility.saveBitmapToFile(bitmap, file);
                if (isSuccess) {
//                    Utility.setWallpaperAddress(file.getAbsolutePath(), this);
                    mSetPreferences.setWallpaperTypeAsImage();
                    mSetPreferences.setWallpaperImageAddress(file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Utility.checkIfWallpapyrusNeedsToBeSetAsLiveWallpaper(this);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (getIntent().getBooleanExtra(IS_PROMPT_FOR_SET_LIVEWALLPAPER, false)) {
                Utility.checkIfWallpapyrusNeedsToBeSetAsLiveWallpaper(this);
            }
        } catch (NullPointerException e) {
//			e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        Log.d(AppConstants.LOG_TAG,
                "in.vineetsirohi.wallpapyrus_lite.pro.SettingsActivity.onSharedPreferenceChanged - "
                        + key);

        Utility.sendPrefsUpdateBroadcast(this, key);

    }

    // Inner class
    static class WallpapyrusClockAppsInfo {

        private String clockType;

        private String pkgName;

        private String appLabel;

        WallpapyrusClockAppsInfo(String clockType, String pkgName,
                String appLabel) {
            super();
            this.clockType = clockType;
            this.pkgName = pkgName;
            this.appLabel = appLabel;
        }

        String getClockType() {
            return clockType;
        }

        void setClockType(String clockType) {
            this.clockType = clockType;
        }

        String getPkgName() {
            return pkgName;
        }

        void setPkgName(String pkgName) {
            this.pkgName = pkgName;
        }

        private String getAppLabel() {
            return appLabel;
        }

        private void setAppLabel(String appLabel) {
            this.appLabel = appLabel;
        }

        String getEntry() {
            return this.appLabel;
        }

        String getEntryValue() {
            return this.clockType + SEPARATOR + this.pkgName;
        }
    }

    static class ClockAppsInfo {

        List<WallpapyrusClockAppsInfo> list;

        ClockAppsInfo(List<WallpapyrusClockAppsInfo> list) {
            super();
            this.list = list;
        }

        String[] getEntries() {
            String[] entries = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                entries[i] = list.get(i).getEntry();
            }
            return entries;
        }

        String[] getEntryValues() {
            String[] entryValues = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                entryValues[i] = list.get(i).getEntryValue();
            }
            return entryValues;
        }

    }

    private List<ApplicationInfo> getThemePackages(PackageManager pm) {
        List<ApplicationInfo> appinfolist = new ArrayList<ApplicationInfo>();
        List<ApplicationInfo> allapps = pm
                .getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo ai : allapps) {
            if (ai.metaData != null) {
                if (ai.metaData.containsKey(WALLPAPYRUS_PRO_CLOCK_PLUGIN)) {
                    appinfolist.add(ai);
                }
            }
        }
        return appinfolist;
    }
}
