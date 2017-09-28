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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import missioncontrolleruv1.MissionControllerUV;
import missioncontrolleruv1.MissionGUI;
import missioncontrolleruv1.ReadController;

/**
 *
 * @author Alejandro Astudillo
 */
public class Communication {
    
    public static MissionGUI window = MissionControllerUV.window; 
    public static ReadController readController = MissionControllerUV.readController;
    Timer timer;
    TemporizerComm mainThread;
    double t;
    float Ts = (float) 0.01;
    public double quad_east, quad_north, quad_elevation, quad_roll, quad_pitch, quad_yaw;
    public int quad_battery, quad_smartphoneBat;
    
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
    
    public boolean connected = false;
    
    public Communication(){
        jTextAreaConsole = window.jTextAreaConsole;        
    }
    
    public void startConnection(String id){
        ip_1 = window.jTF_ip1.getText();
        try {
            clientSocket = new Socket(ip_1, 6789); //192.168.0.18
            sendToServer(id+",0");
            receiveFromServer();
            Thread.sleep(2000);
            if(connected){
                System.out.println("Connection Set with IP: "+ip_1);
                startSendingRC();
            }
            //requestQuadrotorState("1");
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
        connected = false;
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
                        connected = true;
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
        quad_north = Double.parseDouble(receivedFrame[2]);
        quad_east = Double.parseDouble(receivedFrame[3]);
        quad_elevation = Double.parseDouble(receivedFrame[4]);
        quad_roll = Double.parseDouble(receivedFrame[5]);
        quad_pitch = Double.parseDouble(receivedFrame[6]);
        quad_yaw = Double.parseDouble(receivedFrame[7]);
        quad_battery = Integer.parseInt(receivedFrame[8]);
        quad_smartphoneBat = Integer.parseInt(receivedFrame[9]);
        System.out.println("Id: "+receivedFrame[0]);
        System.out.println("North: "+quad_north);
        System.out.println("East: "+quad_east);
        System.out.println("Elevation: "+quad_elevation);
        System.out.println("Roll: "+quad_roll);
        System.out.println("Pitch: "+quad_pitch);
        System.out.println("Yaw: "+quad_yaw);
        System.out.println("QuadBattery: "+quad_battery);
        System.out.println("SmartphoneBattery: "+quad_smartphoneBat);
        window.tv_northQuad.setText(String.valueOf(quad_north));
        window.tv_eastQuad.setText(String.valueOf(quad_east));
        window.tv_elevationQuad.setText(String.valueOf(quad_elevation));
        window.tv_rollQuad.setText(String.valueOf(quad_roll));
        window.tv_pitchQuad.setText(String.valueOf(quad_pitch));
        window.tv_yawQuad.setText(String.valueOf(quad_yaw));
    }
    
    public void requestModeChange(String id, String mode) throws IOException{
        sendToServer(id+",mode,"+mode);
        receiveFromServer();
    }
    
    public void sendWaypoint(String id, int waypointnumber, float north, float east, float elevation, float yaw) throws IOException{
        sendToServer(id+",wy,"+waypointnumber+","+north+","+east+","+elevation+","+yaw);
        receiveFromServer();
    }
    
    public void sendWaypointList(List<double[]> Waypoints, float elev, float yaw) throws IOException{
        float north_coord, east_coord;
        for(int i=0; i<=(Waypoints.size()-1); i++){
            north_coord = 0;
            east_coord = 0;
            sendWaypoint("1", i+1, north_coord, east_coord, elev, yaw);
        }
        //sendWaypoint("1", 1, 865125.540f, 1060712.219f, 971.418f, 0f);
    }
    
    public void requestQuadrotorState(String id) throws IOException{
        sendToServer(id+",state");
        receiveFromServer();
    }
    
    public void sendRCcommands(String id) throws IOException{
        
        sendToServer(id+",rc,"+readController.getRollJoystick()+","+readController.getPitchJoystick()+","+readController.getYawJoystick()+","+
                readController.getThrottleJoystick()+","+readController.getDPadPosition()+","+readController.getXbutton()+","+
                readController.getYbutton()+","+readController.getAbutton()+","+readController.getBbutton()+","+
                readController.getSTARTbutton()+","+readController.getBACKbutton()+","+readController.getLTbutton()+","+
                readController.getRTbutton()+","+readController.getLBbutton()+","+readController.getRBbutton()+","+
                readController.getLJbutton()+","+readController.getRJbutton());
        
        /*System.out.println(id+",rc,"+readController.getRollJoystick()+","+readController.getPitchJoystick()+","+readController.getYawJoystick()+","+
                readController.getThrottleJoystick()+","+readController.getDPadPosition()+","+readController.getXbutton()+","+
                readController.getYbutton()+","+readController.getAbutton()+","+readController.getBbutton()+","+
                readController.getSTARTbutton()+","+readController.getBACKbutton()+","+readController.getLTbutton()+","+
                readController.getRTbutton()+","+readController.getLBbutton()+","+readController.getRBbutton()+","+
                readController.getLJbutton()+","+readController.getRJbutton());*/
    }
    
    public void sendRCwaitForState(String id) throws IOException{
        sendToServer(id+",rcstate,"+readController.getRollJoystick()+","+readController.getPitchJoystick()+","+readController.getYawJoystick()+","+
                readController.getThrottleJoystick()+","+readController.getDPadPosition()+","+readController.getXbutton()+","+
                readController.getYbutton()+","+readController.getAbutton()+","+readController.getBbutton()+","+
                readController.getSTARTbutton()+","+readController.getBACKbutton()+","+readController.getLTbutton()+","+
                readController.getRTbutton()+","+readController.getLBbutton()+","+readController.getRBbutton()+","+
                readController.getLJbutton()+","+readController.getRJbutton());
        receiveFromServer();
    }  
    
    public void startSendingRC() throws IOException{
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        mainThread = new TemporizerComm();
        timer.schedule(mainThread, 10, 100);

        t = 0; // inicia la simulación
    }
    int j = 0;
    Long t_pasado = System.nanoTime();
    private class TemporizerComm extends TimerTask {

        public void run()  {
            j++;
            long t_medido = System.nanoTime();
            float dt = ((float) (t_medido - t_pasado)) / 1000000000.0f; // [s].;
            t_pasado = t_medido;
            try {
                if(j<=10){
                    sendRCcommands("1");
                }
                else {
                    sendRCwaitForState("1");
                }
            } catch (IOException ex) {
                Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
            }

            //System.out.println("Tiempo de hilo = " + dt * 1000);
            t = t + Ts;
        }
    }
    
    

    
}
