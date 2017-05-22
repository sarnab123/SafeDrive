package com.example.safedrive.controller;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.safedrive.SafeDriveApplication;
import com.example.safedrive.controller.listener.AuthListener;
import com.example.safedrive.controller.model.AppError;
import com.example.safedrivelibrary.models.DriveScope;
import com.example.safedrivelibrary.models.DriverPersona;
import com.example.safedrive.networkservices.IRestListener;
import com.example.safedrive.networkservices.UserLoginTask;
import com.example.safedrive.utils.UtilityConstants;
import com.example.safedrive.utils.UtilityMethods;

import java.lang.ref.WeakReference;

/**
 *
 * Controller to determine sign in flow.
 *
 * Input - @see  com.example.safedrive.controller.listener.AuthListener,
 * Created by sarnab.poddar on 5/21/17.
 */

public class AuthController implements IRestListener
{

    final WeakReference<AuthListener> authListener;

    private String mUserEmail;


    public AuthController(WeakReference<AuthListener> authListener)
    {
        this.authListener = authListener;
    }

    public void startAuthentication(@NonNull String userEmail, String userPassword)
    {
        if(TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword))
        {
            AppError appError = new AppError(UtilityConstants.no_auth_error, AppError.ErrorType.AUTH_ERROR);
            authListener.get().onAuthError(appError);
        }
        else {
            mUserEmail = userEmail;

            if (SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState() == null) {
                new UserLoginTask(this, userEmail, userPassword).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                validateUserAndScope(userEmail);
            }
        }
    }


    /**
     * This method will check if the same user is logged in and revalidate the scope if true
     * @param userName
     */
    private void validateUserAndScope(@NonNull String userName)
    {
        DriverPersona loggedinUser = SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState();

        // same user
        // should ideally reverify token if issued
        assert loggedinUser != null;
        if(loggedinUser.getUserEmail().equalsIgnoreCase(userName))
        {
            createDriverPersona(UtilityMethods.getScopeforEmail(userName));
        }
        else{
            if(authListener.get() != null)
            {
               AppError appError = new AppError(UtilityConstants.already_logged_in_code, AppError.ErrorType.AUTH_ERROR);
               authListener.get().onAuthError(appError);
            }
        }
    }


    private void createDriverPersona(byte driverScope)
    {
        DriverPersona authDriverPersona = new DriverPersona();
        authDriverPersona.setDriverID(mUserEmail);
        authDriverPersona.setFirstName("John");
        authDriverPersona.setLastName("Doe");
        authDriverPersona.setUserEmail(mUserEmail);
        if(driverScope == UtilityConstants.AUTH_AUTO)
        {
            authDriverPersona.setDriveScope(DriveScope.AUTO_DRIVE);
        } else if(driverScope == UtilityConstants.AUTH_MANUAL)
        {
            authDriverPersona.setDriveScope(DriveScope.MANUAL_MODE);
        } else if(driverScope == UtilityConstants.AUTH_SESSION)
        {
            authDriverPersona.setDriveScope(DriveScope.AUTO_SESSION_DRIVE);
        } else{
            authDriverPersona.setDriveScope(DriveScope.NO_SCOPE);
        }

        SafeDriveApplication.getInstance().getSharedPrefHelper().saveUserLoggedStatus(authDriverPersona);

        if(authListener.get() != null)
        {
            authListener.get().onAuthSuccess(authDriverPersona);
        }

    }

    /**
     * Called when performed network task is successful.
     *
     * @param driverScope
     */
    @Override
    public void onSuccess(byte driverScope) {

        createDriverPersona(driverScope);
    }

    /**
     * Called when performed network task is not successful.
     *
     * @param error
     */
    @Override
    public void onFailure(Error error) {
        if(authListener.get() != null)
        {
            AppError appError = new AppError(UtilityConstants.no_auth_error, AppError.ErrorType.AUTH_ERROR);
            authListener.get().onAuthError(appError);
        }
    }
}
