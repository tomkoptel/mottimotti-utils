package com.mottimotti.android.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.mottimotti.android.R;

public class AutoLayerImageView extends ImageView {
    private PorterDuff.Mode[] modes = new PorterDuff.Mode[]{
            PorterDuff.Mode.XOR, // 0
            PorterDuff.Mode.CLEAR, // 1
            PorterDuff.Mode.DARKEN, // 2
            PorterDuff.Mode.DST,    // 3
            PorterDuff.Mode.DST_ATOP, // 4
            PorterDuff.Mode.DST_IN,   // 5
            PorterDuff.Mode.DST_OUT,  // 6
            PorterDuff.Mode.DST_OVER, // 7
            PorterDuff.Mode.LIGHTEN,  // 8
            PorterDuff.Mode.MULTIPLY, // 9
            PorterDuff.Mode.SCREEN,   // 10
            PorterDuff.Mode.SRC,      // 11
            PorterDuff.Mode.SRC_ATOP, // 12
            PorterDuff.Mode.SRC_IN,   // 13
            PorterDuff.Mode.SRC_OUT,  // 14
            PorterDuff.Mode.SRC_OVER  // 15
    };
    private int mode = 10;
    private int colorMode = android.R.color.transparent;

    public AutoLayerImageView(Context context) {
        super(context);
        colorMode = getResources().getColor(android.R.color.transparent);
    }

    public AutoLayerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutoLayerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLayerImageView);
        mode = a.getInt(R.styleable.AutoLayerImageView_duff, 10);
        colorMode = a.getInt(R.styleable.AutoLayerImageView_color_mode, getResources().getColor(android.R.color.transparent));
        a.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isEnabled() ||
                event.getAction() == MotionEvent.ACTION_POINTER_DOWN && isEnabled()) {
            setColorFilter(colorMode, modes[mode]); //your color here
        }

        if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_POINTER_UP) {
            setColorFilter(null);
        }

        return super.onTouchEvent(event);
    }

}
