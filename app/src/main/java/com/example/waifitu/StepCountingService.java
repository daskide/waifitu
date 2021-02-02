package com.example.waifitu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class StepCountingService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    Sensor stepDetectorSensor;
    Sensor proximitySensor;

    int currentStepsDetected;
    int direction = -1;
    int pushCounter;

    boolean serviceStopped;

    Intent intent;

    private static final String TAG = "StepService";
    public static final String BROADCAST_ACTION = ".StepCountingService";

    private final Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, stepDetectorSensor, 0);
        sensorManager.registerListener(this, proximitySensor, 0);

        currentStepsDetected = 0;

        serviceStopped = false;
        handler.removeCallbacks(updateBroadcastData);
        handler.post(updateBroadcastData);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            int detectSteps = (int) event.values[0];
            currentStepsDetected += detectSteps;
        }
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            if (Math.abs(event.values[1]) < 0.2) {
                if(direction == 1) {
                    pushCounter += 1;
                    direction = 0;
                    broadcastSensorValue();
                }
            }
            else
                direction = 1;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!serviceStopped) {
                broadcastSensorValue();
                sendNotification();
                handler.postDelayed(this, 10000);
            }
        }
    };

    private void sendNotification() {
        Intent intent = new Intent(this, WalkingFragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification_builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "3000";
            CharSequence name = "Channel Name";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(mChannel);
            notification_builder = new NotificationCompat.Builder(this, chanel_id);
        } else {
            notification_builder = new NotificationCompat.Builder(this);
        }
        Notification n = notification_builder
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("GET MOVING ALREADY!")
                .setContentText("You have made only " +  currentStepsDetected +"/10000 steps today!")
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        notificationManager.notify(0, n);
    }

    private void broadcastSensorValue() {
        intent.putExtra("dsteps", currentStepsDetected);
        intent.putExtra("pushups", pushCounter);
        sendBroadcast(intent);
    }
}
