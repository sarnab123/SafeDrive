package com.example.safedrive.controller.model;
/**
 *
 * Abstraction of general server errors and also safe
 * Created by sarnab.poddar on 5/21/17.
 */

public class AppError extends Error {

    // Type of adapter operation.
    private ErrorType mErrorType;

    // Error code of application.
    private String mCode = "";

    // Exception object
    private Exception mException;

    // Correlation Id
    private String mCorrelationId;


    public AppError(String code,
                 ErrorType errorType) {
        mCode = code;
        mErrorType = errorType;
    }

    public ErrorType getmErrorType() {
        return mErrorType;
    }

    public String getmCode() {
        return mCode;
    }



    public static enum ErrorType {
        NO_NETWORK("Network not connected"),
        AUTH_ERROR("Authentication Error"),
        APPLICATION_EXCEPTION("Application Exception"),
        SDK_KEY_ERROR("App Authentication Failed");

        private ErrorType(String errorDescription) {
            this.mErrorDescription = errorDescription;
        }

        // Description for Error Type
        private String mErrorDescription;

        /**
         * Returns Error Description for Error Type
         *
         * @return Error Description
         */
        public String getErrorDescription() {
            return mErrorDescription;
        }

    }

}
