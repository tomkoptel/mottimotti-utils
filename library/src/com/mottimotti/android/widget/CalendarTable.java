package com.mottimotti.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.mottimotti.android.R;

import java.text.DateFormatSymbols;
import java.util.*;


public class CalendarTable extends TableLayout {
    private static final int ROW_NUMBER = 6;

    private int blockedStateDrawable;
    private int blockedStateTextAppearance;
    private String blockedStateTypeface;
    private int blockedStateTextShadow;

    private int regularStateDrawable;
    private int regularStateTextAppearance;
    private String regularStateTypeface;
    private int regularStateTextShadow;

    private int inactiveStateDrawable;
    private int inactiveStateTextAppearance;
    private String inactiveStateTypeface;
    private int inactiveStateTextShadow;

    private int activeStateDrawable;
    private int activeStateTextAppearance;
    private String activeStateTypeface;
    private int activeStateTextShadow;

    private int currentStateDrawable;
    private int currentStateTextAppearance;
    private String currentStateTypeface;
    private int currentStateTextShadow;

    private int headerStateDrawable;
    private int headerStateTextAppearance;
    private String headerStateTypeface;
    private int headerStateTextShadow;

    private MonthDisplayHelper monthDisplayHelper;
    private boolean blockFutureDays;
    private int weekStartDay;
    private boolean isEdit = false;
    private List<Date> activeDates = new ArrayList<Date>();

    public CalendarTable(Context context) {
        super(context);
    }

