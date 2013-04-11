package com.mottimotti.monthview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import com.edgark.monthview.R;


public class StartupActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TableLayout calendarTableLayout = (TableLayout) findViewById(R.id.calendar_table_layout);

    }
}