package com.nchenari.examplesqlitedb.model;

import java.io.Serializable;

/**
 * Created by nimachenari on 3/6/15.
 */
// individual subject object containing patient/subject info and arrayList of sessions
public class Subject implements Serializable {

    public static final String TAG = "Subject";

    private long primary_id; // internal id for database
    private String externalSubjectID;
    private String firstName;
    private String lastName;

    // Empty constructor
    public Subject() {

    }

    public Subject(String externalSubjectID, String firstName, String lastName) {
        // create subject based on externalSubjectID, firstName, and LastName input by user in corresponding textFields
        this.externalSubjectID = externalSubjectID;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public void setPrimary_id(long primary_id) {
        this.primary_id = primary_id;
    }

    public long getPrimary_id() {
        return primary_id;
    }

    public void setExternalSubjectID(String externalSubjectID) {
        this.externalSubjectID = externalSubjectID;
    }

    public String getExternalSubjectID() {
        return externalSubjectID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        if (lastName.equals("") && firstName.equals("")) {
            return "[ID: " + externalSubjectID + " ] ";
        } else if (lastName.equals("") || firstName.equals("")) {
            if (lastName.equals("")) {
                return "[ID: " + externalSubjectID + " ] n/a, " + firstName;
            } else {
                return "[ID: " + externalSubjectID + " ] " + lastName + ", n/a";
            }
        } else {
            return "[ID: " + externalSubjectID + " ] " + lastName + ", " + firstName;
        }
    }
}