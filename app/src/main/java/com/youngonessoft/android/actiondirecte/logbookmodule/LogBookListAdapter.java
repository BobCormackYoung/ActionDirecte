package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;
import com.youngonessoft.android.actiondirecte.util.TimeUtils;

import java.util.ArrayList;

/**
 * Created by Bobek on 12/11/2017.
 */

public class LogBookListAdapter extends CursorAdapter {

    Context mContext;

    // static variables are bad, is there a workaround?
    public static ArrayList<ExpandedArrayItem> itemExpanded = new ArrayList<ExpandedArrayItem>();
    private Cursor cursor;

    public LogBookListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.cursor = cursor;
        for (int i = 0; i < this.getCount(); i++) {
            cursor.moveToPosition(i);
            int rowID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID));
            itemExpanded.add(i, new ExpandedArrayItem(rowID, false)); // initializes all items value with false
        }
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.log_book_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //map all views
        TextView titleTextView = view.findViewById(R.id.log_book_list_item_title);
        TextView subtitleTextView = view.findViewById(R.id.log_book_list_item_subtitle);
        ImageView titleIcon = view.findViewById(R.id.trophy_icon);
        TextView titleIconText = view.findViewById(R.id.trophy_text);
        final ImageView expandableArrow = view.findViewById(R.id.iv_logbook_expand);
        final LinearLayout dataDisplayWrapper = view.findViewById(R.id.ll_alldata_wrapper);
        final LinearLayout dataClimbingDisplayWrapper = view.findViewById(R.id.ll_logbook_climbing_data_wrapper);
        LinearLayout dataClimbingWrapper1 = view.findViewById(R.id.ll_logbook_climbing_data1_wrapper);
        LinearLayout dataClimbingWrapper2 = view.findViewById(R.id.ll_logbook_climbing_data2_wrapper);
        LinearLayout dataClimbingWrapper3 = view.findViewById(R.id.ll_logbook_climbing_data3_wrapper);
        LinearLayout dataClimbingWrapper4 = view.findViewById(R.id.ll_logbook_climbing_data4_wrapper);
        TextView dataClimbingValue1 = view.findViewById(R.id.tv_logbook_climbing_data1);
        TextView dataClimbingValue2 = view.findViewById(R.id.tv_logbook_climbing_data2);
        TextView dataClimbingValue3 = view.findViewById(R.id.tv_logbook_climbing_data3);
        ImageView dataClimbingValue4 = view.findViewById(R.id.iv_logbook_climbing_data4);

        final LinearLayout dataWorkoutDisplayWrapper = view.findViewById(R.id.ll_logbook_workout_data_wrapper);
        LinearLayout dataWorkoutWrapper1 = view.findViewById(R.id.ll_logbook_workout_data1_wrapper);
        LinearLayout dataWorkoutWrapper2 = view.findViewById(R.id.ll_logbook_workout_data2_wrapper);
        LinearLayout dataWorkoutWrapper3 = view.findViewById(R.id.ll_logbook_workout_data3_wrapper);
        LinearLayout dataWorkoutWrapper4 = view.findViewById(R.id.ll_logbook_workout_data4_wrapper);
        LinearLayout dataWorkoutWrapper5 = view.findViewById(R.id.ll_logbook_workout_data5_wrapper);
        LinearLayout dataWorkoutWrapper6 = view.findViewById(R.id.ll_logbook_workout_data6_wrapper);
        LinearLayout dataWorkoutWrapper7 = view.findViewById(R.id.ll_logbook_workout_data7_wrapper);
        LinearLayout dataWorkoutWrapper8 = view.findViewById(R.id.ll_logbook_workout_data8_wrapper);
        LinearLayout dataWorkoutWrapper9 = view.findViewById(R.id.ll_logbook_workout_data9_wrapper);
        TextView dataWorkoutValue1 = view.findViewById(R.id.tv_logbook_workout_data1);
        TextView dataWorkoutValue2 = view.findViewById(R.id.tv_logbook_workout_data2);
        TextView dataWorkoutValue3 = view.findViewById(R.id.tv_logbook_workout_data3);
        TextView dataWorkoutValue4 = view.findViewById(R.id.tv_logbook_workout_data4);
        TextView dataWorkoutValue5 = view.findViewById(R.id.tv_logbook_workout_data5);
        TextView dataWorkoutValue6 = view.findViewById(R.id.tv_logbook_workout_data6);
        TextView dataWorkoutValue7 = view.findViewById(R.id.tv_logbook_workout_data7);
        TextView dataWorkoutValue8 = view.findViewById(R.id.tv_logbook_workout_data8);
        ImageView dataWorkoutValue9 = view.findViewById(R.id.iv_logbook_workout_data9);

        TextView itemDivider = view.findViewById(R.id.list_item_divider);
        ImageView iconView = view.findViewById(R.id.log_book_list_item_icon_image);

        // check if climb or training
        int logTag = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB));
        // get the foreign key row ID
        final int rowID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID));
        // cursor position
        final int pos = cursor.getPosition();

        //need to hide the data display wrapper initially
        if (itemExpanded.get(pos).getIsExpanded()) {
            dataDisplayWrapper.setVisibility(View.VISIBLE);
            expandableArrow.setImageResource(R.drawable.ic_baseline_expand_less_24px);
        } else {
            dataDisplayWrapper.setVisibility(View.GONE);
            expandableArrow.setImageResource(R.drawable.ic_baseline_expand_more_24px);
        }

        if (logTag == DatabaseContract.IS_CLIMB) {

            dataClimbingDisplayWrapper.setVisibility(View.VISIBLE);
            dataWorkoutDisplayWrapper.setVisibility(View.GONE);

            Bundle climbingDataBundle = DatabaseReadWrite.EditClimbLoadEntry(rowID, context);
            titleTextView.setText(climbingDataBundle.getString("outputRouteName"));
            subtitleTextView.setText(DatabaseReadWrite.getGradeTypeClimb(climbingDataBundle.getInt("outputGradeName"), context) + " | " + DatabaseReadWrite.getGradeTextClimb(climbingDataBundle.getInt("outputGradeNumber"), context));
            dataClimbingValue1.setText(DatabaseReadWrite.getGradeTypeClimb(climbingDataBundle.getInt("outputGradeName"), context) + " | " + DatabaseReadWrite.getGradeTextClimb(climbingDataBundle.getInt("outputGradeNumber"), context));
            dataClimbingValue2.setText(climbingDataBundle.getString("outputLocationName"));
            dataClimbingValue3.setText(DatabaseReadWrite.getAscentNameTextClimb(climbingDataBundle.getInt("outputAscent"), context));
            int firstAscentCode = climbingDataBundle.getInt("outputFirstAscent");
            if (firstAscentCode == DatabaseContract.FIRSTASCENT_TRUE) {
                view.findViewById(R.id.trophy_icon).setVisibility(View.VISIBLE);
                view.findViewById(R.id.trophy_text).setVisibility(View.VISIBLE);
                dataClimbingValue4.setImageResource(R.drawable.ic_baseline_check_box_checked_24px);
            } else {
                view.findViewById(R.id.trophy_icon).setVisibility(View.GONE);
                view.findViewById(R.id.trophy_text).setVisibility(View.GONE);
                dataClimbingValue4.setImageResource(R.drawable.ic_baseline_check_box_unchecked_24px);
            }

            itemDivider.setBackgroundColor(ContextCompat.getColor(context, R.color.colorClimbingItemsV2));
            iconView.setImageResource(R.drawable.icons_drawstringbag96);

        } else if (logTag == DatabaseContract.IS_WORKOUT) {

            dataClimbingDisplayWrapper.setVisibility(View.GONE);
            dataWorkoutDisplayWrapper.setVisibility(View.VISIBLE);

            Bundle workoutDataBundle = DatabaseReadWrite.EditWorkoutLoadEntry(rowID, context);
            int workoutTypeCode = workoutDataBundle.getInt("outputWorkoutTypeCode");
            int workoutCode = workoutDataBundle.getInt("outputWorkoutCode");
            String outputStringWorkoutName = DatabaseReadWrite.getWorkoutTextClimb(workoutCode, context);
            String outputStringWorkoutType = DatabaseReadWrite.getWorkoutTypeClimb(workoutTypeCode, context);
            titleTextView.setText(outputStringWorkoutType + " | " + outputStringWorkoutName);
            subtitleTextView.setVisibility(View.GONE);

            Bundle workoutFieldsBundle = DatabaseReadWrite.workoutLoadFields(workoutCode, context);

            //Item is a training session, not a climb
            int outputIsSetCount = workoutFieldsBundle.getInt("outputIsSetCount");
            int outputIsRepCountPerSet = workoutFieldsBundle.getInt("outputIsRepCountPerSet");
            if (outputIsSetCount == DatabaseContract.IS_TRUE && outputIsRepCountPerSet == DatabaseContract.IS_TRUE) {
                dataWorkoutWrapper1.setVisibility(View.VISIBLE);
                dataWorkoutValue1.setText(Integer.toString(workoutDataBundle.getInt("outputRepCount")) + " x " + Integer.toString(workoutDataBundle.getInt("outputSetCount")));
            } else if (outputIsSetCount == DatabaseContract.IS_TRUE && outputIsRepCountPerSet == DatabaseContract.IS_FALSE) {
                dataWorkoutWrapper1.setVisibility(View.VISIBLE);
                dataWorkoutValue1.setText("1 x " + Integer.toString(workoutDataBundle.getInt("outputSetCount")));
            } else {
                dataWorkoutWrapper1.setVisibility(View.GONE);
            }

            int outputRepDurationPerSet = workoutFieldsBundle.getInt("outputRepDurationPerSet");
            if (outputRepDurationPerSet == DatabaseContract.IS_TRUE) {
                dataWorkoutWrapper2.setVisibility(View.VISIBLE);
                dataWorkoutValue2.setText(TimeUtils.convertDate(workoutDataBundle.getInt("outputRepDuration"), "mm:ss"));
            } else {
                dataWorkoutWrapper2.setVisibility(View.GONE);
            }

            int outputIsRestDuratonPerSet = workoutFieldsBundle.getInt("outputIsRestDuratonPerSet");
            if (outputIsRestDuratonPerSet == DatabaseContract.IS_TRUE) {
                dataWorkoutWrapper3.setVisibility(View.VISIBLE);
                dataWorkoutValue3.setText(TimeUtils.convertDate(workoutDataBundle.getInt("outputRestDuration"), "mm:ss"));
            } else {
                dataWorkoutWrapper3.setVisibility(View.GONE);
            }

            int outputIsWeight = workoutFieldsBundle.getInt("outputIsWeight");
            if (outputIsWeight == DatabaseContract.IS_TRUE) {
                dataWorkoutWrapper4.setVisibility(View.VISIBLE);
                dataWorkoutValue4.setText(Double.toString(workoutDataBundle.getDouble("outputWeight")) + " Kg");
            } else {
                dataWorkoutWrapper4.setVisibility(View.GONE);
            }

            int outputIsGradeCode = workoutFieldsBundle.getInt("outputIsGradeCode");
            if (outputIsGradeCode == DatabaseContract.IS_TRUE) {
                dataWorkoutWrapper5.setVisibility(View.VISIBLE);
                if (workoutDataBundle.getInt("outputGradeCode") == -1 || workoutDataBundle.getInt("outputGradeCode") == 0) {
                    dataWorkoutValue5.setText("No Data");
                } else {
                    String outputStringGradeName = DatabaseReadWrite.getGradeTextClimb(workoutDataBundle.getInt("outputGradeCode"), context);
                    String outputStringGradeType = DatabaseReadWrite.getGradeTypeClimb(workoutDataBundle.getInt("outputGradeTypeCode"), context);
                    dataWorkoutValue5.setText(outputStringGradeType + " | " + outputStringGradeName);
                    subtitleTextView.setVisibility(View.VISIBLE);
                    subtitleTextView.setText(outputStringGradeType + " | " + outputStringGradeName);
                }
            } else {
                dataWorkoutWrapper5.setVisibility(View.GONE);
            }

            int outputIsMoveCount = workoutFieldsBundle.getInt("outputIsMoveCount");
            if (outputIsMoveCount == DatabaseContract.IS_TRUE) {
                dataWorkoutWrapper6.setVisibility(View.VISIBLE);
                dataWorkoutValue6.setText(Integer.toString(workoutDataBundle.getInt("outputMoveCount")));
            } else {
                dataWorkoutWrapper6.setVisibility(View.GONE);
            }

            int outputIsWallAngle = workoutFieldsBundle.getInt("outputIsWallAngle");
            if (outputIsWallAngle == DatabaseContract.IS_TRUE) {
                dataWorkoutWrapper7.setVisibility(View.VISIBLE);
                dataWorkoutValue7.setText(Integer.toString(workoutDataBundle.getInt("outputWallAngle")));
            } else {
                dataWorkoutWrapper7.setVisibility(View.GONE);
            }

            int outputIsHoldType = workoutFieldsBundle.getInt("outputIsHoldType");
            if (outputIsHoldType == DatabaseContract.IS_TRUE) {
                dataWorkoutWrapper8.setVisibility(View.VISIBLE);
                if (workoutDataBundle.getInt("outputHoldType") == -1 || workoutDataBundle.getInt("outputHoldType") == 0) {
                    dataWorkoutValue8.setText("No Data");
                } else {
                    String outputStringHoldType = DatabaseReadWrite.getHoldTypeText(workoutDataBundle.getInt("outputHoldType"), context);
                    dataWorkoutValue8.setText(outputStringHoldType);
                }
            } else {
                dataWorkoutWrapper8.setVisibility(View.GONE);
            }

            dataWorkoutWrapper9.setVisibility(View.VISIBLE);
            if (workoutDataBundle.getInt("outputIsComplete") == DatabaseContract.IS_COMPLETE) {
                dataWorkoutValue9.setImageResource(R.drawable.ic_baseline_check_box_checked_24px);
            } else {
                dataWorkoutValue9.setImageResource(R.drawable.ic_baseline_check_box_unchecked_24px);
            }

            itemDivider.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTrainingClimbItemsV2));
            iconView.setImageResource(R.drawable.icons_weightlifting96);

            view.findViewById(R.id.trophy_icon).setVisibility(View.GONE);
            view.findViewById(R.id.trophy_text).setVisibility(View.GONE);
        }

        //onlick for expanding the data display wrapper
        /*  titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDataDisplayWrapper(dataDisplayWrapper, expandableArrow, pos, rowID);
            }
        });    */

        //onlick for expanding the data display wrapper
        expandableArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDataDisplayWrapper(dataDisplayWrapper, expandableArrow, pos, rowID);
            }
        });

    }

    private void toggleDataDisplayWrapper(View view, ImageView arrow, int pos, int rowID) {

        if (itemExpanded.get(pos).getIsExpanded()) {
            //item is expanded, un-expand it
            //view.findViewById(R.id.ll_copyworkout_workoutdata).setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.ic_baseline_expand_more_24px);
            itemExpanded.set(pos, new ExpandedArrayItem(rowID, false));
        } else if (!itemExpanded.get(pos).getIsExpanded()) {
            //item is unexpanded, expand it
            //view.findViewById(R.id.ll_copyworkout_workoutdata).setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.ic_baseline_expand_less_24px);
            itemExpanded.set(pos, new ExpandedArrayItem(rowID, true));
        }

    }
}


