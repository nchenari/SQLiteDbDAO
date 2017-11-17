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
// The DAO layer contains the classes for all CRUD (Create, Read, Update, and Delete) operations.
// Have two DAO classes: SubjectDAO and SessionDAO

public class SubjectDAO {

    public static final String TAG = "SubjectDAO";

    // Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns = { DBHelper.COLUMN_SUBJECT_PRIMARY_KEY_ID,
        DBHelper.COLUMN_SUBJECT_EXTERNAL_SUBJECT_ID, DBHelper.COLUMN_SUBJECT_FIRST_NAME,
        DBHelper.COLUMN_SUBJECT_LAST_NAME};

    public SubjectDAO(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(context);
        // open the database
        try {
            open();
        } catch (SQLException e) {
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

    public Subject createSubject(String externalSubId, String firstName, String lastName) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SUBJECT_EXTERNAL_SUBJECT_ID, externalSubId);
        values.put(DBHelper.COLUMN_SUBJECT_FIRST_NAME, firstName);
        values.put(DBHelper.COLUMN_SUBJECT_LAST_NAME, lastName);

        long insertId = mDatabase.insert(DBHelper.TABLE_SUBJECTS, null, values);

        Cursor cursor = mDatabase.query(DBHelper.TABLE_SUBJECTS, mAllColumns,
                DBHelper.COLUMN_SUBJECT_PRIMARY_KEY_ID + " = " + insertId, null, null,
                null, null);
        cursor.moveToFirst();
        Subject newSubject = cursorToSubject(cursor);
        cursor.close();
        return newSubject;
    }

    public void deleteSubject(Subject subject) {
        long id = subject.getPrimary_id();
        // delete all sessions for this subject
        ReactionTimeSessionDAO reactionTimeSessionDao = new ReactionTimeSessionDAO(mContext);
        List<ReactionTimeSession> listReactionTimeSessions = reactionTimeSessionDao.getSessionsOfSubject(id);
        if (listReactionTimeSessions != null && !listReactionTimeSessions.isEmpty()) {
            for (ReactionTimeSession s : listReactionTimeSessions) {
                reactionTimeSessionDao.deleteSession(s);
            }
        }
        // then, finally delete subject
        System.out.println("the deleted company has the id: " + id);
        mDatabase.delete(DBHelper.TABLE_SUBJECTS, DBHelper.COLUMN_SUBJECT_PRIMARY_KEY_ID
                + " = " + id, null);
    }

    public List<Subject> getAllSubjects() {
        List<Subject> listSubjects = new ArrayList<>();

        Cursor cursor = mDatabase.query(DBHelper.TABLE_SUBJECTS, mAllColumns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Subject subject = cursorToSubject(cursor);
                listSubjects.add(subject);
                cursor.moveToNext();
            }

            // make sure to close the cursor
            cursor.close();

        }
        return listSubjects;
    }

    public Subject getSubjectById(long id) {
        Cursor cursor = mDatabase.query(DBHelper.TABLE_SUBJECTS, mAllColumns,
                DBHelper.COLUMN_SUBJECT_PRIMARY_KEY_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Subject subject = cursorToSubject(cursor);
        return subject;
    }

    protected Subject cursorToSubject(Cursor cursor) {
        Subject subject = new Subject();
        subject.setPrimary_id(cursor.getLong(0));
        subject.setExternalSubjectID(cursor.getString(1));
        subject.setFirstName(cursor.getString(2));
        subject.setLastName(cursor.getString(3));
        return subject;
    }
}


















