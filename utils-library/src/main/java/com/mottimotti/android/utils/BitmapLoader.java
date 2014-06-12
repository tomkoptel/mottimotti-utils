package com.mottimotti.android.utils;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapLoader {
    public static int getScale(int originalWidth, int originalHeight, final int requiredWidth, final int requiredHeight) {
        int scale = 1;

        if ((originalWidth > requiredWidth) || (originalHeight > requiredHeight)) {
            if (originalWidth < originalHeight)
                scale = Math.round((float) originalWidth / requiredWidth);
            else
                scale = Math.round((float) originalHeight / requiredHeight);
        }
        return scale;
    }

    public static Bitmap loadBitmap(String filePath, int requiredWidth, int requiredHeight) {
        BitmapFactory.Options options = getOptions(filePath, requiredWidth, requiredHeight);
        return BitmapFactory.decodeFile(filePath, options);
    }


    public static BitmapFactory.Options getOptions(String filePath, int requiredWidth, int requiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = getScale(options.outWidth, options.outHeight, requiredWidth, requiredHeight);
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Bitmap loadBitmap(byte[] data, int requiredWidth, int requiredHeight) {
        BitmapFactory.Options options = getOptions(data, requiredWidth, requiredHeight);
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static BitmapFactory.Options getOptions(byte[] data, int requiredWidth, int requiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = getScale(options.outWidth, options.outHeight, requiredWidth, requiredHeight);
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Bitmap loadBitmap(Resources resources, int resourceId, int requiredWidth, int requiredHeight) {
        BitmapFactory.Options options = getOptions(resources, resourceId, requiredWidth, requiredHeight);
        return BitmapFactory.decodeResource(resources, resourceId, options);
    }

    public static BitmapFactory.Options getOptions(Resources resources, int resourceId, int requiredWidth, int requiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(resources, resourceId, options);
        options.inSampleSize = BitmapLoader.getScale(options.outWidth, options.outHeight, requiredWidth, requiredHeight);
        options.inJustDecodeBounds = false;

        return options;
    }
}
