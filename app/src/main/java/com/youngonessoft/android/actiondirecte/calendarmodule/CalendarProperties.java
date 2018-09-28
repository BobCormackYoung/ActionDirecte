package com.youngonessoft.android.actiondirecte.calendarmodule;

import java.util.Calendar;

public class CalendarProperties {

    private int mCalendarSize = 0;
    private int mInitialPosition = 0;
    private int mDisplayedPosition = 0;
    private Calendar mCurrentDate = getCalendar();
    private int mItemLayoutResource;

    /**
     * getCalendar - returns the current date as Calendar setting to midnight
     *
     * @return
     */
    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    /**
     * getCalendarSize - returns the size of the calendar
     *
     * @return
     */
    public int getCalendarSize() {
        return mCalendarSize;
    }

    /**
     * setCalendarSize - sets the size of the calendar
     *
     * @param calendarSize
     */
    public void setCalendarSize(int calendarSize) {
        mCalendarSize = calendarSize;
    }

    /**
     * getInitialPosition - gets and returns the initial position
     *
     * @return
     */
    public int getInitialPosition() {
        return mInitialPosition;
    }

    /**
     * setInitialPosition - sets the initial position of the calendarpage
     *
     * @param initialPosition
     */
    public void setInitialPosition(int initialPosition) {
        mInitialPosition = initialPosition;
    }

    /**
     * getDisplayedPosition- returns the stored current position
     *
     * @return
     */
    public int getDisplayedPosition() {
        return mDisplayedPosition;
    }

    /**
     * setDisplayedPosition - sets the current position in the properties object
     *
     * @param currentPosition
     */
    public void setDisplayedPosition(int displayedPosition) {
        mDisplayedPosition = displayedPosition;
    }

    /**
     * getCurrentDate - returns the current date at midnight
     *
     * @return
     */
    public Calendar getCurrentDate() {
        return mCurrentDate;
    }

    public int getItemLayoutResource() {
        return mItemLayoutResource;
    }

    public void setItemLayoutResource(int itemLayoutResource) {
        mItemLayoutResource = itemLayoutResource;
    }
}
