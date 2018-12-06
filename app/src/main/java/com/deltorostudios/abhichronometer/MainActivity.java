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

public class MainActivity extends AppCompatActivity {

    Chronometer simpleChronometer;
    Button start, stop, restart, setFormat, clearFormat;
    long totalTime;
    Boolean timerRunning = false;

    private static final String PREFS_FILE_MAIN = "mySharedPreferences";
    private static final int PREFS_MODE_MAIN = Context.MODE_PRIVATE;
    private static final String totalTimeKey = "com.deltorostudios.abhichronometer.GreenKey";
    private static final String timerRunningKey = "com.deltorostudios.abhichronometer.BlueKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initiate views
        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        start = (Button) findViewById(R.id.startButton);
        stop = (Button) findViewById(R.id.stopButton);
        restart = (Button) findViewById(R.id.restartButton);

        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_MAIN, PREFS_MODE_MAIN);
        totalTime = prefs.getLong(totalTimeKey, 0);
        timerRunning = prefs.getBoolean(timerRunningKey, false);

        Log.i("testingWithPower","onCreate: totalTime = " + totalTime + "  --  timerRunning = " + timerRunning);


        if (timerRunning) {
            simpleChronometer.setBase(SystemClock.elapsedRealtime() - totalTime);
            simpleChronometer.start();
        } else {
            simpleChronometer.setBase(SystemClock.elapsedRealtime() - totalTime);
        }

        // perform click  event on start button to start a chronometer
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("testingWithPower","Start: "+(SystemClock.elapsedRealtime() - simpleChronometer.getBase()));


                simpleChronometer.setBase(SystemClock.elapsedRealtime() - totalTime);
                simpleChronometer.start();

                // timer Started
                timerRunning = true;
            }
        });


        // perform click  event on stop button to stop the chronometer
        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("testingWithPower","Stop: "+(SystemClock.elapsedRealtime() - simpleChronometer.getBase()));





                totalTime = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                simpleChronometer.stop();

                // timer Paused
                timerRunning = false;
            }
        });

        // perform click  event on restart button to set the base time on chronometer
        restart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("testingWithPower","Restart before: "+(SystemClock.elapsedRealtime() - simpleChronometer.getBase()));




                simpleChronometer.stop();
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                totalTime = 0;

                // timer Paused
                timerRunning = false;

                Log.i("testingWithPower","Restart after: "+(SystemClock.elapsedRealtime() - simpleChronometer.getBase()));
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("testingWithPower","OnPause: "+(SystemClock.elapsedRealtime() - simpleChronometer.getBase()));

        if (timerRunning) {
            totalTime = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
        }


        SharedPreferences prefs = getSharedPreferences(PREFS_FILE_MAIN, PREFS_MODE_MAIN);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(totalTimeKey, totalTime);
        editor.putBoolean(timerRunningKey, timerRunning);
        editor.apply();
    }


}