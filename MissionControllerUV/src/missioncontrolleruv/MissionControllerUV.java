/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package missioncontrolleruv;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * @author Asus
 */
public class MissionControllerUV {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MissionControllerUV missionControllerUV = new MissionControllerUV();
    }
    
    public static MissionGUI window = new MissionGUI();
    public static ReadController readController = new ReadController();
   
    public MissionControllerUV () {      
        
        
    }

}
