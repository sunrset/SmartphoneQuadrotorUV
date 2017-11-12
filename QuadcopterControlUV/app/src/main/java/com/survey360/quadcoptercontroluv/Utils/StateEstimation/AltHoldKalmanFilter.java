package com.survey360.quadcoptercontroluv.Utils.StateEstimation;

import android.content.Context;

import org.ejml.data.DMatrixRMaj;

/**
 * Created by AAstudillo on 03/11/2017.
 */

public class AltHoldKalmanFilter {
    public InitialConditions mInitialConditions = null;

    private static final double dt = 0.01; //Our sample time
    private static final double Q_val = 0.05;
    private static final double R_val = 1;
    public double x_ic, y_ic, z_ic, psi_ic, theta_ic, phi_ic = 0;

    double[] ic, x_hat;
    DMatrixRMaj xhat_k_1, P_k_1, A, B, Q, Ro, H, z, U;
    KalmanFilterTotal f;
    float QUAD_MASS = 1.568f; // [kg]
    final float GRAVITY = 9.807f; // [m/s^2]

    public AltHoldKalmanFilter(Context context) {
        mInitialConditions = new InitialConditions(context);
        mInitialConditions.acquireIC();
    }

    public void initAltHoldKF(float mass, float i_xx, float i_yy, float i_zz){
        x_ic = mInitialConditions.getx_ic();
        y_ic = mInitialConditions.gety_ic();
        z_ic = mInitialConditions.getz_ic();
        psi_ic = mInitialConditions.getpsi_ic();
        theta_ic = mInitialConditions.gettheta_ic();
        phi_ic = mInitialConditions.getphi_ic();

        f = new KalmanFilterOperationsTotal();

        ic = new double[]{z_ic , 0 , psi_ic , 0 , theta_ic , 0 , phi_ic , 0 };
        xhat_k_1 = new DMatrixRMaj(8, 1, true, ic);
        P_k_1 = new DMatrixRMaj(8,8); //9x9 matrix of zeros

        A = createA();
        B = createB(mass, i_xx, i_yy, i_zz);
        Q = createQ(dt*Q_val);
        Ro = createR(R_val);
        H = createH();
        z = new DMatrixRMaj(7, 1); //7x1 vector of zeros
        x_hat = new double[8];

        f.configure(A,B,Q,H);
        f.setState(xhat_k_1, P_k_1);

        QUAD_MASS = mass*1.2f;
    }

    public void executeAltHoldKF(float posz, float psi, float psi_dot, float theta, float theta_dot, float phi, float phi_dot, float[] u){
        z.setData(new double[]{posz,psi,psi_dot,theta,theta_dot,phi,phi_dot});
        U = new DMatrixRMaj(4,1, true, new double[]{(u[0]-17.438f),u[1],u[2],u[3]});
        f.predict(U);
        f.update(z,Ro);
    }

    public double[] getEstimatedState(){
        x_hat = f.getState().getData();
        return x_hat;
    }

    public static DMatrixRMaj createA() {
        double []a = new double[]{
                0, 1 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 1 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 1 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 1 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 0 };
        return new DMatrixRMaj(8,8, true, a);
    }

    public static DMatrixRMaj createB(double m, double i_xx, double i_yy, double i_zz ) {
        double []b = new double[]{
                0,     0 ,       0 ,       0 ,
                (1/m), 0 ,       0 ,       0,
                0,     0 ,       0 ,       0 ,
                0,     (1/i_zz), 0 ,       0 ,
                0,     0 ,       0 ,       0 ,
                0,     0 ,       (1/i_yy), 0 ,
                0,     0 ,       0 ,       0 ,
                0,     0 ,       0 ,       (1/i_xx) };
        return new DMatrixRMaj(8,4, true, b);
    }

    public static DMatrixRMaj createQ(double var) {
        double []q = new double[]{
                var, 0 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, var , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , var , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , var , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , var , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , var , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , var , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , var};
        return new DMatrixRMaj(8,8, true, q);
    }

    public static DMatrixRMaj createH() {
        double []h = new double[]{
                1, 0 , 0 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 1 , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 1 , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 1 , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 1 , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 1 , 0 ,
                0, 0 , 0 , 0 , 0 , 0 , 0 , 1 };
        return new DMatrixRMaj(7,8, true, h);
    }

    public static DMatrixRMaj createR(double var) {
        double []r = new double[]{
                var*1000, 0 , 0 , 0 , 0 , 0 , 0 ,
                0, var , 0 , 0 , 0 , 0 , 0 ,
                0, 0 , var , 0 , 0 , 0 , 0 ,
                0, 0 , 0 , var , 0 , 0 , 0 ,
                0, 0 , 0 , 0 , var , 0 , 0 ,
                0, 0 , 0 , 0 , 0 , var , 0 ,
                0, 0 , 0 , 0 , 0 , 0, var };
        return new DMatrixRMaj(7,7, true, r);
    }
}
