package com.survey360.quadcoptercontroluv.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Asus on 13/07/2017.
 */

public class PermissionsRequest {

    private static final int INITIAL_REQUEST=1337;
    private static final int CAMERA_REQUEST=INITIAL_REQUEST+1;
    private static final int CONTACTS_REQUEST=INITIAL_REQUEST+2;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    private final Context ctx;
    private final Activity act;

    public PermissionsRequest(Context context, Activity activity){
        this.ctx = context;
        this.act = activity;
    }

    public void LocationPermission(){
        if (ContextCompat.checkSelfPermission(this.ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED ) {

        } else {
            ActivityCompat.requestPermissions(this.act, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    LOCATION_REQUEST);
        }
    }
}
