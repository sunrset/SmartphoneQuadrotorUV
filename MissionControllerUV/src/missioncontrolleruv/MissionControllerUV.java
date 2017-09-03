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
    private ArrayList<Controller> foundControllers;
    private JLabel jDPad;
    
    public MissionControllerUV () {      
        
        window.jLabelDPad.setText("");
        foundControllers = new ArrayList<>();
        searchForControllers();
        
        // If at least one controller was found we start showing controller data on window.
        if(!foundControllers.isEmpty())
            startShowingControllerData();
        else
            window.addControllerName("No controller found!");
        
        
    }
    
    private void searchForControllers() {
        /**
        * Search (and save) for controllers of type Controller.Type.STICK,
        * Controller.Type.GAMEPAD, Controller.Type.WHEEL and Controller.Type.FINGERSTICK.
        */
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(int i = 0; i < controllers.length; i++){
            Controller controller = controllers[i];
            
            if (
                    controller.getType() == Controller.Type.STICK || 
                    controller.getType() == Controller.Type.GAMEPAD || 
                    controller.getType() == Controller.Type.WHEEL ||
                    controller.getType() == Controller.Type.FINGERSTICK
               )
            {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);
                
                // Add new controller to the list on the window.
                window.addControllerName(controller.getName() + " - " + controller.getType().toString() + " type");
            }
        }
    }
    
    private void startShowingControllerData(){
     /**
     * Starts showing controller data on the window.
     */
        while(true)
        {
            // Currently selected controller.
            int selectedControllerIndex = window.getSelectedControllerName();
            Controller controller = foundControllers.get(selectedControllerIndex);

            // Pull controller for current data, and break while loop if controller is disconnected.
            if( !controller.poll() ){
                window.showControllerDisconnected();
                break;
            }
            
            // X axis and Y axis
            int xAxisPercentage = 0;
            int yAxisPercentage = 0;
            int zUDPercentage = 0;
            int zLRPercentage = 0;
            int zAxis = 0;
           
                    
            // Go trough all components of the controller.
            Component[] components = controller.getComponents();
            for(int i=0; i < components.length; i++)
            {
                Component component = components[i];
                Component.Identifier componentIdentifier = component.getIdentifier();
                
                // Buttons
                //if(component.getName().contains("Button")){ // If the language is not english, this won't work.
                if(componentIdentifier.getName().matches("^[0-9]*$")){ // If the component identifier name contains only numbers, then this is a button.
                    // Is button pressed?
                    boolean isItPressed = true;
                    if(component.getPollData() == 0.0f)
                        isItPressed = false;
                    
                    // Button index
                    String buttonIndex;
                    int buttonId;
                    buttonIndex = component.getIdentifier().toString();
                    buttonId = Integer.parseInt(buttonIndex);
                    
                    if(isItPressed){
                        switch (buttonId){
                            case 0: System.out.println("Button pressed: Y");
                                break;
                            case 1: System.out.println("Button pressed: B");
                                break;
                            case 2: System.out.println("Button pressed: A");
                                break;
                            case 3: System.out.println("Button pressed: X");
                                break;
                            case 4: System.out.println("Button pressed: LB");
                                break;
                            case 5: System.out.println("Button pressed: RB");
                                break;
                            case 6: System.out.println("Button pressed: LT");
                                break;
                            case 7: System.out.println("Button pressed: RT");
                                break;
                            case 8: System.out.println("Button pressed: BACK");
                                break;
                            case 9: System.out.println("Button pressed: START");
                                break;
                            case 10: System.out.println("Button pressed: LJ");
                                break;
                            case 11: System.out.println("Button pressed: RJ");
                                break;
                        }
                    }
                    // We know that this component was button so we can skip to next component.
                    continue;
                }
                
                // Hat switch
                if(componentIdentifier == Component.Identifier.Axis.POV){
                    int hatSwitchPosition = (int)(1000*component.getPollData());
                    switch (hatSwitchPosition){
                        case 125: window.jLabelDPad.setText("NW");
                            break;
                        case 250: window.jLabelDPad.setText("N");
                            break;
                        case 375: window.jLabelDPad.setText("NE");
                            break;
                        case 500: window.jLabelDPad.setText("E");
                            break;
                        case 625: window.jLabelDPad.setText("SE");
                            break;
                        case 750: window.jLabelDPad.setText("S");
                            break;
                        case 875: window.jLabelDPad.setText("SW");
                            break;
                        case 1000: window.jLabelDPad.setText("W");
                            break;
                        default:   window.jLabelDPad.setText("Not pressed");
                            break;
                    }
                    //System.out.println("Hat switch position: "+hatSwitchPosition);
                    
                    // We know that this component was hat switch so we can skip to next component.
                    continue;
                }
                
                // Axes
                if(component.isAnalog()){
                    float axisValue = component.getPollData();
                    int axisValueInPercentage = getAxisValueInPercentage(axisValue);
                    
                    // X axis
                    if(componentIdentifier == Component.Identifier.Axis.X){
                        xAxisPercentage = axisValueInPercentage;
                        if(xAxisPercentage >=49 && xAxisPercentage <=51){xAxisPercentage = 50;}
                        //System.out.println("xAxisPercentage: "+xAxisPercentage);
                        window.jProgressBarX.setValue(xAxisPercentage);
                        continue; // Go to next component.                        
                    }
                    // Y axis
                    if(componentIdentifier == Component.Identifier.Axis.Y){
                        yAxisPercentage = 100 - axisValueInPercentage;
                        if(yAxisPercentage >=49 && yAxisPercentage <=51){yAxisPercentage = 50;}
                        //System.out.println("yAxisPercentage: "+yAxisPercentage);
                        window.jProgressBarY.setValue(yAxisPercentage);
                        continue; // Go to next component.
                    }
                    // RZ axis
                    if(componentIdentifier == Component.Identifier.Axis.RZ){
                        zUDPercentage = 100 - axisValueInPercentage;
                        if(zUDPercentage >=49 && zUDPercentage <=51){zUDPercentage = 50;}
                        //System.out.println("zUDPercentage: "+zUDPercentage);
                        window.jProgressBarZ.setValue(zUDPercentage);
                        continue; // Go to next component.
                    }
                    // Z axis
                    if(componentIdentifier == Component.Identifier.Axis.Z){
                        if(zAxis == 0){
                            zLRPercentage = axisValueInPercentage;
                            if(zLRPercentage >=49 && zLRPercentage <=51){zLRPercentage = 50;}
                            //System.out.println("zLRAxis: "+zLRPercentage);
                            window.jProgressBarZrot.setValue(zLRPercentage);
                            
                            zAxis++;
                        }
                        else{
                            zAxis = 0;
                        }
                        continue; // Go to next component.
                    }
                  
                }
            }
                      
            // We have to give processor some rest.
            try {
                Thread.sleep(25);
            } catch (InterruptedException ex) {
                Logger.getLogger(MissionControllerUV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getAxisValueInPercentage(float axisValue){
     /**
     * Given value of axis in percentage.
     * Percentages increases from left/top to right/bottom.
     * If idle (in centre) returns 50, if joystick axis is pushed to the left/top 
     * edge returns 0 and if it's pushed to the right/bottom returns 100.
     * 
     * @return value of axis in percentage.
     */
        return (int)(((2 - (1 - axisValue)) * 100) / 2);
    }
}
