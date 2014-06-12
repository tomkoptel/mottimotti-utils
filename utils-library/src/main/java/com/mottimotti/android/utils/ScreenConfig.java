package com.mottimotti.android.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.Locale;

public class ScreenConfig {
    private static final String TAG = ScreenConfig.class.getSimpleName();

    private ScreenConfig() {}

    public static void generateSceenStatistics(Application application) {
        Resources resources = application.getResources();
        int screenLayout = (resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);

        WindowManager wm = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        int[] sizes = getDisplaySize(wm);
        String widthHeight = String.format(Locale.getDefault(), "%dx%d", sizes[0], sizes[1]);

        String screenLayoutSize = "UNDEFINED";
        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                screenLayoutSize = "XLARGE";
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                screenLayoutSize = "LARGE";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                screenLayoutSize = "NORMAL";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                screenLayoutSize = "SMALL";
                break;
        }

        float density = resources.getDisplayMetrics().density;
        String densityValue = "UNDEFINED";
        if (density == 0.75f) {
            densityValue = "0.75 ldpi";
        } else if(density == 1.0f) {
            densityValue = "1.0 mdpi";
        } else if (density == 1.5f) {
            densityValue = "1.5 hdpi";
        } else if (density == 2.0f) {
            densityValue = "2.0 xhdpi";
        } else if (density == 3.0f) {
            densityValue = "3.0 xxhdpi";
        } else if (density == 4.0f) {
            densityValue = "4.0 xxxhdpi";
        }

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(x+y);

        Log.d(TAG, String.format("Device metrics: WidthxHeight %s ScreenLayout %s densityValue %s screenInches %.2f",
                widthHeight, screenLayoutSize, densityValue, screenInches));
    }

    @SuppressLint("NewApi")
    public static int[] getDisplaySize(WindowManager wm) {
        int width, height;
        Display display = wm.getDefaultDisplay();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }
        return new int[] {width, height};
    }
}
