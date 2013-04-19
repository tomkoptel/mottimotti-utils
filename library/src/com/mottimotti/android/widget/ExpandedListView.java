package com.mottimotti.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class ExpandedListView extends ListView {
    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public ExpandedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != oldCount) {
            oldCount = getCount();
            params = getLayoutParams();
            if (params != null) {
                if(getChildAt(0) == null) {
                    super.onDraw(canvas);
                } else {
                    if (oldCount > 0 && getCount() > 0) {
                        params.height = getCount()
                                * (getChildAt(0).getHeight() + getDividerHeight())
                                - getDividerHeight();
                    } else {
                        params.height = 0;
                    }
                }
                setLayoutParams(params);
            } else {
                super.onDraw(canvas);
            }
        } else {
            super.onDraw(canvas);
        }
    }
}