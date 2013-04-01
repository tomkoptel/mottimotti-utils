package com.edgark.monthview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.*;
import android.widget.*;

import java.util.*;

/**
 * Created by.
 * User: EdgarK
 * Date: 3/19/13
 * Time: 2:46 PM
 */
public class MonthView extends LinearLayout implements AdapterView.OnItemClickListener {
    private LinearLayout topLayout;
    private TextView prevTv, curTv, nextTv;

    private GridView headGrid, bodyGrid;

    Integer calendar_today_resource,
            calendar_has_event_resource,
            calendar_other_month_resource,
            calendar_usual_resource,
            calendar_items_resource,
            calendar_head_resource;

    int screenWidth, month, year;

    private OnDayClickListener onDayClickListener;
    private OnMonthSelectedListener monthSelectedListener;

    private List<Date> dates = new ArrayList<Date>();

    private CalendarArrayAdapter<CalendarItem> adapter;
    private List<CalendarItem> values;

    private List<String> dayNames = Arrays.asList(new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"});
    private String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private CustomMonthDisplayHelper monthDisplayHelper;

    public MonthView(Context context) {
        super(context);
        initialize();
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }


    public void initialize() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        screenWidth = display.getWidth();

        this.setOrientation(LinearLayout.VERTICAL);
        topLayout = new LinearLayout(getContext());
        topLayout.setOrientation(LinearLayout.HORIZONTAL);

        prevTv = makeTv(1f, "<");
        topLayout.addView(prevTv);

        curTv = makeTv(8f, "Month");
        topLayout.addView(curTv);

        nextTv = makeTv(1f, ">");
        topLayout.addView(nextTv);

        addView(topLayout);
        headGrid = new GridView(getContext());
        headGrid.setNumColumns(7);
        addView(headGrid);
        bodyGrid = new GridView(getContext());
        bodyGrid.setNumColumns(7);
        bodyGrid.setOnItemClickListener(this);
        addView(bodyGrid);

        this.setBackgroundColor(Color.WHITE);

        prevTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthDisplayHelper.previousMonth();
                prepare();
                dispatchPreviousMonthSelected();
            }
        });
        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthDisplayHelper.nextMonth();
                prepare();
                dispatchNextMonthSelected();
            }
        });

        prepare();
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

    private TextView makeTv(float weight, String text) {
        TextView view = new TextView(getContext());
        view.setText(text);
        view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, weight));
        view.getLayoutParams().height = 40;
        view.setTextSize(24);
        view.setTypeface(Typeface.DEFAULT_BOLD);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        return view;
    }


    private void prepare() {
        if (monthDisplayHelper == null)
            monthDisplayHelper = new CustomMonthDisplayHelper(year, month);
        curTv.setText(String.format("%s %d", monthNames[monthDisplayHelper.getMonth()], monthDisplayHelper.getYear()));
        prepare(monthDisplayHelper);
        adapter.notifyDataSetInvalidated();
    }


    private void prepare(CustomMonthDisplayHelper customDisplayHelper) {

        CalendarArrayAdapter headAdapter = new CalendarArrayAdapter(getContext(), dayNames);
        headAdapter.setHead(true);
        headGrid.setAdapter(headAdapter);


        values = new ArrayList<CalendarItem>();


        for (int j = 0; j < 6; j++) {
            int temp[] = customDisplayHelper.getDigitsForRow(j);
            if (temp[0] < 15 && j > 2) break;
            for (int i = 0, tempLength = temp.length; i < tempLength; i++) {
                int date = temp[i];
                CalendarItem day = new CalendarItem();
                day.setNumber(date);
                day.setMonthNumber(customDisplayHelper.getMonth(j, i));
                day.setYearNumber(customDisplayHelper.getYear(j, i));
                day.setCurrentMonth(customDisplayHelper.isWithinCurrentMonth(j, i));
                day.setToday(customDisplayHelper.isToday(j, i));
                day.setHasEvent(customDisplayHelper.hasEvt(j, i));

                values.add(day);
            }
        }


        adapter = new CalendarArrayAdapter<CalendarItem>(getContext(), values);

        bodyGrid.setAdapter(adapter);

    }


    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (onDayClickListener != null) {
            onDayClickListener.onItemClick(adapter.getItem(i).getDate(), view);
        }
    }


    private final class CalendarArrayAdapter<T> extends ArrayAdapter<T> {
        private int selected;
        private boolean head;

        public CalendarArrayAdapter(Context context, List<T> values) {
            super(context, android.R.layout.test_list_item, values);
        }

        public void select(int position) {
            CalendarItem item = (CalendarItem) this.getItem(position);
            if (item.getType() == CalendarItem.TYPE_HEAD_ITEM) return;
            deselect();
            item.setSelected(true);
            selected = position;
        }

        public void deselect() {
            CalendarItem item = (CalendarItem) this.getItem(selected);
            if (item != null) item.setSelected(false);
        }

        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = LayoutInflater.from(getContext());

            LinearLayout cells = null;
            if (calendar_items_resource != null) {
                cells = (LinearLayout) inflater.inflate(calendar_items_resource, parent, false);
            }


            final TextView cell;

            if (!isHead()) {
                CalendarItem item = (CalendarItem) getItem(position);
                if (item.isTypeOf(CalendarItem.TYPE_TODAY_ITEM)) {
                    cell = makeCell(cells, calendar_today_resource, 0xFFFFFFFF, 0xFFD18E53, 0xFFF7B64A, 0xFFededed);
                } else if (item.isTypeOf(CalendarItem.TYPE_HAS_EVENT_ITEM)) {
                    cell = makeCell(cells, calendar_has_event_resource, 0xFFE09C41, 0xFFB85816, 0xFFE7B64A, 0xFFcdcdcd);
                } else if (item.isTypeOf(CalendarItem.TYPE_OTHER_MONTH_ITEM)) {
                    cell = makeCell(cells, calendar_other_month_resource, 0xFFD7D7D7, 0xFF919191, 0xFFcdcdcd, 0xFFaaaaaa);
                } else {
                    cell = makeCell(cells, calendar_usual_resource, 0xff6D6D6D, 0x090909, 0xFFcdcdcd, 0xFFaaaaaa);
                }

            } else {
                cell = makeCell(cells, calendar_head_resource, 0xff000000, 0x6C6C6C, 0xFFcdcdcd, 0xFFaaaaaa);
            }
            cell.setText(getItem(position).toString());
            final LinearLayout layout = new LinearLayout(getContext());
            if (cells != null) cells.removeView(cell);
            layout.addView(cell);


            return (layout);


        }

        private TextView makeCell(View parrent, Integer textView, int textColor, int shadowColor, int grStart, int grEnd) {
            TextView view;
            if (textView != null && parrent != null) {
                view = (TextView) parrent.findViewById(textView);
            } else {
                view = new TextView(getContext());
                view.setGravity(Gravity.CENTER);
                view.setTextColor(textColor);
                view.setShadowLayer(1, -1, -1, shadowColor);
                if (!isHead()) {
                    view.setTextSize(18);
                    view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 7, screenWidth / 7));
                } else {
                    view.setTextSize(13);
                    view.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 7, 18));
                }
                view.setTypeface(Typeface.DEFAULT_BOLD);
                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.BL_TR,
                        new int[]{grStart, grEnd});
                gd.setCornerRadius(0f);
                view.setBackgroundDrawable(gd);
            }
            return view;
        }


        public boolean isHead() {
            return head;
        }

        public void setHead(boolean head) {
            this.head = head;
        }
    }


    private final class CalendarItem {
        public static final int TYPE_USUAL_ITEM = 0x000;
        public static final int TYPE_HEAD_ITEM = 0x001;
        public static final int TYPE_OTHER_MONTH_ITEM = 0x002;
        public static final int TYPE_SELECTED_ITEM = 0x004;
        public static final int TYPE_HAS_EVENT_ITEM = 0x008;
        public static final int TYPE_TODAY_ITEM = 0x010;


        private int number = 0;
        private int monthNumber;
        private int yearNumber;
        private String name;
        private boolean currentMonth, hasEvent, selected, today;


        public CalendarItem() {
            selected = false;
            currentMonth = true;
            hasEvent = false;
        }

        public boolean isToday() {
            return today;
        }

        public boolean isTypeOf(int type) {
            return (getType() & type) != 0;
        }


        @Override
        public String toString() {
            if (number != 0) return String.valueOf(number);
            return name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getType() {
            int type = TYPE_USUAL_ITEM;
            if (selected) type |= TYPE_SELECTED_ITEM;
            if (hasEvent) type |= TYPE_HAS_EVENT_ITEM;
            if (!currentMonth) type |= TYPE_OTHER_MONTH_ITEM;
            if (number == 0) type |= TYPE_HEAD_ITEM;
            if (today) type |= TYPE_TODAY_ITEM;
            return type;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isCurrentMonth() {
            return currentMonth;
        }

        public void setCurrentMonth(boolean currentMonth) {
            this.currentMonth = currentMonth;
        }

        public int getMonthNumber() {
            return monthNumber;
        }

        public void setMonthNumber(int monthNumber) {
            this.monthNumber = monthNumber;
        }

        public int getYearNumber() {
            return yearNumber;
        }

        public void setYearNumber(int yearNumber) {
            this.yearNumber = yearNumber;
        }

        private Date getDate() {
            return new Date(year - 1900, month, number);
        }

        public boolean isHasEvent() {
            return hasEvent;
        }

        public void setHasEvent(boolean hasEvent) {
            this.hasEvent = hasEvent;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void setToday(boolean today) {
            this.today = today;
        }
    }

    private class CustomMonthDisplayHelper extends MonthDisplayHelper {
        public CustomMonthDisplayHelper(int year, int month, int weekStartDay) {
            super(year, month, weekStartDay);
        }

        public CustomMonthDisplayHelper(int year, int month) {
            super(year, month);
        }

        public int getMonth(int row, int column) {
            if (isWithinCurrentMonth(row, column)) return getMonth();
            if (row == 0) return previousMonthNumber();
            return nextMonthNumber();
        }

        public int getYear(int row, int column) {
            if (isWithinCurrentMonth(row, column)) return getYear();
            if (row == 0 && getMonth() == 1) return getYear() - 1;
            if (getMonth() == 12) return getYear() + 1;
            return getYear();
        }

        public int previousMonthNumber() {
            if (getMonth() == 1) return 12;
            return getMonth() - 1;
        }

        public int nextMonthNumber() {
            if (getMonth() == 12) return 1;
            return getMonth() + 1;
        }

        public boolean isToday(int row, int column) {
            Calendar c = Calendar.getInstance();
            if (c.get(Calendar.YEAR) == getYear() && c.get(Calendar.DAY_OF_MONTH) == getDayAt(row, column) && c.get(Calendar.MONTH) == getMonth(row, column))
                return true;
            return false;
        }

        public boolean hasEvt(int row, int column) {
            return (dates.indexOf(new Date(getYear(row, column) - 1900, getMonth(row, column), getDayAt(row, column))) > -1);
        }
    }


    public TextView getPrevTv() {
        return prevTv;
    }

    public void setPrevTv(TextView prevTv) {
        this.prevTv = prevTv;
    }

    public TextView getCurTv() {
        return curTv;
    }

    public void setCurTv(TextView curTv) {
        this.curTv = curTv;
    }

    public TextView getNextTv() {
        return nextTv;
    }

    public void setNextTv(TextView nextTv) {
        this.nextTv = nextTv;
    }

    public GridView getHeadGrid() {
        return headGrid;
    }

    public void setHeadGrid(GridView headGrid) {
        this.headGrid = headGrid;
    }

    public GridView getBodyGrid() {
        return bodyGrid;
    }

    public void setBodyGrid(GridView bodyGrid) {
        this.bodyGrid = bodyGrid;
    }

    public List<String> getDayNames() {
        return dayNames;
    }

    public void setDayNames(String[] dayNames) {
        this.dayNames = Arrays.asList(dayNames);
    }

    public String[] getMonthNames() {
        return monthNames;
    }

    public void setMonthNames(String[] monthNames) {
        this.monthNames = monthNames;
    }

    public Integer getCalendar_today_resource() {
        return calendar_today_resource;
    }

    public void setCalendar_today_resource(Integer calendar_today_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resourece. Set items layout first");
        this.calendar_today_resource = calendar_today_resource;
    }

    public Integer getCalendar_has_event_resource() {
        return calendar_has_event_resource;
    }

    public void setCalendar_has_event_resource(Integer calendar_has_event_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resourece. Set items layout first");
        this.calendar_has_event_resource = calendar_has_event_resource;
    }

    public Integer getCalendar_other_month_resource() {
        return calendar_other_month_resource;
    }

    public void setCalendar_other_month_resource(Integer calendar_other_month_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resourece. Set items layout first");
        this.calendar_other_month_resource = calendar_other_month_resource;
    }

    public Integer getCalendar_usual_resource() {
        return calendar_usual_resource;
    }

    public void setCalendar_usual_resource(Integer calendar_usual_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resourece. Set items layout first");
        this.calendar_usual_resource = calendar_usual_resource;
    }

    public Integer getCalendar_items_resource() {
        return calendar_items_resource;
    }

    public void setCalendar_items_resource(Integer calendar_items_resource) {
        this.calendar_items_resource = calendar_items_resource;
    }

    public Integer getCalendar_head_resource() {
        return calendar_head_resource;
    }

    public void setCalendar_head_resource(Integer calendar_head_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resourece. Set items layout first");
        this.calendar_head_resource = calendar_head_resource;
    }

    public class SetItemsResourceFirst extends Exception {
        SetItemsResourceFirst(String detailMessage) {
            super(detailMessage);
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
        prepare();
    }

    public CustomMonthDisplayHelper getMonthDisplayHelper() {
        return monthDisplayHelper;
    }

    public interface OnMonthSelectedListener {
        public void onPreviousMonthSelected(Calendar startCalendar, Calendar endCalendar);
        public void onNextMonthSelected(Calendar startCalendar, Calendar endCalendar);
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener selectedListener) {
        this.monthSelectedListener = selectedListener;
    }

    public static interface OnDayClickListener {
        void onItemClick(Date date, View view);
    }
}

