package core;

import agent.TronRunner;
import gui.Battlefield;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import java.awt.Color;

public class Starter {
    public static Battlefield BATTLEFIELD;
    
    public static void main(String[] args) throws ControllerException, InterruptedException {
        Runtime rt = Runtime.instance();
        ProfileImpl p = new ProfileImpl();
        ContainerController cc = rt.createMainContainer(p);
        
        BATTLEFIELD = new Battlefield();
        BATTLEFIELD.setVisible(true);
                
        AgentController commander = cc.createNewAgent("Hamilton", "CommanderAgent", args);
        commander.start();
//        
//        TronRunner runnerA = new TronRunner(BATTLEFIELD, Color.RED);
//        TronRunner runnerB = new TronRunner(BATTLEFIELD, Color.BLUE);
//        
//        BATTLEFIELD.putAgent(runnerA, 1);
//        BATTLEFIELD.putAgent(runnerB, 2);
    }
}
