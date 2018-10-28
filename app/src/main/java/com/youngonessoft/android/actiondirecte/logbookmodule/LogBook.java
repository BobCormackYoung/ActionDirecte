package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseHelper;
import com.youngonessoft.android.actiondirecte.util.TimeUtils;

import java.util.Calendar;

//TODO: Still appears to be a bug when calling position of pager adapter for specific date

public class LogBook extends FragmentActivity {

    private static Context mContext;
    final int ADD_CLIMB_NEW = 0;
    final int ADD_CLIMB_EDIT = 1;
    final int ADD_WORKOUT_NEW = 0;
    final int ADD_WORKOUT_EDIT = 1;
    private CachingFragmentStatePagerAdapter adapterViewPager;
    final long DAYPERIOD = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_book);

        mContext = this;

        // Set header date to current date
        //Calendar currentDate = TimeUtils.FIRST_DAY_OF_TIME;
        Calendar currentDate = Calendar.getInstance();
        long outputDate = currentDate.getTimeInMillis();
        TextView header = findViewById(R.id.textview_date);
        header.setText(TimeUtils.convertDate(outputDate, "yyyy-MM-dd"));

        // Initialise viewPager
        final ViewPager vpPager = (ViewPager) findViewById(R.id.log_book_viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        // Set pager to current date
        vpPager.setCurrentItem(TimeUtils.getPositionForDay(currentDate));
        //Toast.makeText(getApplicationContext(), "Position = " + TimeUtils.getPositionForDay(currentDate), Toast.LENGTH_SHORT).show();

        // Set PageChangeListener
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // When new page is selected, udpate the header to match the new date
            @Override
            public void onPageSelected(int position) {
                Calendar cal = TimeUtils.getDayForPosition(position);
                TextView header = findViewById(R.id.textview_date);
                header.setText(TimeUtils.getFormattedDate(mContext, cal.getTimeInMillis()));
                //Toast.makeText(getApplicationContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Find all button views
        View button_previous_day = findViewById(R.id.button_previous_day);
        View button_next_day = findViewById(R.id.button_next_day);
        View button_add_workout = findViewById(R.id.button_add_workout);
        View button_log_climb = findViewById(R.id.button_log_climb);
        View button_copy_climb = findViewById(R.id.button_copy_workout);

        // Previous Day Button
        button_previous_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vpPager.getCurrentItem();
                int newPosition = position - 1;
                vpPager.setCurrentItem(newPosition, true);
            }
        });

        // Next Day Button
        button_next_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vpPager.getCurrentItem();
                int newPosition = position + 1;
                vpPager.setCurrentItem(newPosition, true);
            }
        });

        // Add Workout Button
        button_add_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the currently displayed page position
                // Get it's calendar instance (date)
                // Convert date to milliseconds
                int position = vpPager.getCurrentItem();
                Calendar cal = TimeUtils.getDayForPosition(position);
                long date = cal.getTimeInMillis();
                // Create intent to launch new activity
                // Attach extras which will:
                // 1) we're adding a new climb
                // 2) negative rowID as we're not reading in any database rows
                // 3) the date we're creating a record for (i.e. the date of the displayed page)
                Intent AddWorkoutIntent = new Intent(LogBook.this, AddWorkout.class);
                AddWorkoutIntent.putExtra("EditOrNewFlag", ADD_WORKOUT_NEW);
                AddWorkoutIntent.putExtra("RowID", -1);
                AddWorkoutIntent.putExtra("Date", date);
                // Start the new activity
                startActivity(AddWorkoutIntent);
            }
        });

        // Add Climb Button
        button_log_climb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the currently displayed page position
                // Get it's calendar instance (date)
                // Convert date to milliseconds
                int position = vpPager.getCurrentItem();
                Calendar cal = TimeUtils.getDayForPosition(position);
                long date = cal.getTimeInMillis();

                // Create intent to launch new activity
                // Attach extras which will:
                // 1) we're adding a new climb
                // 2) negative rowID as we're not reading in any database rows
                // 3) the date we're creating a record for (i.e. the date of the displayed page)
                Intent AddClimbIntent = new Intent(LogBook.this, AddClimb.class);
                AddClimbIntent.putExtra("EditOrNewFlag", ADD_CLIMB_NEW);
                AddClimbIntent.putExtra("RowID", -1);
                AddClimbIntent.putExtra("Date", date);
                // Start the new activity
                startActivity(AddClimbIntent);
            }
        });

        // Copy Climb Button
        button_copy_climb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                int position = vpPager.getCurrentItem();
                Calendar cal = TimeUtils.getDayForPosition(position);
                long date = cal.getTimeInMillis();

                DatabaseHelper tempHandler = new DatabaseHelper(mContext);
                SQLiteDatabase tempDatabase = tempHandler.getWritableDatabase();
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.setTimeInMillis(date);
                tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
                tempCalendar.set(Calendar.MINUTE, 0);
                tempCalendar.set(Calendar.SECOND, 0);
                tempCalendar.set(Calendar.MILLISECOND, 0);

                long dayStart = tempCalendar.getTimeInMillis();
                long dayEnd = dayStart + DAYPERIOD;

                Cursor tempCursor = getCursorWorkoutsBetweenDates(dayStart, dayEnd, tempDatabase);

                int cursorCount = tempCursor.getCount();
                tempCursor.close();
                tempDatabase.close();
                tempHandler.close();

                if (cursorCount == 0) {
                    Toast.makeText(mContext, "No workouts to copy!", Toast.LENGTH_LONG).show();
                } else {
                    Intent CopyWorkoutIntent = new Intent(LogBook.this, CopyWorkout.class);
                    CopyWorkoutIntent.putExtra("Date", date);
                    // Start the new activity
                    startActivity(CopyWorkoutIntent);
                }
            }
        });

    }

    // Pager Adapter
    public static class MyPagerAdapter extends CachingFragmentStatePagerAdapter {

        private Calendar cal;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return TimeUtils.DAYS_OF_TIME;
        }

        @Override
        public Fragment getItem(int position) {
            long timeForPosition = TimeUtils.getDayForPosition(position).getTimeInMillis();
            return LogBookFragmentContent.newInstance(timeForPosition);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Calendar cal = TimeUtils.getDayForPosition(position);
            return TimeUtils.getFormattedDate(mContext, cal.getTimeInMillis());
        }


    }

    public Cursor getCursorWorkoutsBetweenDates(long dateStart, long dateEnd, SQLiteDatabase db) {

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_DATE,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB + "=? AND " + DatabaseContract.CalendarTrackerEntry.COLUMN_DATE + " BETWEEN ? AND ?";
        String[] whereValue = {String.valueOf(DatabaseContract.IS_WORKOUT), String.valueOf(dateStart), String.valueOf(dateEnd)};

        Cursor cursor = db.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        return cursor;
    }

}
