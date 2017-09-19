package com.survey360.quadcoptercontroluv.Utils.StateEstimation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

import java.text.DecimalFormat;

/**
 * Created by Alejandro Astudillo on 09/07/2017.
 */

public class DataCollection implements SensorEventListener {

    GetLocation mGetLocation = null;

    private SensorManager mSensorManager;
    private Sensor RotationSensor, PressureSensor, LinearAccSensor, GyroSensor, AccSensor, MagSensor;

    private float[] mRotationMatrix = new float[16];
    public float[] quaternionVals = new float[4];
    public float[] gyroVals = new float[3];
    public float[] accVals = new float[3];
    public float[] magVals = new float[3];
    public float[] linAccVals = new float[3];
    public float[] earthAccVals = new float[4];
    public float[] orientationValsRad = new float[3];
    public float[] orientationValsDeg = new float[3];
    public float absoluteElevation, baroElevation, elevationZero;
    private static final float ALTITUDE_SMOOTHING = 0.95f;
    public float[] speed = new float[3];
    public float pressure, rawAltitudeUnsmoothed;
    float[] invRotationMatrix = new float[16];

    public long time1; // [nanoseconds].
    public boolean running = false;

    public static final boolean USE_GPS = true;
    public double gps_latitude, gps_longitude, gps_altitude, gps_bearing, gps_accuracy, gps_speed, gps_time;
    public double conv_x, conv_y;

    public DataCollection(Context context){

        mGetLocation = new GetLocation(context);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        RotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        PressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        LinearAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        GyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        AccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        MagSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if((RotationSensor == null)) {
            Log.e("AndroQuadUV", "Rotation sensor is missing!");
        }
        if((GyroSensor == null)) {
            Log.e("AndroQuadUV", "Gyroscope sensor is missing!");
        }
        if((AccSensor == null)) {
            Log.e("AndroQuadUV", "Accelerometer sensor is missing!");
        }
        if((PressureSensor == null)) {
            Log.e("AndroQuadUV", "Barometer sensor is missing!");
        }
        if((LinearAccSensor == null)) {
            Log.e("AndroQuadUV", "Linear Acceleration sensor is missing!");
        }
        if((MagSensor == null)) {
            Log.e("AndroQuadUV", "Magnetic field sensor is missing!");
        }
        if (!mGetLocation.checkLocation())
            return;
        register();


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        // TODO

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        // TODO

        if (event.sensor == RotationSensor) {
            time1 = event.timestamp;
            // Convert the rotation-vector to a 4x4 matrix.
            quaternionVals = event.values;
            SensorManager.getRotationMatrixFromVector(mRotationMatrix,quaternionVals);
            //SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, mRotationMatrix);
            SensorManager.getOrientation(mRotationMatrix, orientationValsRad);

            // Optionally convert the result from radians to degrees
            orientationValsDeg[0] = (float) Math.toDegrees(orientationValsRad[0]); //Yaw
            orientationValsDeg[1] = (float) Math.toDegrees(orientationValsRad[1]); //Pitch
            orientationValsDeg[2] = (float) Math.toDegrees(orientationValsRad[2]); //Roll
        }

        else if(event.sensor == MagSensor){
            magVals = event.values;
        }

        else if(event.sensor == AccSensor){
            accVals = event.values;
        }

        else if(event.sensor == LinearAccSensor){
            linAccVals = event.values;
            earthAccVals[0] = linAccVals[0];
            earthAccVals[1] = linAccVals[1];
            earthAccVals[2] = linAccVals[2];
            earthAccVals[3] = 0;
            android.opengl.Matrix.invertM(invRotationMatrix, 0, mRotationMatrix, 0);
            android.opengl.Matrix.multiplyMV(earthAccVals, 0, invRotationMatrix, 0, earthAccVals, 0);
        }

        else if(event.sensor == GyroSensor){
            gyroVals = event.values;
        }

        else if(event.sensor == PressureSensor){
            pressure = event.values[0];
            rawAltitudeUnsmoothed = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure);
            absoluteElevation = (absoluteElevation*ALTITUDE_SMOOTHING) + (rawAltitudeUnsmoothed*(1.0f-ALTITUDE_SMOOTHING));
            //baroElevation = absoluteElevation - elevationZero;
            baroElevation = absoluteElevation;
        }
        getGPSData();
    }


    public void getGPSData(){
        gps_latitude = mGetLocation.latitudeGPS;
        gps_longitude = mGetLocation.longitudeGPS;
        gps_altitude = mGetLocation.altitudeGPS;
        gps_accuracy = mGetLocation.accuracyGPS;
        gps_bearing = mGetLocation.bearingGPS;
        gps_speed = mGetLocation.speedGPS;
        gps_time = mGetLocation.ElapsedRealtimeNanosGPS;
        conv_x = mGetLocation.Xcoord;
        conv_y = mGetLocation.Ycoord;
    }

    public void closeApp(){
        unregister();
        mGetLocation.LocationOFF();
    }

    public void register(){
        mSensorManager.registerListener(this, PressureSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, LinearAccSensor, 5000);
        mSensorManager.registerListener(this, GyroSensor, 5000);
        mSensorManager.registerListener(this, AccSensor, 5000);
        mSensorManager.registerListener(this, RotationSensor, 5000);
        mSensorManager.registerListener(this, MagSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mGetLocation.LocationON();
        running = true;
    }

    public void unregister(){
        mSensorManager.unregisterListener(this, PressureSensor);
        mSensorManager.unregisterListener(this, LinearAccSensor);
        mSensorManager.unregisterListener(this, GyroSensor);
        mSensorManager.unregisterListener(this, AccSensor);
        mSensorManager.unregisterListener(this, RotationSensor);
        mSensorManager.unregisterListener(this, MagSensor);
        mGetLocation.LocationOFF();
        running = false;
    }
}

