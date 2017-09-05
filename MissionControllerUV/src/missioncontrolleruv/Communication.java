/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package missioncontrolleruv;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro Astudillo
 */
public class Communication {
    
    public static MissionGUI window = MissionControllerUV.window;
    
    public Communication(){
        
        try {
            getIPaddress();
        } catch (SocketException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
        IP();
        
    }
    
    public void createHotspot(){
        
    }
    
    public void dataTrade(){
        
    }
    
    public void getIPaddress() throws SocketException, UnknownHostException{
        System.out.println("Your Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
        for (; n.hasMoreElements();)
        {
            NetworkInterface e = n.nextElement();

            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements();)
            {
                InetAddress addr = a.nextElement();
                System.out.println("  " + addr.getHostAddress());
            }
        }
    }
    
    public void IP(){
        try {
          InetAddress inet = InetAddress.getLocalHost();
          InetAddress[] ips = InetAddress.getAllByName(inet.getCanonicalHostName());
          if (ips  != null ) {
            for (int i = 0; i < ips.length; i++) {
              System.out.println(ips[i]);
            }
          }
        } catch (UnknownHostException e) {

        }
    }
    
}
