package com.survey360.quadcoptercontroluv.TestActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.survey360.quadcoptercontroluv.MenuActivities.TestsActivity;
import com.survey360.quadcoptercontroluv.R;
import com.survey360.quadcoptercontroluv.Utils.Communication.DataExchange;

import java.io.IOException;

public class TcpTest extends AppCompatActivity {

    private ToggleButton tb_tcpserver;
    DataExchange mDataExchange = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_test);

        try {
            mDataExchange = new DataExchange(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tb_tcpserver = (ToggleButton) findViewById(R.id.tb_TCPserver);
        tb_tcpserver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Log.w("Toggle", "ON");
                    mDataExchange.startTCPserver();
                    Toast.makeText(TcpTest.this, "TCP Server Started", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    Log.w("Toggle", "OFF");
                    mDataExchange.stopTCPserver();
                    Toast.makeText(TcpTest.this, "TCP Server Stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intentTest = new Intent(TcpTest.this, TestsActivity.class);
        startActivity(intentTest);
        finish();
    }
}
