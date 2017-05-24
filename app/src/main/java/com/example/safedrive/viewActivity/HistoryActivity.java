package com.example.safedrive.viewActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.safedrive.R;
import com.example.safedrive.controller.SafeDriveController;

/**
 * Created by sarnab.poddar on 5/24/17.
 */

public class HistoryActivity extends AppCompatActivity
{

    private RecyclerView mHistoryView;
    private HistoryAdapter mHistoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history_list);

        mHistoryView = (RecyclerView) findViewById(R.id.id_history_recycler);
        mHistoryAdapter = new HistoryAdapter(this,new SafeDriveController().getUserDriveHistory());

        initializeView();
    }

    private void initializeView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mHistoryView.setLayoutManager(mLayoutManager);
        mHistoryView.setAdapter(mHistoryAdapter);
    }


}
