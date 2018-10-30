package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;
import com.youngonessoft.android.actiondirecte.util.TimeUtils;

import java.util.ArrayList;

public class CopyWorkoutListAdapter extends CursorAdapter {

    private Cursor cursor;
    private ArrayList<CheckedArrayItem> itemChecked = new ArrayList<CheckedArrayItem>();
    private ArrayList<ExpandedArrayItem> itemExpanded = new ArrayList<ExpandedArrayItem>();

    public CopyWorkoutListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.cursor=cursor;
        for (int i = 0; i < this.getCount(); i++) {
            cursor.moveToPosition(i);
            int rowID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID));
            itemChecked.add(i, new CheckedArrayItem(rowID, false)); // initializes all items value with false
            itemExpanded.add(i, new ExpandedArrayItem(rowID, false)); // initializes all items value with false
        }
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.copy_workout_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //map all views
        TextView titleTextView = view.findViewById(R.id.tv_copy_workout_list_item);
        final LinearLayout dataDisplayWrapper = view.findViewById(R.id.ll_copyworkout_workoutdata);
        final ImageView expandableArrow = view.findViewById(R.id.iv_copy_workout_expandable);
        CheckBox checkBox = view.findViewById(R.id.cb_copy_workout_list_item);
        LinearLayout dataWrapper1 = view.findViewById(R.id.ll_copyworkout_data1_wrapper);
        LinearLayout dataWrapper2 = view.findViewById(R.id.ll_copyworkout_data2_wrapper);
        LinearLayout dataWrapper3 = view.findViewById(R.id.ll_copyworkout_data3_wrapper);
        LinearLayout dataWrapper4 = view.findViewById(R.id.ll_copyworkout_data4_wrapper);
        LinearLayout dataWrapper5 = view.findViewById(R.id.ll_copyworkout_data5_wrapper);
        LinearLayout dataWrapper6 = view.findViewById(R.id.ll_copyworkout_data6_wrapper);
        LinearLayout dataWrapper7 = view.findViewById(R.id.ll_copyworkout_data7_wrapper);
        LinearLayout dataWrapper8 = view.findViewById(R.id.ll_copyworkout_data8_wrapper);
        LinearLayout dataWrapper9 = view.findViewById(R.id.ll_copyworkout_data9_wrapper);
        TextView dataValue1 = view.findViewById(R.id.tv_copyworkout_data1);
        TextView dataValue2 = view.findViewById(R.id.tv_copyworkout_data2);
        TextView dataValue3 = view.findViewById(R.id.tv_copyworkout_data3);
        TextView dataValue4 = view.findViewById(R.id.tv_copyworkout_data4);
        TextView dataValue5 = view.findViewById(R.id.tv_copyworkout_data5);
        TextView dataValue6 = view.findViewById(R.id.tv_copyworkout_data6);
        TextView dataValue7 = view.findViewById(R.id.tv_copyworkout_data7);
        TextView dataValue8 = view.findViewById(R.id.tv_copyworkout_data8);
        ImageView dataValue9 = view.findViewById(R.id.iv_copyworkout_data9);

        final int pos = cursor.getPosition();

        // get the foreign key row ID
        final int rowID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID));

        Bundle workoutDataBundle = DatabaseReadWrite.EditWorkoutLoadEntry(rowID, context);

        // update the workout name
        int workoutTypeCode = workoutDataBundle.getInt("outputWorkoutTypeCode");
        int workoutCode = workoutDataBundle.getInt("outputWorkoutCode");
        String outputStringWorkoutName = DatabaseReadWrite.getWorkoutTextClimb(workoutCode, context);
        String outputStringWorkoutType = DatabaseReadWrite.getWorkoutTypeClimb(workoutTypeCode, context);
        titleTextView.setText(outputStringWorkoutType + " | " + outputStringWorkoutName);

        // show & update workout data fields
        Bundle workoutFieldsBundle = DatabaseReadWrite.workoutLoadFields(workoutCode, context);

        int outputIsSetCount = workoutFieldsBundle.getInt("outputIsSetCount");
        int outputIsRepCountPerSet = workoutFieldsBundle.getInt("outputIsRepCountPerSet");
        if (outputIsSetCount == DatabaseContract.IS_TRUE && outputIsRepCountPerSet == DatabaseContract.IS_TRUE) {
            dataWrapper1.setVisibility(View.VISIBLE);
            dataValue1.setText(Integer.toString(workoutDataBundle.getInt("outputRepCount")) + " x " + Integer.toString(workoutDataBundle.getInt("outputSetCount")));
        } else if(outputIsSetCount == DatabaseContract.IS_TRUE && outputIsRepCountPerSet == DatabaseContract.IS_FALSE) {
            dataWrapper1.setVisibility(View.VISIBLE);
            dataValue1.setText("1 x " + Integer.toString(workoutDataBundle.getInt("outputSetCount")));
        } else {
            dataWrapper1.setVisibility(View.GONE);
        }

        int outputRepDurationPerSet = workoutFieldsBundle.getInt("outputRepDurationPerSet");
        if (outputRepDurationPerSet == DatabaseContract.IS_TRUE) {
            dataWrapper2.setVisibility(View.VISIBLE);
            dataValue2.setText(TimeUtils.convertDate(workoutDataBundle.getInt("outputRepDuration"), "mm:ss"));
        } else {
            dataWrapper2.setVisibility(View.GONE);
        }


        int outputIsRestDuratonPerSet = workoutFieldsBundle.getInt("outputIsRestDuratonPerSet");
        if (outputIsRestDuratonPerSet == DatabaseContract.IS_TRUE) {
            dataWrapper3.setVisibility(View.VISIBLE);
            dataValue3.setText(TimeUtils.convertDate(workoutDataBundle.getInt("outputRestDuration"), "mm:ss"));
        } else {
            dataWrapper3.setVisibility(View.GONE);
        }

        int outputIsWeight = workoutFieldsBundle.getInt("outputIsWeight");
        if (outputIsWeight == DatabaseContract.IS_TRUE) {
            dataWrapper4.setVisibility(View.VISIBLE);
            dataValue4.setText(Double.toString(workoutDataBundle.getDouble("outputWeight")) + " Kg");
        } else {
            dataWrapper4.setVisibility(View.GONE);
        }

        int outputIsGradeCode = workoutFieldsBundle.getInt("outputIsGradeCode");
        if (outputIsGradeCode == DatabaseContract.IS_TRUE) {
            dataWrapper5.setVisibility(View.VISIBLE);
            if (workoutDataBundle.getInt("outputGradeCode") == -1 || workoutDataBundle.getInt("outputGradeCode") == 0) {
                dataValue5.setText("No Data");
            } else {
                String outputStringGradeName = DatabaseReadWrite.getGradeTextClimb(workoutDataBundle.getInt("outputGradeCode"), context);
                String outputStringGradeType = DatabaseReadWrite.getGradeTypeClimb(workoutDataBundle.getInt("outputGradeTypeCode"), context);
                dataValue5.setText(outputStringGradeType + " | " + outputStringGradeName);
            }
        } else {
            dataWrapper5.setVisibility(View.GONE);
        }

        int outputIsMoveCount = workoutFieldsBundle.getInt("outputIsMoveCount");
        if (outputIsMoveCount == DatabaseContract.IS_TRUE) {
            dataWrapper6.setVisibility(View.VISIBLE);
            dataValue6.setText(Integer.toString(workoutDataBundle.getInt("outputMoveCount")));
        } else {
            dataWrapper6.setVisibility(View.GONE);
        }

        int outputIsWallAngle = workoutFieldsBundle.getInt("outputIsWallAngle");
        if (outputIsWallAngle == DatabaseContract.IS_TRUE) {
            dataWrapper7.setVisibility(View.VISIBLE);
            dataValue7.setText(Integer.toString(workoutDataBundle.getInt("outputWallAngle")));
        } else {
            dataWrapper7.setVisibility(View.GONE);
        }

        int outputIsHoldType = workoutFieldsBundle.getInt("outputIsHoldType");
        if (outputIsHoldType == DatabaseContract.IS_TRUE) {
            dataWrapper8.setVisibility(View.VISIBLE);
            if (workoutDataBundle.getInt("outputHoldType") == -1 || workoutDataBundle.getInt("outputHoldType") == 0) {
                dataValue8.setText("No Data");
            } else {
                String outputStringHoldType = DatabaseReadWrite.getHoldTypeText(workoutDataBundle.getInt("outputHoldType"), context);
                dataValue8.setText(outputStringHoldType);
            }
        } else {
            dataWrapper8.setVisibility(View.GONE);
        }

        dataWrapper9.setVisibility(View.VISIBLE);
        if (workoutDataBundle.getInt("outputIsComplete")==DatabaseContract.IS_COMPLETE) {
            dataValue9.setImageResource(R.drawable.ic_baseline_check_box_checked_24px);
        } else {
            dataValue9.setImageResource(R.drawable.ic_baseline_check_box_unchecked_24px);
        }


        //need to hide the data display wrapper initially
        if (itemExpanded.get(pos).getIsExpanded()){
            dataDisplayWrapper.setVisibility(View.VISIBLE);
            expandableArrow.setImageResource(R.drawable.ic_baseline_expand_less_24px);
        } else {
            dataDisplayWrapper.setVisibility(View.GONE);
            expandableArrow.setImageResource(R.drawable.ic_baseline_expand_more_24px);
        }

        //onlick for expanding the data display wrapper
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDataDisplayWrapper(dataDisplayWrapper, expandableArrow, pos, rowID);
            }
        });

        //onlick for expanding the data display wrapper
        expandableArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDataDisplayWrapper(dataDisplayWrapper, expandableArrow, pos, rowID);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = v.findViewById(R.id.cb_copy_workout_list_item);

                if (cb.isChecked()) {
                    itemChecked.set(pos, new CheckedArrayItem(rowID, true));
                    // do some operations here
                } else if (!cb.isChecked()) {
                    itemChecked.set(pos, new CheckedArrayItem(rowID, false));
                    // do some operations here
                }
            }
        });

        checkBox.setChecked(itemChecked.get(pos).getIsChecked());

    }

    public ArrayList<CheckedArrayItem> getCopyWorkoutListAdapterCheckedStatus() {
        return itemChecked;
    }

    private void toggleDataDisplayWrapper(View view, ImageView arrow, int pos, int rowID){

        if (itemExpanded.get(pos).getIsExpanded()){
            //item is expanded, un-expand it
            //view.findViewById(R.id.ll_copyworkout_workoutdata).setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.ic_baseline_expand_more_24px);
            itemExpanded.set(pos, new ExpandedArrayItem(rowID, false));
        } else if (!itemExpanded.get(pos).getIsExpanded()){
            //item is unexpanded, expand it
            //view.findViewById(R.id.ll_copyworkout_workoutdata).setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.ic_baseline_expand_less_24px);
            itemExpanded.set(pos, new ExpandedArrayItem(rowID, true));
        }

    }


}
