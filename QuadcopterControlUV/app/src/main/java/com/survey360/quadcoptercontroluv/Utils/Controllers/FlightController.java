package com.survey360.quadcoptercontroluv.Utils.Controllers;

import android.content.Context;
import android.os.BatteryManager;

import com.survey360.quadcoptercontroluv.MenuActivities.MissionActivity;
import com.survey360.quadcoptercontroluv.Utils.Communication.AdkCommunicator;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.DataCollection;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.InitialConditions;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.PositionKalmanFilter;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by AAstudillo on 21/09/2017.
 */

public class FlightController implements AdkCommunicator.AdbListener {

    public DataCollection mDataCollection = null;
    public InitialConditions mInitialConditions = null;
    public PositionKalmanFilter posKF = null;

    public AdkCommunicator adkCommunicator;

    BatteryManager bm;
    private int smartphoneBatLevel;
    private int batteryPercentage = 0;
    private float batteryVoltage;

    public FlightController(Context ctx){
        mDataCollection = new DataCollection(ctx);          // Sensor data acquisition
        mInitialConditions = new InitialConditions(ctx);    // Initial conditions of position
        posKF = new PositionKalmanFilter(ctx);              // Position Kalman filter

        adkCommunicator = new AdkCommunicator(this, ctx);   // Communication with the Arduino Mega ADK

        try {
            adkCommunicator.start(false);                   // Start the communication with the Arduino Mega ADK
        } catch (Exception e) {
            e.printStackTrace();
        }

        bm = (BatteryManager)ctx.getSystemService(BATTERY_SERVICE);
        smartphoneBatLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

    }

    @Override
    public void onBatteryVoltageArrived(float batteryVoltage){ //It's executed when Android receives the Battery data from ADK
        this.batteryVoltage = batteryVoltage;
        this.batteryPercentage = (int)(batteryVoltage*66.6667 - 740); //12.6 V full -- 11.1 V empty
        this.smartphoneBatLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        MissionActivity.tv_quadbatt.setText(this.batteryPercentage + " %");
        MissionActivity.tv_smartbatt.setText(this.smartphoneBatLevel + " %");
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
