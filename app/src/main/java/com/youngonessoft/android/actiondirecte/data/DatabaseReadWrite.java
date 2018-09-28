package com.youngonessoft.android.actiondirecte.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import static com.youngonessoft.android.actiondirecte.util.TimeUtils.convertDate;

/**
 * Created by Bobek on 27/02/2018.
 */

public class DatabaseReadWrite {

    private final static String LOG_TAG = "DatabaseReadWrite";
    private final static int LOG_TRIGGER = 1;

    /**
     * Get the position in the ViewPager for a given day
     *
     * @param inputRowID the row that needs to be read from the database
     * @param mContext   the context
     * @return A bundle containing:
     * String outputRouteName
     * String outputLocationName
     * Long outputDate
     * String outputDateString
     * Int outputFirstAscent
     * Int outputGradeNumber
     * Int outputGradeName
     * Int outputAscent
     */
    public static Bundle EditClimbLoadEntry(int inputRowID, Context mContext) {

        Bundle outputBundle = new Bundle();

        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        String[] projection = {
                DatabaseContract.ClimbLogEntry._ID,
                DatabaseContract.ClimbLogEntry.COLUMN_DATE,
                DatabaseContract.ClimbLogEntry.COLUMN_NAME,
                DatabaseContract.ClimbLogEntry.COLUMN_GRADETYPECODE,
                DatabaseContract.ClimbLogEntry.COLUMN_GRADECODE,
                DatabaseContract.ClimbLogEntry.COLUMN_ASCENTTYPECODE,
                DatabaseContract.ClimbLogEntry.COLUMN_LOCATION,
                DatabaseContract.ClimbLogEntry.COLUMN_FIRSTASCENTCODE,
                DatabaseContract.ClimbLogEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.ClimbLogEntry._ID + "=?";
        String[] whereValue = {String.valueOf(inputRowID)};

        Cursor cursor = database.query(DatabaseContract.ClimbLogEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        try {
            cursor.moveToFirst();

            // Get and set route name
            int idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_NAME);
            String outputRouteName = cursor.getString(idColumnOutput);

            // Get and set  location name
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_LOCATION);
            String outputLocationName = cursor.getString(idColumnOutput);

            // Get date
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_DATE);
            Long outputDate = cursor.getLong(idColumnOutput);
            String outputDateString = convertDate(outputDate, "dd/MM/yyyy");

            // Get whether first ascent or not
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_FIRSTASCENTCODE);
            int outputFirstAscent = cursor.getInt(idColumnOutput);

