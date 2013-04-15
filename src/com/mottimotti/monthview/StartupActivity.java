package com.mottimotti.monthview;

import android.app.Activity;
import android.os.Bundle;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class StartupActivity extends Activity implements CalendarTable.OnMonthSelectedListener {
    private static final int ROW_NUMBER = 5;
    private MonthDisplayHelper monthDisplayHelper;
    private CalendarTable calendarTableLayout;
    private TextView monthPreview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        monthPreview = (TextView) findViewById(R.id.current_month_view);
        calendarTableLayout = (CalendarTable) findViewById(R.id.calendar_table_layout);
        calendarTableLayout.setOnMonthSelectedListener(this);

        updateMonthPreview();
    }

    public void previousMonth(View view) {
        calendarTableLayout.previousMonth();
    }

    public void nextMonth(View view) {
        calendarTableLayout.nextMonth();
    }

    @Override
    public void onPreviousMonthSelected(Calendar startCalendar, Calendar endCalendar) {
        updateMonthPreview();
    }

    @Override
    public void onNextMonthSelected(Calendar startCalendar, Calendar endCalendar) {
        updateMonthPreview();
    }

    private void updateMonthPreview() {
        Calendar calendar = calendarTableLayout.getCurrentCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MMMM");
        monthPreview.setText(sdf.format(calendar.getTime()));
    }
}