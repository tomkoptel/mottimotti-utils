package com.edgark.monthview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.*;

/**
 * Created by.
 * User: EdgarK
 * Date: 3/19/13
 * Time: 2:46 PM
 */
public class MonthView extends LinearLayout implements AdapterView.OnItemClickListener {
    private SAutoLayerTextView curTv;
    private GridView headGrid, bodyGrid;

    private Integer calendar_today_resource,
            calendar_has_event_resource,
            calendar_other_month_resource,
            calendar_usual_resource,
            calendar_items_resource,
            calendar_head_resource;

    int month, year;

    private OnDayClickListener onDayClickListener;
    private OnMonthSelectedListener monthSelectedListener;
    private List<Date> dates = new ArrayList<Date>();

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
        setBackgroundColor(Color.WHITE);
        setOrientation(LinearLayout.VERTICAL);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);



        createTopView();

        headGrid = new GridView(getContext());
        headGrid.setNumColumns(7);
        addView(headGrid);

        bodyGrid = new GridView(getContext());
        bodyGrid.setNumColumns(7);
        bodyGrid.setOnItemClickListener(this);
        addView(bodyGrid);

        prepare();
    }

    private void createTopView() {
        LinearLayout topLayout = createTopLayout();

        View prevTv = makeTv(1f, "<");
        topLayout.addView(prevTv);

        curTv = makeTv(8f, "Month");
        topLayout.addView(curTv);

        View nextTv = makeTv(1f, ">");
        topLayout.addView(nextTv);

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

        addView(topLayout);
    }

    private LinearLayout createTopLayout() {
        int wrap_content = LayoutParams.WRAP_CONTENT;
        int match_parent = LayoutParams.MATCH_PARENT;
        LayoutParams params = new LayoutParams(match_parent, wrap_content);
        params.setMargins(0, 10, 0, 10);

        LinearLayout topLayout = new LinearLayout(getContext());
        topLayout.setOrientation(LinearLayout.HORIZONTAL);
        topLayout.setLayoutParams(params);
        return topLayout;
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

    private SAutoLayerTextView makeTv(float weight, String text) {
        int wrap_content = LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wrap_content, wrap_content, weight);

        SAutoLayerTextView view = new SAutoLayerTextView(getContext());

        view.setClickable(true);
        view.setLayoutParams(params);
        view.setText(text);
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
        ((CalendarArrayAdapter) bodyGrid.getAdapter()).notifyDataSetInvalidated();
    }

    private void prepare(CustomMonthDisplayHelper customDisplayHelper) {
        CalendarArrayAdapter headAdapter = new CalendarArrayAdapter(getContext(), dayNames);
        headAdapter.setHead(true);
        headGrid.setAdapter(headAdapter);

        ArrayList<CalendarItem> values = new ArrayList<CalendarItem>();

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


        CalendarArrayAdapter adapter = new CalendarArrayAdapter<CalendarItem>(getContext(), values);
        bodyGrid.setAdapter(adapter);
    }

    public SAutoLayerTextView getCurTv() {
        return curTv;
    }

    public List<String> getDayNames() {
        return dayNames;
    }

    public void setDayNames(String[] dayNames) {
        this.dayNames = Arrays.asList(dayNames);
    }

    public void setMonthNames(String[] monthNames) {
        this.monthNames = monthNames;
    }

    public void setCalendar_today_resource(Integer calendar_today_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resource. Set items layout first");
        this.calendar_today_resource = calendar_today_resource;
    }

    public void setCalendar_has_event_resource(Integer calendar_has_event_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resource. Set items layout first");
        this.calendar_has_event_resource = calendar_has_event_resource;
    }

    public void setCalendar_other_month_resource(Integer calendar_other_month_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resource. Set items layout first");
        this.calendar_other_month_resource = calendar_other_month_resource;
    }

    public void setCalendar_usual_resource(Integer calendar_usual_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resource. Set items layout first");
        this.calendar_usual_resource = calendar_usual_resource;
    }

    public void setCalendar_items_resource(Integer calendar_items_resource) {
        this.calendar_items_resource = calendar_items_resource;
    }

    public void setCalendar_head_resource(Integer calendar_head_resource) throws SetItemsResourceFirst {
        if (calendar_items_resource == null)
            throw new SetItemsResourceFirst("Failed to set head resource. Set items layout first");
        this.calendar_head_resource = calendar_head_resource;
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

    private final class CalendarArrayAdapter<T> extends ArrayAdapter<T> {
        private int selected;
        private boolean head;
        private final LayoutInflater inflater;
        private final int screenWidth;

        public CalendarArrayAdapter(Context context, List<T> values) {
            super(context, android.R.layout.test_list_item, values);
            inflater = LayoutInflater.from(getContext());
            screenWidth = getScreenSize().x;
        }

        private Point getScreenSize() {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            return getSize(display);
        }

        private Point getSize(Display display) {
            Point size = new Point();

            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
                size.set(display.getWidth(), display.getHeight());
            } else {
                display.getSize(size);
            }
            return size;
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
            final SAutoLayerTextView cell;
            LinearLayout cells = null;
            if (calendar_items_resource != null) {
                cells = (LinearLayout) inflater.inflate(calendar_items_resource, parent, false);
            }

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


            return layout;
        }

        private SAutoLayerTextView makeCell(View parent, Integer textView, int textColor, int shadowColor, int grStart, int grEnd) {
            SAutoLayerTextView view;
            if (textView != null && parent != null) {
                view = (SAutoLayerTextView) parent.findViewById(textView);
            } else {
                view = new SAutoLayerTextView(getContext());
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

        public void setCurrentMonth(boolean currentMonth) {
            this.currentMonth = currentMonth;
        }

        public void setMonthNumber(int monthNumber) {
            this.monthNumber = monthNumber;
        }

        public void setYearNumber(int yearNumber) {
            this.yearNumber = yearNumber;
        }

        private Calendar getDate() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, yearNumber);
            calendar.set(Calendar.MONTH, monthNumber);
            calendar.set(Calendar.DAY_OF_MONTH, number);
            calendar.getTimeInMillis();
            return calendar;
        }

        public void setHasEvent(boolean hasEvent) {
            this.hasEvent = hasEvent;
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
            return (c.get(Calendar.YEAR) == getYear() &&
                    c.get(Calendar.DAY_OF_MONTH) == getDayAt(row, column) &&
                    c.get(Calendar.MONTH) == getMonth(row, column));
        }

        public boolean hasEvt(int row, int column) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, getYear(row, column));
            c.set(Calendar.MONTH, getMonth(row, column));
            c.set(Calendar.DAY_OF_MONTH, getDayAt(row, column));
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.getTimeInMillis();

            return (dates.indexOf(c.getTime()) > -1);
        }
    }

    public interface OnMonthSelectedListener {
        public void onPreviousMonthSelected(Calendar startCalendar, Calendar endCalendar);

        public void onNextMonthSelected(Calendar startCalendar, Calendar endCalendar);
    }

    public void setOnMonthSelectedListener(OnMonthSelectedListener selectedListener) {
        this.monthSelectedListener = selectedListener;
    }

    public static interface OnDayClickListener {
        void onItemClick(Calendar date);
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (onDayClickListener != null) {
            CalendarArrayAdapter adapter = (CalendarArrayAdapter) adapterView.getAdapter();
            CalendarItem item = (CalendarItem) adapter.getItem(i);
            onDayClickListener.onItemClick(item.getDate());
        }
    }
}

