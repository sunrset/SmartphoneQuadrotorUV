/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package missioncontrolleruv1.Communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import missioncontrolleruv1.MissionControllerUV;
import missioncontrolleruv1.MissionGUI;

/**
 *
 * @author Alejandro Astudillo
 */
public class Communication {
    
    public static MissionGUI window = MissionControllerUV.window;        
    Timer timer;
    TemporizerComm mainThread;
    double t;
    float Ts = (float) 0.01;
    
    Socket connectionSocket = null;
    BufferedReader inFromClient = null;
    DataOutputStream outToClient = null;
    JTextArea jTextAreaConsole = null;
    Socket clientSocket = null;
    String receivedSentence, responseSentence, ip_1;
    String[] receivedData;
    BufferedReader inFromServer, inFromUser;
    DataOutputStream outToServer;
    long startTime, estimatedTime;
    Thread decodeFrame;
    
    public Communication(){
        //jTextAreaConsole = window.jTextAreaConsole;
        jTextAreaConsole = window.jTextAreaConsole;
        
    }
    
    public void startConnection(String id){
        ip_1 = window.jTF_ip1.getText();
        try {
            clientSocket = new Socket(ip_1, 6789); //192.168.0.18
            sendToServer(id+",0");
            receiveFromServer();
            System.out.println("Connection Set with IP: "+ip_1);
            sendWaypoint("1", 1, 865125.540f, 1060712.219f, 971.418f, 0f);
            requestQuadrotorState("1");
        } catch (IOException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection with "+ip_1+ " was not set");
        } catch (Exception ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void sendToServer(String sentence) throws IOException{
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        startTime = System.nanoTime();
        outToServer.writeBytes(sentence + '\n');
        //jTextAreaConsole.append(sentence);
        //jTextAreaConsole.setText("sssssssssssssssssssssssssssssssssssssssssssssssssssssssaaaaaaaaaaaaaaaaaw");
        //jTextAreaConsole.setText(null);
    }
    
    public void receiveFromServer(){
        try {
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            receivedSentence = inFromServer.readLine();
            estimatedTime = (System.nanoTime() - startTime)/1000000;
            decodeReceived(receivedSentence);
        } catch (IOException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeCommunication(String id) throws IOException{
        sendToServer(id+",!0");
        clientSocket.close();
    }
        
    public void decodeReceived(final String received){
        decodeFrame = new Thread() {
            @Override
            public void run() {
                receivedData = received.split(",");
                System.out.println("Received: " + received);
                System.out.println("The number of frames is: " + receivedData.length);
                    if(receivedData[1].equals("state")){
                        decodeState(receivedData);
                    }
                    else if(receivedData[1].equals("0")){
                        System.out.println("Connection Set with Quadrotor "+receivedData[0]);
                    }
                    else if(receivedData[1].equals("wys")){
                        System.out.println("Waypoint Set for Quadrotor "+receivedData[0]);
                    }
                    else if(receivedData[1].equals("mode")){
                        window.tf_currentflightmode.setText(receivedData[2]);
                        jTextAreaConsole.append("Quadrotor "+receivedData[0]+" mode changed to: "+receivedData[2]+'\n');
                    }
                System.out.println("timeellapsed: "+estimatedTime+" ms");               
            }
        };
        decodeFrame.start();
    }
    
    private void decodeState(String[] receivedFrame){
        System.out.println("Id: "+receivedFrame[0]);
        System.out.println("North: "+Float.valueOf(receivedFrame[2]));
        System.out.println("East: "+Float.valueOf(receivedFrame[3]));
        System.out.println("Elevation: "+Float.valueOf(receivedFrame[4]));
        System.out.println("Elevation: "+Float.valueOf(receivedFrame[5]));
        System.out.println("QuadBattery: "+Integer.parseInt(receivedFrame[6]));
        System.out.println("SmartphoneBattery: "+Integer.parseInt(receivedFrame[7]));
    }
    
    public void requestModeChange(String id, String mode) throws IOException{
        sendToServer(id+",mode,"+mode);
        receiveFromServer();
    }
    
    public void sendWaypoint(String id, int waypointnumber, float north, float east, float elevation, float yaw) throws IOException{
        sendToServer(id+",wy,"+waypointnumber+","+north+","+east+","+elevation+","+yaw);
        receiveFromServer();
    }
    
    public void requestQuadrotorState(String id) throws IOException{
        sendToServer(id+",state");
        receiveFromServer();
    }
    
    public void sendRCcommands(String id) throws IOException{
        sendToServer(id+",rc,commands");
    }
    
    public void sendRCwaitForState(String id) throws IOException{
        sendToServer(id+",rcstate,commands");
        receiveFromServer();
    }  
    
    private void startMission(){
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        mainThread = new TemporizerComm();
        timer.schedule(mainThread, 10, 10);

        t = 0; // inicia la simulación
    }
    
    Long t_pasado = System.nanoTime();
    private class TemporizerComm extends TimerTask {

        public void run() {

            long t_medido = System.nanoTime();
            float dt = ((float) (t_medido - t_pasado)) / 1000000000.0f; // [s].;
            t_pasado = t_medido;

            System.out.println("Tiempo de hilo = " + dt * 1000);
            t = t + Ts;
        }
    }
    
    

    
}
