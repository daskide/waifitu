package com.example.waifitu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

public class SplashScreen extends AppCompatActivity {

    private static final int Time = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        setContentView(R.layout.activity_splash);

        ActivityStarter starter = new ActivityStarter();
        starter.start();
    }

    private class ActivityStarter extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(Time);
            } catch (Exception e) {
                Log.e("SplashScreen", e.getMessage());
            }

            Intent intent = new Intent(SplashScreen.this, MenuActivity.class);
            SplashScreen.this.startActivity(intent);
            SplashScreen.this.finish();
        }
    }
}