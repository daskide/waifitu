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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.ContextWrapper;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;

import org.w3c.dom.Text;

public class WalkingFragment extends Fragment {

    private Integer countedStep;
    private Integer detectedStep;
    private TextView steps;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detectedStep = 0;
        requireActivity().registerReceiver(broadcastReceiver, new IntentFilter(StepCountingService.BROADCAST_ACTION));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_walking, container, false);

        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        steps = (TextView) getView().findViewById(R.id.steps);
        steps.setText(String.valueOf(detectedStep));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateViews(intent);
        }
    };

    private void updateViews(Intent intent) {
        countedStep = intent.getIntExtra("csteps", 0);
        detectedStep = intent.getIntExtra("dsteps", 0);
        Log.d("test", String.valueOf(countedStep));
        Log.d("test", String.valueOf(detectedStep));

        steps.setText(String.valueOf(detectedStep));
    }
}
