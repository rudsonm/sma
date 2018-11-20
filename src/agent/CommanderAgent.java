package agent;

import core.Starter;
import core.Starter;
import gui.Battlefield;
import gui.GroundPanel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    private final int nRunners = 2;
    
    private HashMap<String, Point> playersPositions;
    private HashMap<String, Color> playersColors;
    
    private final Scanner scanner = new Scanner(System.in);
    
    private final List<AID> runners = new LinkedList<>();
    
    @Override
    protected void setup() {
        super.setup();
        
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour(this);
        
        // SETUP BEHAVIOUR
        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {                
                try {
                    Map<String, AID> runners = lookForRunners();

                    System.out.println("Available Runners:");
                    for (String runner : runners.keySet()) {
                        System.out.println(runner);
                    }

                    System.out.println("Choose the " + nRunners + " runners:");

                    AID[] chosenRunners = new AID[nRunners];
                    for (int i = 0; i < nRunners; ++i){
                        chosenRunners[i] = runners.get(scanner.next());
                    }


                    ACLMessage m = new ACLMessage(ACLMessage.PROPOSE);
                    m.setContent("join battle");

                    for (AID runner : chosenRunners) {
                        m.addReceiver(runner);
                    }

                    System.out.println("Proposing: " + m.getContent());

                    CommanderAgent.this.send(m);

                    int n = 0;
                    while (n < nRunners){
                        ACLMessage reply = CommanderAgent.this.blockingReceive();
                        if (reply == null) continue;
                        if (
                                reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL
                            &&  reply.getContent().equals("joined battle")
                        ){
                            System.out.println("Propose accepted: " + reply.getSender().getLocalName());
                            ++n;
                            CommanderAgent.this.runners.add(reply.getSender());
                        }
                    }

                } catch (FIPAException ex) {
                    Logger.getLogger(CommanderAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // BATTLE BEHAVIOUR
        sequentialBehaviour.addSubBehaviour(new TickerBehaviour(this, 100) {
            @Override protected void onTick() {
                
                ACLMessage requestMove = new ACLMessage(ACLMessage.REQUEST);
                for (AID runner : runners) {
                    requestMove.addReceiver(runner);
                }
                send(requestMove);
                System.out.println("Requested Movement");
                
                Map<AID, String> moves = new HashMap<>(nRunners);
                boolean received = false;
                while (!received){
                    ACLMessage move = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                    
                    if (move == null) continue;
                    
                    moves.put(move.getSender(), move.getContent());
                    
                    received = true;
                    for (AID runner : runners) {
                        if (moves.get(runner) == null){
                            received = false;
                        }
                    }
                }
                
                for (Map.Entry<AID, String> move : moves.entrySet()) {
                    System.out.println("Move of " + move.getKey().getLocalName() + " is " + move.getValue());
                }
                // do something with movement
                
            }
        });
        
        this.addBehaviour(sequentialBehaviour);
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
