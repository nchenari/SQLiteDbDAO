package com.nchenari.examplesqlitedb.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by nimachenari on 4/19/15.
 */



public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "subjectsManager.db";


    // Subjects Table Name
    protected static final String TABLE_SUBJECTS = "subjects";
    // Subjects Table Column Names
    protected static final String COLUMN_SUBJECT_PRIMARY_KEY_ID = "_id";
    protected static final String COLUMN_SUBJECT_EXTERNAL_SUBJECT_ID = "external_subject_id";
    protected static final String COLUMN_SUBJECT_FIRST_NAME = "first_name";
    protected static final String COLUMN_SUBJECT_LAST_NAME = "last_name";


    // ReactTimeSessions Table Name
    protected static final String TABLE_R_T_SESSIONS = "r_t_sessions";
    // ReactTimeSessions Table Column Names
    protected static final String COLUMN_R_T_SESSION_PRIMARY_KEY_ID = "_id";
    protected static final String COLUMN_R_T_SESSION_DATE_TIME = "date_time";
    // stimuli arrays
    protected static final String COLUMN_R_T_SESSION_AUDIO = "audio_rt_array";
    protected static final String COLUMN_R_T_SESSION_VISUAL = "visual_rt_array";
    protected static final String COLUMN_R_T_SESSION_TACTILE = "tactile_rt_array";
    protected static final String COLUMN_R_T_SESSION_VISUAL_AUDIO = "visual_audio_rt_array";
    protected static final String COLUMN_R_T_SESSION_AUDIO_TACTILE = "audio_tactile_rt_array";
    protected static final String COLUMN_R_T_SESSION_VISUAL_TACTILE = "visual_tactile_rt_array";
    protected static final String COLUMN_R_T_SESSION_AUDIO_VISUAL_TACTILE = "audio_visual_tactile_rt_array";
    protected static final String COLUMN_R_T_SESSION_SUBJECT_PRIMARY_KEY_ID = "subject_id";


    // FlashIllusionSessions Table Name
    protected static final String TABLE_F_I_SESSIONS = "f_i_sessions";
    // FlashIllusionSessions Table Column Names
    protected static final String COLUMN_F_I_SESSION_PRIMARY_KEY_ID = "_id";
    protected static final String COLUMN_F_I_SESSION_DATE_TIME = "date_time";
    // stimuli arrays
    protected static final String COLUMN_F_I_SESSION_ONE_FLASH = "one_flash_array";
    protected static final String COLUMN_F_I_SESSION_FISSION = "fission_array";
    protected static final String COLUMN_F_I_SESSION_TWO_FLASH = "two_flash_array";
    protected static final String COLUMN_F_I_SESSION_FUSION = "fusion_array";
    protected static final String COLUMN_F_I_SESSION_SUBJECT_PRIMARY_KEY_ID = "subject_id";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // SQL statement for the Subjects table creation
    private static final String SQL_CREATE_TABLE_SUBJECTS = "CREATE TABLE " + TABLE_SUBJECTS + "("
            + COLUMN_SUBJECT_PRIMARY_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SUBJECT_EXTERNAL_SUBJECT_ID + " TEXT NOT NULL, "
            + COLUMN_SUBJECT_FIRST_NAME + " TEXT, "
            + COLUMN_SUBJECT_LAST_NAME + " TEXT "
            + ");";

    // SQL statement for the ReactTimeSessions table creation
    private static final String SQL_CREATE_TABLE_R_T_SESSIONS = "CREATE TABLE " + TABLE_R_T_SESSIONS + "("
            + COLUMN_R_T_SESSION_PRIMARY_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_R_T_SESSION_DATE_TIME + " TEXT NOT NULL, "
            + COLUMN_R_T_SESSION_AUDIO + " TEXT NOT NULL, "
            + COLUMN_R_T_SESSION_VISUAL + " TEXT NOT NULL, "
            + COLUMN_R_T_SESSION_TACTILE + " TEXT NOT NULL, "
            + COLUMN_R_T_SESSION_VISUAL_AUDIO + " TEXT NOT NULL, "
            + COLUMN_R_T_SESSION_AUDIO_TACTILE + " TEXT NOT NULL, "
            + COLUMN_R_T_SESSION_VISUAL_TACTILE + " TEXT NOT NULL, "
            + COLUMN_R_T_SESSION_AUDIO_VISUAL_TACTILE + " TEXT NOT NULL, "
            + COLUMN_R_T_SESSION_SUBJECT_PRIMARY_KEY_ID + " INTEGER NOT NULL "
            + ");";

    // SQL statement for the FlashIllusionSession table creation
    private static final String SQL_CREATE_TABLE_F_I_SESSIONS = "CREATE TABLE " + TABLE_F_I_SESSIONS + "("
            + COLUMN_F_I_SESSION_PRIMARY_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_F_I_SESSION_DATE_TIME + " TEXT NOT NULL, "
            + COLUMN_F_I_SESSION_ONE_FLASH + " TEXT NOT NULL, "
            + COLUMN_F_I_SESSION_FISSION + " TEXT NOT NULL, "
            + COLUMN_F_I_SESSION_TWO_FLASH + " TEXT NOT NULL, "
            + COLUMN_F_I_SESSION_FUSION + " TEXT NOT NULL, "
            + COLUMN_F_I_SESSION_SUBJECT_PRIMARY_KEY_ID + " INTEGER NOT NULL "
            + ");";

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SUBJECTS);
        db.execSQL(SQL_CREATE_TABLE_R_T_SESSIONS);
        db.execSQL(SQL_CREATE_TABLE_F_I_SESSIONS);
        Log.e(TAG, "Database and tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading the database from version " + oldVersion + " to " + newVersion);

        // Drop older tables if exists -- clear all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_R_T_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_F_I_SESSIONS);

        // recreate tables
        onCreate(db);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }





    // Utility method to convert ArrayList<Integer> to String to be stored in sessions table
    public static final String stringSeparator = ", ";

    public static String convertIntArrayListToString(ArrayList<Integer> arrayList) {
        String string = "";
        for (int i = 0; i < arrayList.size(); i++) {
            string = string + String.valueOf(arrayList.get(i));
            // Do not append comma at the end of last element
            if(i < arrayList.size() - 1) {
                string = string + stringSeparator;
            }
        }
        return string;
    }
    public static ArrayList<Integer> convertStringToIntArrayList(String string) {
        String[] stringArray = string.split(stringSeparator);

        ArrayList<Integer> arrayList = new ArrayList<>();

        for (String str : stringArray) {
            arrayList.add(Integer.valueOf(str));
        }

        return arrayList;
    }
}










