package com.survey360.quadcoptercontroluv.Utils.StateEstimation;

import android.content.Context;

import org.ejml.data.DMatrixRMaj;

/**
 * Created by Alejandro Astudillo on 17/09/2017.
 */

public class PositionKalmanFilter {

    public InitialConditions mInitialConditions = null;

    private static final double dt = 0.01; //Our sample time
    private static final double Q_val = 0.005;
    private static final double R_val = 1000;
    public double x_ic, y_ic, z_ic = 0;

    double[] ic, x_hat;
    DMatrixRMaj xhat_k_1, P_k_1, A, Q, Ro, H, z;
    KalmanFilter f;

    public PositionKalmanFilter(Context context) {
        mInitialConditions = new InitialConditions(context);
        mInitialConditions.acquireIC();
    }

    public void initPositionKF(){
        x_ic = mInitialConditions.getx_ic();
        y_ic = mInitialConditions.gety_ic();
        z_ic = mInitialConditions.getz_ic();

        f = new KalmanFilterOperations();

        ic = new double[]{x_ic, y_ic, z_ic , 0 , 0 , 0 , 0 , 0 , 0 };
        xhat_k_1 = new DMatrixRMaj(9, 1, true, ic);
        //P_k_1 = CommonOps_DDRM.identity(9);
        P_k_1 = new DMatrixRMaj(9,9); //9x9 matrix of zeros

        A = createA(dt);
        Q = createQ(dt*Q_val);
        Ro = createR(R_val);
        H = createH();
        z = new DMatrixRMaj(6, 1); //6x1 vector of zeros
        x_hat = new double[9];

        f.configure(A,Q,H);
        f.setState(xhat_k_1, P_k_1);
    }

    public void executePositionKF(double posx, double posy, double posz, double accx, double accy, double accz){
        z.setData(new double[]{posx,posy,posz,accx,accy,accz});
        f.predict();
        f.update(z,Ro);
    }

    public double[] getEstimatedState(){
        x_hat = f.getState().getData();
        return x_hat;
    }

    public static DMatrixRMaj createA(double dt ) {
        double []a = new double[]{
                1, 0 , 0 , dt , 0 , 0 , 0.5*dt*dt , 0 , 0 ,
                0, 1 , 0 , 0 , dt , 0 , 0 , 0.5*dt*dt , 0 ,
                0, 0 , 1 , 0 , 0 , dt , 0 , 0 , 0.5*dt*dt ,
                0, 0 , 0 , 1 , 0 , 0 , dt , 0 , 0 ,
                0, 0 , 0 , 0 , 1 , 0 , 0 , dt , 0 ,
                0, 0 , 0 , 0 , 0 , 1 , 0 , 0 , dt ,
                0, 0 , 0 , 0 , 0 , 0 , 1 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 1 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 1 };
        return new DMatrixRMaj(9,9, true, a);
    }

    public static DMatrixRMaj createQ(double var) {
        double []q = new double[]{
                var, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, var , 0 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , var , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , var , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , var , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , var , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , var , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , var , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 0 , var };
        return new DMatrixRMaj(9,9, true, q);
    }

    public static DMatrixRMaj createH() {
        double []h = new double[]{
                1, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 1 , 0 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 1 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 1 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 1 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 1 };
        return new DMatrixRMaj(6,9, true, h);

    }

    public static DMatrixRMaj createR(double var) {
        double []r = new double[]{
                var, 0 , 0 , 0 , 0 , 0 ,
                0, var , 0 , 0 , 0 , 0 ,
                0, 0 , var , 0 , 0 , 0 ,
                0, 0 , 0 , var , 0 , 0 ,
                0, 0 , 0 , 0 , var , 0 ,
                0, 0 , 0 , 0 , 0 , var};
        return new DMatrixRMaj(6,6, true, r);
    }
}

