package com.youngonessoft.android.actiondirecte.logbookmodule.workoutpicker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract.WorkoutListEntry;

/**
 * Created by Bobek on 01/02/2018.
 */

public class ChildWorkoutAdapter extends CursorAdapter {

    public ChildWorkoutAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_text);
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow(WorkoutListEntry.COLUMN_NAME));
        // Populate fields with extracted properties
        listItemText.setText(body);
    }
}