package com.iceteck.hivote.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

/**
 * Project Hi-Vote
 * Created by Larry Akah on 11/17/15 11:46 AM.
 */
public class AccountSessionManager {

    private static final String TAG = AccountSessionManager.class.getCanonicalName();
    private static final int PROFILE_PIC_SIZE = 100;
    private String username;
    private String userID;
    private String userAuthToken;
    private String userEmail;
    private String profileImage;
    private String userCoverPhoto;

    private SharedPreferences mSharedPreferences;
    private Context mcontext;
    private GoogleApiClient mGoogleApiClient;
    public static final String USERID = "userid";
    public static final String USERAUTHTOKEN = "authToken";
    public static final String USERNAME = "userAuthName";
    public static final String USEREMAIL = "userEmail";
    public static final String USERPROFILEPHOTO = "profileImg";
    public static final String USERCOVERPHOTO =  "coverPhoto";

    /**
     * Account initiation constructor
     */
    public AccountSessionManager(SharedPreferences sp, Context ctx, GoogleApiClient gap) {
        mSharedPreferences = sp;
        mcontext = ctx;
        mGoogleApiClient = gap;
    }

    //authenticate account
    public boolean isConnected() {
        return mSharedPreferences.getString(AccountSessionManager.USERAUTHTOKEN, "").equals("")? false:true;

    }

    //check if user is already actively logged-in
    public boolean isLoggedIn() {
        return false;
    }

    //remove account from SharedPreference or logout
    public void removeAccount() {
        if(mGoogleApiClient != null )
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                        }
                    });
        }
        mSharedPreferences.edit().clear().commit();
    }

    public String getUsername() {
        return username;
    }

    public String getUserAuthToken() {
        return userAuthToken;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getUserCoverPhoto() {
        return userCoverPhoto;
    }
}
