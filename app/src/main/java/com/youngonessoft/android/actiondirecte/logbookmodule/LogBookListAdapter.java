package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;

/**
 * Created by Bobek on 12/11/2017.
 */

public class LogBookListAdapter extends CursorAdapter {

    Context mContext;
    /** Resource ID for the background color for this list of words */
    private int mTrainingColorResourceId;
    private int mClimbingColorResourceId;

    public LogBookListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.log_book_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String titleText = null;
        String gradeText = null;
        String info1Text = null;
        String info2Text = null;
        String info3Text = null;

        // check if climb or training
        int logTag = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB));
        // get the foreign key row ID
        int rowID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID));

        if (logTag == DatabaseContract.IS_CLIMB) {
            Bundle bundle = DatabaseReadWrite.EditClimbLoadEntry(rowID, context);

            titleText = bundle.getString("outputRouteName");
            info1Text = bundle.getString("outputLocationName");
            int firstAscentCode = bundle.getInt("outputFirstAscent");
            if (firstAscentCode == DatabaseContract.FIRSTASCENT_TRUE) {
                info3Text = "First Ascent";
                view.findViewById(R.id.trophy_icon).setVisibility(View.VISIBLE);
                view.findViewById(R.id.trophy_text).setVisibility(View.VISIBLE);
            } else {
                info3Text = "Repeat Ascent";
                view.findViewById(R.id.trophy_icon).setVisibility(View.GONE);
                view.findViewById(R.id.trophy_text).setVisibility(View.GONE);
            }
            int gradeCode = bundle.getInt("outputGradeNumber");
            gradeText = DatabaseReadWrite.getGradeTextClimb(gradeCode, context);
            int outputAscent = bundle.getInt("outputAscent");
            info2Text = DatabaseReadWrite.getAscentNameTextClimb(outputAscent, context);
            Log.i("LogBookListAdapter", "Route Name: " + titleText + " | GradeCode: " + gradeCode + " | GradeText: " + gradeText);


        } else if (logTag == DatabaseContract.IS_WORKOUT) {
            Bundle bundle = DatabaseReadWrite.EditWorkoutLoadEntry(rowID, context);
            //Item is a training session, not a climb
            int workoutCode = bundle.getInt("outputWorkoutCode");
            titleText = DatabaseReadWrite.getWorkoutTextClimb(workoutCode, context);
            info1Text = "Sets x Reps: " + bundle.getInt("outputSetCount") + "x" + bundle.getInt("outputRepCount");
            info2Text = "Additional Weight: " + bundle.getDouble("outputWeight") + " Kg";
            info3Text = "...";
            gradeText = "WK";
            view.findViewById(R.id.trophy_icon).setVisibility(View.GONE);
            view.findViewById(R.id.trophy_text).setVisibility(View.GONE);
            //TODO: add logic so that correct information is displayed for this item
        }

        TextView titleTextView = (TextView) view.findViewById(R.id.log_book_list_item_title);
        titleTextView.setText(titleText);

        TextView info1TextView = (TextView) view.findViewById(R.id.log_book_list_item_info1);
        info1TextView.setText(info1Text);

        TextView info2TextView = (TextView) view.findViewById(R.id.log_book_list_item_info2);
        info2TextView.setText(info2Text);

        TextView info3TextView = (TextView) view.findViewById(R.id.log_book_list_item_info3);
        info3TextView.setText(info3Text);

        TextView itemDivider = (TextView) view.findViewById(R.id.list_item_divider);
        ImageView iconView = (ImageView) view.findViewById(R.id.log_book_list_item_icon_image);
        TextView gradeTextView = (TextView) view.findViewById(R.id.log_book_list_item_icon);
        if (logTag == DatabaseContract.IS_CLIMB) {
            // Find the color that the resource ID maps to
            int color = ContextCompat.getColor(context, R.color.colorClimbingItemsV2);
            // Set the background color of the text container View
            itemDivider.setBackgroundColor(color);
            // Unhide the grade view value & display
            gradeTextView.setVisibility(View.VISIBLE);
            gradeTextView.setText(gradeText);
            iconView.setImageResource(R.drawable.icons_drawstringbag96);
        } else if (logTag == DatabaseContract.IS_WORKOUT) {
            // Find the color that the resource ID maps to
            int color = ContextCompat.getColor(context, R.color.colorTrainingClimbItemsV2);
            // Set the background color of the text container View
            itemDivider.setBackgroundColor(color);
            // Hide the grade view value
            gradeTextView.setVisibility(View.GONE);
            iconView.setImageResource(R.drawable.icons_weightlifting96);
        }
    }


}


