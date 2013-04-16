package com.mottimotti.monthview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarTable extends TableLayout {
    private static final int ROW_NUMBER = 6;

    private MonthDisplayHelper monthDisplayHelper;
    private OnMonthSelectedListener monthSelectedListener;
    private AttributeSet attrs;
    private boolean blockFutureDays;


    public interface OnMonthSelectedListener {
        public void onPreviousMonthSelected(Calendar startCalendar, Calendar endCalendar);
        public void onNextMonthSelected(Calendar startCalendar, Calendar endCalendar);
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener selectedListener) {
        this.monthSelectedListener = selectedListener;
    }

    public CalendarTable(Context context) {
        super(context);
    }

    public CalendarTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarTable);
        blockFutureDays = array.getBoolean(R.styleable.CalendarTable_blockFutureDays, false);

        init();
    }

    private void init() {
        initMonthDisplayHelper();
        initCalendarRows();
        initCalendarCells();
        showLastRowIfNeed();
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
            CalendarWeek week = new CalendarWeek(this, row);
            initRowCells(week, row);
        }
    }

    private void showLastRowIfNeed() {
        boolean withinCurrentMonth = monthDisplayHelper.isWithinCurrentMonth(ROW_NUMBER - 1, 0);
        getChildAt(ROW_NUMBER - 1).setVisibility(withinCurrentMonth ? VISIBLE : GONE);
    }

    private void initRowCells(CalendarWeek week, int rowIndex) {
        TableRow row = (TableRow) getChildAt(rowIndex);
        CalendarCell calendarDay;
        List<CalendarDay> days = week.getDays();
        for (CalendarDay day : days) {
            calendarDay = CalendarCell.instantiate(getContext(), attrs);
            calendarDay.setState(day.getState());
            calendarDay.setText(day.toString());
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
        showLastRowIfNeed();

        int rowsCount = getChildCount();
        for (int row = 0; row < rowsCount; row++) {
            TableRow rowView = (TableRow) getChildAt(row);
            updateRowCells(rowView, row);
        }
    }

    private void updateRowCells(TableRow rowView, int row) {
        CalendarCell cell;
        int columnsCount = rowView.getChildCount();
        CalendarWeek week = new CalendarWeek(this, row);
        List<CalendarDay> days = week.getDays();

        for (int column = 0; column < columnsCount; column++) {
            cell = (CalendarCell) rowView.getChildAt(column);
            CalendarDay day = days.get(column);
            cell.setState(day.getState());
            cell.setText(day.toString());
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

    public Calendar getHelperCalendar() {
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

    public MonthDisplayHelper getMonthDisplayHelper() {
        return monthDisplayHelper;
    }

    public boolean isFutureDaysBlocked() {
        return blockFutureDays;
    }
}
