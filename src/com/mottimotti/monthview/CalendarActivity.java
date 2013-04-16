package com.mottimotti.monthview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CalendarActivity extends Activity
        implements CalendarTable.OnMonthSelectedListener, CalendarTable.CellClickListener {
    private CalendarTable calendarTableLayout;
    private TextView monthPreview;
    private Toast toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_calendar);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        monthPreview = (TextView) findViewById(R.id.current_month_view);
        calendarTableLayout = (CalendarTable) findViewById(R.id.calendar_table_layout);
        calendarTableLayout.setOnMonthSelectedListener(this);
        calendarTableLayout.setCellClickListener(this);

        updateMonthPreview();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Parcelable parcelable = calendarTableLayout.onSaveInstanceState();
        outState.putParcelable("table", parcelable);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        Parcelable parcelable = savedState.getParcelable("table");
        calendarTableLayout.onRestoreInstanceState(parcelable);
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
        Calendar calendar = calendarTableLayout.getHelperCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MMMM");
        monthPreview.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onClick(Calendar selectedCalendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MMMM - dd");
        String stringDate = sdf.format(selectedCalendar.getTime());
        toast.setText(stringDate);
        toast.show();
    }
}