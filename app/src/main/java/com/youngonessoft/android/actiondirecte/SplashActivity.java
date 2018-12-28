package com.youngonessoft.android.actiondirecte;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.youngonessoft.android.actiondirecte.analysismodule.AnalysisActivity;
import com.youngonessoft.android.actiondirecte.calendarmodule.CalendarOverview;
import com.youngonessoft.android.actiondirecte.data.DatabaseHelper;
import com.youngonessoft.android.actiondirecte.logbookmodule.LogBook;

//TODO: Add license for https://github.com/PhilJay/MPAndroidChart#licence, http://www.apache.org/licenses/LICENSE-2.0

public class SplashActivity extends AppCompatActivity {

    public static final String LOG_TAG = "SplashActivity";
    final int BACKGROUND_COUNT = 5;

    /** Database helper that will provide us access to the database */
    private DatabaseHelper mDbHelper;
    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST = 1337;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);

        ImageView backGroundImage = findViewById(R.id.fullscreen_content);
        //final int random = new Random().nextInt(BACKGROUND_COUNT);
        int random = 0;

        if (random == 0) {
            backGroundImage.setImageResource(R.drawable.background_climbersbackpackcaribiners);
        } else if (random == 1) {
            backGroundImage.setImageResource(R.drawable.background_cloudwrappedmountain);
        } else if (random == 2) {
            backGroundImage.setImageResource(R.drawable.background_cloudywintericeland);
        } else if (random == 3) {
            backGroundImage.setImageResource(R.drawable.background_freshwatermountaincreek);
        } else if (random == 4) {
            backGroundImage.setImageResource(R.drawable.background_mountainmagichour);
        }

        Log.i(LOG_TAG, "Random number: " + random);

        View button1 = findViewById(R.id.button1);
        View button2 = findViewById(R.id.button2);
        View button3 = findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to open the {@link FamilyActivity}
                Intent DailyViewIntent = new Intent(SplashActivity.this, LogBook.class);
                // Start the new activity
                startActivity(DailyViewIntent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to open the {@link FamilyActivity}
                Intent CalendarViewIntent = new Intent(SplashActivity.this, CalendarOverview.class);
                // Start the new activity
                startActivity(CalendarViewIntent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to open the {@link FamilyActivity}
                Intent analysisViewIntent = new Intent(SplashActivity.this, AnalysisActivity.class);
                // Start the new activity
                startActivity(analysisViewIntent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.close();

    }

}
