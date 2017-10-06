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
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

    MissionActivity mMission = this;

    public static ToggleButton tb_led;
    public static TextView tv_arm, tv_flightmode, tv_controller, tv_quadbatt, tv_smartbatt, tv_waypoints;
    public static TextView tv_east, tv_north, tv_elevation, tv_roll, tv_pitch, tv_yaw, tv_dt;
    public static ProgressBar pb_motor1, pb_motor2, pb_motor3, pb_motor4;
    public static Handler UIHandler = new Handler(Looper.getMainLooper());
    public static boolean ic_ready = false;

    DataExchange mDataExchange = null;
    FlightController mFlightController = null;

    FlightController.MotorsPowers motorsPowers;

    public static boolean armed = false;


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
        tv_east = (TextView) findViewById(R.id.tv_east);
        tv_north = (TextView) findViewById(R.id.tv_north);
        tv_elevation = (TextView) findViewById(R.id.tv_elevation);
        tv_roll = (TextView) findViewById(R.id.tv_roll);
        tv_pitch = (TextView) findViewById(R.id.tv_pitch);
        tv_yaw = (TextView) findViewById(R.id.tv_yaw);
        tv_dt = (TextView) findViewById(R.id.tv_dt);
        pb_motor1 = (ProgressBar) findViewById(R.id.pb_motor1);
        pb_motor2 = (ProgressBar) findViewById(R.id.pb_motor2);
        pb_motor3 = (ProgressBar) findViewById(R.id.pb_motor3);
        pb_motor4 = (ProgressBar) findViewById(R.id.pb_motor4);

        tb_led = (ToggleButton) findViewById(R.id.tb_led);
        tb_led.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    turnLed(true);
                } else {
                    // The toggle is disabled
                    turnLed(false);
                }
            }
        });

        mFlightController = new FlightController(this);
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

        //while(!mFlightController.posKF.mInitialConditions.ic_ready){Log.w("Waiting for IC","Initial conditions not set");}
        tv_flightmode.setText("Prepared for Take-off");
        mFlightController.acquireData();
    }

    private void turnLed(boolean on){
        mFlightController.turnLed(on);
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

    public static void waypointsUpdated(){
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                tv_waypoints.setText(String.valueOf(waypointsList1.size()));
            }
        });
    }


    public static void changeFlightMode(String mode){
        flightMode = mode;
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                tv_flightmode.setText(flightMode);
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
        mFlightController.stopAcquiring();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mDataExchange.stopTCPserver();
        mFlightController.stopAcquiring();
        Intent intentMainMenu = new Intent(MissionActivity.this, MainActivity.class);
        startActivity(intentMainMenu);
        finish();
    }
}
