package com.survey360.quadcoptercontroluv.TestActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.survey360.quadcoptercontroluv.Utils.DataCollection;
import com.survey360.quadcoptercontroluv.R;
import com.survey360.quadcoptercontroluv.MenuActivities.TestsActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class AttitudeKFTest extends AppCompatActivity {

    DataCollection mDataCollection = null;

    private static final int INITIAL_REQUEST=1337;
    private static final int CAMERA_REQUEST=INITIAL_REQUEST+1;
    private static final int CONTACTS_REQUEST=INITIAL_REQUEST+2;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    long t_medido;
    float dt;

    Timer timer, timer2;
    Temporizer mainThread;
    double t;
    float Ts = (float) 0.01;
    DecimalFormat df = new DecimalFormat("0.0");
    DecimalFormat dfmm = new DecimalFormat("0.000");
    DecimalFormat dfint = new DecimalFormat("0");

    private Button bt_start, bt_stop;
    private TextView tv_pitch, tv_roll, tv_yaw, tv_GPS_latitude, tv_GPS_longitude, tv_GPS_altitude, tv_GPS_bearing, tv_GPS_accuracy, tv_GPS_speed, tv_GPS_time;

    GraphView graphAKF;
    LineGraphSeries<DataPoint> seriesAKF, seriesAKF2;
    private double graphAKFLastXValue = 0d;

    Spinner spinnerAKF, spinnerAKF2;
    String spinnerSelection1, spinnerSelection2;
    List<String> listSpinner1, listSpinner2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attitude_kftest);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mDataCollection = new DataCollection(this);

        bt_start = (Button) findViewById(R.id.bt_startAKF);
        bt_stop = (Button) findViewById(R.id.bt_stopAKF);

        bt_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){acquireData();
            }

        });
        bt_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                stopAcquiring();
            }

        });

        tv_pitch = (TextView)findViewById(R.id.Pitch_TV);
        tv_roll = (TextView)findViewById(R.id.Roll_TV);
        tv_yaw = (TextView)findViewById(R.id.Yaw_TV);
        tv_GPS_latitude = (TextView)findViewById(R.id.GPS_Latitude_TVp);
        tv_GPS_longitude = (TextView)findViewById(R.id.GPS_Longitude_TV);
        tv_GPS_altitude = (TextView)findViewById(R.id.Altitude_TV);
        tv_GPS_bearing = (TextView)findViewById(R.id.GPS_Bearing_TV);
        tv_GPS_accuracy = (TextView)findViewById(R.id.GPS_Accuracy_TV);
        tv_GPS_speed = (TextView)findViewById(R.id.GPS_Speed_TV);
        tv_GPS_time = (TextView)findViewById(R.id.GPS_Time_TV);

        graphAKF = (GraphView) findViewById(R.id.graphAKF);

        seriesAKF = new LineGraphSeries<>();
        //seriesAKF.setTitle("Roll angle");
        seriesAKF.setColor(Color.RED);

        seriesAKF2 = new LineGraphSeries<>();
        //seriesAKF2.setTitle("Roll angle");
        seriesAKF2.setColor(Color.BLUE);

        graphAKF.getViewport().setXAxisBoundsManual(true);
        graphAKF.getViewport().setMinX(0);
        graphAKF.getViewport().setMaxX(5);
        graphAKF.addSeries(seriesAKF);
        graphAKF.addSeries(seriesAKF2);

        InitializeSpinnersAKF();

    }

    public void InitializeSpinnersAKF() {

        spinnerAKF = (Spinner) findViewById(R.id.spinnerGraphAKF);
        listSpinner1 = new ArrayList<String>();
        listSpinner1.add("Plot 1");
        listSpinner1.add("Roll (RV)");
        listSpinner1.add("Pitch (RV)");
        listSpinner1.add("Yaw (RV)");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listSpinner1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAKF.setAdapter(dataAdapter);

        spinnerAKF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                spinnerSelection1 = (String) adapterView.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        spinnerAKF2 = (Spinner) findViewById(R.id.spinnerGraphAKF2);
        listSpinner2 = new ArrayList<String>();
        listSpinner2.add("Plot 2");
        listSpinner2.add("Roll (KF)");
        listSpinner2.add("Pitch (KF)");
        listSpinner2.add("Yaw (KF)");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listSpinner2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAKF2.setAdapter(dataAdapter2);

        spinnerAKF2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                spinnerSelection2 = (String) adapterView.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

    }


    public void acquireData(){
        mDataCollection.register();
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        mainThread = new Temporizer();
        timer.schedule(mainThread, 10, 10);

        t = 0; // inicia la simulación
    }

    public void stopAcquiring(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        mDataCollection.unregister();
    }

    public void updateTextViews(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_yaw.setText(dfmm.format(mDataCollection.orientationValsDeg[0])+" °");
                tv_pitch.setText(dfmm.format(mDataCollection.orientationValsDeg[1])+" °");
                tv_roll.setText(dfmm.format(mDataCollection.orientationValsDeg[2])+" °");
                tv_GPS_latitude.setText(String.valueOf(mDataCollection.gps_latitude));
                tv_GPS_longitude.setText(String.valueOf(mDataCollection.gps_longitude));
                tv_GPS_altitude.setText(String.valueOf(mDataCollection.gps_altitude));
                tv_GPS_accuracy.setText(String.valueOf(mDataCollection.gps_accuracy));
                tv_GPS_bearing.setText(String.valueOf(mDataCollection.gps_bearing));
                tv_GPS_speed.setText(String.valueOf(mDataCollection.gps_speed));
                tv_GPS_time.setText(String.valueOf(mDataCollection.gps_time));

                graphAKFLastXValue += 0.01d;
                //graphAKFLastXValue += dt/1000;

                if(spinnerSelection1.contentEquals(listSpinner1.get(0))){
                    seriesAKF.appendData(new DataPoint(graphAKFLastXValue, 0), true, 500);
                }
                else if(spinnerSelection1.contentEquals(listSpinner1.get(1))){
                    seriesAKF.appendData(new DataPoint(graphAKFLastXValue, mDataCollection.orientationValsDeg[2]), true, 500);
                }
                else if(spinnerSelection1.contentEquals(listSpinner1.get(2))){
                    seriesAKF.appendData(new DataPoint(graphAKFLastXValue, mDataCollection.orientationValsDeg[1]), true, 500);
                }
                else if(spinnerSelection1.contentEquals(listSpinner1.get(3))){
                    seriesAKF.appendData(new DataPoint(graphAKFLastXValue, mDataCollection.orientationValsDeg[0]), true, 500);
                }

                if(spinnerSelection2.contentEquals(listSpinner2.get(0))){
                    seriesAKF2.appendData(new DataPoint(graphAKFLastXValue, 0), true, 500);
                }
                else if(spinnerSelection2.contentEquals(listSpinner2.get(1))){
                    seriesAKF2.appendData(new DataPoint(graphAKFLastXValue, mDataCollection.orientationValsDeg[2]), true, 500);
                }
                else if(spinnerSelection2.contentEquals(listSpinner2.get(2))){
                    seriesAKF2.appendData(new DataPoint(graphAKFLastXValue, mDataCollection.orientationValsDeg[1]), true, 500);
                }
                else if(spinnerSelection2.contentEquals(listSpinner2.get(3))){
                    seriesAKF2.appendData(new DataPoint(graphAKFLastXValue, mDataCollection.orientationValsDeg[0]), true, 500);
                }

            }
        });
    }

    Long t_pasado = System.nanoTime();
    private class Temporizer extends TimerTask {

        @Override
        public void run() {

            t_medido = System.nanoTime();
            dt = ((float) (t_medido - t_pasado)) / 1000000.0f; // [s].;
            t_pasado = t_medido;
            //Log.w("Hilo 10 ms mainControl", "Tiempo de hilo = " + dt * 1000);

            updateTextViews();
            t = t + dt/1000;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        mDataCollection.register();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mDataCollection.unregister();
    }

    protected void onDestroy(){
        super.onDestroy();
        mDataCollection.closeApp();
    }

    public void onBackPressed() {
        Intent intentTest = new Intent(AttitudeKFTest.this, TestsActivity.class);
        startActivity(intentTest);
        finish();
    }
}
