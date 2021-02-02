package com.example.waifitu;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Time;

public class TimeRunningFragment extends Fragment {

    private final Handler handler = new Handler();
    private MediaPlayer mp;
    private int intervals;
    private int rest_time;
    private int train_time;

    private int cintervals;
    private int crest_time;
    private int ctrain_time;


    private TextView tintervals;
    private TextView tmode;
    private TextView ttime;

    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer_running, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tintervals = (TextView) getView().findViewById(R.id.tintervals);
        tmode = (TextView) getView().findViewById(R.id.label_mode);
        ttime = (TextView) getView().findViewById(R.id.ttime);

        Bundle data;
        data = getArguments();


        intervals = Integer.parseInt(data.getString("int"));
        rest_time = Integer.parseInt(data.getString("res"));
        train_time = Integer.parseInt(data.getString("tra"));
        tintervals.setText("lol");
        ttime.setText("lol");

        //TimeHandler th = new TimeHandler();
        //th.start();

        mActivity = getActivity();
        cintervals = intervals;
        crest_time = rest_time;
        ctrain_time = train_time;
        mp = MediaPlayer.create(mActivity.getApplicationContext(), R.raw.alert);
        tmode.setTextColor(0xFA9467FF);
        ttime.setTextColor(0xFA9467FF);

        Fragment fr = this;

        handler.post(runnabl);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnabl);
        Log.e("tets", "ok");
    }

    private Runnable runnabl = new Runnable() {
        public void run() {
            tintervals.setText("ROUND " + (intervals - cintervals + 1) + "/" + intervals );
            if(ctrain_time > 0) {
                tmode.setText("TRAIN TIME");
                ctrain_time--;
                String text = formatTime(ctrain_time);
                ttime.setText(text);

            } else if (ctrain_time == 0 && crest_time == rest_time){
                mp.start();
                tmode.setTextColor(0xFFB9F077);
                ttime.setTextColor(0xFFB9F077);
                tmode.setText(" REST TIME");
                crest_time--;
                String text = formatTime(crest_time);
                ttime.setText(text);
            }
            else if(crest_time > 0){
                tmode.setText(" REST TIME");
                crest_time--;
                String text = formatTime(crest_time);
                ttime.setText(text);

            }else {
                mp.start();
                tmode.setTextColor(0xFA9467FF);
                ttime.setTextColor(0xFA9467FF);
                tmode.setText("TRAIN TIME");
                crest_time = rest_time;
                ctrain_time = train_time - 1;
                cintervals--;
                String text = formatTime(ctrain_time);
                ttime.setText(text);
            }
            if (cintervals > 0) {
                tintervals.setText("ROUND " + (intervals - cintervals + 1) + "/" + intervals);
                handler.postDelayed(this, 1000);
            }
            else {
                tmode.setText("GOOD JOB");
            }
        }
    };

    private String formatTime(int x) {
        int a = x / 60;
        int b = x % 60;
        String output = "";
        if (a < 10)
            output += "0";
        output += a + ":";
        if (b < 10)
            output += "0";
        output += b;
        return output;
    }

    private class TimeHandler extends Thread {

        @Override
        public void run() {


            //TimeRunningFragment.this.finish();
        }
    }

}
