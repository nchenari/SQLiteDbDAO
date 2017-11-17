package com.nchenari.examplesqlitedb.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nimachenari on 4/23/15.
 */
public class ReactionTimeSessionDAO {

    public static final String TAG = "SessionDAO";

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = { DBHelper.COLUMN_R_T_SESSION_PRIMARY_KEY_ID,
            DBHelper.COLUMN_R_T_SESSION_DATE_TIME, DBHelper.COLUMN_R_T_SESSION_AUDIO,
            DBHelper.COLUMN_R_T_SESSION_VISUAL, DBHelper.COLUMN_R_T_SESSION_TACTILE,
            DBHelper.COLUMN_R_T_SESSION_VISUAL_AUDIO, DBHelper.COLUMN_R_T_SESSION_AUDIO_TACTILE,
            DBHelper.COLUMN_R_T_SESSION_VISUAL_TACTILE, DBHelper.COLUMN_R_T_SESSION_AUDIO_VISUAL_TACTILE,
            DBHelper.COLUMN_R_T_SESSION_SUBJECT_PRIMARY_KEY_ID};

    public ReactionTimeSessionDAO(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(context);
        // open the database
        try {
            open();
        } catch(SQLException e) {
            Log.e(TAG, "SQLException on opening database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public ReactionTimeSession createSession(String dateTime, String audString, String visString,
                                    String tacString, String visAudString, String audTacString,
                                        String visTacString, String audVisTacString, long subjectId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_R_T_SESSION_DATE_TIME, dateTime);
        values.put(DBHelper.COLUMN_R_T_SESSION_AUDIO, audString);
        values.put(DBHelper.COLUMN_R_T_SESSION_VISUAL, visString);
        values.put(DBHelper.COLUMN_R_T_SESSION_TACTILE, tacString);
        values.put(DBHelper.COLUMN_R_T_SESSION_VISUAL_AUDIO, visAudString);
        values.put(DBHelper.COLUMN_R_T_SESSION_AUDIO_TACTILE, audTacString);
        values.put(DBHelper.COLUMN_R_T_SESSION_VISUAL_TACTILE, visTacString);
        values.put(DBHelper.COLUMN_R_T_SESSION_AUDIO_VISUAL_TACTILE, audVisTacString);
        values.put(DBHelper.COLUMN_R_T_SESSION_SUBJECT_PRIMARY_KEY_ID, subjectId);

        long insertId = mDatabase.insert(DBHelper.TABLE_R_T_SESSIONS, null, values);

        Cursor cursor = mDatabase.query(DBHelper.TABLE_R_T_SESSIONS,
                mAllColumns, DBHelper.COLUMN_R_T_SESSION_PRIMARY_KEY_ID + " = " + insertId,
                    null, null, null, null);
        cursor.moveToFirst();
        ReactionTimeSession newReactionTimeSession = cursorToSession(cursor);
        cursor.close();

        return newReactionTimeSession;
    }

    public void deleteSession(ReactionTimeSession reactionTimeSession) {
        long id = reactionTimeSession.getPrimary_id();
        System.out.println("the deleted subject has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_R_T_SESSIONS, DBHelper.COLUMN_R_T_SESSION_PRIMARY_KEY_ID
                + " = " + id, null);
    }

    public List<ReactionTimeSession> getAllSessions() {
        List<ReactionTimeSession> listReactionTimeSessions = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_R_T_SESSIONS,
                mAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            ReactionTimeSession reactionTimeSession = cursorToSession(cursor);
            listReactionTimeSessions.add(reactionTimeSession);
            cursor.moveToNext();
        }

        // make sure to close cursor
        cursor.close();
        return listReactionTimeSessions;
    }

    public List<ReactionTimeSession> getSessionsOfSubject(long subjectId) {
        List<ReactionTimeSession> listReactionTimeSessions = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_R_T_SESSIONS, mAllColumns,
                DBHelper.COLUMN_R_T_SESSION_SUBJECT_PRIMARY_KEY_ID + " = ?",
                    new String[] { String.valueOf(subjectId) }, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ReactionTimeSession reactionTimeSession = cursorToSession(cursor);
            listReactionTimeSessions.add(reactionTimeSession);
            cursor.moveToNext();
        }

        // make sure to close cursor
        cursor.close();
        return listReactionTimeSessions;
    }

    private ReactionTimeSession cursorToSession(Cursor cursor) {
        ReactionTimeSession reactionTimeSession = new ReactionTimeSession();
        reactionTimeSession.setPrimary_id(cursor.getLong(0));
        reactionTimeSession.setLongDateString(cursor.getString(1));
        reactionTimeSession.setAudString(cursor.getString(2));
        reactionTimeSession.setVisString(cursor.getString(3));
        reactionTimeSession.setTacString(cursor.getString(4));
        reactionTimeSession.setVisAudString(cursor.getString(5));
        reactionTimeSession.setAudTacString(cursor.getString(6));
        reactionTimeSession.setVisTacString(cursor.getString(7));
        reactionTimeSession.setAudVisTacString(cursor.getString(8));

        // get the subject by id
        long subjectId = cursor.getLong(9);
        SubjectDAO subjectDao = new SubjectDAO(mContext);
        Subject subject = subjectDao.getSubjectById(subjectId);
        if (subject != null) {
            reactionTimeSession.setSubject(subject);
        }
        return reactionTimeSession;
    }
}









