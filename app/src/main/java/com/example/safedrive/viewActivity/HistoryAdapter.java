package com.example.safedrive.viewActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.safedrive.R;
import com.example.safedrivelibrary.models.ActiveDriveModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarnab.poddar on 5/24/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>
{

    private Context mContext;
    ArrayList<ActiveDriveModel> driveData;

    public HistoryAdapter(Context context, List<ActiveDriveModel> dataModel)
    {
        this.mContext = context;
        driveData = (ArrayList<ActiveDriveModel>) dataModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tripIDView;
        private TextView startTime;
        private TextView endTime;
        private TextView averageSpeed;
        private TextView maxSpeed;
        private TextView distance;
        private TextView driveScore;


        public ViewHolder(View view)
        {
            super(view);

            tripIDView = (TextView) view.findViewById(R.id.id_trip_id);
            startTime = (TextView) view.findViewById(R.id.id_start_time);
            endTime = (TextView) view.findViewById(R.id.id_end_time);
            averageSpeed = (TextView) view.findViewById(R.id.id_trip_distance);
            maxSpeed = (TextView) view.findViewById(R.id.id_average_speed);
            distance = (TextView) view.findViewById(R.id.id_max_speed);
            driveScore = (TextView) view.findViewById(R.id.id_drivescore);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View newView = null;
        ViewHolder newViewHolder = null;

        newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_item, parent , false);
        newViewHolder = new ViewHolder(newView);
        return newViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ActiveDriveModel activeDriveModel = driveData.get(position);

        holder.tripIDView.setText(mContext.getText(R.string.string_tripid) + activeDriveModel.getmTripID());
        holder.startTime.setText(mContext.getText(R.string.string_start_date) + activeDriveModel.getmStartDateTime());
        holder.endTime.setText(mContext.getText(R.string.string_end_date) + activeDriveModel.getmEndDateTime());
        holder.averageSpeed.setText(mContext.getText(R.string.string_average_speed) + activeDriveModel.getmAverageSpeed());
        holder.maxSpeed.setText(mContext.getText(R.string.string_max_speed) + activeDriveModel.getmMaxSpeed());
        holder.distance.setText(mContext.getText(R.string.string_distance) + activeDriveModel.getDistanceInTrip());
        holder.driveScore.setText(mContext.getText(R.string.string_driveScore).toString() + activeDriveModel.getDriveScore());

    }

    @Override
    public int getItemCount() {
        return driveData!=null && !driveData.isEmpty()?driveData.size():0;
    }
}