    public CalendarTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        setUp();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarTable);

        blockFutureDays = a.getBoolean(R.styleable.CalendarTable_blockFutureDays, false);
        weekStartDay = a.getInt(R.styleable.CalendarTable_weekStartDay, Calendar.SUNDAY);

        blockedStateDrawable = a.getResourceId(R.styleable.CalendarTable_blockedStateDrawable, android.R.color.transparent);
        blockedStateTextAppearance = a.getResourceId(R.styleable.CalendarTable_blockedStateTextAppearance, android.R.style.TextAppearance_Medium);
        blockedStateTypeface = a.getString(R.styleable.CalendarTable_blockedStateTypeface);
        blockedStateTextShadow = a.getResourceId(R.styleable.CalendarTable_blockedStateTextShadow, 0);

        regularStateDrawable = a.getResourceId(R.styleable.CalendarTable_regularStateDrawable, android.R.color.transparent);
        regularStateTextAppearance = a.getResourceId(R.styleable.CalendarTable_regularStateTextAppearance, android.R.style.TextAppearance_Medium);
        regularStateTypeface = a.getString(R.styleable.CalendarTable_regularStateTypeface);
        regularStateTextShadow = a.getResourceId(R.styleable.CalendarTable_regularStateTextShadow, 0);

        inactiveStateDrawable = a.getResourceId(R.styleable.CalendarTable_inactiveStateDrawable, android.R.color.transparent);
        inactiveStateTextAppearance = a.getResourceId(R.styleable.CalendarTable_inactiveStateTextAppearance, android.R.style.TextAppearance_Medium);
        inactiveStateTypeface = a.getString(R.styleable.CalendarTable_inactiveStateTypeface);
        inactiveStateTextShadow = a.getResourceId(R.styleable.CalendarTable_inactiveStateTextShadow, 0);

        activeStateDrawable = a.getResourceId(R.styleable.CalendarTable_activeStateDrawable, android.R.color.transparent);
        activeStateTextAppearance = a.getResourceId(R.styleable.CalendarTable_activeStateTextAppearance, android.R.style.TextAppearance_Medium);
        activeStateTypeface = a.getString(R.styleable.CalendarTable_activeStateTypeface);
        activeStateTextShadow = a.getResourceId(R.styleable.CalendarTable_activeStateTextShadow, 0);

        currentStateDrawable = a.getResourceId(R.styleable.CalendarTable_currentStateDrawable, android.R.color.transparent);
        currentStateTextAppearance = a.getResourceId(R.styleable.CalendarTable_currentStateTextAppearance, android.R.style.TextAppearance_Medium);
        currentStateTypeface = a.getString(R.styleable.CalendarTable_currentStateTypeface);
        currentStateTextShadow = a.getResourceId(R.styleable.CalendarTable_activeStateTextShadow, 0);

        headerStateDrawable = a.getResourceId(R.styleable.CalendarTable_headerStateDrawable, android.R.color.transparent);
        headerStateTextAppearance = a.getResourceId(R.styleable.CalendarTable_headerStateTextAppearance, android.R.style.TextAppearance_Medium);
        headerStateTypeface = a.getString(R.styleable.CalendarTable_headerStateTypeface);
        headerStateTextShadow = a.getResourceId(R.styleable.CalendarTable_headerStateTextShadow, 0);

        a.recycle();
    }

    private void setUp() {
        setUpMonthDisplayHelper();
        setUpCalendarRows();
        setUpCalendarCells();
        showLastRowIfNeed();
    }

    private void setUpMonthDisplayHelper() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        monthDisplayHelper = new MonthDisplayHelper(year, month, weekStartDay);
    }

    private void setUpCalendarRows() {
        int wrap_content = TableRow.LayoutParams.WRAP_CONTENT;
        int match_parent = TableRow.LayoutParams.MATCH_PARENT;
        TableRow.LayoutParams params = new TableRow.LayoutParams(match_parent, wrap_content);

        for (int i = 0; i < ROW_NUMBER + 1; i++) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(params);
            addView(tableRow);
        }
    }

    private void setUpCalendarCells() {
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
            if (dayName.equals("")) continue;
            CalendarCell cell = new CalendarCell(getContext());
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
            calendarDay = new CalendarCell(getContext());
            calendarDay.setDay(day);
            row.addView(calendarDay);
        }
    }

    private void showLastRowIfNeed() {
        boolean withinCurrentMonth = monthDisplayHelper.isWithinCurrentMonth(ROW_NUMBER - 1, 0);
        getChildAt(ROW_NUMBER).setVisibility(withinCurrentMonth ? VISIBLE : GONE);
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

    public int getYear() {
        return getHelperCalendar().get(Calendar.YEAR);
    }

    public int getMonth() {
        return getHelperCalendar().get(Calendar.MONTH);
    }

    public int getDay() {
        return getHelperCalendar().get(Calendar.DAY_OF_MONTH);
    }

    public void setBlockedStateDrawable(int blockedStateDrawable) {
        this.blockedStateDrawable = blockedStateDrawable;
    }

    public void setBlockedStateTextAppearance(int blockedStateTextAppearance) {
        this.blockedStateTextAppearance = blockedStateTextAppearance;
    }

    public void setBlockedStateTypeface(String blockedStateTypeface) {
        this.blockedStateTypeface = blockedStateTypeface;
    }

    public void setBlockedStateTextShadow(int blockedStateTextShadow) {
        this.blockedStateTextShadow = blockedStateTextShadow;
    }

    public void setRegularStateDrawable(int regularStateDrawable) {
        this.regularStateDrawable = regularStateDrawable;
    }

    public void setRegularStateTextAppearance(int regularStateTextAppearance) {
        this.regularStateTextAppearance = regularStateTextAppearance;
    }

    public void setRegularStateTypeface(String regularStateTypeface) {
        this.regularStateTypeface = regularStateTypeface;
    }

    public void setRegularStateTextShadow(int regularStateTextShadow) {
        this.regularStateTextShadow = regularStateTextShadow;
    }

    public void setInactiveStateDrawable(int inactiveStateDrawable) {
        this.inactiveStateDrawable = inactiveStateDrawable;
    }

    public void setInactiveStateTextAppearance(int inactiveStateTextAppearance) {
        this.inactiveStateTextAppearance = inactiveStateTextAppearance;
    }

    public void setInactiveStateTypeface(String inactiveStateTypeface) {
        this.inactiveStateTypeface = inactiveStateTypeface;
    }

    public void setInactiveStateTextShadow(int inactiveStateTextShadow) {
        this.inactiveStateTextShadow = inactiveStateTextShadow;
    }

    public void setActiveStateDrawable(int activeStateDrawable) {
        this.activeStateDrawable = activeStateDrawable;
    }

    public void setActiveStateTextAppearance(int activeStateTextAppearance) {
        this.activeStateTextAppearance = activeStateTextAppearance;
    }

    public void setActiveStateTypeface(String activeStateTypeface) {
        this.activeStateTypeface = activeStateTypeface;
    }

    public void setActiveStateTextShadow(int activeStateTextShadow) {
        this.activeStateTextShadow = activeStateTextShadow;
    }

    public void setCurrentStateDrawable(int currentStateDrawable) {
        this.currentStateDrawable = currentStateDrawable;
    }

    public void setCurrentStateTextAppearance(int currentStateTextAppearance) {
        this.currentStateTextAppearance = currentStateTextAppearance;
    }

    public void setCurrentStateTypeface(String currentStateTypeface) {
        this.currentStateTypeface = currentStateTypeface;
    }

    public void setCurrentStateTextShadow(int currentStateTextShadow) {
        this.currentStateTextShadow = currentStateTextShadow;
    }

    public void setHeaderStateDrawable(int headerStateDrawable) {
        this.headerStateDrawable = headerStateDrawable;
    }

    public void setHeaderStateTextAppearance(int headerStateTextAppearance) {
        this.headerStateTextAppearance = headerStateTextAppearance;
    }

    public void setHeaderStateTypeface(String headerStateTypeface) {
        this.headerStateTypeface = headerStateTypeface;
    }

    public void setHeaderStateTextShadow(int headerStateTextShadow) {
        this.headerStateTextShadow = headerStateTextShadow;
    }

    public void setMonthDisplayHelper(MonthDisplayHelper monthDisplayHelper) {
        this.monthDisplayHelper = monthDisplayHelper;
    }

    public void setBlockFutureDays(boolean blockFutureDays) {
        this.blockFutureDays = blockFutureDays;
        updateCalendarTable();
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
        if (monthSelectedListener != null && !isEdit) {
            isEdit = true;
            Calendar[] calendars = getCurrentCalendars();
            monthSelectedListener.onPreviousMonthSelected(calendars[0], calendars[1]);
            isEdit = false;
        }
    }

    private void dispatchNextMonthSelected() {
        if (monthSelectedListener != null && !isEdit) {
            isEdit = true;
            Calendar[] calendars = getCurrentCalendars();
            monthSelectedListener.onNextMonthSelected(calendars[0], calendars[1]);
            isEdit = false;
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

    private class CalendarCell extends TypeFaceTextView implements View.OnClickListener {
        private CalendarDay day;
        private CalendarDay previousDay;

        public CalendarCell(Context context) {
            super(context);
            init();
        }

        public CalendarCell(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public CalendarCell(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public void init() {
            setGravity(Gravity.CENTER);
            setClickable(true);
            setOnClickListener(this);
        }

        public void setState(DayState state) {
            switch (state) {
                case HEADER:
                    applyHeaderState();
                    break;
                case BLOCKED:
                    applyBlockedState();
                    break;
                case REGULAR:
                    applyRegularSettings();
                    break;
                case INACTIVE:
                    applyInActiveSettings();
                    break;
                case ACTIVE:
                    applyActiveSettings();
                    break;
                case CURRENT:
                    applyCurrentState();
                    break;
            }
        }

        private void applyHeaderState() {
            setTextAppearance(getContext(), headerStateTextAppearance);
            setTextShadowAppearance(getContext(), headerStateTextShadow);
            setBackgroundResource(headerStateDrawable);
            loadTypeface(headerStateTypeface);
            setClickable(false);
        }

        private void applyRegularSettings() {
            setTextAppearance(getContext(), regularStateTextAppearance);
            setTextShadowAppearance(getContext(), regularStateTextShadow);
            setBackgroundResource(regularStateDrawable);
            loadTypeface(regularStateTypeface);
            setClickable(true);
        }

        private void applyBlockedState() {
            setTextAppearance(getContext(), blockedStateTextAppearance);
            setTextShadowAppearance(getContext(), blockedStateTextShadow);
            setBackgroundResource(blockedStateDrawable);
            loadTypeface(blockedStateTypeface);
            setClickable(false);
        }

        private void applyInActiveSettings() {
            setTextAppearance(getContext(), inactiveStateTextAppearance);
            setTextShadowAppearance(getContext(), inactiveStateTextShadow);
            setBackgroundResource(inactiveStateDrawable);
            loadTypeface(inactiveStateTypeface);
            setClickable(true);
        }

        private void applyActiveSettings() {
            setTextAppearance(getContext(), activeStateTextAppearance);
            setTextShadowAppearance(getContext(), activeStateTextShadow);
            setBackgroundResource(activeStateDrawable);
            loadTypeface(activeStateTypeface);
            setClickable(true);
        }

        private void applyCurrentState() {
            setTextAppearance(getContext(), currentStateTextAppearance);
            setTextShadowAppearance(getContext(), currentStateTextShadow);
            setBackgroundResource(currentStateDrawable);
            loadTypeface(currentStateTypeface);
            setClickable(true);
        }

        private void setTextShadowAppearance(Context context, int resid) {
            if (resid == 0) {
                setShadowLayer(0, 0, 0, 0);
            } else {
                int[] attrsArray = new int[]{
                        android.R.attr.shadowColor,
                        android.R.attr.shadowDx,
                        android.R.attr.shadowDy,
                        android.R.attr.shadowRadius
                };

                TypedArray appearance = context.obtainStyledAttributes(resid, attrsArray);
                int color = appearance.getColor(0, 0);
                float shadowDx = appearance.getFloat(1, 0);
                float shadowDy = appearance.getFloat(2, 0);
                float radius = appearance.getFloat(3, 0);
                appearance.recycle();

                setShadowLayer(radius, shadowDx, shadowDy, color);
            }
        }

        public void setDay(CalendarDay day) {
            if (previousDay == null) {
                this.previousDay = day;
                setState(day.getState());
            } else {
                this.previousDay = this.day;
                if (previousDay.getState() != day.getState()) {
                    setState(day.getState());
                }
            }
            this.day = day;
            setText(day.toString());
        }

        @Override
        public void onClick(View v) {
            dispatchCellClickListener(day.getCalendar());
        }
    }
}
