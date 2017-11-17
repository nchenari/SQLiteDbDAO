package com.nchenari.examplesqlitedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nchenari.examplesqlitedb.model.ReactionTimeSession;
import com.nchenari.examplesqlitedb.model.ReactionTimeSessionDAO;
import com.nchenari.examplesqlitedb.model.Subject;
import com.nchenari.examplesqlitedb.model.SubjectDAO;

import java.util.ArrayList;

/**
 * Created by nimachenari on 4/4/15.
 */
public class SubjectInfoActivity extends AppCompatActivity{

    private Subject subject;
    private ArrayList<Subject> subjectsArrayList;
    private ArrayList<ReactionTimeSession> sessionsArrayList;
    private ArrayAdapter<ReactionTimeSession> sessionsAdapter;
    private TextView lastSessionTextView;

    // DAO
    SubjectDAO subjectDao;
    ReactionTimeSessionDAO reactionTimeSessionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.hold);
        setContentView(R.layout.activity_subject_info);
        // Support Up button.
        // gettoolbar().setDisplayHomeAsUpEnabled(true);

        // get Intent to get extra data (subject)
        subject = (Subject) getIntent().getSerializableExtra("SubjectToView");
        String externalSubjectID = subject.getExternalSubjectID();
        // sessionsArrayList = subject.getSessionsArrayList();

        TextView subjectID = (TextView) findViewById(R.id.externalSubjectID);
        subjectID.setText("Test Subject ID: " + externalSubjectID);

        lastSessionTextView = (TextView) findViewById(R.id.lastSession);

        TextView firstNameTextView = (TextView) findViewById(R.id.textViewFirstName);
        firstNameTextView.setText(subject.getFirstName());
        TextView lastNameTextView = (TextView) findViewById(R.id.textViewLastName);
        lastNameTextView.setText(subject.getLastName());

        // get sessions from database
        reactionTimeSessionDao = new ReactionTimeSessionDAO(this);
        sessionsArrayList = (ArrayList<ReactionTimeSession>) reactionTimeSessionDao.getSessionsOfSubject(subject.getPrimary_id());

        // if subject has any sessions available
        if (sessionsArrayList.size() > 0) {
            lastSessionTextView.setText("Latest Session: "
                    + sessionsArrayList.get(sessionsArrayList.size() - 1).getLongDateString());

            // create listView and adapter
            ListView sessionsListView = (ListView) findViewById(R.id.listViewSessions);
            sessionsAdapter = new SessionsListAdapter(this, R.layout.save_data_list_item, sessionsArrayList);

            // set listView adapter
            sessionsListView.setAdapter(sessionsAdapter);
        } else {
            lastSessionTextView.setText("No Sessions Saved");

            // set description above listView to blank
            TextView descriptionTextView = (TextView) findViewById(R.id.description);
            descriptionTextView.setText("");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reactionTimeSessionDao.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        sessionsArrayList = (ArrayList<ReactionTimeSession>) reactionTimeSessionDao.getSessionsOfSubject(subject.getPrimary_id());

        // update array adapter to reflect changes to sessionsArrayList (arrayList that populates the ListView)
        sessionsAdapter.clear();
        sessionsAdapter.addAll(sessionsArrayList);
        sessionsAdapter.notifyDataSetChanged();

        if (sessionsArrayList.size() > 0) {
            lastSessionTextView.setText("Latest Session: "
                    + sessionsArrayList.get(sessionsArrayList.size() - 1).getLongDateString());
        } else {
            lastSessionTextView.setText("No Sessions Saved");

            // set description above listView to blank
            TextView descriptionTextView = (TextView) findViewById(R.id.description);
            descriptionTextView.setText("");
        }


        /** if (subjectsArrayList.size() == 0) {

            // clear subjectID in sharedPref
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putString(SubjectManagementActivity.KEY_CURRENT_SUBJECT_ID, "");
            sharedPrefEditor.commit();
        } */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject_or_session_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_discard:
                deleteSubject();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_right);
        super.onPause();
    }

    private void deleteSubject() {
        subjectDao = new SubjectDAO(this);
        subjectDao.deleteSubject(subject);

        // return to previous screen
        onBackPressed();
    }
}