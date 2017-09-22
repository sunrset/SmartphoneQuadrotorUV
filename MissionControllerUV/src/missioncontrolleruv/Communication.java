/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package missioncontrolleruv;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author Alejandro Astudillo
 */
public class Communication {
    
    public static MissionGUI window = MissionControllerUV.window;
    
    Socket connectionSocket = null;
    BufferedReader inFromClient = null;
    DataOutputStream outToClient = null;
    JTextArea jTextAreaConsole = null;
    
    public Communication(){
        jTextAreaConsole = window.jTextAreaConsole;
    }
    
    public void sendToServer(String sentence) throws Exception{
        /*String modifiedSentence;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("192.168.0.18", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sentence = inFromUser.readLine();
        long startTime = System.nanoTime();
        outToServer.writeBytes(sentence + '\n');
        modifiedSentence = inFromServer.readLine();
        long estimatedTime = (System.nanoTime() - startTime)/1000000;
        System.out.println("FROM SERVER: " + modifiedSentence);
        String[] animals = modifiedSentence.split(",");
        System.out.println("The number of animals is: " + animals.length);
        System.out.println("North: "+Float.valueOf(animals[1]));
        System.out.println("East: "+Float.valueOf(animals[2]));
        System.out.println("Elevation: "+Float.valueOf(animals[3]));
        System.out.println("timeellapsed: "+estimatedTime+" ms");
        clientSocket.close();*/
        //jTextAreaConsole.append(sentence);
        //jTextAreaConsole.setText("sssssssssssssssssssssssssssssssssssssssssssssssssssssssaaaaaaaaaaaaaaaaaw");
        //jTextAreaConsole.setText(null);
    }
    
    
    

    
}
