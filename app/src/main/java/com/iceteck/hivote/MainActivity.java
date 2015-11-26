package com.iceteck.hivote;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.iceteck.hivote.utils.SessionManager;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 100;
    private String userName;
    private String userEmail;
    private String profilePicture;
    private SessionManager session;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFacebookProfileInfo();
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
            }
        });
//Button click listeners
        SignInButton btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Loging in with G+");
                signInWithGplus();
            }
        });
//Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
        Intent intent = new Intent(this, CatergoriesActivity.class);
//if user is already logged-in or has a valid session, just launch the categories Activity;
        if(AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired() && session.isLoggedIn()){
            Profile myProfile = Profile.getCurrentProfile();
            intent.putExtra("image", myProfile.getProfilePictureUri(150,150));
            intent.putExtra("cover", myProfile.getProfilePictureUri(150,150));
            startActivity(intent);
        }
        if(session.isLoggedIn()){
            startActivity(intent);
        }
    }
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    private void signInWithGplus() {
        mGoogleApiClient.connect();
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            getGoogleProfileInfo();
            resolveSignInError();
        }
    }
    private void resolveSignInError() {
        if(mConnectionResult != null)
            if (mConnectionResult.hasResolution()) {
                try {
                    mIntentInProgress = true;
                    mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
                } catch (SendIntentException ex) {
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
            else{
                Toast.makeText(this, getResources().getString(R.string.common_google_play_services_api_unavailable_text), Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.getErrorDialog(mConnectionResult.getErrorCode(), this, 0 ).show();
            }
    }
    private void loginSuccess() {
        Intent intent = new Intent(this, CatergoriesActivity.class);
        startActivity(intent);
    }
    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        getGoogleProfileInfo();
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0)
                    .show();
            return;
        }
        if (!mIntentInProgress) {
//Store the connectionResult for later usage
            mConnectionResult = result;
            if (mSignInClicked) {
                resolveSignInError();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }
    private void getGoogleProfileInfo() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;
                Log.e(LOG_TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                userName = personName;
                userEmail = email;
                profilePicture = personPhotoUrl;
                session.createLoginSession(userName, userEmail, profilePicture);
                loginSuccess();
            } else {
                Toast.makeText(getApplicationContext(), "Person info is null", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void getFacebookProfileInfo() {
        if (AccessToken.getCurrentAccessToken() != null) {
            Profile profile = Profile.getCurrentProfile();
            String personName = profile.getName();
            String email = profile.getId();
            String personPhotoUrl = String.valueOf(profile.getProfilePictureUri(PROFILE_PIC_SIZE,
                    PROFILE_PIC_SIZE));
            Log.e(LOG_TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);
            userName = personName;
            userEmail = email;
            profilePicture = personPhotoUrl;
            session.createLoginSession(userName, userEmail, profilePicture);
            loginSuccess();
        }
    }
}
