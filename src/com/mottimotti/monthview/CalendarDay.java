package com.mottimotti.monthview;

public class CalendarDay {
    private int day;
    private DayState state;

    public CalendarDay(int dayOfMonth, DayState state) {
        this.day = dayOfMonth;
        this.state = state;
    }

    public DayState getState() {
        return state;
    }

    @Override
    public String toString() {
        return String.valueOf(day);
    }
}
