package com.survey360.quadcoptercontroluv.Utils.wifi;

import android.content.Context;
import android.widget.Toast;

import com.survey360.quadcoptercontroluv.Utils.wifi.api.WifiStatus;
import com.survey360.quadcoptercontroluv.Utils.wifi.api.wifiHotSpots;

/**
 * Created by AAstudillo on 18/09/2017.
 */

public class CommunicationManager {
    public static Context ctx;
    wifiHotSpots hotutil = null;
    WifiStatus wu = null;

    public CommunicationManager(Context context){
        hotutil = new wifiHotSpots(context);
        wu = new WifiStatus(context);
        ctx = context;
    }

    public void startHotspot(){
        if(hotutil.startHotSpot(true)){
            Toast.makeText(ctx, " Device HotSpot is Turned On", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(ctx, "Device HotSpot is Not Turned On", Toast.LENGTH_LONG).show();
        }
    }

    public void stopHotspot(){
        if(hotutil.startHotSpot(false)){
            Toast.makeText(ctx, " Device HotSpot is Turned Off", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(ctx, "Device HotSpot is Not Turned Off", Toast.LENGTH_LONG).show();
        }
    }

    public void configureHotspot(String SSID, String password){
        if(hotutil.setHotSpot(SSID,password)){
            Toast.makeText(ctx, " SSID And PassWord Of Device HotSpot is Changed ", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(ctx, "SSID And PassWord Of Device HotSpot Not Changed", Toast.LENGTH_LONG).show();
        }
    }

    public void checkDeviceSupportWIFI(){
        if(wu.checkWifi(wu.SUPPORT_WIFI)){
            Toast.makeText(ctx, "Yes, Device Support Wifi", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(ctx, "No, Device DO Not Support Wifi", Toast.LENGTH_LONG).show();
        }
    }

    public void checkWIFIon(){
        if(wu.checkWifi(wu.IS_WIFI_ON)){
            Toast.makeText(ctx, "Yes, Wifi Is Turned On", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(ctx, "Wifi Is Turned Off", Toast.LENGTH_LONG).show();
        }
    }

    public void turnWIFIon(){
        if(wu.checkWifi(wu.WIFI_ON)){
            Toast.makeText(ctx, " Yes Wifi Is Turned On", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(ctx, "Wifi Was Not Turned On", Toast.LENGTH_LONG).show();
        }
    }

    public void turnWIFIoff(){
        if(wu.checkWifi(wu.WIFI_OFF)){
            Toast.makeText(ctx, " Wifi Is Turned Off", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(ctx, "Wifi Was Not Turned Off", Toast.LENGTH_LONG).show();
        }
    }
}
