/**
 * Fetching user's information name, email, profile pic
 * */
private void getProfileInformation() {
    try {
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi
                    .getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personPhotoUrl = currentPerson.getImage().getUrl();
            String personGooglePlusProfile = currentPerson.getUrl();
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
 
            Log.e(TAG, "Name: " + personName + ", plusProfile: "
                    + personGooglePlusProfile + ", email: " + email
                    + ", Image: " + personPhotoUrl);
 
            txtName.setText(personName);
            txtEmail.setText(email);
 
            // by default the profile url gives 50x50 px image only
            // we can replace the value with whatever dimension we want by
            // replacing sz=X
            personPhotoUrl = personPhotoUrl.substring(0,
                    personPhotoUrl.length() - 2)
                    + PROFILE_PIC_SIZE;
 
            new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
 
        } else {
            Toast.makeText(getApplicationContext(),
                    "Person information is null", Toast.LENGTH_LONG).show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
 
/**
 * Background Async task to load user profile picture from url
 * */
private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
 
    public LoadProfileImage(ImageView bmImage) {
        this.bmImage = bmImage;
    }
 
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
 
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

/**
 * Revoking access from google
 * */
private void revokeGplusAccess() {
    if (mGoogleApiClient.isConnected()) {
        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status arg0) {
                        Log.e(TAG, "User access revoked!");
                        mGoogleApiClient.connect();
                        updateUI(false);
                    }
 
                });
    }
}
