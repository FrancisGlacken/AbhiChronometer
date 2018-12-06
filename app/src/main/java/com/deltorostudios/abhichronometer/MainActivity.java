package com.deltorostudios.abhichronometer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Chronometer simpleChronometer;
    Button start, stop, restart, setFormat, clearFormat;
    long totalTime, timeAfterlife, timeOnDestroy, timeOnCreate;
    Boolean timerRunning = false;

    private static final String PREFS_FILE_MAIN = "mySharedPreferences";
    private static final int PREFS_MODE_MAIN = Context.MODE_PRIVATE;
    private static final String totalTimeKey = "com.deltorostudios.abhichronometer.GreenKey";
    private static final String timerRunningKey = "com.deltorostudios.abhichronometer.BlueKey";
    private static final String timeOnDestroyKey = "com.deltorostudios.abhichronometer.RedKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initiate Views.
        simpleChronometer = findViewById(R.id.simpleChronometer);
        start = findViewById(R.id.startButton);
        stop = findViewById(R.id.stopButton);
        restart = findViewById(R.id.restartButton);

        // Use sharedPrefs to get saved data or set them to defaults
        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_MAIN, PREFS_MODE_MAIN);
        totalTime = prefs.getLong(totalTimeKey, 0);
        timeOnDestroy = prefs.getLong(timeOnDestroyKey, 0);
        timerRunning = prefs.getBoolean(timerRunningKey, false);

        // If timer is running
        if (timerRunning) {

            // Make timeAfterlife equal the the time the app was terminated
            timeOnCreate = SystemClock.elapsedRealtime();
            timeAfterlife = timeOnCreate - timeOnDestroy;

            // Set chronometer base to current elapsedRT minus the totalTime before app termination minus
            // Thus allowing our timer to be accurate after app reincarnation
            simpleChronometer.setBase(SystemClock.elapsedRealtime() - totalTime - timeAfterlife);
            simpleChronometer.start();
        } else {
            simpleChronometer.setBase(SystemClock.elapsedRealtime() - totalTime);
        }

        // OnClick for start button
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                simpleChronometer.setBase(SystemClock.elapsedRealtime() - totalTime);
                simpleChronometer.start();

                // timer Started
                timerRunning = true;
            }
        });


        // OnClick for stop button
        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                totalTime = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                simpleChronometer.stop();

                // timer Paused
                timerRunning = false;
            }
        });

        // OnClick for restart button
        restart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                simpleChronometer.stop();
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                totalTime = 0;
                timeOnDestroy = 0;
                timeOnCreate = 0;
                timeAfterlife = 0;

                // timer Paused
                timerRunning = false;
            }
        });

    }

    // Saved stuff to sharedPrefs
    @Override
    protected void onPause() {
        super.onPause();

        if (timerRunning) {
            totalTime = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
            timeOnDestroy = SystemClock.elapsedRealtime();
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_MAIN, PREFS_MODE_MAIN);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(totalTimeKey, totalTime);
        editor.putLong(timeOnDestroyKey, timeOnDestroy);
        editor.putBoolean(timerRunningKey, timerRunning);
        editor.apply();
    }

    // Saved stuff to sharedPrefs
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timerRunning) {
            totalTime = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
            timeOnDestroy = SystemClock.elapsedRealtime();
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_MAIN, PREFS_MODE_MAIN);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(totalTimeKey, totalTime);
        editor.putLong(timeOnDestroyKey, timeOnDestroy);
        editor.putBoolean(timerRunningKey, timerRunning);
        editor.apply();
    }
}