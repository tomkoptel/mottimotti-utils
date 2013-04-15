package com.edgark.monthview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class SAutoLayerTextView extends TextView {
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
        SAutoLayerDrawable layer = new SAutoLayerDrawable(d);
        super.setBackgroundDrawable(layer);
    }

    @Override
    public void setBackground(Drawable d) {
        SAutoLayerDrawable layer = new SAutoLayerDrawable(d);
        super.setBackground(layer);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable leftOriginal, Drawable topOriginal,
                                                        Drawable rightOriginal, Drawable bottomOriginal) {
        Drawable left = leftOriginal;
        Drawable top = topOriginal;
        Drawable right = rightOriginal;
        Drawable bottom = bottomOriginal;

        if(leftOriginal != null) {
            left = new SAutoLayerDrawable(leftOriginal);
        }
        if(rightOriginal != null) {
            right = new SAutoLayerDrawable(rightOriginal);
        }
        if(topOriginal != null) {
            top = new SAutoLayerDrawable(topOriginal);
        }
        if(bottomOriginal != null) {
            bottom = new SAutoLayerDrawable(bottomOriginal);
        }
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }
}
