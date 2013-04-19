package com.mottimotti.android.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import com.mottimotti.android.R;
import com.mottimotti.android.app.AnimationSherlockActivity;
import com.mottimotti.android.widget.RadioGroupSegmented;

public class StartupActivity extends AnimationSherlockActivity {
    private RadioGroupSegmented radioGroupSegmented;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        radioGroupSegmented = (RadioGroupSegmented) findViewById(R.id.radio_group_segmented);
    }

    public void openCalendarPage(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivityWithAnimation(intent, FROM_LEFT_TO_RIGHT);
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