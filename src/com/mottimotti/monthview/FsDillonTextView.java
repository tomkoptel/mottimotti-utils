package com.mottimotti.monthview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class FsDillonTextView extends SAutoLayerTextView {
    public FsDillonTextView(Context context) {
        super(context);
    }

    public FsDillonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FsDillonTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD_ITALIC) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/fs_dillon_bold_italic.ttf"));
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/fs_dillon_regular.ttf"));
        }
    }
}
