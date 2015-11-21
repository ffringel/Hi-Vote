package com.iceteck.hivote.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.iceteck.hivote.CatergoriesActivity;
import com.iceteck.hivote.MainActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences prefs;
    Editor editor;
    Context context;

    //SharedPref mode
    int PRIVATE_MODE = 0;

    //SharedPref filename
    private static final String PREF_NAME = "Hi-Vote Preferences";
    private static final String IS_LOGIN = "ISLoggedIn";

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PICTURE = "profilePicture";

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void createLoginSession(String name, String email, String profilePicture) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PICTURE, profilePicture);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        user.put(KEY_PICTURE, prefs.getString(KEY_PICTURE, null));

        return user;
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void logoutUser() {
        //Clearing all data from shared Preferences
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }
}
