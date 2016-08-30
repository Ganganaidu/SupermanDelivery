package com.supermandelivery.utils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.supermandelivery.MyApplication;

/**
 * Saving data across the application
 */
public class AppPreferences {

    private static final String APP_SHARED_PREFS = "com.snappit.utils";
    private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;

    private static AppPreferences instance = null;

    public static AppPreferences getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return new AppPreferences(MyApplication.getInstance());
        }
    }

    /**
     * Saving data in shared preferences which will store life time of Application
     */
    public AppPreferences(Context context) {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    /*
     *  Delete the all the preferences
     */
    public void deletePref() {
        this.prefsEditor.clear();
        this.prefsEditor.commit();
    }

    /**
     * Save opt state
     */
    public void saveOtpState(boolean value) {
        prefsEditor.putBoolean("otpState", value);
        prefsEditor.commit();
    }

    public boolean getOtpState() {
        return appSharedPrefs.getBoolean("otpState", false);
    }

    /**
     * Save otp confirm state
     */
    public void saveOtpConfirmState(boolean value) {
        prefsEditor.putBoolean("otpConfirmState", value);
        prefsEditor.commit();
    }

    public boolean getOtpConfirmState() {
        return appSharedPrefs.getBoolean("otpConfirmState", false);
    }

    /**
     * Save profile state
     */
    public void saveProfileState(boolean value) {
        prefsEditor.putBoolean("saveProfileState", value);
        prefsEditor.commit();
    }

    public boolean getProfileState() {
        return appSharedPrefs.getBoolean("saveProfileState", false);
    }

    /**
     * Save user phone number
     */
    public void savePhoneNUmber(String name) {
        prefsEditor.putString("userphonenumber", name);
        prefsEditor.commit();
    }

    public String getPhoneNumber() {
        return appSharedPrefs.getString("userphonenumber", "");
    }

    /**
     * Save user name
     */
    public void saveUserName(String name) {
        prefsEditor.putString("UserName", name);
        prefsEditor.commit();
    }

    public String getUserName() {
        return appSharedPrefs.getString("UserName", "");
    }

    /**
     * Save user email id
     */
    public void saveUserEmailId(String name) {
        prefsEditor.putString("userEmail", name);
        prefsEditor.commit();
    }

    public String getUserEmailId() {
        return appSharedPrefs.getString("userEmail", "");
    }

    /**
     * Save user email id
     */
    public void saveLatitude(String latitude) {
        prefsEditor.putString("latitude", latitude);
        prefsEditor.commit();
    }

    public String getLatitude() {
        return appSharedPrefs.getString("latitude", "0.0");
    }

    /**
     * Save user email id
     */
    public void saveLongitude(String Longitude) {
        prefsEditor.putString("Longitude", Longitude);
        prefsEditor.commit();
    }

    public String getLongitude() {
        return appSharedPrefs.getString("Longitude", "0.0");
    }


    /**
     * Save access token
     */
    public void saveAccessToken(String accessToken) {
        prefsEditor.putString("accessToken", accessToken);
        prefsEditor.commit();
    }

    public String getAccessToken() {
        return appSharedPrefs.getString("accessToken", "");
    }


    /**
     * Save push token
     */
    public void savePushToken(String accessToken) {
        prefsEditor.putString("pushaccessToken", accessToken);
        prefsEditor.commit();
    }

    public String getPushToken() {
        return appSharedPrefs.getString("pushaccessToken", "");
    }


    /**
     * Save user ID
     */
    public void saveUserId(String accessToken) {
        prefsEditor.putString("userId", accessToken);
        prefsEditor.commit();
    }

    public String getUserId() {
        return appSharedPrefs.getString("userId", "");
    }


}