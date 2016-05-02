package in.vineetsirohi.utility;

import com.fasterxml.jackson.core.type.TypeReference;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vineet on 22/11/13.
 */
public class ColorsCache {

    private static final String COLORS_FILE = "colors_cache_file";

    private static final int CACHE_SIZE = 10;

    private Context mContext;

    private List<Integer> mColors;

    public ColorsCache(Context context) {
        mContext = context;
        initColors();
    }

    private void initColors() {
        mColors = new ArrayList<Integer>();
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            mColors = CommonUtils.getNonFailingObjectMapper().readValue(
                    getColorsFile()
                    , new TypeReference<ArrayList<Integer>>() {
            });
        } catch (Exception ignore) {
            Log.d(AppConstants.LOG_TAG,
                    "in.vineetsirohi.customwidget.util.TaskerVariablesContainer" +
                            ".loadFromFile" + ": exception");
        }
        Log.d(AppConstants.LOG_TAG, "in.vineetsirohi.customwidget.util.TaskerVariablesContainer" +
                ".loadFromFile" + mColors.toString());

    }

    private File getColorsFile() {
        // TODO: change to internalMemory Location
        return new File(FileUtils.getWallpapyrusDir(), COLORS_FILE);
    }

    public void put(int color) {
        Log.d(AppConstants.LOG_TAG, "in.vineetsirohi.customwidget.util.TaskerVariablesContainer.put"
                + ": tasker variables:" + color);
        if (mColors.contains(color)) {
            return;
        }

        mColors.add(0, color);

        // remove extra colors
        if (mColors.size() > CACHE_SIZE) {
            for (int i = CACHE_SIZE; i < mColors.size(); i++) {
                mColors.remove(i);
            }
        }
        saveToFile();
    }

    private void saveToFile() {
        try {
            CommonUtils.getNonFailingObjectMapper().writeValue(getColorsFile(), mColors);
        } catch (IOException ignore) {
            Log.d(AppConstants.LOG_TAG,
                    "in.vineetsirohi.customwidget.util.TaskerVariablesContainer" +
                            ".saveToFile" + ": exception");
        }
    }

    public int[] get() {
        int[] array = new int[mColors.size()];
        for (int i = 0; i < mColors.size(); i++) {
            array[i] = mColors.get(i);
        }
        return array;
    }
}
