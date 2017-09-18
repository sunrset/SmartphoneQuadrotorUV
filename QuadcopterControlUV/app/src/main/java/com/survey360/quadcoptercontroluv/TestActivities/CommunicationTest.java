package com.survey360.quadcoptercontroluv.TestActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.survey360.quadcoptercontroluv.R;
import com.survey360.quadcoptercontroluv.Utils.wifi.CommunicationManager;

public class CommunicationTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_test);
        CommunicationManager mCommManager = new CommunicationManager(this);
    }
}
