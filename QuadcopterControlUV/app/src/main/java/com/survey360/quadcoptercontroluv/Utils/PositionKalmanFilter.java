package com.survey360.quadcoptercontroluv.Utils;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

/**
 * Created by Alejandro Astudillo on 17/09/2017.
 */

public class PositionKalmanFilter {

    private static final double dt = 0.01; //Our sample time
    private static final double Q_val = 0.005;
    private static final double R_val = 1000;

    double []ic = new double[]{1, 0 , 0 , dt , 0 , 0 , 0.5*dt*dt , 0 , 0 };
    DMatrixRMaj xhat_k_1 = new DMatrixRMaj(9, 1, true, ic);

    //DMatrixRMaj P_k_1 = CommonOps_DDRM.identity(9);
    DMatrixRMaj P_k_1 = new DMatrixRMaj(9,9); //9x9 matrix of zeros

    DMatrixRMaj A = createA(dt);
    DMatrixRMaj Q = createQ(dt*Q_val);
    DMatrixRMaj Ro = createR(R_val);
    DMatrixRMaj H = createH();
    // z is the 6x1 measurement vector
    //KalmanFilter f = new KalmanFilterEquation();
    KalmanFilter f = new KalmanFilterOperations();
    DMatrixRMaj z = new DMatrixRMaj(6, 1);
    double[] x_hat = new double[9];

    public void initPositionKF(){
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

