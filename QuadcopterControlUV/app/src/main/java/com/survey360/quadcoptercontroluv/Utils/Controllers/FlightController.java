package com.survey360.quadcoptercontroluv.Utils.Controllers;

import android.content.Context;
import android.os.BatteryManager;
import android.util.Log;

import com.survey360.quadcoptercontroluv.MenuActivities.MissionActivity;
import com.survey360.quadcoptercontroluv.Utils.Communication.AdkCommunicator;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.DataCollection;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.InitialConditions;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.PositionKalmanFilter;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by AAstudillo on 21/09/2017.
 */

public class FlightController implements AdkCommunicator.AdbListener {

    public DataCollection mDataCollection = null;
    //public InitialConditions mInitialConditions = null;
    public PositionKalmanFilter posKF = null;

    public AdkCommunicator adkCommunicator;

    DecimalFormat df = new DecimalFormat("0.000");

    BatteryManager bm;
    private int smartphoneBatLevel;
    private int batteryPercentage = 0;
    private float batteryVoltage;

    Timer timer;
    TemporizerControlSystem mainThread;
    double t;
    float Ts = (float) 0.01;

    public FlightController(Context ctx){
        mDataCollection = new DataCollection(ctx);          // Sensor data acquisition
        //mInitialConditions = new InitialConditions(ctx);    // Initial conditions of position
        posKF = new PositionKalmanFilter(ctx);              // Position Kalman filter and sensor data acquisition

        adkCommunicator = new AdkCommunicator(this, ctx);   // Communication with the Arduino Mega ADK

        try {
            adkCommunicator.start(false);                   // Start the communication with the Arduino Mega ADK
        } catch (Exception e) {
            e.printStackTrace();
        }

        bm = (BatteryManager)ctx.getSystemService(BATTERY_SERVICE);
        smartphoneBatLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

    }

    public void acquireData(){
        posKF.initPositionKF();
        mDataCollection.register();
        startMission();
    }

    public void stopAcquiring(){
        mDataCollection.unregister();
    }



    @Override
    public void onBatteryVoltageArrived(float batteryVoltage){ //It's executed when Android receives the Battery data from ADK
        this.batteryVoltage = batteryVoltage;
        this.batteryPercentage = (int)(batteryVoltage*66.6667 - 740); //12.6 V full -- 11.1 V empty
        this.smartphoneBatLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        MissionActivity.UIHandler.post(new Runnable() {
            @Override
            public void run() {
                MissionActivity.tv_quadbatt.setText(batteryPercentage + " %");
                MissionActivity.tv_smartbatt.setText(smartphoneBatLevel + " %");
            }
        });

    }

    public void turnLed(boolean on){
        if(on){
            adkCommunicator.commTest(1,0,0,0);
        }
        else{
            adkCommunicator.commTest(0,0,0,0);
        }
    }

    private void setControlOutputs(){
        if(MissionActivity.armed){
            ;
        }
    }

    private void startMission(){
        Log.w("Mission start","Mission start");
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
        long t_medido;
        float dt;

        @Override
        public void run() {
            t_medido = System.nanoTime();
            dt = ((float) (t_medido - t_pasado)) / 1000000000.0f; // [s].;
            t_pasado = t_medido;

            //Log.w("Mission Thread", "Thread time = " + dt * 1000);
            MissionActivity.UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    MissionActivity.tv_roll.setText(df.format(mDataCollection.orientationValsDeg[2]) + " °");
                    MissionActivity.tv_pitch.setText(df.format(mDataCollection.orientationValsDeg[1]) + " °");
                    MissionActivity.tv_yaw.setText(df.format(mDataCollection.orientationValsDeg[0]) + " °");
                    MissionActivity.tv_dt.setText(df.format(dt*1000)+" ms");
                }
            });

            t = t + Ts;
        }
    }


    public static class MotorsPowers
    {
        //public int nw, ne, se, sw; // 0-1023 (10 bits values).
        public int m1, m2, m3, m4;

        public int getMean()
        {
            return (m1+m2+m3+m4) / 4;
        }
    }
}
