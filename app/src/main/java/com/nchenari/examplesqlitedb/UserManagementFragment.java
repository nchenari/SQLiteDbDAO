package com.nchenari.examplesqlitedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nchenari.examplesqlitedb.model.Subject;
import com.nchenari.examplesqlitedb.model.SubjectDAO;

import java.util.ArrayList;

/**
 * Created by nimachenari on 3/4/15.
 */
public class UserManagementFragment extends Fragment {
    private Context context;
    private View v;

    private EditText editTextExternalSubjectID;
    private EditText editTextFirstName;
    private EditText editTextLastName;

    private ArrayList<Subject> subjectsArrayList;
    private ArrayAdapter subjectsAdapter;

    private TextView totalSubjects;

    private SubjectDAO subjectDao;



    // current test subject constant
    protected static final String KEY_CURRENT_SUBJECT_ID = "currentSubject";

    // TAG constant
    private static final String TAG = "UserManagementFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user_management, container, false);
        context = v.getContext();


        // initialize editText fields (subjectID, fistName, lastName)
        editTextExternalSubjectID = v.findViewById(R.id.editTextSubjectID);
        editTextFirstName = v.findViewById(R.id.editTextFirstName);
        editTextLastName = v.findViewById(R.id.editTextLastName);

        // add create subject button
        Button createSubjectButton = v.findViewById(R.id.createSubjectBtn);
        createSubjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createSubject();
            }
        });

        // retrieve all subjects from database
        subjectDao = new SubjectDAO(context);
        subjectsArrayList = (ArrayList<Subject>) subjectDao.getAllSubjects();

        // set up listView
        ListView subjectsListView = v.findViewById(R.id.listViewSubjects);
        subjectsAdapter = new SubjectsListAdapter(context, R.layout.save_data_list_item, subjectsArrayList);
        // set listView adapter
        subjectsListView.setAdapter(subjectsAdapter);
        // set listView OnItemClickListener (action switches current test subject)
        subjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {

                Subject selectedSubject = (Subject) adapter.getItemAtPosition(position);
                switchCurrentTestSubject(selectedSubject.getExternalSubjectID());
            }
        });

        // initialize TextView for total number of subjects
        totalSubjects = v.findViewById(R.id.subjectsTotal);
        // get default shared preferences to display currently selected subject's ID.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        // Retrieve and display currently selected subject.
        String selectedSubjectID = sharedPref.getString(KEY_CURRENT_SUBJECT_ID, "");
        totalSubjects.setText("Total Users: " + subjectsArrayList.size());


        return v;
    }

    private void createSubject() {
        if (editTextExternalSubjectID.getText().length() == 0) {
            Toast.makeText(context, "Please enter a valid User ID and retry.",
                    Toast.LENGTH_SHORT).show();
        } else if (subjectAlreadyExists()) {
            Toast.makeText(context,
                    "User with entered ID already exists. Please use unique ID.",
                    Toast.LENGTH_LONG).show();
        } else {
            // hide on screen keyboard.
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            // retrieve subjectID, firstName, and lastName from text fields.
            String externalSubjectID = editTextExternalSubjectID.getText().toString();
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();

            // clear edit texts
            editTextExternalSubjectID.setText("");
            editTextFirstName.setText("");
            editTextLastName.setText("");

            // add new subject to database
            Subject newSubject = subjectDao.createSubject(externalSubjectID, firstName, lastName);

            // update array adapter to reflect changes to subjectsArrayList (arrayList that populates the ListView)
            subjectsAdapter.clear();
            // retrieve all subjects from database to update
            subjectsArrayList = (ArrayList<Subject>) subjectDao.getAllSubjects();

            subjectsAdapter.addAll(subjectsArrayList);
            subjectsAdapter.notifyDataSetChanged();

            // update total number of subjects
            totalSubjects.setText("Total Number of Users: " + subjectsArrayList.size());

            // if only one subject in subjectsArrayList, set newly created subject as current test subject
            if (subjectsArrayList.size() == 1) {
                switchCurrentTestSubject(newSubject.getExternalSubjectID());
            }

            // notify user
            Toast.makeText(context,
                    "New User Added",
                    Toast.LENGTH_LONG).show();

            Log.i(TAG, "New User Added" );
        }
    }

    private void switchCurrentTestSubject(String selectedID) {  // on regular click select list view item subject object as current test subject.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPref.getString(KEY_CURRENT_SUBJECT_ID, "").equals(selectedID)) {
            // current test subject has not changed.
            Toast.makeText(context, "Already Current User", Toast.LENGTH_SHORT).show();
        } else {
            // Edit the saved preferences
            // The SharedPreferences editor - must use commit() to submit changes
            SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putString(KEY_CURRENT_SUBJECT_ID, selectedID);
            sharedPrefEditor.commit();

            // notify user of change.
            Toast userChangedToast = Toast.makeText(context, "Current User Changed", Toast.LENGTH_SHORT);
            userChangedToast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 150);
            userChangedToast.show();
        }
    }

    private boolean subjectAlreadyExists() {
        for (Subject subject : subjectsArrayList) {
            if (subject.getExternalSubjectID().equals(editTextExternalSubjectID.getText().toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        // retrieve all subjects from database
        subjectsArrayList = (ArrayList<Subject>) subjectDao.getAllSubjects();

        // update array adapter to reflect changes to subjectsArrayList (arrayList that populates the ListView)
        subjectsAdapter.clear();
        subjectsAdapter.addAll(subjectsArrayList);
        subjectsAdapter.notifyDataSetChanged();

        totalSubjects.setText("Total Number of Users: " + subjectsArrayList.size());

        if (subjectsArrayList.size() == 0) {

            // clear subjectID in sharedPref
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putString(KEY_CURRENT_SUBJECT_ID, "");
            sharedPrefEditor.commit();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // close SQLite database on destroy
        subjectDao.close();
        Log.i(TAG, "DAO closed");
    }
}



























