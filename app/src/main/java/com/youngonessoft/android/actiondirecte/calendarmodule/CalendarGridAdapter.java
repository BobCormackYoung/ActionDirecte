package com.youngonessoft.android.actiondirecte.calendarmodule;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youngonessoft.android.actiondirecte.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarGridAdapter extends ArrayAdapter<MonthDays> {

    private final String LOG_TAG = getClass().getSimpleName();
    private final int LOG_SWITCH = 1; //1=log, 0=no log

    private LayoutInflater mLayoutInflater;
    private int mMonth;
    private CalendarProperties mCalendarProperties;

    CalendarGridAdapter(Context context, CalendarProperties calendarProperties, ArrayList<MonthDays> dates, int month) {
        super(context, calendarProperties.getItemLayoutResource(), dates);

        mCalendarProperties = calendarProperties;
        if (month < 0) {
            mMonth = 11; //December = is month -1
        } else {
            mMonth = month; //all other months are correct
        }
        //mMonth = month < 0 ? 11 : month;
        mLayoutInflater = LayoutInflater.from(context);

        if (LOG_SWITCH == 1) {
            Log.i(LOG_TAG, "Month: " + month + ", mMonth: " + mMonth);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        //Log.i(LOG_TAG,"in getView");

        if (view == null) {
            view = mLayoutInflater.inflate(mCalendarProperties.getItemLayoutResource(), parent, false);
        }

        MonthDays monthDay = getItem(position);

        TextView dayLabel = (TextView) view.findViewById(R.id.tv_daynumber);
        LinearLayout climbTextViewHolder = (LinearLayout) view.findViewById(R.id.calendar_climb_symbol_holder);
        TextView climbTextView = (TextView) view.findViewById(R.id.calendar_climb_symbol);
        LinearLayout workoutTextViewHolder = (LinearLayout) view.findViewById(R.id.calendar_workout_symbol_holder);
        TextView workoutTextView = (TextView) view.findViewById(R.id.calendar_workout_symbol);
        LinearLayout workoutClimbTextViewHolder = (LinearLayout) view.findViewById(R.id.calendar_workoutclimb_symbol_holder);
        TextView workoutClimbTextView = (TextView) view.findViewById(R.id.calendar_workoutclimb_symbol);

        Calendar day = new GregorianCalendar();
        day.setTime(monthDay.getDate());

        dayLabel.setText(String.valueOf(day.get(Calendar.DAY_OF_MONTH)));

        if (isCurrentMonthDay(day) == true) {
            if (isCurrentDay(day) == true) {
                dayLabel.setTypeface(Typeface.DEFAULT_BOLD, 1); // BOLD if today
            } else {
                dayLabel.setTypeface(Typeface.DEFAULT, 0); // NORMAL is this month
            }
        } else {
            dayLabel.setTypeface(Typeface.DEFAULT, 2); // ITALICS if bounding months
        }

        if (monthDay.getFlagClimbing() == 0) {
            climbTextViewHolder.setVisibility(View.GONE);
        } else {
            climbTextViewHolder.setVisibility(View.VISIBLE);
            climbTextView.setBackgroundResource(R.drawable.climb_circle);
        }

        if (monthDay.getFlagWorkout() == 0) {
            workoutTextViewHolder.setVisibility(View.GONE);
        } else {
            workoutTextViewHolder.setVisibility(View.VISIBLE);
            workoutTextView.setBackgroundResource(R.drawable.workout_circle);
        }

        if (monthDay.getFlagWorkoutClimb() == 0) {
            workoutClimbTextViewHolder.setVisibility(View.GONE);
        } else {
            workoutClimbTextViewHolder.setVisibility(View.VISIBLE);
            workoutClimbTextView.setBackgroundResource(R.drawable.workoutclimb_circle);
        }

        return view;
    }

    private boolean isCurrentMonthDay(Calendar day) {
        return day.get(Calendar.MONTH) == mMonth;
    }

    private boolean isCurrentDay(Calendar day) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);

        return day.getTimeInMillis() == today.getTimeInMillis();
    }

}
