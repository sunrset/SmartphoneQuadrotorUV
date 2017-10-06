package com.survey360.quadcoptercontroluv.Utils.Controllers;

import android.content.Context;

import com.survey360.quadcoptercontroluv.Utils.StateEstimation.DataCollection;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.InitialConditions;
import com.survey360.quadcoptercontroluv.Utils.StateEstimation.PositionKalmanFilter;

/**
 * Created by AAstudillo on 21/09/2017.
 */

public class FlightController {

    public DataCollection mDataCollection = null;
    public InitialConditions mInitialConditions = null;
    public PositionKalmanFilter posKF = null;

    public FlightController(Context ctx){
        mDataCollection = new DataCollection(ctx);
        mInitialConditions = new InitialConditions(ctx);
        posKF = new PositionKalmanFilter(ctx);
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
