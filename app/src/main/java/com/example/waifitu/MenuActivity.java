package com.example.waifitu;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MenuActivity extends AppCompatActivity {


    private GoogleSignInOptionsExtension fitnessOptions;
    private Intent intent;
    private Integer countedStep;
    private Integer detectedStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalkingFragment()).commit();

        //subscribeToSteps();
        //configureAPIClient();
        Log.i("idktag", "step");
        ActivityCompat.requestPermissions(MenuActivity.this,
                new String[] { Manifest.permission.ACTIVITY_RECOGNITION },
                0);
        startService(new Intent(this, StepCountingService.class));

        //intent = new Intent(this, StepCountingService.class);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_walking:
                        selectedFragment = new WalkingFragment();
                        break;
                    case R.id.nav_workouts:
                        selectedFragment = new WorkoutsFragment();
                        break;
                    case R.id.nav_timer:
                        selectedFragment = new TimerFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });


    }


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });



    public void configureAPIClient() {
        fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .build();
        //852372471692-1ij5nubogqi6nin7i04aenmok6n5vgp6.apps.googleusercontent.com

        GoogleSignInAccount googleSignInAccount =
                GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        if (!GoogleSignIn.hasPermissions(googleSignInAccount, fitnessOptions)) {
            Log.i("idktag", "no permissions");
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    1, // e.g. 1
                    googleSignInAccount,
                    fitnessOptions);
        } else {
            Log.i("idktag", "user has permissions!");
        }

        Task<Void> response = Fitness.getRecordingClient(this, googleSignInAccount)
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE);
        Task<Void> response2 = Fitness.getSensorsClient(this, googleSignInAccount)
                .add(
                        new SensorRequest.Builder()
                                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                                .setSamplingRate(1, TimeUnit.MINUTES)  // sample once per minute
                                .build(),
                        dataPoint -> Log.i("idktag", "Sdatad!"));
    }

    private void test() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("idktag", "nie ma");
            ActivityCompat.requestPermissions(MenuActivity.this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    1);
            // Permission is not granted
        }

        Fitness.getRecordingClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA);
        Fitness.getRecordingClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("idktag", "Successfully subscribed!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("idktag", "Successfuls!");
                    }
                });


    }
}