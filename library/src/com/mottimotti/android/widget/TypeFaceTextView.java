package com.mottimotti.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import com.mottimotti.android.R;

public class TypeFaceTextView extends AutoLayerTextView {
    public TypeFaceTextView(Context context) {
        super(context);
    }

    public TypeFaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TypeFaceTextView);
        loadResources(array);
    }

    public TypeFaceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TypeFaceTextView, defStyle, 0);
        loadResources(array);
    }

    private void loadResources(TypedArray array) {
        String typefaceReference = array.getString(R.styleable.TypeFaceTextView_typeFaceReference);
        loadTypeface(typefaceReference);
    }

    public void loadTypeface(String typefaceReference) {
        if (typefaceReference == null) return;
        Typeface type = Typeface.createFromAsset(getContext().getAssets(), typefaceReference);
        setTypeface(type);
    }
}
