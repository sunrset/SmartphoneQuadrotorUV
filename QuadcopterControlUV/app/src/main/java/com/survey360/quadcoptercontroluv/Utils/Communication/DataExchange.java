package com.survey360.quadcoptercontroluv.Utils.Communication;

/**
 * Created by AAstudillo on 18/09/2017.
 */

import android.content.Context;
import android.widget.Toast;

import com.survey360.quadcoptercontroluv.MenuActivities.MissionActivity;

import java.io.*;
import java.net.*;

public class DataExchange {

    Thread tcpServer, decodeFrame;

    String clientSentence;
    String responseSentence;
    ServerSocket welcomeSocket1 = new ServerSocket(6789);
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    Socket connectionSocket1 = null;
    Context ctx;

    String[] receivedData;

    public DataExchange(Context context) throws IOException {
        ctx = context;
    }

    public void startTCPserver() {
        tcpServer = new Thread() {
            @Override
            public void run() {
                try {
                    connectionSocket1 = welcomeSocket1.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    TCPserver();
                }
            }
        };
        tcpServer.start();
        Toast.makeText(ctx, "TCP Server Started", Toast.LENGTH_SHORT).show();
    }

    public void stopTCPserver() {
        tcpServer.interrupt();
        Toast.makeText(ctx, "TCP Server Stopped", Toast.LENGTH_SHORT).show();
    }

    private void TCPserver(){
        try{
            //connectionSocket = welcomeSocket.accept();
            inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket1.getInputStream()));
            outToClient = new DataOutputStream(connectionSocket1.getOutputStream());
            clientSentence = null;
            while((clientSentence = inFromClient.readLine()) == null){ }
            //clientSentence = inFromClient.readLine();
            decodeReceived(clientSentence);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decodeReceived(final String received){
        decodeFrame = new Thread() {
            @Override
            public void run() {
                receivedData = received.split(",");
                System.out.println("Received: " + received);

                responseSentence = received + '\n';

                try {
                    if(receivedData[1].equals("0")){ // Started Connection
                        verifyConnectionStarted(receivedData[0]);
                    }
                    else if(receivedData[1].equals("!0")){ // End connection query
                        stopTCPserver();
                    }
                    else if(receivedData[1].equals("wy")){ // Waypoint received
                        decodeWaypoints(receivedData);
                    }
                    else if(receivedData[1].equals("state")){ // Quadrotor state query
                        sendState(receivedData[0]);
                    }
                    else if(receivedData[1].equals("mode")){ // Requested flight mode change
                        changeFlightMode(receivedData[2]);
                    }
                    else if(receivedData[1].equals("rc")){ // RC controller commands received
                        decodeRCframe(receivedData);
                    }
                    else if(receivedData[1].equals("rcstate")){ // RC controller commands received, quadrotor state query
                        decodeRCframe(receivedData);
                        sendState(receivedData[0]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        decodeFrame.start();
    }

    private void decodeWaypoints(String[] receivedFrame) throws IOException {
        outToClient.writeBytes(receivedData[0]+",wys"+'\n');
        int wp_id =  Integer.parseInt(receivedFrame[2]) - 1;
        float wp_north = Float.valueOf(receivedFrame[3]);
        float wp_east = Float.valueOf(receivedFrame[4]);
        float wp_elevation = Float.valueOf(receivedFrame[5]);
        float wp_yaw = Float.valueOf(receivedFrame[6]);
        System.out.println("Waypoint number: "+wp_id);
        System.out.println("North: "+wp_north);
        System.out.println("East: "+wp_east);
        System.out.println("Elevation: "+wp_elevation);
        System.out.println("Yaw: "+wp_yaw);
        if(MissionActivity.waypointsList1.size()<=wp_id) {
            MissionActivity.waypointsList1.add(new float[]{wp_north, wp_east, wp_elevation, wp_yaw});
        }
        else{
            MissionActivity.waypointsList1.set(wp_id,new float[]{wp_north, wp_east, wp_elevation, wp_yaw});
        }
    }

    private void resetWaypoints(String id){
        if(id.equals("1")) {
            MissionActivity.waypointsList1.clear();
        }
    }

    private void changeFlightMode(String mode) throws IOException {
        MissionActivity.changeFlightMode(mode);
        outToClient.writeBytes(receivedData[0]+",mode,"+receivedData[2]+'\n');
    }

    private void verifyConnectionStarted(String id) throws IOException {
        outToClient.writeBytes(id+",0"+'\n');
    }

    private void sendState(String id) throws IOException {
        outToClient.writeBytes(id+",state,843211.10,1062939.204,1930.204,0,85,74"+'\n');
    }

    private void decodeRCframe(String[] receivedFrame){

    }


}