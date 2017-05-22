package com.example.safedrive.viewActivity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.safedrive.R;
import com.example.safedrive.controller.SafeDriveController;
import com.example.safedrivelibrary.models.ActiveDriveModel;
import com.example.safedrivelibrary.models.DriverPersona;

import java.util.Calendar;

/**
 * Created by sarnab.poddar on 5/22/17.
 */

public class FutureDriveUIHelper
{
    private View futureView;
    private Activity parentActivity;
    private DriverPersona driverPersona;

    private SafeDriveController safeDriveController;

    private Button setFutureTrip;
    private TimePicker timePicker;

    public FutureDriveUIHelper(final Activity parentActivity, View futureView)
    {
        this.futureView = futureView;
        this.parentActivity = parentActivity;
        safeDriveController = new SafeDriveController();

        timePicker = (TimePicker) futureView.findViewById(R.id.id_future_timepicker);
        setFutureTrip = (Button) futureView.findViewById(R.id.id_setfuturetime);

        setFutureTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND,0);
                safeDriveController.setUpFutureSession(cal.getTimeInMillis(), parentActivity);
            }
        });
    }

    public void updateUIWithModel(@Nullable ActiveDriveModel activeDriveModel , @NonNull DriverPersona driverPersona)
    {



    }
}
