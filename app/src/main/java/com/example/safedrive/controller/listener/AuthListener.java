package com.example.safedrive.controller.listener;

import com.example.safedrive.controller.model.AppError;
import com.example.safedrivelibrary.models.DriverPersona;

/**
 *
 * Interface to get back user scope based on login/registration flow
 *
 *
 * Created by sarnab.poddar on 5/21/17.
 */

public interface AuthListener {

    // Successful login and responds with the @see com.example.safedrivelibrary.models.DriverPersona
    void onAuthSuccess(DriverPersona driverPersona);

    // Failure to login - multiple reasons - as documented in @see com.example.safedrive.controller.model.AppError
    void onAuthError(AppError error);
}
