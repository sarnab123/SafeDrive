package com.example.safedrive.networkservices;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public interface IRestListener {
    /**
     * Called when performed network task is successful.
     *
     */
    void onSuccess(byte valueObject);

    /**
     * Called when performed network task is not successful.
     */
    void onFailure(Error error);


}
