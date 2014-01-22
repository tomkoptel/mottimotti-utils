package com.mottimotti.android.sample;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;
import com.mottimotti.android.R;

public class SampleActivity extends SherlockActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_layout);
    }
}
