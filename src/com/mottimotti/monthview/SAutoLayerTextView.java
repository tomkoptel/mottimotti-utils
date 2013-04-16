package com.mottimotti.monthview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

class SAutoLayerTextView extends TextView {
    public SAutoLayerTextView(Context context) {
        super(context);
    }

    public SAutoLayerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SAutoLayerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        if (d == null) return;
        SAutoLayerDrawable layer = new SAutoLayerDrawable(d);
        super.setBackgroundDrawable(layer);
    }

    @Override
    public void setBackground(Drawable d) {
        if (d == null) return;
        SAutoLayerDrawable layer = new SAutoLayerDrawable(d);

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
            left = new SAutoLayerDrawable(leftOriginal);
        }
        if (rightOriginal != null) {
            right = new SAutoLayerDrawable(rightOriginal);
        }
        if (topOriginal != null) {
            top = new SAutoLayerDrawable(topOriginal);
        }
        if (bottomOriginal != null) {
            bottom = new SAutoLayerDrawable(bottomOriginal);
        }
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    @Override
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable startOriginal, Drawable topOriginal,
                                                                Drawable endOriginal, Drawable bottomOriginal) {
        Drawable start = startOriginal;
        Drawable top = topOriginal;
        Drawable end = endOriginal;
        Drawable bottom = bottomOriginal;

        if (startOriginal != null) {
            start = new SAutoLayerDrawable(startOriginal);
        }
        if (endOriginal != null) {
            end = new SAutoLayerDrawable(endOriginal);
        }
        if (topOriginal != null) {
            top = new SAutoLayerDrawable(topOriginal);
        }
        if (bottomOriginal != null) {
            bottom = new SAutoLayerDrawable(bottomOriginal);
        }
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
    }
}