            // Get grade
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_GRADECODE);
            int outputGradeNumber = cursor.getInt(idColumnOutput);
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_GRADETYPECODE);
            int outputGradeName = cursor.getInt(idColumnOutput);

            // Get ascent type
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_ASCENTTYPECODE);
            int outputAscent = cursor.getInt(idColumnOutput);

            outputBundle.putString("outputRouteName", outputRouteName);
            outputBundle.putString("outputLocationName", outputLocationName);
            outputBundle.putLong("outputDate", outputDate);
            outputBundle.putString("outputDateString", outputDateString);
            outputBundle.putInt("outputFirstAscent", outputFirstAscent);
            outputBundle.putInt("outputGradeNumber", outputGradeNumber);
            outputBundle.putInt("outputGradeName", outputGradeName);
            outputBundle.putInt("outputAscent", outputAscent);

            return outputBundle;

        } finally {
            cursor.close();
            database.close();
        }

    }

    /**
     * Get the position in the ViewPager for a given day
     *
     * @param inputRowID the row that needs to be read from the database
     * @param mContext   the context
     * @return A bundle containing all values
     */
    public static Bundle EditWorkoutLoadEntry(int inputRowID, Context mContext) {

        Bundle outputBundle = new Bundle();

        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        String[] projection = {
                DatabaseContract.WorkoutLogEntry._ID,
                DatabaseContract.WorkoutLogEntry.COLUMN_DATE,
                DatabaseContract.WorkoutLogEntry.COLUMN_WORKOUTTYPECODE,
                DatabaseContract.WorkoutLogEntry.COLUMN_WORKOUTCODE,
                DatabaseContract.WorkoutLogEntry.COLUMN_ISCLIMB,
                DatabaseContract.WorkoutLogEntry.COLUMN_WEIGHT,
                DatabaseContract.WorkoutLogEntry.COLUMN_SETCOUNT,
                DatabaseContract.WorkoutLogEntry.COLUMN_REPCOUNTPERSET,
                DatabaseContract.WorkoutLogEntry.COLUMN_REPDURATIONPERSET,
                DatabaseContract.WorkoutLogEntry.COLUMN_RESTDURATIONPERSET,
                DatabaseContract.WorkoutLogEntry.COLUMN_GRADETYPECODE,
                DatabaseContract.WorkoutLogEntry.COLUMN_GRADECODE,
                DatabaseContract.WorkoutLogEntry.COLUMN_MOVECOUNT,
                DatabaseContract.WorkoutLogEntry.COLUMN_WALLANGLE,
                DatabaseContract.WorkoutLogEntry.COLUMN_HOLDTYPE,
                DatabaseContract.WorkoutLogEntry.COLUMN_ISCOMPLETE};
        String whereClause = DatabaseContract.WorkoutLogEntry._ID + "=?";
        String[] whereValue = {String.valueOf(inputRowID)};

        Cursor cursor = database.query(DatabaseContract.WorkoutLogEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        try {
            cursor.moveToFirst();

            int idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_DATE);
            Long outputDate = cursor.getLong(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_WORKOUTTYPECODE);
            int outputWorkoutTypeCode = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_WORKOUTCODE);
            int outputWorkoutCode = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_ISCLIMB);
            int outputIsClimb = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_WEIGHT);
            double outputWeight = cursor.getDouble(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_SETCOUNT);
            int outputSetCount = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_REPCOUNTPERSET);
            int outputRepCount = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_REPDURATIONPERSET);
            int outputRepDuration = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_RESTDURATIONPERSET);
            int outputRestDuration = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_GRADETYPECODE);
            int outputGradeTypeCode = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_GRADECODE);
            int outputGradeCode = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_MOVECOUNT);
            int outputMoveCount = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_WALLANGLE);
            int outputWallAngle = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_HOLDTYPE);
            int outputHoldType = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutLogEntry.COLUMN_ISCOMPLETE);
            int outputIsComplete = cursor.getInt(idColumnOutput);

            outputBundle.putLong("outputDate", outputDate);
            outputBundle.putInt("outputWorkoutTypeCode", outputWorkoutTypeCode);
            outputBundle.putInt("outputWorkoutCode", outputWorkoutCode);
            outputBundle.putInt("outputIsClimb", outputIsClimb);
            outputBundle.putDouble("outputWeight", outputWeight);
            outputBundle.putInt("outputSetCount", outputSetCount);
            outputBundle.putInt("outputRepCount", outputRepCount);
            outputBundle.putInt("outputRepDuration", outputRepDuration);
            outputBundle.putInt("outputRestDuration", outputRestDuration);
            outputBundle.putInt("outputGradeTypeCode", outputGradeTypeCode);
            outputBundle.putInt("outputGradeCode", outputGradeCode);
            outputBundle.putInt("outputMoveCount", outputMoveCount);
            outputBundle.putInt("outputWallAngle", outputWallAngle);
            outputBundle.putInt("outputHoldType", outputHoldType);
            outputBundle.putInt("outputIsComplete", outputIsComplete);

            return outputBundle;

        } finally {
            cursor.close();
            database.close();
        }

    }

    /**
     * Get the Ascent type name for a given ID
     *
     * @param infoCode the ascent type ID
     * @param mContext the context
     * @return A string containing the Ascent Type Name
     */
    public static String getAscentNameTextClimb(int infoCode, Context mContext) {

        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        //ascent type
        String[] projection = {
                DatabaseContract.AscentEntry._ID,
                DatabaseContract.AscentEntry.COLUMN_ASCENTTYPENAME};
        String whereClause = DatabaseContract.AscentEntry._ID + "=?";
        String[] whereValue = {String.valueOf(infoCode)};

        Cursor cursor = database.query(DatabaseContract.AscentEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.AscentEntry.COLUMN_ASCENTTYPENAME);

        try {
            cursor.moveToFirst();
            return cursor.getString(idColumnOutput);
        } finally {
            cursor.close();
            database.close();
        }


    }

    /**
     * Get the Hold type name for a given ID
     *
     * @param infoCode the ascent type ID
     * @param mContext the context
     * @return A string containing the Hold Type Name
     */
    public static String getHoldTypeText(int infoCode, Context mContext) {

        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        //ascent type
        String[] projection = {
                DatabaseContract.HoldTypeEntry._ID,
                DatabaseContract.HoldTypeEntry.COLUMN_HOLDTYPE};
        String whereClause = DatabaseContract.HoldTypeEntry._ID + "=?";
        String[] whereValue = {String.valueOf(infoCode)};

        Cursor cursor = database.query(DatabaseContract.HoldTypeEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.HoldTypeEntry.COLUMN_HOLDTYPE);

        try {
            cursor.moveToFirst();
            return cursor.getString(idColumnOutput);
        } finally {
            cursor.close();
            database.close();
        }


    }

    /**
     * Get the Grade name for a given ID
     *
     * @param gradeCode the grade ID
     * @param mContext  the context
     * @return A string containing the Grade Name
     */
    public static String getGradeTextClimb(int gradeCode, Context mContext) {

        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        //grade type
        String[] projection = {
                DatabaseContract.GradeListEntry._ID,
                DatabaseContract.GradeListEntry.COLUMN_GRADENAME};
        String whereClause = DatabaseContract.GradeListEntry._ID + "=?";
        String[] whereValue = {String.valueOf(gradeCode)};

        Cursor cursor = database.query(DatabaseContract.GradeListEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.GradeListEntry.COLUMN_GRADENAME);

        try {
            cursor.moveToFirst();
            return cursor.getString(idColumnOutput);
        } finally {
            cursor.close();
            database.close();
        }

    }

    /**
     * Get the Grade name for a given ID
     *
     * @param gradeTypeCode the grade type ID
     * @param mContext      the context
     * @return A string containing the Grade Type Name
     */
    public static String getGradeTypeClimb(int gradeTypeCode, Context mContext) {

        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        //grade type
        String[] projection = {
                DatabaseContract.GradeTypeEntry._ID,
                DatabaseContract.GradeTypeEntry.COLUMN_GRADETYPENAME};
        String whereClause = DatabaseContract.GradeTypeEntry._ID + "=?";
        String[] whereValue = {String.valueOf(gradeTypeCode)};

        Cursor cursor = database.query(DatabaseContract.GradeTypeEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.GradeTypeEntry.COLUMN_GRADETYPENAME);

        try {
            cursor.moveToFirst();
            return cursor.getString(idColumnOutput);
        } finally {
            cursor.close();
            database.close();
        }

    }

    /**
     * Insert the climb data into the database
     *
     * @param routeName    String route name
     * @param locationName String location name
     * @param ascentType   int code for ascent
     * @param gradeType    int code for grade type
     * @param gradeNumber  int code for specific grade
     * @param date         int date&time in milliseconds from epoch
     * @param firstAscent  int firstascent or not (1 = true, 0 = false)
     * @param mContext     Context context
     * @return the row ID that has been added
     */
    public static long writeClimbLogData(String routeName, String locationName, int ascentType, int gradeType, int gradeNumber, long date, int firstAscent, Context mContext) {
        // Gets the database in write mode
        //Create handler to connect to SQLite DB
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_DATE, date);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_NAME, routeName);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_GRADETYPECODE, gradeType);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_GRADECODE, gradeNumber);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_ASCENTTYPECODE, ascentType);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_LOCATION, locationName);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_FIRSTASCENTCODE, firstAscent);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_ISCLIMB, DatabaseContract.IS_CLIMB);

        long newRowId = database.insert(DatabaseContract.ClimbLogEntry.TABLE_NAME, null, values);
        database.close();
        return newRowId;

    }

    /**
     * Update existing climb log entry
     *
     * @param routeName    String route name
     * @param locationName String location name
     * @param ascentType   int code for ascent
     * @param gradeType    int code for grade type
     * @param gradeNumber  int code for specific grade
     * @param date         int date&time in milliseconds from epoch
     * @param firstAscent  int firstascent or not (1 = true, 0 = false)
     * @param rowID        int row ID of log that we want to edit
     * @param mContext     Context context
     * @return the row ID that has been edited
     */
    public static long updateClimbLogData(String routeName, String locationName, int ascentType, int gradeType, int gradeNumber, long date, int firstAscent, int rowID, Context mContext) {

        // Gets the database in write mode
        //Create handler to connect to SQLite DB
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_DATE, date);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_NAME, routeName);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_GRADETYPECODE, gradeType);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_GRADECODE, gradeNumber);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_ASCENTTYPECODE, ascentType);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_LOCATION, locationName);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_FIRSTASCENTCODE, firstAscent);
        values.put(DatabaseContract.ClimbLogEntry.COLUMN_ISCLIMB, DatabaseContract.IS_CLIMB);

        String whereClauseFive = DatabaseContract.ClimbLogEntry._ID + "=?";
        String[] whereValueFive = {String.valueOf(rowID)};

        long newRowId = database.update(DatabaseContract.ClimbLogEntry.TABLE_NAME, values, whereClauseFive, whereValueFive);
        database.close();
        return newRowId;

    }

    /**
     * Get the Workout name for a given ID
     *
     * @param workoutCode the workout ID
     * @param mContext    the context
     * @return A string containing the Workout Name
     */
    public static String getWorkoutTextClimb(int workoutCode, Context mContext) {

        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        //grade type
        String[] projection = {
                DatabaseContract.WorkoutListEntry._ID,
                DatabaseContract.WorkoutListEntry.COLUMN_NAME};
        String whereClause = DatabaseContract.WorkoutListEntry._ID + "=?";
        String[] whereValue = {String.valueOf(workoutCode)};

        Cursor cursor = database.query(DatabaseContract.WorkoutListEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_NAME);

        try {
            cursor.moveToFirst();
            return cursor.getString(idColumnOutput);
        } finally {
            cursor.close();
            database.close();
        }

    }

    /**
     * Get the workout name for a given ID
     *
     * @param workoutTypeCode the workout type ID
     * @param mContext        the context
     * @return A string containing the workout Type Name
     */
    public static String getWorkoutTypeClimb(int workoutTypeCode, Context mContext) {

        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        //grade type
        String[] projection = {
                DatabaseContract.WorkoutTypeEntry._ID,
                DatabaseContract.WorkoutTypeEntry.COLUMN_WORKOUTTYPENAME};
        String whereClause = DatabaseContract.WorkoutTypeEntry._ID + "=?";
        String[] whereValue = {String.valueOf(workoutTypeCode)};

        Cursor cursor = database.query(DatabaseContract.WorkoutTypeEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutTypeEntry.COLUMN_WORKOUTTYPENAME);

        try {
            cursor.moveToFirst();
            return cursor.getString(idColumnOutput);
        } finally {
            cursor.close();
            database.close();
        }

    }

    /**
     * Load the field data for the add workout class
     *
     * @param inputRowID the workout row of interest
     * @param mContext   the context
     * @return a Bundle containing true/false statements for
     */
    public static Bundle workoutLoadFields(int inputRowID, Context mContext) {

        Bundle outputBundle = new Bundle();

        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        String[] projection = {
                DatabaseContract.WorkoutListEntry._ID,
                DatabaseContract.WorkoutListEntry.COLUMN_ISCLIMB,
                DatabaseContract.WorkoutListEntry.COLUMN_ISGRADECODE,
                DatabaseContract.WorkoutListEntry.COLUMN_ISREPCOUNTPERSET,
                DatabaseContract.WorkoutListEntry.COLUMN_ISREPDURATIONPERSET,
                DatabaseContract.WorkoutListEntry.COLUMN_ISRESTDURATIONPERSET,
                DatabaseContract.WorkoutListEntry.COLUMN_ISSETCOUNT,
                DatabaseContract.WorkoutListEntry.COLUMN_ISMOVECOUNT,
                DatabaseContract.WorkoutListEntry.COLUMN_ISWALLANGLE,
                DatabaseContract.WorkoutListEntry.COLUMN_ISHOLDTYPE,
                DatabaseContract.WorkoutListEntry.COLUMN_ISWEIGHT};
        String whereClause = DatabaseContract.WorkoutListEntry._ID + "=?";
        String[] whereValue = {String.valueOf(inputRowID)};

        Cursor cursor = database.query(DatabaseContract.WorkoutListEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        try {
            cursor.moveToFirst();

            // Get and set route name
            int idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISCLIMB);
            int outputIsClimb = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISGRADECODE);
            int outputIsGradeCode = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISREPCOUNTPERSET);
            int outputIsRepCountPerSet = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISREPDURATIONPERSET);
            int outputRepDurationPerSet = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISRESTDURATIONPERSET);
            int outputIsRestDuratonPerSet = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISSETCOUNT);
            int outputIsSetCount = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISMOVECOUNT);
            int outputIsMoveCount = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISWALLANGLE);
            int outputIsWallAngle = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISHOLDTYPE);
            int outputIsHoldType = cursor.getInt(idColumnOutput);

            idColumnOutput = cursor.getColumnIndex(DatabaseContract.WorkoutListEntry.COLUMN_ISWEIGHT);
            int outputIsWeight = cursor.getInt(idColumnOutput);

            outputBundle.putInt("outputIsClimb", outputIsClimb);
            outputBundle.putInt("outputIsGradeCode", outputIsGradeCode);
            outputBundle.putInt("outputIsRepCountPerSet", outputIsRepCountPerSet);
            outputBundle.putInt("outputRepDurationPerSet", outputRepDurationPerSet);
            outputBundle.putInt("outputIsRestDuratonPerSet", outputIsRestDuratonPerSet);
            outputBundle.putInt("outputIsSetCount", outputIsSetCount);
            outputBundle.putInt("outputIsMoveCount", outputIsMoveCount);
            outputBundle.putInt("outputIsWallAngle", outputIsWallAngle);
            outputBundle.putInt("outputIsHoldType", outputIsHoldType);
            outputBundle.putInt("outputIsWeight", outputIsWeight);

            return outputBundle;

        } finally {
            cursor.close();
            database.close();
        }

    }

    /**
     * Insert workout into the database
     *
     * @param date            long date
     * @param workoutTypeCode int code for workout tpe
     * @param workoutCode     int code for actual workout
     * @param weight          long weight
     * @param setCount        int
     * @param repCount        int
     * @param repDuration     int
     * @param restDuration    int
     * @param gradeTypeCode   int
     * @param gradeCode       int
     * @param mContext        Context
     * @return
     */
    public static long writeWorkoutLogData(long date, int workoutTypeCode, int workoutCode, double weight, int setCount, int repCount, int repDuration, int restDuration, int gradeTypeCode, int gradeCode, int moveCount, int holdType, int wallAngle, int isComplete, Context mContext) {
        // Gets the database in write mode
        //Create handler to connect to SQLite DB
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_DATE, date); // long
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_WORKOUTTYPECODE, workoutTypeCode); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_WORKOUTCODE, workoutCode); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_ISCLIMB, DatabaseContract.IS_WORKOUT); // int = 0
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_WEIGHT, weight); // long
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_SETCOUNT, setCount); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_REPCOUNTPERSET, repCount); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_REPDURATIONPERSET, repDuration); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_RESTDURATIONPERSET, restDuration); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_GRADETYPECODE, gradeTypeCode); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_MOVECOUNT, moveCount); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_GRADECODE, gradeCode); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_HOLDTYPE, holdType); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_WALLANGLE, wallAngle); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_ISCOMPLETE, isComplete); // int


        long newRowId = database.insert(DatabaseContract.WorkoutLogEntry.TABLE_NAME, null, values);
        database.close();
        return newRowId;

    }

    /**
     * Insert workout into the database
     *
     * @param date            long date
     * @param workoutTypeCode int code for workout tpe
     * @param workoutCode     int code for actual workout
     * @param weight          long weight
     * @param setCount        int
     * @param repCount        int
     * @param repDuration     int
     * @param restDuration    int
     * @param gradeTypeCode   int
     * @param gradeCode       int
     * @param mContext        Context
     * @return
     */
    public static long updateWorkoutLogData(long date, int workoutTypeCode, int workoutCode, double weight, int setCount, int repCount, int repDuration, int restDuration, int gradeTypeCode, int gradeCode, int moveCount, int holdType, int wallAngle, int rowID, int isComplete, Context mContext) {
        // Gets the database in write mode
        //Create handler to connect to SQLite DB
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_DATE, date); // long
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_WORKOUTTYPECODE, workoutTypeCode); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_WORKOUTCODE, workoutCode); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_ISCLIMB, DatabaseContract.IS_WORKOUT); // int = 0
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_WEIGHT, weight); // long
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_SETCOUNT, setCount); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_REPCOUNTPERSET, repCount); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_REPDURATIONPERSET, repDuration); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_RESTDURATIONPERSET, restDuration); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_GRADETYPECODE, gradeTypeCode); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_MOVECOUNT, moveCount); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_GRADECODE, gradeCode); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_HOLDTYPE, holdType); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_WALLANGLE, wallAngle); // int
        values.put(DatabaseContract.WorkoutLogEntry.COLUMN_ISCOMPLETE, isComplete); // int

        String whereClauseFive = DatabaseContract.WorkoutLogEntry._ID + "=?";
        String[] whereValueFive = {String.valueOf(rowID)};

        long newRowId = database.update(DatabaseContract.WorkoutLogEntry.TABLE_NAME, values, whereClauseFive, whereValueFive);
        database.close();
        return newRowId;

    }

    /**
     * Write a log to the calendar tracker app
     *
     * @param isTrue     int = 1 for a climb, int = 0 for not a climb
     * @param outputDate long date
     * @param rowID      int rowID that we want to reference in the climb/training log
     * @param mContext   context
     * @return
     */
    public static long writeCalendarUpdate(int isTrue, long outputDate, long rowID, Context mContext) {
        // Gets the database in write mode
        // Create handler to connect to SQLite DB
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CalendarTrackerEntry.COLUMN_DATE, outputDate);
        values.put(DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB, isTrue);
        values.put(DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID, rowID);

        long newRowId = database.insert(DatabaseContract.CalendarTrackerEntry.TABLE_NAME, null, values);
        database.close();
        return newRowId;
    }

    /**
     * Get the child table row ID for a specific calendar entry
     *
     * @param id
     * @param mContext
     * @return
     */
    public static int getCalendarTrackerChildRowID(long id, Context mContext) {
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID};
        String whereClause = DatabaseContract.CalendarTrackerEntry._ID + "=?";
        String[] whereValue = {String.valueOf(id)};

        Cursor cursor = database.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.CalendarTrackerEntry.COLUMN_ROWID);

        try {
            cursor.moveToFirst();
            return cursor.getInt(idColumnOutput);
        } finally {
            cursor.close();
            database.close();
        }
    }

    /**
     * Return the boolean value for whether the calender tracker item is a climb or not
     *
     * @param id
     * @param mContext
     * @return
     */
    public static int getCalendarTrackerIsClimb(long id, Context mContext) {
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.CalendarTrackerEntry._ID + "=?";
        String[] whereValue = {String.valueOf(id)};

        Cursor cursor = database.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB);

        try {
            cursor.moveToFirst();
            return cursor.getInt(idColumnOutput);
        } finally {
            cursor.close();
            database.close();
        }
    }

    /**
     * Get a cursor for the workout list for a specific workout type
     *
     * @param id int workout type ID
     * @param db database being queried
     * @return Cursor cursor
     */
    public static Cursor getWorkoutList(int id, SQLiteDatabase db) {
        //grade type
        String[] projection = {
                DatabaseContract.WorkoutListEntry._ID,
                DatabaseContract.WorkoutListEntry.COLUMN_WORKOUTTYPECODE,
                DatabaseContract.WorkoutListEntry.COLUMN_NAME,
                DatabaseContract.WorkoutListEntry.COLUMN_DESCRIPTION};
        String whereClause = DatabaseContract.WorkoutListEntry.COLUMN_WORKOUTTYPECODE + "=?";
        String[] whereValue = {String.valueOf(id)};

        Cursor cursor = db.query(DatabaseContract.WorkoutListEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        return cursor;
    }

    /**
     * return a cursor for all workout types
     *
     * @param db the database being queried
     * @return Cursor cursor
     */
    public static Cursor getWorkoutTypes(SQLiteDatabase db) {
        //grade type
        String[] projection = {
                DatabaseContract.WorkoutTypeEntry._ID,
                DatabaseContract.WorkoutTypeEntry.COLUMN_WORKOUTTYPENAME,
                DatabaseContract.WorkoutTypeEntry.COLUMN_DESCRIPTION};

        Cursor cursor = db.query(DatabaseContract.WorkoutTypeEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }

    /**
     * Get a cursor for the grade list for a specific grade type
     *
     * @param id int workout type ID
     * @param db database being queried
     * @return Cursor cursor
     */
    public static Cursor getGradeList(int id, SQLiteDatabase db) {
        //grade type
        String[] projection = {
                DatabaseContract.GradeListEntry._ID,
                DatabaseContract.GradeListEntry.COLUMN_GRADETYPECODE,
                DatabaseContract.GradeListEntry.COLUMN_GRADENAME,
                DatabaseContract.GradeListEntry.COLUMN_RELATIVEDIFFICULTY};
        String whereClause = DatabaseContract.GradeListEntry.COLUMN_GRADETYPECODE + "=?";
        String[] whereValue = {String.valueOf(id)};

        Cursor cursor = db.query(DatabaseContract.GradeListEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        return cursor;
    }

    /**
     * return a cursor for all grade types
     *
     * @param db the database being queried
     * @return Cursor cursor
     */
    public static Cursor getGradeTypes(SQLiteDatabase db) {
        //grade type
        String[] projection = {
                DatabaseContract.GradeTypeEntry._ID,
                DatabaseContract.GradeTypeEntry.COLUMN_GRADETYPENAME};

        Cursor cursor = db.query(DatabaseContract.GradeTypeEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }

    /**
     * return a cursor for all ascent types
     *
     * @param db the database being queried
     * @return Cursor cursor
     */
    public static Cursor getAscentList(SQLiteDatabase db) {
        //grade type
        String[] projection = {
                DatabaseContract.AscentEntry._ID,
                DatabaseContract.AscentEntry.COLUMN_ASCENTTYPENAME,
                DatabaseContract.AscentEntry.COLUMN_DESCRIPTION};

        Cursor cursor = db.query(DatabaseContract.AscentEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }

    /**
     * return a cursor for all hold types
     *
     * @param db the database being queried
     * @return Cursor cursor
     */
    public static Cursor getHoldTypeList(SQLiteDatabase db) {
        //grade type
        String[] projection = {
                DatabaseContract.HoldTypeEntry._ID,
                DatabaseContract.HoldTypeEntry.COLUMN_HOLDTYPE,
                DatabaseContract.HoldTypeEntry.COLUMN_DESCRIPTION};

        Cursor cursor = db.query(DatabaseContract.HoldTypeEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }

    /**
     * Method for outputting the count of climbs in the database
     *
     * @param day      a calendar pointing at the particular day
     * @param mContext context
     * @return int, the count for that date
     */
    public static int getClimbCount(Calendar day, Context mContext) {
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        int dayLength = 86400000 - 2;

        day.set(Calendar.HOUR, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 1);

        long dayStart = day.getTimeInMillis();
        long dayEnd = dayStart + dayLength;

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_DATE,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB + "=? AND " + DatabaseContract.CalendarTrackerEntry.COLUMN_DATE + " BETWEEN ? AND ?";
        String[] whereValue = {String.valueOf(DatabaseContract.IS_CLIMB), String.valueOf(dayStart), String.valueOf(dayEnd)};

        Cursor cursor = database.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int output = cursor.getCount();

        try {
            return output;
        } finally {
            cursor.close();
            database.close();
            handler.close();
        }
    }

    /**
     * Method for outputting the count of workouts in the database
     *
     * @param day      a calendar pointing at the particular day
     * @param mContext context
     * @return int, the count for that date
     */
    public static int getWorkoutCount(Calendar day, Context mContext) {
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();
        int dayLength = 86400000 - 2;

        day.set(Calendar.HOUR, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 1);

        long dayStart = day.getTimeInMillis();
        long dayEnd = dayStart + dayLength;

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_DATE,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB + "=? AND " + DatabaseContract.CalendarTrackerEntry.COLUMN_DATE + " BETWEEN ? AND ?";
        String[] whereValue = {String.valueOf(DatabaseContract.IS_WORKOUT), String.valueOf(dayStart), String.valueOf(dayEnd)};

        Cursor cursor = database.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int output = cursor.getCount();

        try {
            return output;
        } finally {
            cursor.close();
            database.close();
            handler.close();
        }
    }

    /**
     * Method for outputting the count of workout climbs in the database
     *
     * @param day      a calendar pointing at the particular day
     * @param mContext context
     * @return int, the count for that date
     */
    public static int getWorkoutClimbCount(Calendar day, Context mContext) {
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        int dayLength = 86400000 - 2;

        day.set(Calendar.HOUR, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 1);

        long dayStart = day.getTimeInMillis();
        long dayEnd = dayStart + dayLength;

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_DATE,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB + "=? AND " + DatabaseContract.CalendarTrackerEntry.COLUMN_DATE + " BETWEEN ? AND ?";
        String[] whereValue = {String.valueOf(DatabaseContract.IS_GYMCLIMB), String.valueOf(dayStart), String.valueOf(dayEnd)};
        Cursor cursor = database.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int output = cursor.getCount();

        try {
            return output;
        } finally {
            cursor.close();
            database.close();
            handler.close();
        }
    }

    /**
     * Method for outputting a list of days with climb data
     *
     * @param day
     * @param mContext
     * @return
     */
    public static ArrayList<Integer> getClimbMonthCount(Calendar day, Context mContext) {
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        long dayLength = 86400000;

        day.set(Calendar.HOUR, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);

        long monthStart = day.getTimeInMillis();
        long monthEnd = monthStart + dayLength * 42;
        //Log.i(LOG_TAG, "Month Start: " + monthStart + " | Month End: " + monthEnd);

        ArrayList<Integer> eventsList = new ArrayList<>(42);
        for (int i = 0; i < 42; i++) {
            eventsList.add(0);
        }

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_DATE,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB + "=? AND " + DatabaseContract.CalendarTrackerEntry.COLUMN_DATE + " BETWEEN ? AND ?";
        String[] whereValue = {String.valueOf(DatabaseContract.IS_CLIMB), String.valueOf(monthStart), String.valueOf(monthEnd)};

        Cursor cursor = database.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.CalendarTrackerEntry.COLUMN_DATE);

        int cursorCount = cursor.getCount();
        //Log.i(LOG_TAG, "idColumnOutput: " + idColumnOutput + " | cursorCount: " + cursorCount);

        if (cursorCount != 0) {
            //cursor.moveToFirst();
            while (cursor.moveToNext()) {
                long outputDate = cursor.getLong(idColumnOutput);
                long outputDay = (outputDate - monthStart) / dayLength;
                //Log.i(LOG_TAG, "outputDate: " + outputDate + " | outputDay: " + outputDay);
                eventsList.set((int) outputDay, 1);
            }
        }

        try {
            return eventsList;
        } finally {
            cursor.close();
            database.close();
            handler.close();
        }
    }

    /**
     * Method for outputting a list of days with workout data
     *
     * @param day
     * @param mContext
     * @return
     */
    public static ArrayList<Integer> getWorkoutMonthCount(Calendar day, Context mContext) {
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        long dayLength = 86400000;

        day.set(Calendar.HOUR, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);

        long monthStart = day.getTimeInMillis();
        long monthEnd = monthStart + dayLength * 42;
        Log.i(LOG_TAG, "Month Start: " + monthStart + " | Month End: " + monthEnd);

        ArrayList<Integer> eventsList = new ArrayList<>(42);
        for (int i = 0; i < 42; i++) {
            eventsList.add(0);
        }

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_DATE,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB + "=? AND " + DatabaseContract.CalendarTrackerEntry.COLUMN_DATE + " BETWEEN ? AND ?";
        String[] whereValue = {String.valueOf(DatabaseContract.IS_WORKOUT), String.valueOf(monthStart), String.valueOf(monthEnd)};

        Cursor cursor = database.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.CalendarTrackerEntry.COLUMN_DATE);

        int cursorCount = cursor.getCount();
        Log.i(LOG_TAG, "idColumnOutput: " + idColumnOutput + " | cursorCount: " + cursorCount);

        if (cursorCount != 0) {
            //cursor.moveToFirst();
            while (cursor.moveToNext()) {
                long outputDate = cursor.getLong(idColumnOutput);
                long outputDay = (outputDate - monthStart) / dayLength;
                Log.i(LOG_TAG, "outputDate: " + outputDate + " | outputDay: " + outputDay);
                eventsList.set((int) outputDay, 1);
            }
        }

        try {
            return eventsList;
        } finally {
            cursor.close();
            database.close();
            handler.close();
        }
    }

    /**
     * Method for outputting a list of days with workout climb data
     *
     * @param day
     * @param mContext
     * @return
     */
    public static ArrayList<Integer> getWorkoutClimbMonthCount(Calendar day, Context mContext) {
        DatabaseHelper handler = new DatabaseHelper(mContext);
        SQLiteDatabase database = handler.getWritableDatabase();

        long dayLength = 86400000;

        day.set(Calendar.HOUR, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);

        long monthStart = day.getTimeInMillis();
        long monthEnd = monthStart + dayLength * 42;
        Log.i(LOG_TAG, "Month Start: " + monthStart + " | Month End: " + monthEnd);

        ArrayList<Integer> eventsList = new ArrayList<>(42);
        for (int i = 0; i < 42; i++) {
            eventsList.add(0);
        }

        //grade type
        String[] projection = {
                DatabaseContract.CalendarTrackerEntry._ID,
                DatabaseContract.CalendarTrackerEntry.COLUMN_DATE,
                DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB};
        String whereClause = DatabaseContract.CalendarTrackerEntry.COLUMN_ISCLIMB + "=? AND " + DatabaseContract.CalendarTrackerEntry.COLUMN_DATE + " BETWEEN ? AND ?";
        String[] whereValue = {String.valueOf(DatabaseContract.IS_GYMCLIMB), String.valueOf(monthStart), String.valueOf(monthEnd)};
        Cursor cursor = database.query(DatabaseContract.CalendarTrackerEntry.TABLE_NAME,
                projection,
                whereClause,
                whereValue,
                null,
                null,
                null);

        int idColumnOutput = cursor.getColumnIndex(DatabaseContract.CalendarTrackerEntry.COLUMN_DATE);

        int cursorCount = cursor.getCount();
        Log.i(LOG_TAG, "idColumnOutput: " + idColumnOutput + " | cursorCount: " + cursorCount);

        if (cursorCount != 0) {
            //cursor.moveToFirst();
            while (cursor.moveToNext()) {
                long outputDate = cursor.getLong(idColumnOutput);
                long outputDay = (outputDate - monthStart) / dayLength;
                Log.i(LOG_TAG, "outputDate: " + outputDate + " | outputDay: " + outputDay);
                eventsList.set((int) outputDay, 1);
            }
        }

        try {
            return eventsList;
        } finally {
            cursor.close();
            database.close();
            handler.close();
        }
    }

}


