/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package missioncontrolleruv;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import static missioncontrolleruv.MissionControllerUV.window;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * @author Asus
 */
public class ReadController {
    
    public static MissionGUI window = new MissionGUI();
    private ArrayList<Controller> foundControllers;
    
    public int xAxisPercentage = 0, yAxisPercentage = 0, zUDPercentage = 0, zLRPercentage = 0, zAxis = 0, hatSwitchPosition = 0;
    public boolean xIsPressed = false, yIsPressed = false, bIsPressed = false, aIsPressed = false;
    public boolean ltIsPressed = false, rtIsPressed = false, lbIsPressed = false, rbIsPressed = false;
    public boolean startIsPressed = false, backIsPressed = false, ljIsPressed = false, rjIsPressed = false;
    
    public ReadController(){
        
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
            
            // Go trough all components of the controller.
            Component[] components = controller.getComponents();
            for(int i=0; i < components.length; i++){
                
                Component component = components[i];
                Component.Identifier componentIdentifier = component.getIdentifier();
                
                // Buttons
                if(componentIdentifier.getName().matches("^[0-9]*$")){ // If the component identifier name contains only numbers, then this is a button.
                    // Is button pressed?
                    boolean isItPressed = true;
                    if(component.getPollData() == 0.0f){
                        isItPressed = false;
                    }
                    // Button index
                    String buttonIndex;
                    int buttonId;
                    buttonIndex = component.getIdentifier().toString();
                    buttonId = Integer.parseInt(buttonIndex);
                    
                    if(isItPressed){
                        switch (buttonId){
                            case 0: yIsPressed = true;
                                break;
                            case 1: bIsPressed = true;
                                break;
                            case 2: aIsPressed = true;
                                break;
                            case 3: xIsPressed = true;
                                break;
                            case 4: lbIsPressed = true;
                                break;
                            case 5: rbIsPressed = true;
                                break;
                            case 6: ltIsPressed = true;
                                break;
                            case 7: rtIsPressed = true;
                                break;
                            case 8: backIsPressed = true;
                                break;
                            case 9: startIsPressed = true;
                                break;
                            case 10: ljIsPressed = true;
                                break;
                            case 11: rjIsPressed = true;
                                break;
                        }
                    }
                    else{
                        switch (buttonId){
                            case 0: yIsPressed = false;
                                break;
                            case 1: bIsPressed = false;
                                break;
                            case 2: aIsPressed = false;
                                break;
                            case 3: xIsPressed = false;
                                break;
                            case 4: lbIsPressed = false;
                                break;
                            case 5: rbIsPressed = false;
                                break;
                            case 6: ltIsPressed = false;
                                break;
                            case 7: rtIsPressed = false;
                                break;
                            case 8: backIsPressed = false;
                                break;
                            case 9: startIsPressed = false;
                                break;
                            case 10: ljIsPressed = false;
                                break;
                            case 11: rjIsPressed = false;
                                break;
                        }
                    }
                        
                    /*
                    if(isItPressed){
                        switch (buttonId){
                            case 0: System.out.println("Button pressed: Y");
                                    window.jButtonControlY.doClick();
                                break;
                            case 1: System.out.println("Button pressed: B");
                                    window.jButtonControlB.doClick();
                                break;
                            case 2: System.out.println("Button pressed: A");
                                    window.jButtonControlA.doClick();
                                break;
                            case 3: System.out.println("Button pressed: X"); 
                                    window.jButtonControlX.doClick();
                                break;
                            case 4: System.out.println("Button pressed: LB");
                                    window.jButtonControlLB.doClick();
                                break;
                            case 5: System.out.println("Button pressed: RB");
                                    window.jButtonControlRB.doClick();
                                break;
                            case 6: System.out.println("Button pressed: LT");
                                    window.jButtonControlLT.doClick();
                                break;
                            case 7: System.out.println("Button pressed: RT");
                                    window.jButtonControlRT.doClick();
                                break;
                            case 8: System.out.println("Button pressed: BACK");
                                    window.jButtonControlBack.doClick();
                                break;
                            case 9: System.out.println("Button pressed: START");
                                    window.jButtonControlStart.doClick();
                                break;
                            case 10: System.out.println("Button pressed: LJ");
                                     window.jButtonControlLJ.doClick();
                                break;
                            case 11: System.out.println("Button pressed: RJ");
                                     window.jButtonControlRJ.doClick();
                                break;
                        }
                    }*/
                    // We know that this component was button so we can skip to next component.
                    continue;
                }
                
                // Hat switch
                if(componentIdentifier == Component.Identifier.Axis.POV){
                    hatSwitchPosition = (int)(1000*component.getPollData());
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
                        continue; // Go to next component.                        
                    }
                    // Y axis
                    if(componentIdentifier == Component.Identifier.Axis.Y){
                        yAxisPercentage = 100 - axisValueInPercentage;
                        if(yAxisPercentage >=49 && yAxisPercentage <=51){yAxisPercentage = 50;}
                        continue; // Go to next component.
                    }
                    // RZ axis
                    if(componentIdentifier == Component.Identifier.Axis.RZ){
                        zUDPercentage = 100 - axisValueInPercentage;
                        if(zUDPercentage >=49 && zUDPercentage <=51){zUDPercentage = 50;}
                        continue; // Go to next component.
                    }
                    // Z axis
                    if(componentIdentifier == Component.Identifier.Axis.Z){
                        if(zAxis == 0){
                            zLRPercentage = axisValueInPercentage;
                            if(zLRPercentage >=49 && zLRPercentage <=51){zLRPercentage = 50;}
                            
                            zAxis++;
                        }
                        else{
                            zAxis = 0;
                        }
                        continue; // Go to next component.
                    }
                  
                }
            }
            showControllerInWindow();
                      
