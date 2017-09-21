/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package missioncontrolleruv;
/**
 *
 * @author Alejandro Astudillo
 */
public class MissionControllerUV {

    /**
     * @param args the command line arguments
     */
    
    public static boolean controllerTest = false;
    
    public static void main(String[] args) {
        // TODO code application logic here
        MissionControllerUV missionControllerUV = new MissionControllerUV();
    }
    
    public static MissionGUI window = new MissionGUI();
    public static ReadController readController = new ReadController();
    public static Communication communication = new Communication();
   
    public MissionControllerUV () {      
        //constructor
        
    }

}
