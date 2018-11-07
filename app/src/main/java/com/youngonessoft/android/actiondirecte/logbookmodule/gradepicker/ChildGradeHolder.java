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
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseHelper;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;

/**
 * Created by Bobek on 11/02/2018.
 */

public class ChildGradeHolder extends AppCompatActivity {

    private static final String TAG = "ChildGradeHolder";
    Cursor cursor;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_child_list);

        Intent selectorIntent = getIntent();
        int selectorID = selectorIntent.getIntExtra("selector", 0);

        //Create handler to connect to SQLite DB
        DatabaseHelper handler = new DatabaseHelper(this);
        database = handler.getWritableDatabase();

        Log.i(TAG, "SELECT * FROM " + DatabaseContract.GradeListEntry.TABLE_NAME + " where " + DatabaseContract.GradeListEntry.COLUMN_GRADETYPECODE + "=" + selectorID);
        //final Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.GradeListEntry.TABLE_NAME + " where " + DatabaseContract.GradeListEntry.COLUMN_GRADETYPECODE + "=" + selectorID, null);
        cursor = DatabaseReadWrite.getGradeList(selectorID, database);
        Log.i(TAG, "Count: " + cursor.getCount());

        ChildGradeAdapter childAdapter = new ChildGradeAdapter(this, cursor);

        ListView childListView = findViewById(R.id.child_list);

        childListView.setAdapter(childAdapter);

        childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Log.i(TAG, Long.toString(id));

                int output = (int) id;

                Intent outputIntent = new Intent();
                outputIntent.putExtra("OutputData", output);
                setResult(RESULT_OK, outputIntent);
                try {
                    finish();
                } finally {
                    cursor.close();
                    database.close();
                    Log.i(TAG, "finally...");
                }
            }
        });

    }
}

