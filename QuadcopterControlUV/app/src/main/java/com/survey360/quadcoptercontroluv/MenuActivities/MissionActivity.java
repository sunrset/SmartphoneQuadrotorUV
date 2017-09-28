package com.survey360.quadcoptercontroluv.MenuActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.survey360.quadcoptercontroluv.R;
import com.survey360.quadcoptercontroluv.Utils.Communication.DataExchange;
import com.survey360.quadcoptercontroluv.Utils.Controllers.FlightController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MissionActivity extends AppCompatActivity {

    DataExchange mDataExchange = null;
    FlightController mFlightController = null;

    Timer timer;
    TemporizerControlSystem mainThread;
    double t;
    float Ts = (float) 0.01;

    public static String flightMode = "AltHold";
    public static List<float[]> waypointsList1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        mFlightController = new FlightController(this);

        try {
            mDataExchange = new DataExchange(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDataExchange.startTCPserver();
        Toast.makeText(MissionActivity.this, "TCP Server Started", Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //while(!mFlightController.mInitialConditions.ic_ready){}
        startMission();
    }

    private void startMission(){
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        mainThread = new TemporizerControlSystem();
        timer.schedule(mainThread, 10, 9);

        t = 0; // inicia la simulación
    }

    Long t_pasado = System.nanoTime();
    private class TemporizerControlSystem extends TimerTask {
        long t_medido;
        float dt;
        @Override
        public void run() {
            t_medido = System.nanoTime();
            dt = ((float) (t_medido - t_pasado)) / 1000000000.0f; // [s].;
            t_pasado = t_medido;

            Log.w("Mission Thread", "Thread time = " + dt * 1000);
            t = t + Ts;
        }
    }


    public static void changeFlightMode(String mode){
        flightMode = mode;
        if(flightMode.equals("Stabilize")){

        }
        else if(flightMode.equals("AltHold")){

        }
        else if(flightMode.equals("Loiter")){

        }
        else if(flightMode.equals("RTL")){

        }
        else if(flightMode.equals("Auto")){

        }
        else if(flightMode.equals("Land")){

        }
    }

    protected void onDestroy(){
        mDataExchange.stopTCPserver();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mDataExchange.stopTCPserver();
        Intent intentMainMenu = new Intent(MissionActivity.this, MainActivity.class);
        startActivity(intentMainMenu);
        finish();
    }
}
