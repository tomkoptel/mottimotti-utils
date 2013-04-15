package com.mottimotti.monthview;


import android.util.MonthDisplayHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarWeek {
    private List<CalendarDay> days = new ArrayList<CalendarDay>();

    public CalendarWeek(MonthDisplayHelper monthDisplayHelper, int week) {
        CalendarFilter filter = new CalendarFilter(monthDisplayHelper);
        Calendar startCalendar = filter.getStartCalendar();
        startCalendar.add(Calendar.WEEK_OF_MONTH, week);

        collectCalendarDays(startCalendar);
    }

    private void collectCalendarDays(Calendar startCalendar) {
        CalendarDay calendarDay;
        Calendar currentCalendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            int dayOfMonth = startCalendar.get(Calendar.DAY_OF_MONTH);
            calendarDay = new CalendarDay();
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }
}
