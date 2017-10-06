package com.survey360.quadcoptercontroluv.MenuActivities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.survey360.quadcoptercontroluv.R;
import com.survey360.quadcoptercontroluv.Utils.Communication.DataExchange;
import com.survey360.quadcoptercontroluv.Utils.Controllers.FlightController;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MissionActivity extends AppCompatActivity{

    public static TextView tv_arm, tv_flightmode, tv_controller, tv_quadbatt, tv_smartbatt, tv_waypoints;
    public static ProgressBar pb_motor1, pb_motor2, pb_motor3, pb_motor4;
    public static Handler UIHandler = new Handler(Looper.getMainLooper());

    DataExchange mDataExchange = null;
    FlightController mFlightController = null;

    FlightController.MotorsPowers motorsPowers;

    public static boolean armed = false;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tv_arm = (TextView) findViewById(R.id.tv_arm);
        tv_flightmode = (TextView) findViewById(R.id.tv_flightmode);
        tv_controller = (TextView) findViewById(R.id.tv_controller);
        tv_quadbatt = (TextView) findViewById(R.id.tv_quadbatt);
        tv_smartbatt = (TextView) findViewById(R.id.tv_smartbatt);
        tv_waypoints = (TextView) findViewById(R.id.tv_waypoints);
        pb_motor1 = (ProgressBar) findViewById(R.id.pb_motor1);
        pb_motor2 = (ProgressBar) findViewById(R.id.pb_motor2);
        pb_motor3 = (ProgressBar) findViewById(R.id.pb_motor3);
        pb_motor4 = (ProgressBar) findViewById(R.id.pb_motor4);

        //mFlightController = new FlightController(this);
        //motorsPowers = new FlightController.MotorsPowers();

        // Start the sensor acquisition
        try {
            mDataExchange = new DataExchange(this);
            Thread.sleep(500);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mDataExchange.startTCPserver();
        Toast.makeText(MissionActivity.this, "TCP Server Started", Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //while(!mFlightController.posKF.mInitialConditions.ic_ready){;}
        startMission();
    }

    public static void armMotors(){
        armed =  true;
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                tv_arm.setText("Armed");
                tv_arm.setTextColor(Color.GREEN);
            }
        });

    }

    public static void disarmMotors(){
        armed = false;
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                tv_arm.setText("Disarmed");
                tv_arm.setTextColor(Color.RED);
            }
        });

    }

    private void startMission(){
        Log.w("Mission start","Mission start");
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

            //Log.w("Mission Thread", "Thread time = " + dt * 1000);
            t = t + Ts;
        }
    }


    public static void changeFlightMode(String mode){
        flightMode = mode;
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                tv_flightmode.setText(flightMode);
                tv_waypoints.setText(String.valueOf(waypointsList1.size()));
                Log.w("Waypoints size: ","----> "+String.valueOf(waypointsList1.size()));
            }
        });

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
