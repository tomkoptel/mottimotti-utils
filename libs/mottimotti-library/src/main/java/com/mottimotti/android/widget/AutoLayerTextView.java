package com.mottimotti.android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoLayerTextView extends TextView {
    public AutoLayerTextView(Context context) {
        super(context);
    }

    public AutoLayerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLayerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBackgroundDrawable(Drawable d) {
        if (d == null) return;
        AutoLayerDrawable layer = new AutoLayerDrawable(d);
        super.setBackgroundDrawable(layer);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBackground(Drawable d) {
        if (d == null) return;
        AutoLayerDrawable layer = new AutoLayerDrawable(d);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackgroundDrawable(layer);
        } else {
            super.setBackground(layer);
        }
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable leftOriginal, Drawable topOriginal,
                                                        Drawable rightOriginal, Drawable bottomOriginal) {
        Drawable left = leftOriginal;
        Drawable top = topOriginal;
        Drawable right = rightOriginal;
        Drawable bottom = bottomOriginal;

        if (leftOriginal != null) {
            left = new AutoLayerDrawable(leftOriginal);
        }
        if (rightOriginal != null) {
            right = new AutoLayerDrawable(rightOriginal);
        }
        if (topOriginal != null) {
            top = new AutoLayerDrawable(topOriginal);
        }
        if (bottomOriginal != null) {
            bottom = new AutoLayerDrawable(bottomOriginal);
        }
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }
}
