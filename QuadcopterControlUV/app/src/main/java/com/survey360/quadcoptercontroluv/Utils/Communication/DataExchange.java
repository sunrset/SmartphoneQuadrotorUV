package com.survey360.quadcoptercontroluv.Utils.Communication;

/**
 * Created by AAstudillo on 18/09/2017.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.*;

public class DataExchange {

    Thread tcpServer;

    String clientSentence;
    String capitalizedSentence;
    ServerSocket welcomeSocket = new ServerSocket(6789);
    Context ctx;

    public DataExchange(Context context) throws IOException {
        ctx = context;
    }

    public void startTCPserver() {
        tcpServer = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Socket connectionSocket = null;
                    try{
                        connectionSocket = welcomeSocket.accept();
                        BufferedReader inFromClient =
                                new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                        //char[] receivedBuffer = new char[8];
                        //inFromClient.read(receivedBuffer, 0, 8);
                        clientSentence = inFromClient.readLine();
                        System.out.println("Received: " + clientSentence);
                        //Log.e("Received: ", "receivedBuffer: "+receivedBuffer);
                        capitalizedSentence = clientSentence.toUpperCase() + '\n';
                        outToClient.writeBytes(capitalizedSentence);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
}
