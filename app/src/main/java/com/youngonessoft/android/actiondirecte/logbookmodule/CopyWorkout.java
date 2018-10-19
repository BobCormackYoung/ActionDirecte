package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.util.TimeUtils;

import java.util.Calendar;

public class CopyWorkout extends AppCompatActivity {

    long singleDateOutput;
    private int numberPickerOutput = 1;

    private int copyToggle = 1; //Option for toggling 1=single day, 2=multi day

    EditText copyWorkoutDate;
    EditText copyWorkoutEveryWeek;
    EditText copyWorkoutForWeeks;

    TextView copyWorkoutMonday;
    TextView copyWorkoutTuesday;
    TextView copyWorkoutWednesday;
    TextView copyWorkoutThursday;
    TextView copyWorkoutFriday;
    TextView copyWorkoutSaturday;
    TextView copyWorkoutSunday;

    Button saveButton;
    Button cancelButton;

    RadioButton copyWorkoutSingleDay;
    RadioButton copyWorkoutMultipleDay;

    DatePickerDialog.OnDateSetListener date;

    NumberPicker mNumberPicker;

    Calendar mCalendar;

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_workout);
        mapViews();
        refreshViews();
        onClickListenerInitiation();
        mContext = this;
        mCalendar = Calendar.getInstance();
    }

    private void mapViews(){
        copyWorkoutDate = findViewById(R.id.et_copy_workout_date);
        copyWorkoutEveryWeek = findViewById(R.id.et_copy_workout_everyweek);
        copyWorkoutForWeeks = findViewById(R.id.et_copy_workout_forweeks);

        copyWorkoutMonday = findViewById(R.id.tv_copy_workout_monday);
        copyWorkoutTuesday = findViewById(R.id.tv_copy_workout_tuesday);
        copyWorkoutWednesday = findViewById(R.id.tv_copy_workout_wednesday);
        copyWorkoutThursday = findViewById(R.id.tv_copy_workout_thursday);
        copyWorkoutFriday = findViewById(R.id.tv_copy_workout_friday);
        copyWorkoutSaturday = findViewById(R.id.tv_copy_workout_saturday);
        copyWorkoutSunday = findViewById(R.id.tv_copy_workout_sunday);

        copyWorkoutSingleDay = findViewById(R.id.rb_copy_workout_single_day);
        copyWorkoutMultipleDay = findViewById(R.id.rb_copy_workout_multiple_days);

        saveButton = findViewById(R.id.copy_workout_save);
        cancelButton = findViewById(R.id.copy_workout_cancel);
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

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendar.set(Calendar.HOUR_OF_DAY, 0);
                mCalendar.set(Calendar.MINUTE, 0);
                mCalendar.set(Calendar.SECOND, 0);
                mCalendar.set(Calendar.MILLISECOND, 1);
                singleDateOutput=mCalendar.getTimeInMillis();
                updateDate();
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

            }
        });
        copyWorkoutTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
            }
        });
        copyWorkoutWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
            }
        });
        copyWorkoutThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
            }
        });
        copyWorkoutFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
            }
        });
        copyWorkoutSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
            }
        });
        copyWorkoutSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMultipleDay();
            }
        });

        copyWorkoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSingleDay();
                new DatePickerDialog(mContext, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: check which of the cases, single or multiday, has been selected
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

    private void updateDate() {
        copyWorkoutDate.setText(TimeUtils.getFormattedDate(mContext, mCalendar.getTimeInMillis()));
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

}
