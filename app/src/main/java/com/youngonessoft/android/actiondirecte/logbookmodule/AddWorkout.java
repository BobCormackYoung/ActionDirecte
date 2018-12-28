package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;
import com.youngonessoft.android.actiondirecte.logbookmodule.gradepicker.ParentGradeHolder;
import com.youngonessoft.android.actiondirecte.logbookmodule.holdtypepicker.HoldTypeHolder;
import com.youngonessoft.android.actiondirecte.logbookmodule.workoutpicker.ParentWorkoutHolder;
import com.youngonessoft.android.actiondirecte.util.TimeUtils;

// TODO: Add the location for adding information about the movecount

public class AddWorkout extends AppCompatActivity {

    final int PICK_TRAINING_REQUEST = 1;
    final int PICK_GRADE_REQUEST = 2;
    final int PICK_HOLDTYPE_REQUEST = 3;
    final int ADD_WORKOUT_NEW = 0;
    final int ADD_WORKOUT_EDIT = 1;
    final int INCREMENT_TIME_SHORT = 5000; // 5 seconds in milliseconds
    final int INCREMENT_TIME_LONG = 60000; // 1 minute in milliseconds
    final double INCREMENT_WEIGHT_SMALL = 0.5; // 0.5 KG
    final double INCREMENT_WEIGHT_LARGE = 5; // 5 KG
    final int INCREMENT_COUNT_SMALL = 1; // set count
    final int INCREMENT_COUNT_LARGE = 5; // set count
    final int ABSOLUTE_MINIMUM = 0;
    int outputWorkoutNumber = -1; // Workout number
    int outputWorkoutName = -1; // Workout name
    int inputIntentCode = -1;
    int inputRowID = -1;
    long stopTime = 0;
    long saveTime = 0;

    int triggerWeight = 0;
    int triggerSetCount = 0;
    int triggerRepCount = 0;
    int triggerRestPerSet = 0;
    int triggerRepDuration = 0;
    int triggerGradeCode = 0;
    int triggerMoveCount = 0;
    int triggerWallAngle = 0;
    int triggerHoldType = 0;

    // Initialise output values
    long outputDate = -1;
    int outputGradeNumber = -1;
    int outputGradeName = -1;
    int outputHoldType = -1;
    int outputCompleteCheckedState = 0; // 1 = checked/complete, 0 = unchecked/incomplete

    // Initialise counters
    double counterWeight = 0;
    int counterRestTime = 0;
    int counterRepCount = 1;
    int counterRepTime = 0;
    int counterSetCount = 1;
    int counterMoveCount = 0;
    int counterWallAngle = 0;


    TextView titleWrapper; // Title wrapper

    // Chronometer
    LinearLayout chronometerWrapper;
    Chronometer chronometerTimer;
    Button chronometerStart;
    Button chronometerPause;
    Button chronometerReset;
    Button chronometerSave;

    LinearLayout workoutDateWrapper; // Date wrapper
    EditText workoutDateEditText; // Date EditText

    LinearLayout workoutTrainingWrapper; // Training type/name wrapper
    EditText workoutTrainingEditText; // Training type/name EditText

    LinearLayout workoutWeightWrapper; // Weight wrapper
    EditText workoutWeightEditText; // Weight EditText
    Button workoutWeightButtonMinus; // Weight MinusButton
    Button workoutWeightButtonPlus; // Weight PlusButton
    Button workoutWeightButtonMinusBig;  // Weight MinusButton
    Button workoutWeightButtonPlusBig;  // Weight PlusButton

    LinearLayout workoutSetCountWrapper; // Set Count  Wrapper
    EditText workoutSetCountEditText; // Set Count EditText
    Button workoutSetCountButtonMinus; // Set Count MinusButton
    Button workoutSetCountButtonPlus; // Set Count PlusButton
    Button workoutSetCountButtonMinusBig; // Set Count MinusButton
    Button workoutSetCountButtonPlusBig; // Set Count PlusButton

    LinearLayout workoutRepCountWrapper; // Rep Count Wrapper
    EditText workoutRepCountEditText; // Set Count EditText
    Button workoutRepCountButtonMinus; // Set Count MinusButton
    Button workoutRepCountButtonPlus; // Set Count PlusButton
    Button workoutRepCountButtonMinusBig; // Set Count MinusButton
    Button workoutRepCountButtonPlusBig; // Set Count PlusButton

    LinearLayout workoutRepDurationWrapper; // Rep Duration Wrapper
    EditText workoutRepDurationEditText; // Rep Duration EditText
    Button workoutRepDurationButtonMinus; // Rep Duration MinusButton
    Button workoutRepDurationButtonPlus; // Rep Duration PlusButton
    Button workoutRepDurationButtonMinusBig; // Rep Duration MinusButton
    Button workoutRepDurationButtonPlusBig; // Rep Duration PlusButton

