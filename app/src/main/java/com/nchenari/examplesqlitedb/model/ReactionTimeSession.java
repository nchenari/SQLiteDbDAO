package com.nchenari.examplesqlitedb.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nimachenari on 4/24/15.
 */
public class ReactionTimeSession implements Serializable {

    public static final String TAG = "Session";

    private long primary_id; // internal primary key for session
    private String longDateString;
    // Arrays as strings to be parsed out into arrays.
    private String audString;
    private String visString;
    private String tacString;
    private String visAudString;
    private String audTacString;
    private String visTacString;
    private String audVisTacString;
    // Subject session belongs to
    private Subject subject;

    // ArrayLists for MSI calculations
    private ArrayList<Integer> audioArray;
    private ArrayList<Integer> visualArray;
    private ArrayList<Integer> tactileArray;
    private ArrayList<Integer> visualAudioArray;
    private ArrayList<Integer> audioTactileArray;
    private ArrayList<Integer> visualTactileArray;
    private ArrayList<Integer> visualAudioTactileArray;

    // Session object constructor
    public ReactionTimeSession() {

    }

    public ReactionTimeSession(String longDateString, String audString, String visString, String tacString,
                               String visAudString, String audTacString, String visTacString,
                               String audVisTacString) {
        this.longDateString = longDateString;
        this.audString = audString;
        this.visString = visString;
        this.tacString = tacString;
        this.visAudString = visAudString;
        this.audTacString = audTacString;
        this.visTacString = visTacString;
        this.audVisTacString = audVisTacString;

    }

    public String toString() {
        return longDateString;
    }

    public String getAllDataAsString() {
        return longDateString + "\n" + " Audio rt: " + audString + "\n"
                + " Visual rt: " + visString + "\n"
                + " Tactile rt: " + tacString + "\n"
                + " Audio + Visual rt: " + visAudString + "\n"
                + " Audio + Tactile rt: " + audTacString + "\n"
                + " Visual + Tactile rt: " + visTacString + "\n"
                + " Audio + Visual + Tactile rt: " + audVisTacString;
    }

    public void setPrimary_id(long primary_id) {
        this.primary_id = primary_id;
    }

    public long getPrimary_id() {
        return primary_id;
    }

    public void setLongDateString(String longDateString) {
        this.longDateString = longDateString;
    }

    public String getLongDateString() {
        return longDateString;
    }

    public void setAudString(String audString) {
        this.audString = audString;
    }

    public String getAudString() {
        return audString;
    }

    public void setVisString(String visString) {
        this.visString = visString;
    }

    public String getVisString() {
        return visString;
    }

    public void setTacString(String tacString) {
        this.tacString = tacString;
    }

    public String getTacString() {
        return tacString;
    }

    public void setVisAudString(String visAudString) {
        this.visAudString = visAudString;
    }

    public String getVisAudString() {
        return visAudString;
    }

    public void setAudTacString(String audTacString) {
        this.audTacString = audTacString;
    }

    public String getAudTacString() {
        return audTacString;
    }

    public void setVisTacString(String visTacString) {
        this.visTacString = visTacString;
    }

    public String getVisTacString() {
        return visTacString;
    }

    public void setAudVisTacString(String audVisTacString) {
        this.audVisTacString = audVisTacString;
    }

    public String getAudVisTacString() {
        return audVisTacString;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    /** MSI calculation methods */
    private void createArraysFromStrings() { // so that we can use MSIProcessorHelper methods
        // arrays of reactTimes
        audioArray = DBHelper.convertStringToIntArrayList(audString);
        visualArray = DBHelper.convertStringToIntArrayList(visString);
        tactileArray = DBHelper.convertStringToIntArrayList(tacString);
        visualAudioArray = DBHelper.convertStringToIntArrayList(visAudString);
        audioTactileArray = DBHelper.convertStringToIntArrayList(audTacString);
        visualTactileArray = DBHelper.convertStringToIntArrayList(visTacString);
        visualAudioTactileArray = DBHelper.convertStringToIntArrayList(audVisTacString);
    }

}