class GetLocation {

    //protected MainActivity ctx;
    private final Context ctx;

    LocationManager locationManager;
    double longitudeGPS, latitudeGPS, altitudeGPS, bearingGPS, accuracyGPS, speedGPS, ElapsedRealtimeNanosGPS;
    double Xcoord, Ycoord;

    int MIN_GPS_UPDATE_TIME = 1000; // ms
    int MIN_GPS_UPDATE_DISTANCE = 0; // m

    DecimalFormat dfmeters = new DecimalFormat("############.####");
    DecimalFormat dfdegrees = new DecimalFormat("########.########");
    DecimalFormat dfseconds = new DecimalFormat("############.######");
    DecimalFormat dfminutes = new DecimalFormat("############");
    DecimalFormat dfdeg = new DecimalFormat("############");

    Proj4 mProj4 = null;
    ProjCoordinate p_result, p_in, converted_coord;
    double[] resultDMS = new double[3];

    public GetLocation(Context ctx) {
        this.ctx = ctx;
        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        mProj4 = new Proj4(this.ctx);
        p_in = new ProjCoordinate();
        p_result = new ProjCoordinate();

    }

    public boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this.ctx);
        dialog.setTitle("Enable Location")
                .setMessage("Your location is turned off.\nPlease turn it on")
                .setPositiveButton("Location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        ctx.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void LocationON(){
        /*if (!checkLocation())
            return;*/

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( this.ctx, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( this.ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return ;
            }
            locationManager.requestLocationUpdates(provider, MIN_GPS_UPDATE_TIME, MIN_GPS_UPDATE_DISTANCE, locationListenerBest);
        }
    }
    public void LocationOFF() {
        /*if (!checkLocation())
            return;*/
        /*if (ActivityCompat.checkSelfPermission(this.ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListenerBest);
        }*/
        locationManager.removeUpdates(locationListenerBest);
    }

    public ProjCoordinate convertCoordinates(double coord_x, double coord_y){
        p_in.x = coord_x;
        p_in.y = coord_y;

        p_result = mProj4.TransformCoordinates(p_in, mProj4.crsWGS84, mProj4.crsMagnaSirgasWest);

        return p_result;
    }

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {

            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();
            altitudeGPS = location.getAltitude();
            bearingGPS = location.getBearing();
            accuracyGPS = location.getAccuracy();
            speedGPS = location.getSpeed();
            ElapsedRealtimeNanosGPS = location.getElapsedRealtimeNanos();
            //Log.w("ElapsedRealTimeNanos","ElapsedRealtimeNanosGPS = " + ElapsedRealtimeNanosGPS);
            converted_coord = convertCoordinates(longitudeGPS, latitudeGPS);
            Xcoord = converted_coord.x;
            Ycoord = converted_coord.y;

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

}

class Proj4 {

    public CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    public CRSFactory csFactory = new CRSFactory();

    public CoordinateReferenceSystem crsWGS84, crsECEF, crsMagnaSirgasWest, crsMagnaSirgasCali;

    public Proj4(Context ctx) {
        Configure();
    }


    public void Configure(){

        crsWGS84 = csFactory.createFromName("EPSG:4326");
        //crsECEF = csFactory.createFromParameters("EPSG:4978", "+proj=geocent +datum=WGS84 +units=m +no_defs");
        crsMagnaSirgasWest = csFactory.createFromName("EPSG:3115");
        crsMagnaSirgasCali = csFactory.createFromParameters("SR-ORG:7664", "+proj=tmerc +lat_0=3.441883333 +lon_0=-76.5205625 +k=1 +x_0=1061900.18 +y_0=872364.63 +a=6379137 +b=6357748.961329674 +units=m +no_defs");


    }

    public ProjCoordinate TransformCoordinates(ProjCoordinate p_in, CoordinateReferenceSystem crs_in, CoordinateReferenceSystem crs_out){

        ProjCoordinate p_out = new ProjCoordinate();
        CoordinateTransform transformation = ctFactory.createTransform(crs_in, crs_out);

        transformation.transform(p_in, p_out);

        return p_out;
    }

    public double ConvertToDecimalDegrees(double degrees, double minutes, double seconds){
        double result = 0;
        if (degrees < 0) {result = degrees + (-1*((minutes/60) + (seconds/3600)));}
        if (degrees >= 0) {result = degrees + (minutes/60) + (seconds/3600);}
        return result;
    }

    public double[] ConvertToDMS(double decimaldegrees){
        double result[] = new double[3];
        result[0] = (long) decimaldegrees;                              //DEGREES
        result[1] = (long) ((decimaldegrees - result[0])*60);           //MINUTES
        result[2] = Math.abs((decimaldegrees - result[0] - (result[1]/60))*3600); //SECONDS

        result[1] = Math.abs(result[1]);
        return result;
    }


}