    LinearLayout workoutRestWrapper; // Rest per set wrapper
    EditText workoutRestEditText; // Rest per set EditText
    Button workoutRestButtonMinus; // Rest per set MinusButton
    Button workoutRestButtonPlus; // Rest per set PlusButton
    Button workoutRestButtonMinusBig; // Rest per set MinusButton
    Button workoutRestButtonPlusBig; // Rest per set PlusButton

    LinearLayout workoutGradeWrapper; // Grade Wrapper
    EditText workoutGradeEditText; // Grade EditText

    LinearLayout workoutMoveCountWrapper; // Move Count wrapper
    EditText workoutMoveCountEditText; // Move Count EditText
    Button workoutMoveCountButtonMinus; // Move Count MinusButton
    Button workoutMoveCountButtonPlus; // Move Count PlusButton
    Button workoutMoveCountButtonMinusBig; // Move Count MinusButton
    Button workoutMoveCountButtonPlusBig; // Move Count PlusButton

    LinearLayout workoutWallAngleWrapper; // Wall Angle wrapper
    EditText workoutWallAngleEditText; // Wall Angle EditText
    Button workoutWallAngleButtonMinus; // Wall Angle MinusButton
    Button workoutWallAngleButtonPlus; // Wall Angle PlusButton
    Button workoutWallAngleButtonMinusBig; // Wall Angle MinusButton
    Button workoutWallAngleButtonPlusBig; // Wall Angle PlusButton

    LinearLayout workoutHoldTypeWrapper; // Hold Type Wrapper
    EditText workoutHoldTypeEditText; // Hold Type EditText

    LinearLayout workoutCompleteWrapper; // Hold Type Wrapper
    CheckBox workoutCompleteCheckBox; // Hold Type EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);

        Log.i("AddWorkout", "onCreate");

        Intent inputIntent = getIntent();
        inputIntentCode = inputIntent.getIntExtra("EditOrNewFlag", 0);
        inputRowID = inputIntent.getIntExtra("RowID", 0);
        outputDate = inputIntent.getLongExtra("Date", 0);

        mapViews();

        if (inputIntentCode == ADD_WORKOUT_NEW) {

            workoutDateEditText.setText(TimeUtils.convertDate(outputDate, "yyyy-MM-dd"));

            showViews();

        } else if (inputIntentCode == ADD_WORKOUT_EDIT) {

            // Get the workout entry information
            Bundle workoutEntryBundle = DatabaseReadWrite.EditWorkoutLoadEntry(inputRowID, this);
            getWorkoutInfoFromBundle(workoutEntryBundle);

            // Get which fields should be shown
            Bundle workoutFieldsBundle = DatabaseReadWrite.workoutLoadFields(outputWorkoutNumber, this);
            getTrainingInputFields(workoutFieldsBundle);

            //show the relevant views
            showViews();

            //fill the views with data
            fillViews();
        }

        onClickListenerInitiation();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("AddWorkout", "onActivityResult");

