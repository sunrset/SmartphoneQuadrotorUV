package com.survey360.quadcoptercontroluv.Utils.Communication;

/**
 * Created by AAstudillo on 18/09/2017.
 */

import android.content.Context;

import java.io.*;
import java.net.*;

public class DataExchange {

    String clientSentence;
    String capitalizedSentence;
    ServerSocket welcomeSocket = new ServerSocket(6789);

    public DataExchange(Context context) throws IOException {

    }

    public void TCPserver() throws IOException {
        while (true) {
            //TODO: Add Thread or ASyncTask to handle TCPserver in the loop
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);
            capitalizedSentence = clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence);
        }
    }
}
