package com.youngonessoft.android.actiondirecte.logbookmodule.gradepicker;

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
import com.youngonessoft.android.actiondirecte.data.DatabaseHelper;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;

/**
 * Created by Bobek on 11/02/2018.
 */

public class ParentGradeHolder extends AppCompatActivity {

    private static final String TAG = "ParentGradeHolder";
    final int REQUEST_CODE_NUMBER = 3;
    SQLiteDatabase database;
    Cursor cursor;
    private int outputGradeName = 0;
    private int outputGradeNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_list);

        //Create handler to connect to SQLite DB
        DatabaseHelper handler = new DatabaseHelper(this);
        database = handler.getWritableDatabase();
        //Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.GradeTypeEntry.TABLE_NAME, null);
        cursor = DatabaseReadWrite.getGradeTypes(database);

        ParentGradeAdapter parentAdapter = new ParentGradeAdapter(this, cursor);

        ListView parentListView = (ListView) findViewById(R.id.parent_listview);

        parentListView.setAdapter(parentAdapter);

        parentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Log.i(TAG, Long.toString(id));

                outputGradeName = (int) id;

                // Create new intent
                Intent selectorIntent = new Intent(ParentGradeHolder.this, ChildGradeHolder.class);
                // Add extra information to intent so that subsequent activity knows that we're requesting to generate list of grades
                selectorIntent.putExtra("selector", outputGradeName);
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
                outputGradeNumber = data.getIntExtra("OutputData", 0);

                Intent outputIntent = new Intent();
                outputIntent.putExtra("OutputGradeNumber", outputGradeNumber);
                outputIntent.putExtra("OutputGradeName", outputGradeName);
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

