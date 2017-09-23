package com.survey360.quadcoptercontroluv.Utils.Communication;

/**
 * Created by AAstudillo on 18/09/2017.
 */

import android.content.Context;
import android.widget.Toast;

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
                    if(receivedData[0].equals("wy")){
                        decodeWaypoints(receivedData);
                        outToClient.writeBytes("wys"+'\n');
                    }
                    else if(receivedData[0].equals("0")){
                        outToClient.writeBytes("0"+'\n');
                    }
                    if(receivedData[0].equals("state")){
                        outToClient.writeBytes("state,843211.10,1062939.204,1930.204,85,74"+'\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        decodeFrame.start();
    }

    private void decodeWaypoints(String[] receivedFrame){
        System.out.println("North: "+Float.valueOf(receivedFrame[1]));
        System.out.println("East: "+Float.valueOf(receivedFrame[2]));
        System.out.println("Elevation: "+Float.valueOf(receivedFrame[3]));
    }
}