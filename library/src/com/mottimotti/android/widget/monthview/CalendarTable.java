package com.mottimotti.android.widget.monthview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.mottimotti.android.widget.R;

import java.text.DateFormatSymbols;
import java.util.*;


public class CalendarTable extends TableLayout implements CalendarCell.CellClickListener {
    private static final int ROW_NUMBER = 6;

    private MonthDisplayHelper monthDisplayHelper;
    private AttributeSet attrs;
    private boolean blockFutureDays;
    private int weekStartDay;
    private List<Date> activeDates = new ArrayList<Date>();

    public CalendarTable(Context context) {
        super(context);
    }

    public CalendarTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalendarTable);
        blockFutureDays = array.getBoolean(R.styleable.CalendarTable_blockFutureDays, false);
        weekStartDay = array.getInt(R.styleable.CalendarTable_weekStartDay, Calendar.SUNDAY);

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
        monthDisplayHelper = new MonthDisplayHelper(year, month, weekStartDay);
    }

    private void initCalendarRows() {
        int wrap_content = TableRow.LayoutParams.WRAP_CONTENT;
        int match_parent = TableRow.LayoutParams.MATCH_PARENT;
        TableRow.LayoutParams params = new TableRow.LayoutParams(match_parent, wrap_content);

        for (int i = 0; i < ROW_NUMBER + 1; i++) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(params);
            addView(tableRow);
        }
    }

    private void initCalendarCells() {
        initCalendarHeader();
        initCalendarBody();
    }

    private void initCalendarHeader() {
        TableRow row = (TableRow) getChildAt(0);
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] dayNames = symbols.getShortWeekdays();

        String[] before = Arrays.copyOfRange(dayNames, 0, weekStartDay);
        String[] after = Arrays.copyOfRange(dayNames, weekStartDay, dayNames.length);

        List<String> days = new ArrayList<String>();
        days.addAll(Arrays.asList(after));
        days.addAll(Arrays.asList(before));

        for (String dayName : days) {
            if(dayName.equals("")) continue;
            CalendarCell cell = new CalendarCell(getContext(), attrs);
            cell.setGravity(Gravity.CENTER);
            cell.setState(DayState.HEADER);
            cell.setText(String.format("%s.", dayName));
            row.addView(cell);
        }
    }

    private void initCalendarBody() {
        for (int row = 0; row < ROW_NUMBER; row++) {
            CalendarWeek week = new CalendarWeek(this, row);
            initRowCells(week, row);
        }
    }

    private void initRowCells(CalendarWeek week, int rowIndex) {
        TableRow row = (TableRow) getChildAt(rowIndex + 1);
        CalendarCell calendarDay;
        List<CalendarDay> days = week.getDays();
        for (CalendarDay day : days) {
            calendarDay = CalendarCell.instantiate(getContext(), attrs);
            calendarDay.setCellClickListener(this);
            calendarDay.setDay(day);
            row.addView(calendarDay);
        }
    }

    private void showLastRowIfNeed() {
        boolean withinCurrentMonth = monthDisplayHelper.isWithinCurrentMonth(ROW_NUMBER - 1, 0);
        getChildAt(ROW_NUMBER).setVisibility(withinCurrentMonth ? VISIBLE : GONE);
    }

    @Override
    public void onClick(Calendar selectedCalendar) {
        dispatchCellClickListener(selectedCalendar);
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
        for (int row = 1; row < rowsCount; row++) {
            TableRow rowView = (TableRow) getChildAt(row);
            updateRowCells(rowView, row - 1);
        }
    }

    private void updateRowCells(TableRow rowView, int rowIndex) {
        CalendarCell cell;
        int columnsCount = rowView.getChildCount();
        CalendarWeek week = new CalendarWeek(this, rowIndex);
        List<CalendarDay> days = week.getDays();

        for (int column = 0; column < columnsCount; column++) {
            cell = (CalendarCell) rowView.getChildAt(column);
            cell.setDay(days.get(column));
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

    public void setActiveDates(List<Date> dates) {
        this.activeDates = dates;
        updateCalendarTable();
    }

    public List<Date> getActiveDates() {
        return activeDates;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        int year = savedState.currentYear;
        int month = savedState.currentMonth;
        monthDisplayHelper = new MonthDisplayHelper(year, month);
        updateCalendarTable();
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Calendar calendar = getHelperCalendar();
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentYear = calendar.get(Calendar.YEAR);
        savedState.currentMonth = calendar.get(Calendar.MONTH);
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentMonth;
        int currentYear;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            int[] val = new int[2];
            in.readIntArray(val);
            currentYear = val[0];
            currentMonth = val[1];
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeIntArray(new int[]{currentYear, currentMonth});
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private OnMonthSelectedListener monthSelectedListener;

    public interface OnMonthSelectedListener {
        public void onPreviousMonthSelected(Calendar startCalendar, Calendar endCalendar);

        public void onNextMonthSelected(Calendar startCalendar, Calendar endCalendar);
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener selectedListener) {
        this.monthSelectedListener = selectedListener;
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

    private CellClickListener clickListener;

    public interface CellClickListener {
        public void onClick(Calendar selectedCalendar);
    }

    public void setCellClickListener(CellClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void dispatchCellClickListener(Calendar calendar) {
        if (clickListener != null) {
            clickListener.onClick(calendar);
        }
    }
}
