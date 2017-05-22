package com.example.safedrivelibrary.models;

/**
 * Persona Model is to determine the driver's scope.
 *
 * Ideally - this persona scope will be server dependent - and based on Driver Login.
 * Created by sarnab.poddar on 5/21/17.
 */

public class DriverPersona
{

    // scopes based on OAUTH
    private DriveScope driveScope;

    // driverID based on OAUTH
    private String driverID;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public UserMode getDriveMode() {
        return driveMode;
    }

    public void setDriveMode(UserMode driveMode) {
        this.driveMode = driveMode;
    }

    private String firstName;

    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String userEmail;

    private String userPhoneNumber;

    private UserMode driveMode;

    public DriveScope getDriveScope() {
        return driveScope;
    }

    public void setDriveScope(DriveScope driveScope) {
        this.driveScope = driveScope;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public enum UserMode {
        DRIVER,
        PASSENGER;
    }

}
