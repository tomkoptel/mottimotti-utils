package com.mottimotti.android.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class AutoLayerButton extends Button {
    public AutoLayerButton(Context context) {
        super(context);
    }

    public AutoLayerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLayerButton(Context context, AttributeSet attrs, int defStyle) {
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
}