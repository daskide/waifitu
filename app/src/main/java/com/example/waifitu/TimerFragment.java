package com.example.waifitu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class TimerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button start = (Button) getView().findViewById(R.id.start_btn);
        EditText intervals = (EditText) getView().findViewById(R.id.edit_intervals);
        EditText rest_time = (EditText) getView().findViewById(R.id.edit_rest_time);
        EditText train_time = (EditText) getView().findViewById(R.id.edit_train_time);

        start.setOnClickListener(v -> {
            Fragment newFragment = new TimeRunningFragment();
            Bundle data = new Bundle();
            data.putString("int", intervals.getText().toString());
            data.putString("res", rest_time.getText().toString());
            data.putString("tra", train_time.getText().toString());
            newFragment.setArguments(data);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }
}
