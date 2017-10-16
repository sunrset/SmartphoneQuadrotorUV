package com.survey360.quadcoptercontroluv.Utils.Controllers;

import android.content.Context;
import android.os.BatteryManager;
import android.util.Log;

import com.survey360.quadcoptercontroluv.MenuActivities.MissionActivity;
import com.survey360.quadcoptercontroluv.Utils.Communication.AdkCommunicator;
import com.survey360.quadcoptercontroluv.Utils.PermissionsRequest;
import com.survey360.quadcoptercontroluv.Utils.SaveFile;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.DataCollection;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.InitialConditions;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.PositionKalmanFilter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by AAstudillo on 21/09/2017.
 */

public class FlightController implements AdkCommunicator.AdbListener {

    public DataCollection mDataCollection;
    public PositionKalmanFilter posKF;
    public AdkCommunicator adkCommunicator;
    public MotorsPowers motorsPowers;


    DecimalFormat df = new DecimalFormat("0.000");

    BatteryManager bm;
    private int smartphoneBatLevel;
    private int batteryPercentage = 0;

    Timer controllerScheduler;
    ControllerThread controllerThread;
    double t;
    long measured_time, last_time;
    float delta_time;
    private boolean controlExecuting = false;

    public float[] controlSignals = new float[4];

    public SaveFile mSaveFile;
    private ArrayList<String> dataList;


    public FlightController(Context ctx){

        mDataCollection = new DataCollection(ctx);          // Sensor data acquisition
        posKF = new PositionKalmanFilter(ctx);              // Position Kalman filter and Initial position acquisition
        adkCommunicator = new AdkCommunicator(this, ctx);   // Communication with the Arduino Mega ADK
        motorsPowers = new MotorsPowers();                  // Class that contains the signals sent to the motors


        try {
            adkCommunicator.start(false);                   // Start the communication with the Arduino Mega ADK
        } catch (Exception e) {
            e.printStackTrace();
        }

        bm = (BatteryManager)ctx.getSystemService(BATTERY_SERVICE);
        smartphoneBatLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        mSaveFile = new SaveFile(ctx);                         // Data logging class
        dataList = new ArrayList<>();
    }

    public void acquireData(){
        posKF.initPositionKF();                             // Initialize the Position Kalman Filter matrices
        mDataCollection.register();                         // Begins to acquire sensor data

        Log.w("Mission thread: ","Thread start");
        if (controllerScheduler != null) {                  // Check that the controllerThread is not running through the scheduler
            controllerScheduler.cancel();
        }
        last_time = System.nanoTime();
        controllerScheduler = new Timer();
        controllerThread = new ControllerThread();
        controllerScheduler.schedule(controllerThread, 0, 10);  // The controllerThread is executed each 10 ms
        t = 0;
    }

    public void stopAcquiring(){
        //adkCommunicator.stop();
        mDataCollection.unregister();
        mSaveFile.saveArrayList(dataList, "dataFlightController");
    }

    private void ControllerExecution(){
        if(MissionActivity.armed){                          // The control outputs are only set, if the motors are armed
            setControlOutputs(controlSignals[0],controlSignals[1],controlSignals[2],controlSignals[3]);
        }
    }

    private void setControlOutputs(float u, float tau_psi, float tau_theta, float tau_phi){
        // L*cos(pi/4) = 0.25*(2^0.5)/2 = 0.17677669529

        motorsPowers.m1 = 0; // [0, 255]
        motorsPowers.m2 = 0;
        motorsPowers.m3 = 0;
        motorsPowers.m4 = 0;

        adkCommunicator.setPowers(motorsPowers);
    }

    @Override
    public void onBatteryVoltageArrived(float batteryVoltage){ //It's executed when Android receives the Battery data from ADK
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
        if(on){adkCommunicator.commTest(1,0,0,0);}
        else{adkCommunicator.commTest(0,0,0,0);}
    }



