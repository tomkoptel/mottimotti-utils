package com.mottimotti.android.sample;

import android.os.Bundle;
import android.os.Parcelable;
import com.actionbarsherlock.app.SherlockActivity;
import com.mottimotti.android.R;
import com.mottimotti.android.widget.RadioGroupSegmented;

public class SegmentedRadioGroupActivity extends SherlockActivity {
    private RadioGroupSegmented radioGroupSegmented;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_segmented_group);
        radioGroupSegmented = (RadioGroupSegmented) findViewById(R.id.radio_group_segmented);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Parcelable parcelable = radioGroupSegmented.onSaveInstanceState();
        outState.putParcelable("radio_group", parcelable);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        Parcelable parcelable = savedState.getParcelable("radio_group");
        radioGroupSegmented.onRestoreInstanceState(parcelable);
    }
}