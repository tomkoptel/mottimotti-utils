package com.mottimotti.android.widget.monthview;


import android.util.MonthDisplayHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class CalendarWeek {
    private final MonthDisplayHelper monthDisplayHelper;
    private List<CalendarDay> days = new ArrayList<CalendarDay>();
    private final boolean blockFutureDays;
    private final Calendar currentCalendar;
    private final Calendar helperCalendar;
    private List<Date> activeDates = new ArrayList<Date>();

    public CalendarWeek(CalendarTable table, int week) {
        this.monthDisplayHelper = table.getMonthDisplayHelper();
        this.blockFutureDays = table.isFutureDaysBlocked();
        this.activeDates = table.getActiveDates();
        this.currentCalendar = getCurrentCalendar();
        this.helperCalendar = getHelperCalendar();

        CalendarFilter filter = new CalendarFilter(monthDisplayHelper);
        Calendar startCalendar = filter.getStartCalendar();
        startCalendar.add(Calendar.WEEK_OF_MONTH, week);

        collectCalendarDays(startCalendar);
    }

    private void collectCalendarDays(Calendar startCalendar) {
        for (int i = 0; i < 7; i++) {
            DayState dayState = getDayState(startCalendar);
            CalendarDay calendarDay = new CalendarDay(startCalendar.getTime(), dayState);
            days.add(calendarDay);

            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            startCalendar.getTimeInMillis();
        }
    }

    private DayState getDayState(Calendar startCalendar) {
        // look up for active dates
        if (activeDates.indexOf(startCalendar.getTime()) != -1) {
            return DayState.ACTIVE;
        } else {
            if (blockFutureDays) {
                return getFilteredDayState(startCalendar);
            } else {
                return getGeneralDayState(startCalendar);
            }
        }
    }

    private DayState getFilteredDayState(Calendar startCalendar) {
        DayState dayState;

        Date currentDate = currentCalendar.getTime();
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);

        Date helperDate = helperCalendar.getTime();
        int helperMonth = helperCalendar.get(Calendar.MONTH);

        Date startDate = startCalendar.getTime();
        int startMonth = startCalendar.get(Calendar.MONTH);
        int startYear = startCalendar.get(Calendar.YEAR);

        // future year days will be filtered
        if (startYear <= currentYear) {
            // Days which goes after current month day wont be accessible
            if (startDate.compareTo(currentDate) > 0) {
                dayState = DayState.BLOCKED;
            } else {
                // This goes for offset days
                if (startDate.compareTo(helperDate) < 0) {
                    dayState = DayState.INACTIVE;
                    // This goes for days which follows next after first day of moth
                } else if (startDate.compareTo(helperDate) > 0) {
                    // This check whether day goes before current date
                    if (startDate.compareTo(currentDate) < 0) {
                        dayState = (startMonth == helperMonth) ? DayState.REGULAR : DayState.INACTIVE;
                        // This goes for day which is equal current one
                    } else if (startDate.compareTo(currentDate) == 0) {
                        // Current day can only reside in current month
                        dayState = (startMonth == currentMonth) ? DayState.CURRENT : DayState.REGULAR;
                    } else { // This goes for days which follow after current day
                        dayState = DayState.BLOCKED;
                    }
                } else { // This is the first day of month helper
                    dayState = DayState.REGULAR;
                }
            }
        } else {
            dayState = DayState.BLOCKED;
        }

        return dayState;
    }

    private DayState getGeneralDayState(Calendar startCalendar) {
        DayState dayState;

        Date currentDate = currentCalendar.getTime();
        int currentMonth = currentCalendar.get(Calendar.MONTH);

        Date helperDate = helperCalendar.getTime();
        int helperMonth = helperCalendar.get(Calendar.MONTH);

        Date startDate = startCalendar.getTime();
        int startMonth = startCalendar.get(Calendar.MONTH);

        // This goes for offset days
        if (startDate.compareTo(helperDate) < 0) {
            dayState = DayState.INACTIVE;
            // This goes for days which follows next after first day of moth
        } else if (startDate.compareTo(helperDate) > 0) {
            // This check whether day goes before current date
            if (startDate.compareTo(currentDate) < 0) {
                dayState = (startMonth == helperMonth) ? DayState.REGULAR : DayState.INACTIVE;
                // This goes for day which is equal current one
            } else if (startDate.compareTo(currentDate) == 0) {
                // Current day can only reside in current month
                dayState = (startMonth == currentMonth) ? DayState.CURRENT : DayState.REGULAR;
            } else { // This goes for days which follow after current day
                dayState = (startMonth == helperMonth) ? DayState.REGULAR : DayState.INACTIVE;
            }
        } else { // This is the first day of month helper
            dayState = DayState.REGULAR;
        }

        return dayState;
    }

    private Calendar getCurrentCalendar() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        mCalendar.getTimeInMillis();
        return mCalendar;
    }

    private Calendar getHelperCalendar() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, monthDisplayHelper.getYear());
        mCalendar.set(Calendar.MONTH, monthDisplayHelper.getMonth());
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        mCalendar.getTimeInMillis();
        return mCalendar;
    }

    public List<CalendarDay> getDays() {
        return days;
    }
}
