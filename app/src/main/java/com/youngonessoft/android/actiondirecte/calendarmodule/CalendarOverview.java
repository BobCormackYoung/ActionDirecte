package com.youngonessoft.android.actiondirecte.calendarmodule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.util.TimeUtils;

import java.util.Calendar;

public class CalendarOverview extends AppCompatActivity {

    public static final int CALENDAR_SIZE = 2401; //total calendar size (100years either side)
    public static final int CALENDAR_MID_PAGE = CALENDAR_SIZE / 2; //find location of the mid-page
    private final String LOG_TAG = getClass().getSimpleName();
    public TextView btPrevious;
    public TextView tvMonth;
    public TextView btNext;
    private CalendarProperties mCalendarProperties;
    private CalendarPagerAdapter mCalendarPagerAdapter;
    private int mCurrentPage = CALENDAR_MID_PAGE; //set the initial current page to the mid page
    private ViewPager mViewPager;
    private Context mContext;

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // do nothing
        }

        @Override
        public void onPageSelected(int position) {
            // Get Calendar object instance
            Calendar calendar = (Calendar) mCalendarProperties.getCurrentDate().clone();

            // Add months to Calendar (a number of months depends on ViewPager position)
            int initialPosition = mCalendarProperties.getInitialPosition();
            calendar.add(Calendar.MONTH, (position - initialPosition));
            long dateInMillis = calendar.getTimeInMillis();
            String dateFormatted = TimeUtils.convertDate(dateInMillis, "MM-yyyy");
            tvMonth.setText(dateFormatted);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // do nothing
        }
    };
    View.OnClickListener onClickListenerNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int currentPosition = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(currentPosition + 1, true);
        }
    };
    View.OnClickListener getOnClickListenerPrevious = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int currentPosition = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(currentPosition - 1, true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_overview);

        mContext = this;

        btPrevious = findViewById(R.id.cal_button_previous_day);
        tvMonth = findViewById(R.id.cal_textview_date);
        btNext = findViewById(R.id.cal_button_next_day);

        mCalendarProperties = new CalendarProperties();
        mCalendarProperties.setCalendarSize(CALENDAR_SIZE);
        mCalendarProperties.setInitialPosition(CALENDAR_MID_PAGE);
        mCalendarProperties.setDisplayedPosition(mCurrentPage);
        mCalendarProperties.setItemLayoutResource(R.layout.gridview_item_calendar_day);

        mViewPager = findViewById(R.id.vp_calendar); //find the viewpager
        mCalendarPagerAdapter = new CalendarPagerAdapter(mContext, mCalendarProperties); //create the new pageradapter
        mViewPager.setAdapter(mCalendarPagerAdapter); //set the pageradapter for our new view pager

        mViewPager.setCurrentItem(CALENDAR_MID_PAGE);

        mViewPager.addOnPageChangeListener(onPageChangeListener);
        btPrevious.setOnClickListener(getOnClickListenerPrevious);
        btNext.setOnClickListener(onClickListenerNext);

        Calendar calendar = (Calendar) mCalendarProperties.getCurrentDate().clone();
        long dateInMillis = calendar.getTimeInMillis();
        String dateFormatted = TimeUtils.convertDate(dateInMillis, "MM-yyyy");
        tvMonth.setText(dateFormatted);
    }
}
