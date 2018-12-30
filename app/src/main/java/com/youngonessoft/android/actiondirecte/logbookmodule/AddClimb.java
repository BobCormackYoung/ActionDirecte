package com.youngonessoft.android.actiondirecte.logbookmodule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    final int PICK_GRADE_REQUEST = 1;
    final int PICK_ASCENT_TYPE_REQUEST = 2;
    final int ADD_CLIMB_NEW = 0;
    final int ADD_CLIMB_EDIT = 1;
    int outputGradeNumber = -1;
    int outputGradeName = -1;
    int outputAscent = -1;
    int outputLocationId;
    String outputLocationName = null;
    String outputRouteName = null;
    String outputDateString = null;
    int outputFirstAscent = -1;
    long outputDate = -1;
    int inputIntentCode = -1;
    int inputRowID = -1;
    private static final int LOCATION_REQUEST = 3;
    private static final int REQUEST_CHECK_SETTINGS = 4;
    Context mContext;
    int outputHasGps = 0;
    double outputLatitude = 0;
    double outputLongitude = 0;
    boolean gpsAccessPermission = false;
    Boolean mRequestingLocationUpdates = false;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_climb);

        mContext = AddClimb.this;

        checkGpsAccessPermission();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i("AddClimb GPS", "mLocationCallback > onLocationResult = null");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    TextView textViewLatitude = findViewById(R.id.tv_latitude);
                    TextView textViewLongitude = findViewById(R.id.tv_longitude);
                    outputLatitude = location.getLatitude();
                    textViewLatitude.setText("" + outputLatitude);
                    outputLongitude = location.getLongitude();
                    textViewLongitude.setText("" + outputLongitude);
                    Log.i("AddClimb GPS", "mLocationCallback > onLocationResult =  long: " + outputLongitude + " lat: " + outputLatitude);
                    outputHasGps = DatabaseContract.IS_GPS_TRUE;

                    // new GPS value... turn off GPS updates
                    Log.i("AddClimb GPS", "mLocationCallback > onLocationResult = turning off location updates");
                    stopLocationUpdates();
                    mRequestingLocationUpdates = false;
                }
            }

            ;
        };

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
            outputLocationId = bundle.getInt("outputLocationId");
            Bundle locationDataBundle = DatabaseReadWrite.LocationLoadEntry(outputLocationId, this);

            EditText routeNameView = findViewById(R.id.editText);
            outputRouteName = bundle.getString("outputRouteName");
            routeNameView.setText(outputRouteName);

            EditText locationNameView = findViewById(R.id.editText2);
            outputLocationName = locationDataBundle.getString("outputLocationName");
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

            // Set grade view
            // Get grade name
            outputGradeNumber = bundle.getInt("outputGradeNumber");
            outputGradeName = bundle.getInt("outputGradeName");
            String outputStringGradeName = DatabaseReadWrite.getGradeTextClimb(outputGradeNumber, this);
            String outputStringGradeType = DatabaseReadWrite.getGradeTypeClimb(outputGradeName, this);
            EditText gradeView = findViewById(R.id.editText4);
            gradeView.setText(outputStringGradeType + " | " + outputStringGradeName);

            // Set ascent type
            outputAscent = bundle.getInt("outputAscent");
            String outputStringAscentType = DatabaseReadWrite.getAscentNameTextClimb(outputAscent, this);
            EditText ascentTypeView = findViewById(R.id.editText3);
            ascentTypeView.setText(outputStringAscentType);

            // Set GPS data
            outputHasGps = locationDataBundle.getInt("outputIsGps");
            TextView textViewLatitude = findViewById(R.id.tv_latitude);
            TextView textViewLongitude = findViewById(R.id.tv_longitude);
            if (outputHasGps == DatabaseContract.IS_GPS_TRUE) {
                outputLatitude = locationDataBundle.getDouble("outputGpsLatitude");
                textViewLatitude.setText("" + outputLatitude);
                outputLongitude = locationDataBundle.getDouble("outputGpsLongitude");
                textViewLongitude.setText("" + outputLongitude);
            } else {
                textViewLatitude.setText("No data");
                textViewLongitude.setText("No data");
            }
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

        // Listener for GPS button
        Button gpsButton = findViewById(R.id.bt_getGps);
        gpsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (gpsAccessPermission) {
                    Log.i("AddClimb GPS", "onCreate > GPS Button Pressed Access > gpsAccessPermission=true");
                    gpsGetLastLocation();
                    createLocationRequest();
                    startLocationUpdates();
                } else {
                    Log.i("AddClimb GPS", "onCreate > GPS Button Pressed Access > gpsAccessPermission=false");
                }
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
                        long updateResult = DatabaseReadWrite.updateClimbLogData(outputRouteName, outputLocationName, outputAscent,
                                outputGradeName, outputGradeNumber, outputDate, outputFirstAscent, outputHasGps, outputLatitude,
                                outputLongitude, inputRowID, AddClimb.this);
                        //Toast.makeText(getApplicationContext(), "Existing Row ID: " + String.valueOf(updateResult), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (inputIntentCode == ADD_CLIMB_NEW) {
                        long writeResult = DatabaseReadWrite.writeClimbLogData(outputRouteName, outputLocationName, outputAscent,
                                outputGradeName, outputGradeNumber, outputDate, outputFirstAscent, outputHasGps, outputLatitude,
                                outputLongitude, AddClimb.this);
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
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Log.i("AddClimb GPS", "onActivityResult = Success!");
                mRequestingLocationUpdates = true;
            } else {
                Log.i("AddClimb GPS", "onActivityResult = No Success!");
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

    // check permission for accessing location
    private void checkGpsAccessPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.i("AddClimb GPS", "checkGpsAccessPermission = No access, ask get permission");
                requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
            }
        } else {
            gpsAccessPermission = true;
        }
    }

    // handle gps permission request result
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            gpsAccessPermission = true;
            Log.i("AddClimb GPS", "onRequestPermissionsResult = Access granted");
        }

    }

    // get the last GPS location
    @SuppressLint("MissingPermission")
    private void gpsGetLastLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    TextView textViewLatitude = findViewById(R.id.tv_latitude);
                    TextView textViewLongitude = findViewById(R.id.tv_longitude);
                    outputLatitude = location.getLatitude();
                    textViewLatitude.setText("" + outputLatitude);
                    outputLongitude = location.getLongitude();
                    textViewLongitude.setText("" + outputLongitude);
                    Log.i("AddClimb GPS", "gpsGetLastLocation > long: " + outputLongitude + " lat: " + outputLatitude);
                    outputHasGps = DatabaseContract.IS_GPS_TRUE;
                } else {
                    Log.i("AddClimb GPS", "gpsGetLastLocation = Null");
                }
            }
        });
    }

    // create the location request
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // 10 seonds
        mLocationRequest.setFastestInterval(5000); // 5 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                mRequestingLocationUpdates = true;
                Log.i("AddClimb GPS", "createLocationRequest > task.addOnSuccessListener = Success!");
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    Log.i("AddClimb GPS", "createLocationRequest > task.addOnFailureListener = No Success!");
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(AddClimb.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        Log.i("AddClimb GPS", "startLocationUpdates = starting updates");

        if (gpsAccessPermission) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            Log.i("AddClimb GPS", "onResume = restarting location updates");
            startLocationUpdates();
        } else {
            Log.i("AddClimb GPS", "onResume = not restarting location updates");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            Log.i("AddClimb GPS", "onPause = stopping location updates");
            stopLocationUpdates();
        } else {
            Log.i("AddClimb GPS", "onPause = no location updates to stop");
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        Log.i("AddClimb GPS", "stopLocationUpdates = stopping location updates");
    }

}

