package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseHelper;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;

import static com.youngonessoft.android.actiondirecte.util.TimeUtils.millisToStartOfDay;

/**
 * Created by Bobek on 26/02/2018.
 */

public class LogBookFragmentContent extends Fragment {

    private static final String KEY_DATE = "date";
    final long DAYPERIOD = 86400000;
    final int ITEM_NEW = 0;
    final int ITEM_EDIT = 1;
    long fragmentDate;
    LogBookListAdapter adapter;
    Cursor cursor;
    DatabaseHelper handler;
    SQLiteDatabase database;
    private TextView tvContent;

    public static LogBookFragmentContent newInstance(long date) {
        LogBookFragmentContent fragmentFirst = new LogBookFragmentContent();
        Bundle args = new Bundle();
        args.putLong(KEY_DATE, date);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final long millis = getArguments().getLong(KEY_DATE);
        if (millis > 0) {
            final Context context = getActivity();
            if (context != null) {
                //tvContentValue = "This is the content for the date " + TimeUtils.getFormattedDate(context, millis);
                fragmentDate = millis;
                return;
            }
        }

        fragmentDate = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_book, container, false);

        final Context context = getActivity();
        handler = new DatabaseHelper(context);
        database = handler.getWritableDatabase();
        long dayStart = fragmentDate - millisToStartOfDay();
        long dayEnd = dayStart + DAYPERIOD;
        cursor = getCursorBetweenDates(dayStart, dayEnd, database);
        TextView columnLine = view.findViewById(R.id.log_book_list_column);
        TextView noDataInfo = view.findViewById(R.id.log_book_list_blank);

        try {
            if (cursor.getCount() == 0) {
                columnLine.setVisibility(View.GONE);
                noDataInfo.setVisibility(View.VISIBLE);

            } else {
                columnLine.setVisibility(View.VISIBLE);
                noDataInfo.setVisibility(View.GONE);
            }

            adapter = new LogBookListAdapter(context, cursor);
            ListView listView = view.findViewById(R.id.log_book_list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // Find the child row ID & whether it is a climb or not
                    int childRowID = DatabaseReadWrite.getCalendarTrackerChildRowID(id, context);
                    int isClimb = DatabaseReadWrite.getCalendarTrackerIsClimb(id, context);

                    // if it is a climb, then start a new intent for modifying the climb, if not, start for modifying training
                    if (isClimb == DatabaseContract.IS_CLIMB) {
                        Intent editClimbIntent = new Intent(context, AddClimb.class);
                        editClimbIntent.putExtra("EditOrNewFlag", ITEM_EDIT);
                        editClimbIntent.putExtra("RowID", childRowID);
                        editClimbIntent.putExtra("Date", fragmentDate);
                        // Start the new activity
                        Log.i("TAG ME UP", "OnItemClick " + (int) id + " " + ITEM_EDIT + " " + fragmentDate);
                        startActivity(editClimbIntent);
                    } else {
                        Intent editWorkoutIntent = new Intent(context, AddWorkout.class);
                        editWorkoutIntent.putExtra("EditOrNewFlag", ITEM_EDIT);
                        editWorkoutIntent.putExtra("RowID", childRowID);
                        editWorkoutIntent.putExtra("Date", fragmentDate);
                        // Start the new activity
                        Log.i("TAG ME UP", "OnItemClick " + (int) id + " " + ITEM_EDIT + " " + fragmentDate);
                        startActivity(editWorkoutIntent);
                    }
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

                    AlertDialog.Builder deleteAlert = deleteDialog(id);

                    deleteAlert.show();

                    return true;
                }
            });

            return view;
        } finally {
            Log.i("LogBookFragmentContent", "in finally clause");
            cursor.close();
            if (cursor.isClosed()) {
                Log.i("LogBookFragmentContent", "in finally clause, is closed");
            } else {
                Log.i("LogBookFragmentContent", "in finally clause, is open");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCursor();
    }

    @Override
    public void onDestroy() {
        Log.i("LogBookFragmentContent", "fragment columns before" + cursor.getColumnCount());
        cursor.close();
        Log.i("LogBookFragmentContent", "fragment columns after" + cursor.getColumnCount());
        Log.i("LogBookFragmentContent", "fragment destroyed");
        super.onDestroy();
    }

    public Cursor getCursorBetweenDates(long dateStart, long dateEnd, SQLiteDatabase db) {
        return db.rawQuery("select * from " + DatabaseContract.CalendarTrackerEntry.TABLE_NAME + " where " + DatabaseContract.CalendarTrackerEntry.COLUMN_DATE + " BETWEEN '" + dateStart + "' AND '" + dateEnd + "' ORDER BY Date ASC", null);
    }

    public AlertDialog.Builder deleteDialog(final long id) {

        final Context context = getActivity();
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        //alert.setTitle("Alert!!");
        alert.setMessage("Are you sure you wish to delete this ");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Find the child row ID & whether it is a climb or not
                int childRowID = DatabaseReadWrite.getCalendarTrackerChildRowID(id, context);
                int isClimb = DatabaseReadWrite.getCalendarTrackerIsClimb(id, context);
                // if it is a climb, delete entry from the climb-log, if not a climb delete from the training log
                if (isClimb == DatabaseContract.IS_CLIMB) {
                    String table = DatabaseContract.ClimbLogEntry.TABLE_NAME;
                    String whereClause = "_id=?";
                    String[] whereArgs = new String[]{String.valueOf(childRowID)};
                    database.delete(table, whereClause, whereArgs);
                } else {
                    String table = DatabaseContract.WorkoutLogEntry.TABLE_NAME;
                    String whereClause = "_id=?";
                    String[] whereArgs = new String[]{String.valueOf(childRowID)};
                    database.delete(table, whereClause, whereArgs);
                }
                // delete from the calendar tracker
                String table = DatabaseContract.CalendarTrackerEntry.TABLE_NAME;
                String whereClause = "_id=?";
                String[] whereArgs = new String[]{String.valueOf(id)};
                database.delete(table, whereClause, whereArgs);

                Toast.makeText(context, "Clicked Yes, deleting " + id, Toast.LENGTH_LONG).show();

                refreshCursor();

                dialog.dismiss();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Clicked No", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        return (alert);

    }

    public void refreshCursor() {
        long dayStart = fragmentDate - millisToStartOfDay();
        long dayEnd = dayStart + DAYPERIOD;
        cursor.close();
        cursor = getCursorBetweenDates(dayStart, dayEnd, database);

        // referencing static variable... is this okay?
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int rowID = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID));
            LogBookListAdapter.itemExpanded.add(i, new ExpandedArrayItem(rowID, false)); // initializes all items value with false
        }
        adapter.changeCursor(cursor);
    }

}