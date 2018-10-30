package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseHelper;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;
import com.youngonessoft.android.actiondirecte.util.CheckableTextView;
import com.youngonessoft.android.actiondirecte.util.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class CopyWorkout extends AppCompatActivity {

    long singleDateOutput;
    private int numberPickerOutput = 1;

    private int copyToggle = 1; //Option for toggling 1=single day, 2=multi day

    EditText copyWorkoutDate;
    EditText copyWorkoutStartingDate;
    EditText copyWorkoutEveryWeek;
    EditText copyWorkoutForWeeks;
    EditText copyWorkoutXTimes;
    CheckBox copyWorkoutIsComplete;

    CheckableTextView copyWorkoutMonday;
    CheckableTextView copyWorkoutTuesday;
    CheckableTextView copyWorkoutWednesday;
    CheckableTextView copyWorkoutThursday;
    CheckableTextView copyWorkoutFriday;
    CheckableTextView copyWorkoutSaturday;
    CheckableTextView copyWorkoutSunday;

    boolean[] multipleDaysOutputArray = new boolean[7];
    boolean outputIsComplete;
    int multipleDaysOutputEveryWeek = 1;
    int multipleDaysOutputForWeeks = 1;
    long multipleDayStartingDate;
    int singleDayOutputXTimes = 1;

    ListView copyWorkoutListView;

    Button saveButton;
    Button cancelButton;

    RadioButton copyWorkoutSingleDay;
    RadioButton copyWorkoutMultipleDay;

    DatePickerDialog.OnDateSetListener dateSingle;
    DatePickerDialog.OnDateSetListener dateStarting;

    NumberPicker mNumberPicker;

    Calendar mCalendarSingle;
    Calendar mCalendarStarting;

    Context mContext;

    CopyWorkoutListAdapter listAdapter;
    Cursor cursor;
    DatabaseHelper handler;
    SQLiteDatabase database;
    long inputDate;
    final long DAYPERIOD = 86400000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_workout);
        Intent inputIntent = getIntent();
        inputDate = inputIntent.getLongExtra("Date", 0);

        multipleDayStartingDate=inputDate;
        mCalendarStarting = Calendar.getInstance();
        mCalendarStarting.setTimeInMillis(multipleDayStartingDate);

        singleDateOutput=inputDate;
        mCalendarSingle = Calendar.getInstance();
        mCalendarSingle.setTimeInMillis(singleDateOutput);

        mapViews();
        updateMultipleStartingDate();
        refreshViews();
        onClickListenerInitiation();
        updateSingleDate();
        updateStartingDate();

        mContext = this;


        handler = new DatabaseHelper(mContext);
        database = handler.getWritableDatabase();
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTimeInMillis(inputDate);
        tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        tempCalendar.set(Calendar.MILLISECOND, 0);

        long dayStart = tempCalendar.getTimeInMillis();
        long dayEnd = dayStart + DAYPERIOD;
        cursor = getCursorBetweenDates(dayStart, dayEnd, database);

        listAdapter = new CopyWorkoutListAdapter(mContext, cursor);
        copyWorkoutListView.setAdapter(listAdapter);

    }

    private void mapViews(){
        copyWorkoutDate = findViewById(R.id.et_copy_workout_date);
        copyWorkoutStartingDate = findViewById(R.id.et_copy_workout_starting_date);
        copyWorkoutEveryWeek = findViewById(R.id.et_copy_workout_everyweek);
        copyWorkoutForWeeks = findViewById(R.id.et_copy_workout_forweeks);
        copyWorkoutXTimes = findViewById(R.id.et_copy_workout_xtimes);
        copyWorkoutIsComplete = findViewById(R.id.cb_copy_workout_complete);

        copyWorkoutMonday = findViewById(R.id.tv_copy_workout_monday);
        copyWorkoutTuesday = findViewById(R.id.tv_copy_workout_tuesday);
        copyWorkoutWednesday = findViewById(R.id.tv_copy_workout_wednesday);
        copyWorkoutThursday = findViewById(R.id.tv_copy_workout_thursday);
        copyWorkoutFriday = findViewById(R.id.tv_copy_workout_friday);
        copyWorkoutSaturday = findViewById(R.id.tv_copy_workout_saturday);
        copyWorkoutSunday = findViewById(R.id.tv_copy_workout_sunday);

        copyWorkoutSingleDay = findViewById(R.id.rb_copy_workout_single_day);
        copyWorkoutMultipleDay = findViewById(R.id.rb_copy_workout_multiple_days);

        saveButton = findViewById(R.id.bt_copy_workout_save);
        cancelButton = findViewById(R.id.bt_copy_workout_cancel);

        copyWorkoutListView = findViewById(R.id.copy_workout_listview);
    }

    private void refreshViews(){
        if (copyToggle==1){
            copyWorkoutSingleDay.setChecked(true);
            copyWorkoutMultipleDay.setChecked(false);
        } else if (copyToggle==2) {
            copyWorkoutSingleDay.setChecked(false);
            copyWorkoutMultipleDay.setChecked(true);
        }
    }

    private void onClickListenerInitiation(){

        dateSingle = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendarSingle.set(Calendar.YEAR, year);
                mCalendarSingle.set(Calendar.MONTH, monthOfYear);
                mCalendarSingle.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendarSingle.set(Calendar.HOUR_OF_DAY, 0);
                mCalendarSingle.set(Calendar.MINUTE, 0);
                mCalendarSingle.set(Calendar.SECOND, 0);
                mCalendarSingle.set(Calendar.MILLISECOND, 1);
                singleDateOutput=mCalendarSingle.getTimeInMillis();
                updateSingleDate();
            }

        };

        dateStarting = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendarStarting.set(Calendar.YEAR, year);
                mCalendarStarting.set(Calendar.MONTH, monthOfYear);
                mCalendarStarting.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendarStarting.set(Calendar.HOUR_OF_DAY, 0);
                mCalendarStarting.set(Calendar.MINUTE, 0);
                mCalendarStarting.set(Calendar.SECOND, 0);
                mCalendarStarting.set(Calendar.MILLISECOND, 1);
                multipleDayStartingDate=mCalendarStarting.getTimeInMillis();
                updateStartingDate();
            }

        };


        copyWorkoutSingleDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSingleDay();
            }
        });
        copyWorkoutMultipleDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
            }
        });

        copyWorkoutMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                multipleDaysOutputArray[0] = copyWorkoutMonday.isChecked();
            }
        });
        copyWorkoutTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                multipleDaysOutputArray[1] = copyWorkoutTuesday.isChecked();
            }
        });
        copyWorkoutWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                multipleDaysOutputArray[2] = copyWorkoutWednesday.isChecked();
            }
        });
        copyWorkoutThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                multipleDaysOutputArray[3] = copyWorkoutThursday.isChecked();
            }
        });
        copyWorkoutFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                multipleDaysOutputArray[4] = copyWorkoutFriday.isChecked();
            }
        });
        copyWorkoutSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                multipleDaysOutputArray[5] = copyWorkoutSaturday.isChecked();
            }
        });
        copyWorkoutSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                multipleDaysOutputArray[6] = copyWorkoutSunday.isChecked();
            }
        });

        copyWorkoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSingleDay();
                new DatePickerDialog(mContext, dateSingle, mCalendarSingle
                        .get(Calendar.YEAR), mCalendarSingle.get(Calendar.MONTH),
                        mCalendarSingle.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        copyWorkoutStartingDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                new DatePickerDialog(mContext, dateStarting, mCalendarStarting
                        .get(Calendar.YEAR), mCalendarStarting.get(Calendar.MONTH),
                        mCalendarStarting.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        copyWorkoutEveryWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                numberPickerDialog(1,52, copyWorkoutEveryWeek).show();

            }
        });
        copyWorkoutForWeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
                numberPickerDialog(1,52, copyWorkoutForWeeks).show();

            }
        });

        copyWorkoutXTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSingleDay();
                numberPickerDialog(1,100, copyWorkoutXTimes).show();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                outputIsComplete = copyWorkoutIsComplete.isChecked();

                ArrayList<CheckedArrayItem> outputArrayList = listAdapter.getCopyWorkoutListAdapterCheckedStatus();
                int outputArrayListSize = outputArrayList.size();
                int checkCount = 0;
                for (int iv=1; iv<outputArrayListSize; iv++) {
                    if (outputArrayList.get(iv).getIsChecked()){
                        checkCount++;
                    }
                }


                if (outputArrayListSize==0) {
                    // don't do anything, no items selected for copy
                    Log.i("CopyWorkout", "outputArrayListSize = 0");
                }else if (checkCount==0) {
                    Toast.makeText(mContext,"No items selected for copy",Toast.LENGTH_LONG).show();
                } else {
                    // do something, items have been selected for copy
                    if (copyWorkoutSingleDay.isChecked()) {
                        // loop through array to copy items
                        for (int ii=1; ii<(singleDayOutputXTimes+1); ii++){
                            for (int i=0; i<outputArrayListSize; i++) {
                                CheckedArrayItem temp = outputArrayList.get(i);
                                if (temp.getIsChecked()) {
                                    long newRowID = DatabaseReadWrite.copyWorkoutEntry(temp.getRowID(), singleDateOutput, outputIsComplete, mContext);
                                    long newRowIDCalendar = DatabaseReadWrite.writeCalendarUpdate(0, singleDateOutput, newRowID, mContext);

                                    Log.i("CopyWorkout", "outputArrayList "+i+", "+temp.getRowID()+", "+temp.getIsChecked()+" | newRowID = " + newRowID);
                                } else {
                                    Log.i("CopyWorkout", "outputArrayList "+i+", "+temp.getRowID()+", "+temp.getIsChecked()+" | Not copied");
                                }
                            }
                        }
                        finish();
                    } else if (copyWorkoutMultipleDay.isChecked()) {
                        //create temporary calendar to iterate on, starting at the starting date
                        Calendar tempOutputCalendar = mCalendarStarting;
                        if (tempOutputCalendar.DAY_OF_WEEK==Calendar.SUNDAY){
                            tempOutputCalendar.add(Calendar.DATE,-1);
                            tempOutputCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                        } else {
                            tempOutputCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                        }

                        for (int i=1; i<(multipleDaysOutputForWeeks+1); i++){
                            //cycle through weeks
                            tempOutputCalendar.add(Calendar.WEEK_OF_YEAR,multipleDaysOutputEveryWeek);
                            for (int iii=0;iii<7;iii++){
                                if (multipleDaysOutputArray[iii]){
                                    tempOutputCalendar.add(Calendar.DATE,iii);
                                    for (int iv=0; iv<outputArrayListSize; iv++) {
                                        CheckedArrayItem temp = outputArrayList.get(iv);
                                        if (temp.getIsChecked()) {
                                            long newRowID = DatabaseReadWrite.copyWorkoutEntry(temp.getRowID(), tempOutputCalendar.getTimeInMillis(), outputIsComplete, mContext);
                                            long newRowIDCalendar = DatabaseReadWrite.writeCalendarUpdate(0, tempOutputCalendar.getTimeInMillis(), newRowID, mContext);
                                            Log.i("CopyWorkout", "multiday "+iv+", "+multipleDaysOutputArray[iv]+", newRowID"+newRowID+", "+multipleDaysOutputEveryWeek+", "+multipleDaysOutputForWeeks+", "+TimeUtils.getFormattedDate(mContext, mCalendarStarting.getTimeInMillis()));
                                        }
                                    }
                                    if (iii==7){
                                        tempOutputCalendar.add(Calendar.DATE,-1);
                                        tempOutputCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                                    } else {
                                        tempOutputCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                                    }
                                }
                            }
                        }
                        finish();
                    }
                }


                //TODO: bug found when copy workout to Mondays, starting 22nd Oct... on calendar view shows as Tuesday (in next Month)
                //TODO: check if all required data is entered
                //TODO: check for correctness of data... i.e. past dates etc
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkSingleDay(){
        copyWorkoutSingleDay.setChecked(true);
        copyWorkoutMultipleDay.setChecked(false);
    }

    private void checkMultipleDay(){
        copyWorkoutSingleDay.setChecked(false);
        copyWorkoutMultipleDay.setChecked(true);
    }

    private void updateSingleDate() {
        copyWorkoutDate.setText(TimeUtils.getFormattedDate(mContext, mCalendarSingle.getTimeInMillis()));
    }

    private void updateStartingDate() {
        copyWorkoutStartingDate.setText(TimeUtils.getFormattedDate(mContext, mCalendarStarting.getTimeInMillis()));
    }

    private void updateMultipleStartingDate(){
        copyWorkoutStartingDate.setText(TimeUtils.getFormattedDate(mContext, multipleDayStartingDate));
    }

    public AlertDialog.Builder numberPickerDialog(int minValue, int maxValue, final EditText outputView){

        mNumberPicker = new NumberPicker(mContext);
        mNumberPicker.setMaxValue(maxValue);
        mNumberPicker.setMinValue(minValue);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(mNumberPicker);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numberPickerOutput = mNumberPicker.getValue();
                outputView.setText(""+numberPickerOutput);
                refreshOutputs();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
    }

    public Cursor getCursorBetweenDates(long dateStart, long dateEnd, SQLiteDatabase db) {

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_DATE,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB + "=? AND " + DatabaseContract.CalendarTrackerEntry.COLUMN_DATE + " BETWEEN ? AND ?";
        String[] whereValue = {String.valueOf(DatabaseContract.IS_WORKOUT), String.valueOf(dateStart), String.valueOf(dateEnd)};

        Cursor cursor = db.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        return cursor;
    }

    private void refreshOutputs(){
        multipleDaysOutputEveryWeek = Integer.parseInt(copyWorkoutEveryWeek.getText().toString());
        multipleDaysOutputForWeeks = Integer.parseInt(copyWorkoutForWeeks.getText().toString());
        singleDayOutputXTimes = Integer.parseInt(copyWorkoutXTimes.getText().toString());

        multipleDaysOutputArray[0] = copyWorkoutMonday.isChecked();
        multipleDaysOutputArray[1] = copyWorkoutTuesday.isChecked();
        multipleDaysOutputArray[2] = copyWorkoutWednesday.isChecked();
        multipleDaysOutputArray[3] = copyWorkoutThursday.isChecked();
        multipleDaysOutputArray[4] = copyWorkoutFriday.isChecked();
        multipleDaysOutputArray[5] = copyWorkoutSaturday.isChecked();
        multipleDaysOutputArray[6] = copyWorkoutSunday.isChecked();
    }

}
