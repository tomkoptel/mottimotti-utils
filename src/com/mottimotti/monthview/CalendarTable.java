package com.mottimotti.monthview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.edgark.monthview.CalendarFilter;

import java.util.Calendar;


public class CalendarTable extends TableLayout {
    private static final int ROW_NUMBER = 5;

    private MonthDisplayHelper monthDisplayHelper;
    private OnMonthSelectedListener monthSelectedListener;


    public interface OnMonthSelectedListener {
        public void onPreviousMonthSelected(Calendar startCalendar, Calendar endCalendar);
        public void onNextMonthSelected(Calendar startCalendar, Calendar endCalendar);
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener selectedListener) {
        this.monthSelectedListener = selectedListener;
    }

    public CalendarTable(Context context) {
        super(context);
        init();
    }

    public CalendarTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initMonthDisplayHelper();
        initCalendarRows();
        initCalendarCells();
    }

    private void initMonthDisplayHelper() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        monthDisplayHelper = new MonthDisplayHelper(year, month);
    }

    private void initCalendarRows() {
        int wrap_content = TableRow.LayoutParams.WRAP_CONTENT;
        int match_parent = TableRow.LayoutParams.MATCH_PARENT;
        TableRow.LayoutParams params = new TableRow.LayoutParams(match_parent, wrap_content);

        for (int i = 0; i < ROW_NUMBER; i++) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(params);
            addView(tableRow);
        }
    }

    private void initCalendarCells() {
        for (int row = 0; row < ROW_NUMBER; row++) {
            int[] digits = monthDisplayHelper.getDigitsForRow(row);
            initRowCells(digits, row);
        }
    }

    private void initRowCells(int[] days, int rowIndex) {
        TableRow row = (TableRow) getChildAt(rowIndex);
        CalendarCell calendarDay;

        for (int i = 0; i < days.length; i++) {
            int day = days[i];
            calendarDay = CalendarCell.instantiate(getContext());
            calendarDay.setText(String.valueOf(day));
            row.addView(calendarDay);
        }
    }

    public void previousMonth() {
        monthDisplayHelper.previousMonth();
        dispatchPreviousMonthSelected();
        updateCalendarTable();
    }

    public void nextMonth() {
        monthDisplayHelper.nextMonth();
        dispatchNextMonthSelected();
        updateCalendarTable();
    }

    private void updateCalendarTable() {
        int rowsCount = getChildCount();
        for (int row = 0; row < rowsCount; row++) {
            TableRow rowView = (TableRow) getChildAt(row);
            updateRowCells(rowView, row);
        }
    }

    private void updateRowCells(TableRow rowView, int row) {
        TextView cell;
        int columnsCount = rowView.getChildCount();
        for (int column = 0; column < columnsCount; column++) {
            cell = (TextView) rowView.getChildAt(column);
            int day = monthDisplayHelper.getDayAt(row, column);
            cell.setText(String.valueOf(day));
        }
    }

    private void dispatchPreviousMonthSelected() {
        if (monthSelectedListener != null) {
            Calendar[] calendars = getCurrentCalendars();
            monthSelectedListener.onPreviousMonthSelected(calendars[0], calendars[1]);
        }
    }

    private void dispatchNextMonthSelected() {
        if (monthSelectedListener != null) {
            Calendar[] calendars = getCurrentCalendars();
            monthSelectedListener.onNextMonthSelected(calendars[0], calendars[1]);
        }
    }

    public Calendar[] getCurrentCalendars() {
        CalendarFilter calendarFilters = new CalendarFilter(monthDisplayHelper);
        return calendarFilters.getFilters();
    }

    public Calendar getCurrentCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, monthDisplayHelper.getYear());
        calendar.set(Calendar.MONTH, monthDisplayHelper.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.getTimeInMillis();
        return calendar;
    }
}
