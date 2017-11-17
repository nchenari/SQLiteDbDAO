package com.nchenari.examplesqlitedb;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nchenari.examplesqlitedb.model.DBHelper;
import com.nchenari.examplesqlitedb.model.ReactionTimeSessionDAO;
import com.nchenari.examplesqlitedb.model.Subject;
import com.nchenari.examplesqlitedb.model.SubjectDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // DAO objects
    private SubjectDAO subjectDao;
    private ReactionTimeSessionDAO reactionTimeSessionDao;

    private String selectedSubjectID;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.frame, new UserManagementFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // Get instance of SharedPreferences.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        // Retrieve current subject being tested
        selectedSubjectID = sharedPref.getString(UserManagementFragment.KEY_CURRENT_SUBJECT_ID, "");

    }

    /** Activity lifecycle callback methods. (except onCreate()) */
    @Override
    public void onResume() {
        super.onResume();
        // Update preferences when user navigates back to reactionTestFragment from settingsFragment or subject management fragment.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        selectedSubjectID = sharedPref.getString(UserManagementFragment.KEY_CURRENT_SUBJECT_ID, "");

    }

    @Override
    public void onPause() {
        super.onPause();

        // Handle Database
        if (subjectDao != null) {
            subjectDao.close();
            Log.i(TAG, "DAO closed");
        }
        if (reactionTimeSessionDao != null) {
            reactionTimeSessionDao.close();
            Log.i(TAG, "DAO closed");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Handle Database
        if (subjectDao != null) {
            subjectDao.close();
            Log.i(TAG, "DAO closed");
        }
        if (reactionTimeSessionDao != null) {
            reactionTimeSessionDao.close();
            Log.i(TAG, "DAO closed");
        }

    }

    /**
    private void saveSession() {
        // save all stimuli ArrayLists at end of session as concatenated strings
        String audString = DBHelper.convertIntArrayListToString(audioArray);
        String visString = DBHelper.convertIntArrayListToString(visualArray);
        String tacString = DBHelper.convertIntArrayListToString(tactileArray);
        String visAudString = DBHelper.convertIntArrayListToString(visualAudioArray);
        String audTacString = DBHelper.convertIntArrayListToString(audioTactileArray);
        String visTacString = DBHelper.convertIntArrayListToString(visualTactileArray);
        String audVisTacString = DBHelper.convertIntArrayListToString(visualAudioTactileArray);

        Log.i(TAG, "Audio RTs: " + audString);
        Log.i(TAG, "Visual RTs: " + visString);
        Log.i(TAG, "Tactile RTs: " + tacString);
        Log.i(TAG, "Visual + Audio RTs: " + visAudString);
        Log.i(TAG, "Audio + Tactile RTs: " + audTacString);
        Log.i(TAG, "Visual + Tactile RTs: " + visTacString);
        Log.i(TAG, "Audio + Visual + Tactile RTs: " + audVisTacString);

        // get db subject id based on external subject id.
        subjectDao = new SubjectDAO(context);

        // retrieve all subjects from database to update
        ArrayList<Subject> subjectsArrayList = new ArrayList<>();
        if (subjectDao.getAllSubjects() != null) {
            subjectsArrayList = (ArrayList<Subject>) subjectDao.getAllSubjects();
        }

        // get internal subject id from external subject id
        long subjectId = 0;
        for (Subject subject : subjectsArrayList) {
            if (subject.getExternalSubjectID().equals(selectedSubjectID)) {
                subjectId = subject.getPrimary_id();
            }
        }

        // save data to new Session in db
        reactionTimeSessionDao = new ReactionTimeSessionDAO(context);
        reactionTimeSessionDao.createSession(dateTime, audString, visString, tacString, visAudString,
                audTacString, visTacString, audVisTacString, subjectId);

        Log.i(TAG, "Session saved to database.");

    } **/


}
