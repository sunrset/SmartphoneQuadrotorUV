package com.survey360.quadcoptercontroluv.Utils.StateEstimation;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by AAstudillo on 17/09/2017.
 */

public class InitialConditions {

    private static Context ctx;
    DataCollection mDataCollection = null;
    double x_ic, y_ic, z_ic = 0;
    public boolean ic_ready = false;


    public InitialConditions(Context context){
        mDataCollection = new DataCollection(context);
        ctx = context;
    }

    public void acquireIC(){

        new Thread(new Runnable() {
            public void run() {
                showInitialToast();
                mDataCollection.register();
                int j = 0;
                while(j<=15) {
                    x_ic = mDataCollection.conv_x;
                    y_ic = mDataCollection.conv_y;
                    z_ic = mDataCollection.baroElevation;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    j++;
                    if(x_ic==0 && j>=5){
                        showWarningToast();
                        j = 0;
                    }
                }
                mDataCollection.unregister();
                ic_ready = true;

                showFinalToast();

            }
        }).start();

    }

    public void showWarningToast(){
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, "GPS signal is poor", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showInitialToast(){
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, "Wait for initial position to be acquired", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showFinalToast(){
        ((Activity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, "Initial position acquired", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public double getx_ic(){
        return x_ic;
    }

    public double gety_ic(){
        return y_ic;
    }

    public double getz_ic(){
        return z_ic;
    }
}

