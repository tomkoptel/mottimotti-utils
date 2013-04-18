package com.mottimotti.android.widget.monthview;

import java.util.Calendar;
import java.util.Date;

class CalendarDay {
    private final Calendar calendar;
    private final DayState state;

    public CalendarDay(Date date, DayState state) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.getTimeInMillis();

        this.state = state;
    }

    public DayState getState() {
        return state;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public String toString() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(day);
    }
}
