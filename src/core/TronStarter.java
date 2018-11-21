package core;


import gui.Battlefield;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import java.awt.Color;


public class TronStarter {
    public static void main(String[] args) throws StaleProxyException, InterruptedException {
        
        Runtime jadeRuntime = jade.core.Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        
        AgentContainer container = jadeRuntime.createMainContainer(profile);
        
        container.createNewAgent("p1", "agent.TronRunner", null).start();
        container.createNewAgent("p2", "agent.TronRunner", null).start();
        container.createNewAgent("p3", "agent.TronRunner", null).start();
        container.createNewAgent("p4", "agent.TronRunner", null).start();
        
        
//        container.createNewAgent("rma", "jade.tools.rma.rma", null).start();
        
        Thread.sleep(1000);        
        container.createNewAgent("commanderAgent", "agent.CommanderAgent", null).start();
    }
}
