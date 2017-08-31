package com.survey360.quadcoptercontroluv.TestActivities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.felhr.usbserial.UsbSerialDevice;
import com.survey360.quadcoptercontroluv.MenuActivities.TestsActivity;
import com.survey360.quadcoptercontroluv.R;

public class MotorsTestActivity extends AppCompatActivity {

    private TextView tv_batteryvolt, tv_batterypercent, tv_m1, tv_m2, tv_m3, tv_m4;
    private SeekBar sb_motor1, sb_motor2, sb_motor3, sb_motor4;
    private ProgressBar pb_batteryperc;
    private ToggleButton tb_arm;
    int progresssbMotor1 = 0, progresssbMotor2= 0, progresssbMotor3 = 0, progresssbMotor4 = 0;
    int batteryVoltage = 0, batteryPercentage = 0;
    boolean armed = false;


    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    UsbDevice device;
    UsbDeviceConnection connection;
    UsbManager usbManager;
    UsbSerialDevice serialPort;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motors_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tv_batteryvolt = (TextView)findViewById(R.id.tv_BatVolt);
        tv_batterypercent = (TextView)findViewById(R.id.tv_BatPercent);
        tv_m1 = (TextView)findViewById(R.id.tv_m1);
        tv_m2 = (TextView)findViewById(R.id.tv_m2);
        tv_m3 = (TextView)findViewById(R.id.tv_m3);
        tv_m4 = (TextView)findViewById(R.id.tv_m4);

        sb_motor1 = (SeekBar)findViewById(R.id.sb_Motor1);
        sb_motor2 = (SeekBar)findViewById(R.id.sb_Motor2);
        sb_motor3 = (SeekBar)findViewById(R.id.sb_Motor3);
        sb_motor4 = (SeekBar)findViewById(R.id.sb_Motor4);
        pb_batteryperc = (ProgressBar)findViewById(R.id.pb_BatPercent);
        tb_arm = (ToggleButton)findViewById(R.id.tb_Arm);

        sb_motor1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progresssbMotor1 = progressValue;
                tv_m1.setText(String.valueOf(progresssbMotor1)+" %");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_motor2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progresssbMotor2 = progressValue;
                tv_m2.setText(String.valueOf(progresssbMotor2)+" %");

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_motor3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progresssbMotor3 = progressValue;
                tv_m3.setText(String.valueOf(progresssbMotor3)+" %");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_motor4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progresssbMotor4 = progressValue;
                tv_m4.setText(String.valueOf(progresssbMotor4)+" %");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tb_arm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(tb_arm.isChecked()){
                    armed = true;
                    tv_batterypercent.setText("Armed");
                    //Button is ON
                    // Do Something
                }
                else{
                    tv_batterypercent.setText("Disarmed");
                    armed = false;
                    sb_motor1.setProgress(0);
                    sb_motor2.setProgress(0);
                    sb_motor3.setProgress(0);
                    sb_motor4.setProgress(0);
                    //Button is OFF
                    // Do Something
                }
            }
        });
    }

    void getBatteryData(){
        pb_batteryperc.setProgress(progresssbMotor1);
    }

    public void onBackPressed() {
        Intent intentTest = new Intent(MotorsTestActivity.this, TestsActivity.class);
        startActivity(intentTest);
        finish();
    }


}