        // Check which request we're responding to
        if (requestCode == PICK_TRAINING_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                putWorkout(data);
            }
        } else if (requestCode == PICK_GRADE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                putGrade(data);
            }
        } else if (requestCode == PICK_HOLDTYPE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                putHoldType(data);
            }
        }
    }

    private void mapViews() {

        Log.i("AddWorkout", "mapViews");

        // Title wrapper
        //titleWrapper = findViewById(R.id.pageTitle);

        // Chronometer
        chronometerWrapper = findViewById(R.id.chronometerWrapper);
        chronometerTimer = findViewById(R.id.chronometer);
        chronometerStart = findViewById(R.id.buttonStartChrono);
        chronometerPause = findViewById(R.id.buttonPauseChrono);
        chronometerReset = findViewById(R.id.buttonResetChrono);
        chronometerSave = findViewById(R.id.buttonSaveChrono);

        // Date Wrapper
        workoutDateWrapper = findViewById(R.id.workoutTextWrapper1);
        workoutDateEditText = findViewById(R.id.workoutEditText1);

        // Workout Type Wrapper
        workoutTrainingWrapper = findViewById(R.id.workoutTextWrapper2);
        workoutTrainingEditText = findViewById(R.id.workoutEditText2);

        // Weight Data Input Wrapper
        workoutWeightWrapper = findViewById(R.id.workoutTextWrapper3);
        workoutWeightEditText = findViewById(R.id.workoutEditText3);
        workoutWeightButtonMinus = findViewById(R.id.workoutButtonMinus3);
        workoutWeightButtonPlus = findViewById(R.id.workoutButtonPlus3);
        workoutWeightButtonMinusBig = findViewById(R.id.workoutButtonMinus3a);
        workoutWeightButtonPlusBig = findViewById(R.id.workoutButtonPlus3a);

        // Set Count Input Wrapper
        workoutSetCountWrapper = findViewById(R.id.workoutTextWrapper4);
        workoutSetCountEditText = findViewById(R.id.workoutEditText4);
        workoutSetCountButtonMinus = findViewById(R.id.workoutButtonMinus4);
        workoutSetCountButtonPlus = findViewById(R.id.workoutButtonPlus4);
        workoutSetCountButtonMinusBig = findViewById(R.id.workoutButtonMinus4a);
        workoutSetCountButtonPlusBig = findViewById(R.id.workoutButtonPlus4a);

        // Rep Count Input Wrapper
        workoutRepCountWrapper = findViewById(R.id.workoutTextWrapper5);
        workoutRepCountEditText = findViewById(R.id.workoutEditText5);
        workoutRepCountButtonMinus = findViewById(R.id.workoutButtonMinus5);
        workoutRepCountButtonPlus = findViewById(R.id.workoutButtonPlus5);
        workoutRepCountButtonMinusBig = findViewById(R.id.workoutButtonMinus5a);
        workoutRepCountButtonPlusBig = findViewById(R.id.workoutButtonPlus5a);

        // Rep Duration Input Wrapper
        workoutRepDurationWrapper = findViewById(R.id.workoutTextWrapper6);
        workoutRepDurationEditText = findViewById(R.id.workoutEditText6);
        workoutRepDurationButtonMinus = findViewById(R.id.workoutButtonMinus6);
        workoutRepDurationButtonPlus = findViewById(R.id.workoutButtonPlus6);
        workoutRepDurationButtonMinusBig = findViewById(R.id.workoutButtonMinus6a);
        workoutRepDurationButtonPlusBig = findViewById(R.id.workoutButtonPlus6a);

        // Rest Per Set Input Wrapper
        workoutRestWrapper = findViewById(R.id.workoutTextWrapper7);
        workoutRestEditText = findViewById(R.id.workoutEditText7);
        workoutRestButtonMinus = findViewById(R.id.workoutButtonMinus7);
        workoutRestButtonPlus = findViewById(R.id.workoutButtonPlus7);
        workoutRestButtonMinusBig = findViewById(R.id.workoutButtonMinus7a);
        workoutRestButtonPlusBig = findViewById(R.id.workoutButtonPlus7a);

        // Grade or Difficulty Input Wrapper
        workoutGradeWrapper = findViewById(R.id.workoutTextWrapper8);
        workoutGradeEditText = findViewById(R.id.workoutEditText8);

        // Move Count Input Wrapper
        workoutMoveCountWrapper = findViewById(R.id.workoutTextWrapper9);
        workoutMoveCountEditText = findViewById(R.id.workoutEditText9);
        workoutMoveCountButtonMinus = findViewById(R.id.workoutButtonMinus9);
        workoutMoveCountButtonPlus = findViewById(R.id.workoutButtonPlus9);
        workoutMoveCountButtonMinusBig = findViewById(R.id.workoutButtonMinus9a);
        workoutMoveCountButtonPlusBig = findViewById(R.id.workoutButtonPlus9a);

        // Wall Angle Input Wrapper
        workoutWallAngleWrapper = findViewById(R.id.workoutTextWrapper10);
        workoutWallAngleEditText = findViewById(R.id.workoutEditText10);
        workoutWallAngleButtonMinus = findViewById(R.id.workoutButtonMinus10);
        workoutWallAngleButtonPlus = findViewById(R.id.workoutButtonPlus10);
        workoutWallAngleButtonMinusBig = findViewById(R.id.workoutButtonMinus10a);
        workoutWallAngleButtonPlusBig = findViewById(R.id.workoutButtonPlus10a);

        // Hold Type Input Wrapper
        workoutHoldTypeWrapper = findViewById(R.id.workoutTextWrapper11);
        workoutHoldTypeEditText = findViewById(R.id.workoutEditText11);

        // Completeness Input Wrapper
        workoutCompleteWrapper = findViewById(R.id.workoutTextWrapper12);
        workoutCompleteCheckBox = findViewById(R.id.workoutCheckBox12);

    }

    private void onClickListenerInitiation() {

        Log.i("AddWorkout", "onClickListenerInitiation");

        // Listener for the training selection
        workoutTrainingEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputIntentCode == ADD_WORKOUT_NEW) {
                    // Only allow workout type to be changed if saving a new workout
                    pickTraining();
                    resetOutputs();
                    fillViews();
                }
            }
        });

        // Listener for the save button
        Button saveButton = findViewById(R.id.log_training_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (workoutCompleteCheckBox.isChecked()) {
                    outputCompleteCheckedState = 1;
                } else {
                    outputCompleteCheckedState = 0;
                }

                if (inputIntentCode == ADD_WORKOUT_NEW) {
                    if (outputWorkoutNumber != -1 && outputWorkoutName != -1) {
                        long outputRow = DatabaseReadWrite.writeWorkoutLogData(outputDate, outputWorkoutName, outputWorkoutNumber, counterWeight, counterSetCount,
                                counterRepCount, counterRepTime, counterRestTime, outputGradeName, outputGradeNumber, counterMoveCount, outputHoldType, counterWallAngle, outputCompleteCheckedState, AddWorkout.this);
                        DatabaseReadWrite.writeCalendarUpdate(DatabaseContract.IS_WORKOUT, outputDate, outputRow, AddWorkout.this);
                        //Toast.makeText(getApplicationContext(), "New Row ID: " + outputRow, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "No workout type selected", Toast.LENGTH_SHORT).show();
                    }
                } else if (inputIntentCode == ADD_WORKOUT_EDIT) {
                    long outputRow = DatabaseReadWrite.updateWorkoutLogData(outputDate, outputWorkoutName, outputWorkoutNumber, counterWeight, counterSetCount,
                            counterRepCount, counterRepTime, counterRestTime, outputGradeName, outputGradeNumber, counterMoveCount, outputHoldType, counterWallAngle, inputRowID, outputCompleteCheckedState, AddWorkout.this);
                    //Toast.makeText(getApplicationContext(), "New Row ID: " + outputRow, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        // Listener for the cancel button
        Button cancelButton = findViewById(R.id.log_training_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Chronometer
        chronometerStart.setClickable(true);
        chronometerPause.setClickable(false);
        chronometerSave.setClickable(false);
        chronometerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometerTimer.setBase(SystemClock.elapsedRealtime() + stopTime);
                chronometerTimer.start();
                chronometerStart.setClickable(false);
                chronometerPause.setClickable(true);
                chronometerSave.setClickable(false);
            }
        });
        chronometerPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTime = chronometerTimer.getBase() - SystemClock.elapsedRealtime();
                chronometerTimer.stop();
                chronometerStart.setClickable(true);
                chronometerPause.setClickable(false);
                chronometerSave.setClickable(true);
            }
        });
        chronometerReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometerTimer.setBase(SystemClock.elapsedRealtime());
                stopTime = 0;
                chronometerTimer.stop();
                chronometerStart.setClickable(true);
                chronometerPause.setClickable(false);
                chronometerSave.setClickable(false);
            }
        });
        chronometerSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTime = Math.abs(stopTime);

                Log.i("CHRONO_LOG", "Saving Time: " + saveTime);

                if (outputWorkoutNumber == -1) {
                    // No workout selected
                    Log.i("CHRONO_LOG", "Option 1");
                    Toast.makeText(getApplicationContext(), "No workout selected. Can't save data!", Toast.LENGTH_SHORT).show();
                } else {
                    // Workout is selected
                    // Give different options for saving data depending on data required for workout
                    Log.i("CHRONO_LOG", "Option 2");
                    Log.i("CHRONO_LOG", "triggerRepDuration = " + triggerRepDuration + ", triggerRestPerSet = " + triggerRestPerSet);
                    if (triggerRepDuration == 1 & triggerRestPerSet == 0) {
                        // Need a
                        counterRepTime = (int) saveTime;
                        Log.i("CHRONO_LOG", "Option 2:1 " + counterRepTime);
                        workoutRepDurationEditText.setText(TimeUtils.convertDate(counterRepTime, "mm:ss"));
                    } else if (triggerRepDuration == 1 & triggerRestPerSet == 1) {
                        CharSequence[] pickerValues = new CharSequence[]{"Rep Duration", "Rest Per Set"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddWorkout.this);
                        builder.setTitle("Pick a value to save to:");
                        builder.setItems(pickerValues, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    counterRepTime = (int) saveTime;
                                    Log.i("CHRONO_LOG", "Option 2:2 " + counterRepTime);
                                    workoutRepDurationEditText.setText(TimeUtils.convertDate(counterRepTime, "mm:ss"));
                                } else if (which == 1) {
                                    counterRestTime = (int) saveTime;
                                    Log.i("CHRONO_LOG", "Option 2:2 " + counterRepTime);
                                    workoutRestEditText.setText(TimeUtils.convertDate(counterRestTime, "mm:ss"));
                                }
                            }
                        });
                        builder.show();
                    } else if (triggerRepDuration == 0 & triggerRestPerSet == 1) {
                        CharSequence[] pickerValues = new CharSequence[]{"Rest Per Set"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddWorkout.this);
                        builder.setTitle("Pick a value to save to:");
                        builder.setItems(pickerValues, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                counterRestTime = (int) saveTime;
                                Log.i("CHRONO_LOG", "Option 2:3 " + saveTime);
                                workoutRestEditText.setText(TimeUtils.convertDate(counterRestTime, "mm:ss"));
                            }
                        });
                        builder.show();
                    }
                }


            }
        });

        // Weight Button Listeners
        workoutWeightButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterWeight - INCREMENT_WEIGHT_SMALL >= 0) {
                    counterWeight = counterWeight - INCREMENT_WEIGHT_SMALL;
                } else {
                    counterWeight = 0;
                }
                workoutWeightEditText.setText("" + counterWeight + " Kg");
            }
        });
        workoutWeightButtonMinusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterWeight - INCREMENT_WEIGHT_LARGE >= 0) {
                    counterWeight = counterWeight - INCREMENT_WEIGHT_LARGE;
                } else {
                    counterWeight = 0;
                }
                workoutWeightEditText.setText("" + counterWeight + " Kg");
            }
        });
        workoutWeightButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterWeight = counterWeight + INCREMENT_WEIGHT_SMALL;
                workoutWeightEditText.setText("" + counterWeight + " Kg");
            }
        });
        workoutWeightButtonPlusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterWeight = counterWeight + INCREMENT_WEIGHT_LARGE;
                workoutWeightEditText.setText("" + counterWeight + " Kg");
            }
        });

        // Set Count Button Listeners
        workoutSetCountButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterSetCount - INCREMENT_COUNT_SMALL >= 1) {
                    counterSetCount = counterSetCount - INCREMENT_COUNT_SMALL;
                } else {
                    counterSetCount = 1;
                }
                workoutSetCountEditText.setText("" + counterSetCount);
            }
        });
        workoutSetCountButtonMinusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterSetCount - INCREMENT_COUNT_LARGE >= 1) {
                    counterSetCount = counterSetCount - INCREMENT_COUNT_LARGE;
                } else {
                    counterSetCount = 1;
                }
                workoutSetCountEditText.setText("" + counterSetCount);
            }
        });
        workoutSetCountButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterSetCount = counterSetCount + INCREMENT_COUNT_SMALL;
                workoutSetCountEditText.setText("" + counterSetCount);
            }
        });
        workoutSetCountButtonPlusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterSetCount = counterSetCount + INCREMENT_COUNT_LARGE;
                workoutSetCountEditText.setText("" + counterSetCount);
            }
        });

        // Rep Count Button Listeners
        workoutRepCountButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterRepCount - INCREMENT_COUNT_SMALL >= 1) {
                    counterRepCount = counterRepCount - INCREMENT_COUNT_SMALL;
                } else {
                    counterRepCount = 1;
                }
                workoutRepCountEditText.setText("" + counterRepCount);
            }
        });
        workoutRepCountButtonMinusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterRepCount - INCREMENT_COUNT_LARGE >= 1) {
                    counterRepCount = counterRepCount - INCREMENT_COUNT_LARGE;
                } else {
                    counterRepCount = 1;
                }
                workoutRepCountEditText.setText("" + counterRepCount);
            }
        });
        workoutRepCountButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterRepCount = counterRepCount + INCREMENT_COUNT_SMALL;
                workoutRepCountEditText.setText("" + counterRepCount);
            }
        });
        workoutRepCountButtonPlusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterRepCount = counterRepCount + INCREMENT_COUNT_LARGE;
                workoutRepCountEditText.setText("" + counterRepCount);
            }
        });

        // Rep Duration Button Listeners
        workoutRepDurationButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterRepTime - INCREMENT_TIME_SHORT >= 0) {
                    counterRepTime = counterRepTime - INCREMENT_TIME_SHORT;
                } else {
                    counterRepTime = 0;
                }
                workoutRepDurationEditText.setText(TimeUtils.convertDate(counterRepTime, "mm:ss"));
            }
        });
        workoutRepDurationButtonMinusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterRepTime - INCREMENT_TIME_LONG >= 0) {
                    counterRepTime = counterRepTime - INCREMENT_TIME_LONG;
                } else {
                    counterRepTime = 0;
                }
                workoutRepDurationEditText.setText(TimeUtils.convertDate(counterRepTime, "mm:ss"));
            }
        });
        workoutRepDurationButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterRepTime = counterRepTime + INCREMENT_TIME_SHORT;
                workoutRepDurationEditText.setText(TimeUtils.convertDate(counterRepTime, "mm:ss"));
            }
        });
        workoutRepDurationButtonPlusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterRepTime = counterRepTime + INCREMENT_TIME_LONG;
                workoutRepDurationEditText.setText(TimeUtils.convertDate(counterRepTime, "mm:ss"));
            }
        });

        // Rest Duration Button Listeners
        workoutRestButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterRestTime - INCREMENT_TIME_SHORT >= 0) {
                    counterRestTime = counterRestTime - INCREMENT_TIME_SHORT;
                } else {
                    counterRestTime = 0;
                }
                workoutRestEditText.setText(TimeUtils.convertDate(counterRestTime, "mm:ss"));
            }
        });
        workoutRestButtonMinusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterRestTime - INCREMENT_TIME_LONG >= 0) {
                    counterRestTime = counterRestTime - INCREMENT_TIME_LONG;
                } else {
                    counterRestTime = 0;
                }
                workoutRestEditText.setText(TimeUtils.convertDate(counterRestTime, "mm:ss"));
            }
        });
        workoutRestButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterRestTime = counterRestTime + INCREMENT_TIME_SHORT;
                workoutRestEditText.setText(TimeUtils.convertDate(counterRestTime, "mm:ss"));
            }
        });
        workoutRestButtonPlusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterRestTime = counterRestTime + INCREMENT_TIME_LONG;
                workoutRestEditText.setText(TimeUtils.convertDate(counterRestTime, "mm:ss"));
            }
        });

        // Grade Type Listener
        workoutGradeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickGrade();
            }
        });

        // Move Count Button Listeners
        workoutMoveCountButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterMoveCount - INCREMENT_COUNT_SMALL >= 0) {
                    counterMoveCount = counterMoveCount - INCREMENT_COUNT_SMALL;
                } else {
                    counterMoveCount = 0;
                }
                workoutMoveCountEditText.setText(counterMoveCount + "");
            }
        });
        workoutMoveCountButtonMinusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterMoveCount - INCREMENT_COUNT_LARGE >= 0) {
                    counterMoveCount = counterMoveCount - INCREMENT_COUNT_LARGE;
                } else {
                    counterMoveCount = 0;
                }
                workoutMoveCountEditText.setText(counterMoveCount + "");
            }
        });
        workoutMoveCountButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterMoveCount = counterMoveCount + INCREMENT_COUNT_SMALL;
                workoutMoveCountEditText.setText(counterMoveCount + "");
            }
        });
        workoutMoveCountButtonPlusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterMoveCount = counterMoveCount + INCREMENT_COUNT_LARGE;
                workoutMoveCountEditText.setText(counterMoveCount + "");
            }
        });

        // Wall Angle Button Listeners
        workoutWallAngleButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterWallAngle - INCREMENT_COUNT_SMALL >= 0) {
                    counterWallAngle = counterWallAngle - INCREMENT_COUNT_SMALL;
                } else {
                    counterWallAngle = 0;
                }
                workoutWallAngleEditText.setText(counterWallAngle + "");
            }
        });
        workoutWallAngleButtonMinusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterWallAngle - INCREMENT_COUNT_LARGE >= 0) {
                    counterWallAngle = counterWallAngle - INCREMENT_COUNT_LARGE;
                } else {
                    counterWallAngle = 0;
                }
                workoutWallAngleEditText.setText(counterWallAngle + "");
            }
        });
        workoutWallAngleButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterWallAngle = counterWallAngle + INCREMENT_COUNT_SMALL;
                workoutWallAngleEditText.setText(counterWallAngle + "");
            }
        });
        workoutWallAngleButtonPlusBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterWallAngle = counterWallAngle + INCREMENT_COUNT_LARGE;
                workoutWallAngleEditText.setText(counterWallAngle + "");
            }
        });

        // Hold Type Listener
        workoutHoldTypeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickHoldType();
            }
        });

    }

    private void getTrainingInputFields(Bundle bundle) {

        Log.i("AddWorkout", "getTrainingInputFields");

        //int outputIsClimb = bundle.getInt("outputIsClimb");
        triggerWeight = bundle.getInt("outputIsWeight");
        triggerSetCount = bundle.getInt("outputIsSetCount");
        triggerRepCount = bundle.getInt("outputIsRepCountPerSet");
        triggerRepDuration = bundle.getInt("outputRepDurationPerSet");
        triggerRestPerSet = bundle.getInt("outputIsRestDuratonPerSet");
        triggerGradeCode = bundle.getInt("outputIsGradeCode");
        triggerMoveCount = bundle.getInt("outputIsMoveCount");
        triggerWallAngle = bundle.getInt("outputIsWallAngle");
        triggerHoldType = bundle.getInt("outputIsHoldType");

    }

    private void pickTraining() {

        Log.i("AddWorkout", "pickTraining");

        // Create new intent
        Intent selectorIntent = new Intent(this, ParentWorkoutHolder.class);
        // Start activity for getting result
        startActivityForResult(selectorIntent, PICK_TRAINING_REQUEST);
    }

    private void pickGrade() {

        Log.i("AddWorkout", "pickGrade");

        // Create new intent
        Intent selectorIntent = new Intent(this, ParentGradeHolder.class);
        // Start activity for getting result
        startActivityForResult(selectorIntent, PICK_GRADE_REQUEST);
    }

    private void pickHoldType() {

        Log.i("AddWorkout", "pickHoldType");

        // Create new intent
        Intent selectorIntent = new Intent(this, HoldTypeHolder.class);
        // Start activity for getting result
        startActivityForResult(selectorIntent, PICK_HOLDTYPE_REQUEST);
    }

    private void putWorkout(Intent inputData) {

        Log.i("AddWorkout", "putWorkout");

        // The user picked a workout, get the workout name
        outputWorkoutNumber = inputData.getIntExtra("OutputWorkoutNumber", -1);
        String outputStringWorkoutName = DatabaseReadWrite.getWorkoutTextClimb(outputWorkoutNumber, this);

        // The user picked a workout, get the workout type name
        // Put workout text in the view
        outputWorkoutName = inputData.getIntExtra("OutputWorkoutName", -1);
        String outputStringWorkoutType = DatabaseReadWrite.getWorkoutTypeClimb(outputWorkoutName, this);
        workoutTrainingEditText.setText(outputStringWorkoutType + " | " + outputStringWorkoutName);

        // Display the missing fields
        Bundle bundle = DatabaseReadWrite.workoutLoadFields(outputWorkoutNumber, this);
        getTrainingInputFields(bundle);
        showViews();
    }

    private void putGrade(Intent data) {

        Log.i("AddWorkout", "putGrade");

        // The user picked a grade, get the grade number
        outputGradeNumber = data.getIntExtra("OutputGradeNumber", -1);
        String outputStringGradeName = DatabaseReadWrite.getGradeTextClimb(outputGradeNumber, this);

        Log.i("ADDWORKOUT LOG", "put grade: " + outputGradeNumber + " " + outputGradeName);
        outputGradeName = data.getIntExtra("OutputGradeName", -1);
        String outputStringGradeType = DatabaseReadWrite.getGradeTypeClimb(outputGradeName, this);
        workoutGradeEditText.setText(outputStringGradeType + " | " + outputStringGradeName);
    }

    private void putHoldType(Intent data) {

        Log.i("AddWorkout", "putHoldType");

        outputHoldType = data.getIntExtra("OutputData", -1);
        String outputStringHoldType = DatabaseReadWrite.getHoldTypeText(outputHoldType, this);
        workoutHoldTypeEditText.setText(outputStringHoldType);
    }

    private void resetOutputs() {

        Log.i("AddWorkout", "resetOutputs");

        // reset output values
        outputGradeNumber = -1;
        outputGradeName = -1;
        outputHoldType = -1;
        outputCompleteCheckedState = 0;

        // reset counters
        counterWeight = 0;
        counterRestTime = 0;
        counterRepCount = 1;
        counterRepTime = 0;
        counterSetCount = 1;
        counterMoveCount = 0;
        counterWallAngle = 0;
    }

    private void showViews() {
        Log.i("AddWorkout", "showViews");

        /*switch (triggerWeight) {
            case DatabaseContract.IS_TRUE:
                workoutWeightWrapper.setVisibility(View.VISIBLE);
                break;
            case DatabaseContract.IS_FALSE:
                workoutWeightWrapper.setVisibility(View.GONE);
                break;
        }*/

        if (triggerWeight == DatabaseContract.IS_TRUE) {
            workoutWeightWrapper.setVisibility(View.VISIBLE);
        } else {
            workoutWeightWrapper.setVisibility(View.GONE);
        }

        if (triggerSetCount == DatabaseContract.IS_TRUE) {
            workoutSetCountWrapper.setVisibility(View.VISIBLE);
        } else {
            workoutSetCountWrapper.setVisibility(View.GONE);
        }

        if (triggerRepCount == DatabaseContract.IS_TRUE) {
            workoutRepCountWrapper.setVisibility(View.VISIBLE);
        } else {
            workoutRepCountWrapper.setVisibility(View.GONE);
        }

        if (triggerRepDuration == DatabaseContract.IS_TRUE) {
            workoutRepDurationWrapper.setVisibility(View.VISIBLE);
        } else {
            workoutRepDurationWrapper.setVisibility(View.GONE);
        }

        if (triggerRestPerSet == DatabaseContract.IS_TRUE) {
            workoutRestWrapper.setVisibility(View.VISIBLE);
        } else {
            workoutRestWrapper.setVisibility(View.GONE);
        }

        if (triggerGradeCode == DatabaseContract.IS_TRUE) {
            workoutGradeWrapper.setVisibility(View.VISIBLE);
        } else {
            workoutGradeWrapper.setVisibility(View.GONE);
        }

        if (triggerMoveCount == DatabaseContract.IS_TRUE) {
            workoutMoveCountWrapper.setVisibility(View.VISIBLE);
        } else {
            workoutMoveCountWrapper.setVisibility(View.GONE);
        }

        if (triggerWallAngle == DatabaseContract.IS_TRUE) {
            workoutWallAngleWrapper.setVisibility(View.VISIBLE);
        } else {
            workoutWallAngleWrapper.setVisibility(View.GONE);
        }

        if (triggerHoldType == DatabaseContract.IS_TRUE) {
            workoutHoldTypeWrapper.setVisibility(View.VISIBLE);
        } else {
            workoutHoldTypeWrapper.setVisibility(View.GONE);
        }

        workoutCompleteWrapper.setVisibility(View.VISIBLE);

    }

    private void hideViews() {

        Log.i("AddWorkout", "hideViews");

        // Weight Data Input Wrapper
        workoutWeightWrapper.setVisibility(View.GONE);
        // Set Count Input Wrapper
        workoutSetCountWrapper.setVisibility(View.GONE);
        // Rep Count Input Wrapper
        workoutRepCountWrapper.setVisibility(View.GONE);
        // Rep Duration Input Wrapper
        workoutRepDurationWrapper.setVisibility(View.GONE);
        // Rest Per Set Input Wrapper
        workoutRestWrapper.setVisibility(View.GONE);
        Log.i("AddWorkout", "hideViews, workoutRestWrapper = " + workoutRestWrapper.getVisibility());
        // Grade or Difficulty Input Wrapper
        workoutGradeWrapper.setVisibility(View.GONE);
        // Move Count Input Wrapper
        workoutMoveCountWrapper.setVisibility(View.GONE);
        // Wall Angle Input Wrapper
        workoutWallAngleWrapper.setVisibility(View.GONE);
        // Hold Type Input Wrapper
        workoutHoldTypeWrapper.setVisibility(View.GONE);
        // Complete Input Wrapper
        workoutCompleteWrapper.setVisibility(View.GONE);
    }

    private void fillViews() {

        Log.i("AddWorkout", "fillViews");

        workoutDateEditText.setText(TimeUtils.convertDate(outputDate, "yyyy-MM-dd"));
        workoutWeightEditText.setText("" + counterWeight + " Kg");
        workoutRestEditText.setText(TimeUtils.convertDate(counterRestTime, "mm:ss"));
        workoutRepCountEditText.setText(Integer.toString(counterRepCount));
        workoutRepDurationEditText.setText(TimeUtils.convertDate(counterRepTime, "mm:ss"));
        workoutSetCountEditText.setText(Integer.toString(counterSetCount));
        workoutMoveCountEditText.setText(Integer.toString(counterMoveCount));
        workoutWallAngleEditText.setText(Integer.toString(counterWallAngle));

        if (outputGradeNumber == -1) {
            workoutGradeEditText.setText("");
        } else {
            String outputStringGradeName = DatabaseReadWrite.getGradeTextClimb(outputGradeNumber, this);
            String outputStringGradeType = DatabaseReadWrite.getGradeTypeClimb(outputGradeName, this);
            workoutGradeEditText.setText(outputStringGradeType + " | " + outputStringGradeName);
        }

        if (outputHoldType == -1) {
            workoutHoldTypeEditText.setText("");
        } else {
            String outputStringHoldType = DatabaseReadWrite.getHoldTypeText(outputHoldType, this);
            workoutHoldTypeEditText.setText(outputStringHoldType);
        }

        if (outputWorkoutNumber == -1) {
            workoutTrainingEditText.setText("");
        } else {
            String outputStringWorkoutName = DatabaseReadWrite.getWorkoutTextClimb(outputWorkoutNumber, this);
            String outputStringWorkoutType = DatabaseReadWrite.getWorkoutTypeClimb(outputWorkoutName, this);
            workoutTrainingEditText.setText(outputStringWorkoutType + " | " + outputStringWorkoutName);
        }

        if (outputCompleteCheckedState == 0) {
            workoutCompleteCheckBox.setChecked(false);
        } else if (outputCompleteCheckedState == 1) {
            workoutCompleteCheckBox.setChecked(true);
        }

    }

    private void getWorkoutInfoFromBundle(Bundle inputBundle) {

        Log.i("AddWorkout", "getWorkoutInfoFromBundle");

        outputWorkoutNumber = inputBundle.getInt("outputWorkoutCode");
        outputWorkoutName = inputBundle.getInt("outputWorkoutTypeCode");
        counterWeight = inputBundle.getDouble("outputWeight");
        counterRestTime = inputBundle.getInt("outputRestDuration");
        counterRepCount = inputBundle.getInt("outputRepCount");
        counterRepTime = inputBundle.getInt("outputRepDuration");
        counterSetCount = inputBundle.getInt("outputSetCount");
        outputGradeName = inputBundle.getInt("outputGradeTypeCode");
        outputGradeNumber = inputBundle.getInt("outputGradeCode");
        counterMoveCount = inputBundle.getInt("outputMoveCount");
        counterWallAngle = inputBundle.getInt("outputWallAngle");
        outputHoldType = inputBundle.getInt("outputHoldType");
        outputCompleteCheckedState = inputBundle.getInt("outputIsComplete");
    }

}
