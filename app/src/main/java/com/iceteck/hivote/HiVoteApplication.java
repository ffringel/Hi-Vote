package com.iceteck.hivote;

import android.app.Application;

import com.facebook.FacebookSdk;

public class HiVoteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
