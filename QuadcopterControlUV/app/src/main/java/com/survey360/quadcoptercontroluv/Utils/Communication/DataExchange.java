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
    String capitalizedSentence;
    ServerSocket welcomeSocket = new ServerSocket(6789);
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    Socket connectionSocket = null;
    Context ctx;

    String[] receivedData;

    public DataExchange(Context context) throws IOException {
        ctx = context;
    }

    public void startTCPserver() {
        tcpServer = new Thread() {
            @Override
            public void run() {
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
            connectionSocket = welcomeSocket.accept();
            inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
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
                capitalizedSentence = received + '\n';
                try {
                    outToClient.writeBytes(capitalizedSentence);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        decodeFrame.start();
    }
}