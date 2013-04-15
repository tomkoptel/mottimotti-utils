package com.mottimotti.monthview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class HelveticaNeueTextView extends SAutoLayerTextView {

    public HelveticaNeueTextView(Context context) {
        super(context);
    }

    public HelveticaNeueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HelveticaNeueTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/helvetica_neue_bold.ttf"));
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/helvetica_neue.ttf"));
        }
    }
}