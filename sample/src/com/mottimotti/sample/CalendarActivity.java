package com.mottimotti.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.mottimotti.widget.monthview.CalendarTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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

        populateActiveDates();
        updateMonthPreview();
    }

    private void populateActiveDates() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.getTimeInMillis();

        List<Date> activeDates = new ArrayList<Date>();
        for (int i = 0; i < 5; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            activeDates.add(calendar.getTime());
        }
        calendarTableLayout.setActiveDates(activeDates);
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