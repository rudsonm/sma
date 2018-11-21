package agent;

import gui.Battlefield;
import gui.GroundPanel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ContainerController;
import java.awt.Color;
import java.awt.Component;
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
    private final int nRunners = 4;
    private int nWallAgents = 0;
    
    private HashMap<AID, Point> playersPositions = new HashMap<>();
    private HashMap<AID, Color> playersColors = new HashMap<>();
    
    private final Scanner scanner = new Scanner(System.in);
    
    private final List<AID> runners = new LinkedList<>();
    
    private Battlefield battlefield = new Battlefield();

    public CommanderAgent() {
    }
    
    @Override
    protected void setup() {
        try {                        
            createWallAgents();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
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
                    
                    int team = 1;
                    for(AID runner : chosenRunners)
                        CommanderAgent.this.placeAgentInBattlefield(runner, team++);
                    
                    CommanderAgent.this.battlefield.setVisible(true);
                } catch (FIPAException ex) {
                    Logger.getLogger(CommanderAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        // BATTLE BEHAVIOUR
        sequentialBehaviour.addSubBehaviour(new TickerBehaviour(this, INTERVAL) {
            @Override protected void onTick() {
                
                ACLMessage battlefieldUpdate = new ACLMessage(ACLMessage.INFORM);
                try {
                    battlefieldUpdate.setContentObject(battlefield);
                } catch (IOException ex) {
//                    Logger.getLogger(CommanderAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (AID runner : runners) {
                    battlefieldUpdate.addReceiver(runner);
                }
                CommanderAgent.this.send(battlefieldUpdate);
                
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
                
                try {
                    for (Map.Entry<AID, String> move : moves.entrySet()) {
                        if(move.getValue() != null)
                            moveRunner(move.getKey(), move.getValue());
                        System.out.println("Move of " + move.getKey().getLocalName() + " is " + move.getValue());
                    }
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
                
            }
        });
        
        this.addBehaviour(sequentialBehaviour);
    }
    
    protected void moveRunner(AID runner, String direction) throws Exception {
        Point point = playersPositions.get(runner);
        GroundPanel panel = (GroundPanel) battlefield.getComponentAt(point);
        panel.removeTronRunner();
        panel.setBackground(WallAgent.COLOR);
        panel.repaint();
        Object[] args = new Object[1];
        args[0] = panel;
        this.getContainerController().createNewAgent(getNextWallAgentName(), "agent.WallAgent", args).start();

        switch(direction) {
            case "R": point.x++; break;
            case "L": point.x--; break;
            case "B": point.y++; break;
            case "T": point.y--; break;
        }
        
        Color playerColor = playersColors.get(runner);
        panel = (GroundPanel) battlefield.getComponentAt(point);
        panel.setBackground(playerColor);        
        panel.setTronRunner(runner);
        panel.repaint();
        playersPositions.replace(runner, point);
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
    
    private void createWallAgents() throws Exception {
        int lines = this.battlefield.LINES;
        int columns = this.battlefield.COLUMNS;
        for (int i = 0; i < lines; i++) {
            Object[] args = new Object[1];
            Component c = this.battlefield.getComponentAt(i, 0);
            c.setBackground(WallAgent.COLOR);
            args[0] = c;
            this.getContainerController().createNewAgent(getNextWallAgentName(), "agent.WallAgent", args).start();
            
            c = this.battlefield.getComponentAt(i, columns - 1);
            c.setBackground(WallAgent.COLOR);            
            args[0] = c;
            this.getContainerController().createNewAgent(getNextWallAgentName(), "agent.WallAgent", args).start();
        }
        
        for (int i = 0; i < columns; i++) {
            Object[] args = new Object[1];
            Component c = this.battlefield.getComponentAt(0, i);
            c.setBackground(WallAgent.COLOR);
            args[0] = c;
            this.getContainerController().createNewAgent(getNextWallAgentName(), "agent.WallAgent", args).start();
                        
            c = this.battlefield.getComponentAt(lines - 1, i);
            c.setBackground(WallAgent.COLOR);
            args[0] = c;
            this.getContainerController().createNewAgent(getNextWallAgentName(), "agent.WallAgent", args).start();
        }
    }
    
    private void placeAgentInBattlefield(AID agent, int team) {
        Point point = null;
        switch(team) {
            case 1:
                point = new Point(2, battlefield.LINES / 2);
            break;
            case 2:
                point = new Point(battlefield.COLUMNS - 3, battlefield.LINES / 2);
            break;
            case 3:
                point = new Point(battlefield.COLUMNS / 2, 2);
            break;
            case 4:
                point = new Point(battlefield.COLUMNS / 2, battlefield.LINES - 3);
            break;
            default:
                point = new Point(battlefield.COLUMNS / 2, battlefield.LINES / 2);
            break;
        }
        Color[] colors = { Color.CYAN, Color.GREEN, Color.RED, Color.ORANGE };
        Color color = colors[team - 1];
        playersPositions.put(agent, point);
        playersColors.put(agent, color);
        battlefield.putAgent(color, point);
    }
    
    private String getNextWallAgentName() {
        return "WallAgent" + (this.nWallAgents++);
    }
    
}