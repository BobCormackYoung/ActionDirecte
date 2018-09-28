package com.youngonessoft.android.actiondirecte.logbookmodule.workoutpicker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseHelper;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;

/**
 * Created by Bobek on 11/02/2018.
 */

public class ParentWorkoutHolder extends AppCompatActivity {

    private static final String TAG = "ParentWorkoutHolder";
    final int REQUEST_CODE_NUMBER = 3;
    SQLiteDatabase database;
    Cursor cursor;
    private int outputWorkoutName = 0;
    private int outputWorkoutNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_list);

        Log.i(TAG, "ParentWorkoutHolder_1");

        //Create handler to connect to SQLite DB
        DatabaseHelper handler = new DatabaseHelper(this);
        database = handler.getWritableDatabase();
        //final Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.WorkoutTypeEntry.TABLE_NAME, null);

        cursor = DatabaseReadWrite.getWorkoutTypes(database);

        Log.i(TAG, "SELECT * FROM " + DatabaseContract.WorkoutTypeEntry.TABLE_NAME);

        ParentWorkoutAdapter parentAdapter = new ParentWorkoutAdapter(this, cursor);

        ListView parentListView = (ListView) findViewById(R.id.parent_listview);

        parentListView.setAdapter(parentAdapter);

        parentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                outputWorkoutName = (int) id;

                Log.i(TAG, "" + outputWorkoutName);

                // Create new intent
                Intent selectorIntent = new Intent(ParentWorkoutHolder.this, ChildWorkoutHolder.class);
                // Add extra information to intent so that subsequent activity knows that we're requesting to generate list of grades
                selectorIntent.putExtra("selector", outputWorkoutName);
                // Start activity for getting result
                startActivityForResult(selectorIntent, REQUEST_CODE_NUMBER);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_NUMBER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a grade,
                outputWorkoutNumber = data.getIntExtra("OutputData", 0);

                Log.i("TAG_Ouput", "" + outputWorkoutName + " | " + outputWorkoutNumber);

                Intent outputIntent = new Intent();
                outputIntent.putExtra("OutputWorkoutNumber", outputWorkoutNumber);
                outputIntent.putExtra("OutputWorkoutName", outputWorkoutName);
                setResult(RESULT_OK, outputIntent);

                try {
                    finish();
                } finally {
                    cursor.close();
                    database.close();
                    Log.i(TAG, "finally...");
                }

            }
            // TODO: Error handler for nothing passed back
        }
    }
}