    private class ControllerThread extends TimerTask {
        @Override
        public void run() {
            measured_time = System.nanoTime();
            delta_time = ((float) (measured_time - last_time)) / 1000000.0f; // [ms].;
            last_time = measured_time;
            t = t + delta_time;     // [ms];

            posKF.executePositionKF(mDataCollection.conv_x,mDataCollection.conv_y,mDataCollection.baroElevation,mDataCollection.earthAccVals[0],mDataCollection.earthAccVals[1],mDataCollection.earthAccVals[2]);

            setQuadrotorState();
            editGUI();

            if(controlExecuting) {

                ControllerExecution();

                dataList.add(System.lineSeparator() + t + "," + delta_time + "," + MissionActivity.quadrotorState[0] +
                        "," + MissionActivity.quadrotorState[1] + "," + MissionActivity.quadrotorState[2] +
                        "," + MissionActivity.quadrotorState[3]+ "," + MissionActivity.quadrotorState[4] +
                        "," + MissionActivity.quadrotorState[5] + "," + controlSignals[0] + "," + controlSignals[1] +
                        "," + controlSignals[2] + "," + controlSignals[3] + "," + motorsPowers.m1 + "," + motorsPowers.m2 +
                        "," + motorsPowers.m3 + "," + motorsPowers.m4 +
                        "," + MissionActivity.quadrotorState[6] + "," + MissionActivity.quadrotorState[7] + " ");

            }


        }

        private void setQuadrotorState(){
            MissionActivity.quadrotorState[0] = (float)posKF.getEstimatedState()[0];    // x [m]
            MissionActivity.quadrotorState[1] = (float)posKF.getEstimatedState()[1];    // y [m]
            MissionActivity.quadrotorState[2] = (float)posKF.getEstimatedState()[2];    // z [m]
            MissionActivity.quadrotorState[3] = mDataCollection.orientationValsRad[2];  // roll [rad]
            MissionActivity.quadrotorState[4] = mDataCollection.orientationValsRad[1];  // pitch [rad]
            MissionActivity.quadrotorState[5] = mDataCollection.orientationValsRad[0];  // yaw [rad]
            MissionActivity.quadrotorState[6] = batteryPercentage;                      // quadrotor battery [%]
            MissionActivity.quadrotorState[7] = smartphoneBatLevel;                     // smartphone battery [%]
        }

        private void editGUI(){
            MissionActivity.UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(MissionActivity.armed){
                        MissionActivity.tv_east.setText(df.format(MissionActivity.quadrotorState[0]));
                        MissionActivity.tv_north.setText(df.format(MissionActivity.quadrotorState[1]));
                        MissionActivity.tv_elevation.setText(df.format(MissionActivity.quadrotorState[2]));
                    }
                    MissionActivity.tv_roll.setText(df.format(mDataCollection.orientationValsDeg[2]) + " °");
                    MissionActivity.tv_pitch.setText(df.format(mDataCollection.orientationValsDeg[1]) + " °");
                    MissionActivity.tv_yaw.setText(df.format(mDataCollection.orientationValsDeg[0]) + " °");
                    MissionActivity.tv_dt.setText(df.format(delta_time)+" ms");
                    MissionActivity.pb_rolljoystick.setProgress(MissionActivity.mDataExchange.rollJoystick);
                    MissionActivity.pb_pitchjoystick.setProgress(MissionActivity.mDataExchange.pitchJoystick);
                    MissionActivity.pb_yawjoystick.setProgress(MissionActivity.mDataExchange.yawJoystick);
                    MissionActivity.pb_throttlejoystick.setProgress(MissionActivity.mDataExchange.throttleJoystick);
                }
            });
        }
    }


    public static class MotorsPowers
    {
        public int m1, m2, m3, m4;  // 0-1023 (10 bits values).

        public int getMean()
        {
            return (m1+m2+m3+m4) / 4;
        }
    }
}
