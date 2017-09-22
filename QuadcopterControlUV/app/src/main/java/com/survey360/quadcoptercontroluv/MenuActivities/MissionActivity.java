package com.survey360.quadcoptercontroluv.MenuActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.survey360.quadcoptercontroluv.R;
import com.survey360.quadcoptercontroluv.Utils.Communication.DataExchange;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MissionActivity extends AppCompatActivity {

    DataExchange mDataExchange = null;

    Timer timer;
    TemporizerControlSystem mainThread;
    double t;
    float Ts = (float) 0.01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        try {
            mDataExchange = new DataExchange(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDataExchange.startTCPserver();
    }

    private void startMission(){
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        mainThread = new TemporizerControlSystem();
        timer.schedule(mainThread, 10, 10);

        t = 0; // inicia la simulación
    }

    Long t_pasado = System.nanoTime();
    private class TemporizerControlSystem extends TimerTask {

        @Override
        public void run() {

            long t_medido = System.nanoTime();
            float dt = ((float) (t_medido - t_pasado)) / 1000000000.0f; // [s].;
            t_pasado = t_medido;

            Log.w("Hilo 10 ms mainControl", "Tiempo de hilo = " + dt * 1000);
            t = t + Ts;
        }
    }

    protected void onDestroy(){
        mDataExchange.stopTCPserver();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intentMainMenu = new Intent(MissionActivity.this, MainActivity.class);
        startActivity(intentMainMenu);
        finish();
    }
}
