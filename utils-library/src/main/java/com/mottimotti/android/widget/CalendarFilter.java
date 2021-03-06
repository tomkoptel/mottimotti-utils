package com.mottimotti.android.widget;

import android.util.MonthDisplayHelper;

import java.util.Calendar;

public class CalendarFilter {
    private final MonthDisplayHelper helper;
    private final int startDay;
    private final int endDay;
    private final int firstDayInLastWeek;

    public CalendarFilter(MonthDisplayHelper helper) {
        this.helper = helper;
        this.startDay = helper.getDayAt(0, 0);
        if(helper.isWithinCurrentMonth(5, 0)) {
            this.firstDayInLastWeek = helper.getDayAt(5, 0);
            this.endDay = helper.getDayAt(5, 6);
        } else {
            this.firstDayInLastWeek = helper.getDayAt(4, 0);
            this.endDay = helper.getDayAt(4, 6);
        }
    }

    public Calendar getStartCalendar() {
        return getFilters()[0];
    }

    public Calendar[] getFilters() {
        if (previousMonthVisible() && nextMonthVisible()) {
            return getFiltersForPastAndNextMonth();
        } else if (previousMonthVisible() && !nextMonthVisible()) {
            return getFiltersForPastAndCurrentMonth();
        } else if (!previousMonthVisible() && nextMonthVisible()) {
            return getFiltersForCurrentAndNext();
        } else {
            return getFiltersForCurrentMonth();
        }
    }

    public boolean nextMonthVisible() {
        int daysInMonth = helper.getNumberOfDaysInMonth();
        return (firstDayInLastWeek + 6 != daysInMonth);
    }

    public boolean previousMonthVisible() {
        return helper.getOffset() > 0;
    }

    private Calendar[] getFiltersForPastAndNextMonth() {
        Calendar previousCalendar = getCurrentCalendar();
        previousCalendar.add(Calendar.MONTH, -1);
        previousCalendar.set(Calendar.DAY_OF_MONTH, startDay);
        previousCalendar.getTimeInMillis();

        Calendar nextCalendar = getCurrentCalendar();
        nextCalendar.add(Calendar.MONTH, 1);
        nextCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        nextCalendar.getTimeInMillis();

        return new Calendar[]{previousCalendar, nextCalendar};
    }

    private Calendar[] getFiltersForPastAndCurrentMonth() {
        Calendar previousCalendar = getCurrentCalendar();
        previousCalendar.add(Calendar.MONTH, -1);
        previousCalendar.set(Calendar.DAY_OF_MONTH, startDay);
        previousCalendar.getTimeInMillis();

        Calendar currentCalendar = getCurrentCalendar();
        currentCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        currentCalendar.getTimeInMillis();

        return new Calendar[]{previousCalendar, currentCalendar};
    }

    private Calendar[] getFiltersForCurrentAndNext() {
        Calendar currentCalendar = getCurrentCalendar();
        currentCalendar.set(Calendar.DAY_OF_MONTH, startDay);
        currentCalendar.getTimeInMillis();

        Calendar nextCalendar = getCurrentCalendar();
        nextCalendar.add(Calendar.MONTH, 1);
        nextCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        nextCalendar.getTimeInMillis();

        return new Calendar[]{currentCalendar, nextCalendar};
    }

    private Calendar[] getFiltersForCurrentMonth() {
        Calendar startCalendar = getCurrentCalendar();
        startCalendar.set(Calendar.DAY_OF_MONTH, startDay);
        startCalendar.getTimeInMillis();

        Calendar endCalendar = getCurrentCalendar();
        startCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        startCalendar.getTimeInMillis();

        return new Calendar[]{startCalendar, endCalendar};
    }

    private Calendar getCurrentCalendar() {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.set(Calendar.YEAR, helper.getYear());
        currentCalendar.set(Calendar.MONTH, helper.getMonth());
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        currentCalendar.getTimeInMillis();
        return currentCalendar;
    }
}