            // We have to give processor some rest.
            try {
                Thread.sleep(25);
            } catch (InterruptedException ex) {
                Logger.getLogger(MissionControllerUV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void showControllerInWindow(){
        
        window.jProgressBarX.setValue(xAxisPercentage);
        window.jProgressBarY.setValue(yAxisPercentage);
        window.jProgressBarZ.setValue(zUDPercentage);
        window.jProgressBarZrot.setValue(zLRPercentage);
        
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
        
        if(yIsPressed)
            window.jButtonControlY.getModel().setPressed(true);
        else
            window.jButtonControlY.getModel().setPressed(false);
        if(xIsPressed)
            window.jButtonControlX.getModel().setPressed(true);
        else
            window.jButtonControlX.getModel().setPressed(false);
        if(aIsPressed)
            window.jButtonControlA.getModel().setPressed(true);
        else
            window.jButtonControlA.getModel().setPressed(false);
        if(bIsPressed)
            window.jButtonControlB.getModel().setPressed(true);
        else
            window.jButtonControlB.getModel().setPressed(false);
        if(startIsPressed)
            window.jButtonControlStart.getModel().setPressed(true);
        else
            window.jButtonControlStart.getModel().setPressed(false);
        if(backIsPressed)
            window.jButtonControlBack.getModel().setPressed(true);
        else
            window.jButtonControlBack.getModel().setPressed(false);
        if(ljIsPressed)
            window.jButtonControlLJ.getModel().setPressed(true);
        else
            window.jButtonControlLJ.getModel().setPressed(false);
        if(rjIsPressed)
            window.jButtonControlRJ.getModel().setPressed(true);
        else
            window.jButtonControlRJ.getModel().setPressed(false);
        if(lbIsPressed)
            window.jButtonControlLB.getModel().setPressed(true);
        else
            window.jButtonControlLB.getModel().setPressed(false);
        if(rbIsPressed)
            window.jButtonControlRB.getModel().setPressed(true);
        else
            window.jButtonControlRB.getModel().setPressed(false);
        if(ltIsPressed)
            window.jButtonControlLT.getModel().setPressed(true);
        else
            window.jButtonControlLT.getModel().setPressed(false);
        if(rtIsPressed)
            window.jButtonControlRT.getModel().setPressed(true);
        else
            window.jButtonControlRT.getModel().setPressed(false);
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
