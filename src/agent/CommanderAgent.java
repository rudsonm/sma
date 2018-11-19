package agent;

import core.Starter;
import core.Starter;
import gui.Battlefield;
import gui.GroundPanel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 5966868
 */
public class CommanderAgent extends Agent {

    private final int INTERVAL = 1000;
    
    private HashMap<String, Point> playersPositions;
    private HashMap<String, Color> playersColors;
    
    
    private final DFAgentDescription description = new DFAgentDescription();
    
    private final Scanner scanner = new Scanner(System.in);
    
    @Override
    protected void setup() {
        super.setup();
        description.setName(getAID());
        
        try {
            
//            DFService.register(this, this.description);
            Map<String, AID> runners = lookForRunners();
            
            System.out.println("Available Runners:");
            for (String runner : runners.keySet()) {
                System.out.println(runner);
            }
            
            int nRunners = 2;
            System.out.println("Choose the " + nRunners + " runners:");
            
            AID[] chosenRunners = new AID[nRunners];
            for (int i = 0; i < nRunners; ++i){
                chosenRunners[i] = runners.get(scanner.next());
                DFService.deregister(this, chosenRunners[i]);
            }
            
            for (AID runner : chosenRunners) {
                jade.domain.introspection.ACLMessage message = new jade.domain.introspection.ACLMessage();
                message.setPayload("Join Battlefield");
                
                // TODO: send message
                
            }
            
            
            
        } catch (FIPAException ex) {
            Logger.getLogger(CommanderAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void getActions(final ServiceDescription sd) {
        this.addBehaviour(new TickerBehaviour(this, INTERVAL) {
            @Override
            protected void onTick() {
                ACLMessage message = myAgent.receive();
                String type = message.getOntology();
                if(type != "Action")
                    return;
                
                AID sender = message.getSender();
                String senderName = sender.getLocalName();
                String direction = message.getContent();
                
                Point point = playersPositions.get(senderName);
                GroundPanel panel = (GroundPanel) Starter.BATTLEFIELD.getComponentAt(point);
                panel.setWallAgent(new AssassinStaticWall(panel));
                
                switch(direction) {
                    case "R":
                        point.x++;
                    break;
                    case "L":
                        point.x--;
                    break;
                    case "B":
                        point.y++;
                    break;
                    case "T":
                        point.y--;
                    break;                    
                }
                
                Color playerColor = playersColors.get(senderName);
                Starter.BATTLEFIELD.getComponentAt(point).setBackground(playerColor);
                playersPositions.replace(senderName, point);
            }
        });
    }
    
    

    private Map<String, AID> lookForRunners() throws FIPAException{
        DFAgentDescription runnerDescription = new DFAgentDescription();
        ServiceDescription runnerService = new ServiceDescription();
        runnerService.setType("Runner");
        runnerDescription.addServices(runnerService);
                
        DFAgentDescription[] result = DFService.search(this, runnerDescription);
        
        Map<String, AID> runners = new HashMap<>();
        
        for (DFAgentDescription desc : result) {
            runners.put(desc.getName().getLocalName(), desc.getName());
        }
        
        return runners;
    }
    
}
