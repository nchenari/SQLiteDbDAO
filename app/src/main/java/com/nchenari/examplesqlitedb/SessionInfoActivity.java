package com.nchenari.examplesqlitedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nchenari.examplesqlitedb.model.ReactionTimeSession;
import com.nchenari.examplesqlitedb.model.ReactionTimeSessionDAO;

/**
 * Created by nimachenari on 4/5/15.
 */
public class SessionInfoActivity extends AppCompatActivity {

    private ReactionTimeSession reactionTimeSession;

    // DAO
    private ReactionTimeSessionDAO reactionTimeSessionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_right, R.anim.hold);
        setContentView(R.layout.activity_session_info);
        // Support Up button.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get Intent to get extra data (subject)
        reactionTimeSession = (ReactionTimeSession) getIntent().getSerializableExtra("SessionToView");

        TextView sessionDataTextView = findViewById(R.id.sessionDataTextView);
        sessionDataTextView.setText(reactionTimeSession.getAllDataAsString());

        // instantiate database
        reactionTimeSessionDao = new ReactionTimeSessionDAO(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reactionTimeSessionDao.close();
    }

    @Override
    protected void onPause() {
        // TODO: handle going back to previous screen or leaving app completely
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_right);
        super.onPause();
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
                deleteSession();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteSession() {
        reactionTimeSessionDao.deleteSession(reactionTimeSession);
        // return to previous screen
        onBackPressed();
    }
}
