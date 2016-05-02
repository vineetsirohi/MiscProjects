package in.vineetsirohi.utility;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import in.vineetsirohi.wallpapyrus_lite.pro.MyApplication;
import in.vineetsirohi.wallpapyrus_lite.pro.R;


/**
 * Created by vineet on 20/11/13.
 */
public class ColorPickerAlertDialog {
    private static final int MAX_ALPHA = 255;
    public static final String MAX_ALPHA_HEX = "ff";
    private static final String HASH = "#";

    private static boolean mIsUpdateEditText;

    public static AlertDialog create(Context context, int initialColor
            , final ColorPicker.OnColorChangedListener listener) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.color_picker, null);

        mIsUpdateEditText = true;

        final ColorPicker picker = (ColorPicker) view.findViewById(R.id.picker);
        final SVBar svBar = (SVBar) view.findViewById(R.id.svbar);
        final EditText editText = (EditText) view.findViewById(R.id.editText);

        picker.addSVBar(svBar);
        picker.setColor(ColorUtils.getColorWithAlpha(initialColor, MAX_ALPHA));
        editText.setText(getHexColor(initialColor));

        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                listener.onColorChanged(ColorUtils.getColorWithoutAlpha(color));
                if (mIsUpdateEditText)
                    editText.setText(getHexColor(color));
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String color = new StringBuilder()
                        .append(HASH)
                        .append(MAX_ALPHA_HEX)
                        .append(s.toString())
                        .toString();

                mIsUpdateEditText = false;
                try {
                    picker.setColor(Color.parseColor(color));
                } catch (IllegalArgumentException e) {
                    // do nothing
                }
                mIsUpdateEditText = true;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setPositiveButton(context.getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.mColorsCache.put(picker.getColor());
            }
        });
        final AlertDialog alertDialog = builder.create();

        int[] colorCache = MyApplication.mColorsCache.get();
        if (colorCache != null && colorCache.length > 0) {
            final LinearLayout container = (LinearLayout) view.findViewById(R.id.colorCacheContainer);
            for (final int cacheColor : colorCache) {
                View child = new View(context);
                child.setBackgroundColor(cacheColor);
                child.setLayoutParams(getLayoutParamsForColorCacheViews(context));
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onColorChanged(ColorUtils.getColorWithoutAlpha(cacheColor));
                        alertDialog.dismiss();
                    }
                });

                container.addView(child);
            }
        }

        return alertDialog;
    }

    private static LinearLayout.LayoutParams getLayoutParamsForColorCacheViews(Context context) {
        int _36dp = (int) context.getResources().getDimension(R.dimen._36dp);
        int _48dp = (int) context.getResources().getDimension(R.dimen._48dp);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(_36dp, _48dp);
        int _8dp = (int) context.getResources().getDimension(R.dimen._8dp);
        layoutParams.setMargins(_8dp, 0, _8dp, 0);
        return layoutParams;
    }

    private static String getHexColor(int color) {
        return Integer.toHexString(color).substring(2);
    }
}
