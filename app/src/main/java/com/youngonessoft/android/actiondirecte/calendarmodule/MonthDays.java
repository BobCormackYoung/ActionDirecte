package com.youngonessoft.android.actiondirecte.calendarmodule;

import java.util.Date;

/**
 * Created by Bobek on 27/03/2018.
 */

public class MonthDays {

    private Date mDate;
    private int mFlagClimbing;
    private int mFlagWorkout;
    private int mFlagWorkoutClimb;

    public MonthDays(Date date, int flagClimbing, int flagWorkout, int flagWorkoutClimb) {
        mDate = date;
        mFlagClimbing = flagClimbing;
        mFlagWorkout = flagWorkout;
        mFlagWorkoutClimb = flagWorkoutClimb;
    }

    public Date getDate() {
        return mDate;
    }

    public int getFlagClimbing() {
        return mFlagClimbing;
    }

    public int getFlagWorkout() {
        return mFlagWorkout;
    }

    public int getFlagWorkoutClimb() {
        return mFlagWorkoutClimb;
    }

}
