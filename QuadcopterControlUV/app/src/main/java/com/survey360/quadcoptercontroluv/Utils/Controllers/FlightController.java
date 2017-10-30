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

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.factory.LinearSolverFactory_DDRM;
import org.ejml.interfaces.linsol.LinearSolver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.BATTERY_SERVICE;
import static org.ejml.dense.row.CommonOps_DDRM.mult;

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
    private boolean controlExecuting = true;

    public float[] controlSignals = new float[4];

    public SaveFile mSaveFile;
    private ArrayList<String> dataList;

    public final float QUAD_MASS = 1.568f; // [kg]
    public final float GRAVITY = 9.807f; // [m/s^2]
    public final float L = 0.244f; // [m]
    public final float TORQUE_DISTANCE = L*((float)Math.cos(Math.toRadians(45))); // [m]
    public final float K_T = 0.0210f;

    public float X_ref = 0f;
    public float Xdot_ref = 0f;
    public float Y_ref = 0f;
    public float Ydot_ref = 0f;
    public float Z_ref = 0f;
    public float Zdot_ref = 0f;
    public float Psi_ref = 0f;
    public float Psidot_ref = 0f;
    public float Theta_ref = -0.1f;
    public float Thetadot_ref = 0f;
    public float Phi_ref = -0.1f;
    public float Phidot_ref = 0f;

    public float Throttle = 0f;

    private String FlightMode = "Stabilize";

    public float[] Motor_Forces = new float[4];

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
        dataList = new ArrayList<>();
        controllerScheduler.schedule(controllerThread, 0, 10);  // The controllerThread is executed each 10 ms
        t = 0;
    }

    public void stopAcquiring(){
        //adkCommunicator.stop();
        mDataCollection.unregister();
        mSaveFile.saveArrayList(dataList, "dataFlightController");
        //dataList.clear();
    }

    private void ControllerExecution(){
        if(MissionActivity.armed){                          // The control outputs are only set, if the motors are armed

            if(FlightMode.equals("Stabilize")) {

                Throttle = ((MissionActivity.mDataExchange.throttleJoystick)-50f)*(1/50); // [N] [-1, 1]
                Psi_ref = ((MissionActivity.mDataExchange.yawJoystick)-50f)*((10*3.1416f/180)/50);  // [N] [-1, 1]
                Theta_ref = ((MissionActivity.mDataExchange.rollJoystick)-50f)*((15*3.1416f/180)/50);
                Phi_ref = ((MissionActivity.mDataExchange.pitchJoystick)-50f)*((15*3.1416f/180)/50);

                // LQR controller ---------------------
                controlSignals[0] = 0f;
                controlSignals[1] = -1.0986f*(mDataCollection.psi-Psi_ref) - 0.2717f*(mDataCollection.psi_dot-Psidot_ref);
                controlSignals[2] = -1.0404f*(mDataCollection.theta-Theta_ref) - 0.1606f*(mDataCollection.theta_dot-Thetadot_ref);
                controlSignals[3] = -1.0464f*(mDataCollection.phi-Phi_ref) - 0.1681f*(mDataCollection.phi_dot-Phidot_ref);
                // ------------------------------------

                controlSignals[0] = controlSignals[0] + QUAD_MASS*GRAVITY + Throttle;
                    // QUAD_MASS*GRAVITY is the necessary thrust to overcome the gravity [N]
            }
            /*
            else if(FlightMode.equals("AltHold")){

            }
            else if(FlightMode.equals("Loiter")){

            }
            else if(FlightMode.equals("RTL")){

            }
            else if(FlightMode.equals("Auto")){

            }
            else if(FlightMode.equals("Land")){

            }
            */

            setControlOutputs(controlSignals[0],controlSignals[1],controlSignals[2],controlSignals[3]);
        }
        else{
            motorsPowers.m1 = 0;
            motorsPowers.m2 = 0;
            motorsPowers.m3 = 0;
            motorsPowers.m4 = 0;

            adkCommunicator.setPowers(motorsPowers);
        }
    }

    private void setControlOutputs(float u, float tau_psi, float tau_theta, float tau_phi){

        Motor_Forces[0] = 0.2500f*u + 11.9048f*tau_psi - 1.4490f*tau_theta - 1.4490f*tau_phi; // [N]
        Motor_Forces[1] = 0.2500f*u - 11.9048f*tau_psi - 1.4490f*tau_theta + 1.4490f*tau_phi; // [N]
        Motor_Forces[2] = 0.2500f*u + 11.9048f*tau_psi + 1.4490f*tau_theta + 1.4490f*tau_phi; // [N]
        Motor_Forces[3] = 0.2500f*u - 11.9048f*tau_psi + 1.4490f*tau_theta - 1.4490f*tau_phi; // [N]

        motorsPowers.m1 = (int)(-1.983f*Math.pow(Motor_Forces[0],2) + 47.84f*Motor_Forces[0] + 3.835f); // [0, 255]
        motorsPowers.m2 = (int)(-1.983f*Math.pow(Motor_Forces[1],2) + 47.84f*Motor_Forces[1] + 3.835f); // [0, 255]
        motorsPowers.m3 = (int)(-1.983f*Math.pow(Motor_Forces[2],2) + 47.84f*Motor_Forces[2] + 3.835f); // [0, 255]
        motorsPowers.m4 = (int)(-1.983f*Math.pow(Motor_Forces[3],2) + 47.84f*Motor_Forces[3] + 3.835f); // [0, 255]

        if(motorsPowers.m1 > 255){motorsPowers.m1 = 255;} // Motors saturation
        if(motorsPowers.m1 < 0){motorsPowers.m1 = 0;}
        if(motorsPowers.m2 > 255){motorsPowers.m2 = 255;}
        if(motorsPowers.m2 < 0){motorsPowers.m2 = 0;}
        if(motorsPowers.m3 > 255){motorsPowers.m3 = 255;}
        if(motorsPowers.m3 < 0){motorsPowers.m3 = 0;}
        if(motorsPowers.m4 > 255){motorsPowers.m4 = 255;}
        if(motorsPowers.m4 < 0){motorsPowers.m4 = 0;}

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

    public void changeFlightMode(String flightMode){
        FlightMode = flightMode;
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
                    MissionActivity.pb_motor1.setProgress(motorsPowers.m1*100/255);
                    MissionActivity.pb_motor2.setProgress(motorsPowers.m2*100/255);
                    MissionActivity.pb_motor3.setProgress(motorsPowers.m3*100/255);
                    MissionActivity.pb_motor4.setProgress(motorsPowers.m4*100/255);
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
