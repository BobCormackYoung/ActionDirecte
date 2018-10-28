package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;
import com.youngonessoft.android.actiondirecte.logbookmodule.ascentpicker.AscentHolder;
import com.youngonessoft.android.actiondirecte.logbookmodule.gradepicker.ParentGradeHolder;
import com.youngonessoft.android.actiondirecte.util.TimeUtils;

/**
 * Created by Bobek on 11/02/2018.
 */

public class AddClimb extends AppCompatActivity {

    final String LOG_TAG = "LogClimbTag";

    final int PICK_GRADE_REQUEST = 1;
    final int PICK_ASCENT_TYPE_REQUEST = 2;
    final int ADD_CLIMB_NEW = 0;
    final int ADD_CLIMB_EDIT = 1;
    int outputGradeNumber = -1;
    int outputGradeName = -1;
    int outputAscent = -1;
    String outputLocationName = null;
    String outputRouteName = null;
    String outputDateString = null;
    int outputFirstAscent = -1;
    long outputDate = -1;
    int inputIntentCode = -1;
    int inputRowID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_climb);

        Intent inputIntent = getIntent();
        inputIntentCode = inputIntent.getIntExtra("EditOrNewFlag", 0);
        inputRowID = inputIntent.getIntExtra("RowID", 0);
        outputDate = inputIntent.getLongExtra("Date", 0);

        if (inputIntentCode == ADD_CLIMB_NEW) {
            // Add a new climb, don't import any data to the form
            //outputDate = Calendar.getInstance().getTimeInMillis();
            String outputDateString = TimeUtils.convertDate(outputDate, "yyyy-MM-dd");
            EditText dateView = findViewById(R.id.editText5);
            dateView.setText(outputDateString);

        } else if (inputIntentCode == ADD_CLIMB_EDIT) {
            // Edit existing record, import data into the form

            // Load climb log data for a specific row ID
            Bundle bundle = DatabaseReadWrite.EditClimbLoadEntry(inputRowID, this);

            EditText routeNameView = findViewById(R.id.editText);
            outputRouteName = bundle.getString("outputRouteName");
            routeNameView.setText(outputRouteName);

            EditText locationNameView = findViewById(R.id.editText2);
            outputLocationName = bundle.getString("outputLocationName");
            locationNameView.setText(outputLocationName);

            EditText dateView = findViewById(R.id.editText5);
            outputDate = bundle.getLong("outputDate");
            outputDateString = bundle.getString("outputDateString");
            dateView.setText(outputDateString);

            CheckBox firstAscentCheckBox = findViewById(R.id.checkbox_firstascent);
            outputFirstAscent = bundle.getInt("outputFirstAscent");
            if (outputFirstAscent == DatabaseContract.FIRSTASCENT_TRUE) {
                firstAscentCheckBox.setChecked(true);
            } else if (outputFirstAscent == DatabaseContract.FIRSTASCENT_FALSE) {
                firstAscentCheckBox.setChecked(false);
            }
            outputGradeNumber = bundle.getInt("outputGradeNumber");
            outputGradeName = bundle.getInt("outputGradeName");
            outputAscent = bundle.getInt("outputAscent");

            // Set grade view
            // Get grade name
            String outputStringGradeName = DatabaseReadWrite.getGradeTextClimb(outputGradeNumber, this);
            String outputStringGradeType = DatabaseReadWrite.getGradeTypeClimb(outputGradeName, this);
            EditText gradeView = findViewById(R.id.editText4);
            gradeView.setText(outputStringGradeType + " | " + outputStringGradeName);

            // Set ascent type
            String outputStringAscentType = DatabaseReadWrite.getAscentNameTextClimb(outputAscent, this);
            EditText ascentTypeView = findViewById(R.id.editText3);
            ascentTypeView.setText(outputStringAscentType);
        }


        // Listener for the grade selection
        EditText gradeView = findViewById(R.id.editText4);
        gradeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickGrade();
            }
        });

        // Listener for the ascent-type selection
        EditText ascentTypeView = findViewById(R.id.editText3);
        ascentTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAscentType();
            }
        });

        // Listener for the save button
        Button saveButton = findViewById(R.id.log_climb_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText routeNameView = findViewById(R.id.editText);
                EditText locationNameView = findViewById(R.id.editText2);
                CheckBox firstAscentCheckBox = findViewById(R.id.checkbox_firstascent);

                outputRouteName = routeNameView.getText().toString();
                outputLocationName = locationNameView.getText().toString();
                if (firstAscentCheckBox.isChecked()) {
                    outputFirstAscent = DatabaseContract.FIRSTASCENT_TRUE;
                } else {
                    outputFirstAscent = DatabaseContract.FIRSTASCENT_FALSE;
                }


                if (outputRouteName.trim().equals("") || outputLocationName.trim().equals("")
                        || outputAscent == -1 || outputGradeName == -1 || outputGradeNumber == -1
                        || outputDate == -1 || outputFirstAscent == -1) {
                    Toast.makeText(getApplicationContext(), "Insufficient information - please ensure all fields are filled", Toast.LENGTH_SHORT).show();
                } else {
                    if (inputIntentCode == ADD_CLIMB_EDIT) {
                        long updateResult = DatabaseReadWrite.updateClimbLogData(outputRouteName, outputLocationName, outputAscent, outputGradeName, outputGradeNumber, outputDate, outputFirstAscent, inputRowID, AddClimb.this);
                        //Toast.makeText(getApplicationContext(), "Existing Row ID: " + String.valueOf(updateResult), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (inputIntentCode == ADD_CLIMB_NEW) {
                        long writeResult = DatabaseReadWrite.writeClimbLogData(outputRouteName, outputLocationName, outputAscent, outputGradeName, outputGradeNumber, outputDate, outputFirstAscent, AddClimb.this);
                        DatabaseReadWrite.writeCalendarUpdate(DatabaseContract.IS_CLIMB, outputDate, writeResult, AddClimb.this);
                        //Toast.makeText(getApplicationContext(), "New Row ID: " + String.valueOf(writeResult), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }
        });

        // Listener for the cancel button
        Button cancelButton = findViewById(R.id.log_climb_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_GRADE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                putGrade(data);
            } else {
                Log.i(LOG_TAG, "result not okay");
            }
        } else if (requestCode == PICK_ASCENT_TYPE_REQUEST) {
            if (resultCode == RESULT_OK) {
                putAscent(data);
            } else {
                Log.i(LOG_TAG, "result not okay");
            }
        }
    }

    // method for launching the activity for picking the grade
    private void pickGrade() {
        // Create new intent
        Intent selectorIntent = new Intent(this, ParentGradeHolder.class);
        // Start activity for getting result
        startActivityForResult(selectorIntent, PICK_GRADE_REQUEST);
    }

    // method for launching the activity for picking the ascent type
    private void pickAscentType() {
        // Create new intent
        Intent selectorIntent = new Intent(this, AscentHolder.class);
        // Start activity for getting result
        startActivityForResult(selectorIntent, PICK_ASCENT_TYPE_REQUEST);
    }

    // method for inserting the grade data into the method variables & insert into textviews
    private void putGrade(Intent data) {
        // The user picked a grade, get the grade number
        outputGradeNumber = data.getIntExtra("OutputGradeNumber", 0);
        String outputStringGradeName = DatabaseReadWrite.getGradeTextClimb(outputGradeNumber, this);

        // The user picked a grade, get the grade number
        // Put grade text date in the view
        outputGradeName = data.getIntExtra("OutputGradeName", 0);
        String outputStringGradeType = DatabaseReadWrite.getGradeTypeClimb(outputGradeName, this);
        EditText gradeView = findViewById(R.id.editText4);
        gradeView.setText(outputStringGradeType + " | " + outputStringGradeName);
    }

    // method for inserting the ascent data into the method variables & insert into textviews
    private void putAscent(Intent data) {
        outputAscent = data.getIntExtra("OutputData", 0);

        //Create handler to connect to SQLite DB
        String outputStringAscentType = DatabaseReadWrite.getAscentNameTextClimb(outputAscent, this);
        EditText gradeView = findViewById(R.id.editText3);
        gradeView.setText(outputStringAscentType);
    }

}

