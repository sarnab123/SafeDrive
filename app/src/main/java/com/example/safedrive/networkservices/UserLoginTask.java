package com.example.safedrive.networkservices;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.safedrive.utils.UtilityConstants;
import com.example.safedrive.utils.UtilityMethods;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class UserLoginTask extends AsyncTask<Void, Void, Boolean>{

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "auto@example.com:qwerty", "manual@example.com:qwerty", "session@example.com:qwerty"
    };

    private final String mEmail;
    private final String mPassword;

    //TODO : RestListener = placeholder for rest API listener
    private IRestListener mRestListener;


    public UserLoginTask(IRestListener restListener, String email, String password) {
        mEmail = email;
        mPassword = password;
        mRestListener = restListener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(mEmail)) {
                // Account exists, return true if the password matches.
                return pieces[1].equals(mPassword);
            }
        }

        // TODO: register the new account here.
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(mRestListener != null)
        {
            // TODO : scope defination should come from server - hardcoding them for now
            if(!TextUtils.isEmpty(mEmail) && success)
            {
                mRestListener.onSuccess(UtilityMethods.getScopeforEmail(mEmail));
            }
            else {
                mRestListener.onFailure(new Error(UtilityConstants.no_auth_error));
            }

        }
    }

    @Override
    protected void onCancelled() {

    }

}
