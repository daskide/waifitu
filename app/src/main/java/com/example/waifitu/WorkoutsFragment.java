package com.example.waifitu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WorkoutsFragment extends Fragment {
    private Integer pushCounter;
    private Integer totalPushups;
    private TextView pushups;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pushCounter = 0;
        totalPushups = 0;
        requireActivity().registerReceiver(broadcastReceiver, new IntentFilter(StepCountingService.BROADCAST_ACTION));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_workouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pushups = (TextView) getView().findViewById(R.id.pushups);
        pushups.setText(String.valueOf(pushCounter));
        Button but = (Button) getActivity().findViewById(R.id.start_workout_btn);
        but.setOnClickListener(v -> {
            pushCounter = totalPushups;
            pushups.setText(String.valueOf(totalPushups - pushCounter));
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateViews(intent);
        }
    };

    // Retrieve and set data of counter
    // Author: Paras Bansal
    private void updateViews(Intent intent) {
        totalPushups = intent.getIntExtra("pushups", 0);

        pushups.setText(String.valueOf(totalPushups - pushCounter));
    }
}
