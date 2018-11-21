package agent;

import core.MetaData;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TronRunner extends Agent {
    protected int[][] battlefield;
    protected Dimension fieldDimension;
    protected Map<AID, Point> positions;
            
    private AID commander;
    
    @Override
    protected void setup() {
        
        DFAgentDescription description = new DFAgentDescription();
        description.setName(getAID());
        
        ServiceDescription service = new ServiceDescription();
        service.setType("Runner");
        service.setName(getLocalName());
        
        description.addServices(service);
        
        try {
            
            DFService.register(this, description);
            
        } catch (FIPAException ex) {
            Logger.getLogger(TronRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();
        
        sequentialBehaviour.addSubBehaviour(
            new ReceiverBehaviour(this, -1, new MessageTemplate((ACLMessage m) -> {
                AID sender = m.getSender();
                String performative = ACLMessage.getPerformative(m.getPerformative());

                System.out.println("---------");
                System.out.println("Received message (" + getLocalName() + "):");
                System.out.println("Performative: " + performative);
                System.out.println("Content: " + m.getContent());
                System.out.println("Sender: " + sender.getName());
                System.out.println("---------");

                if (
                        performative.equals("PROPOSE")
                    &&  m.getContent().equals("join battle")){
                    commander = sender;                    
                    return true;
                }
                return false;
            }))
        );
        sequentialBehaviour.addSubBehaviour(
            new OneShotBehaviour() {
                @Override public void action() {
                    try {
                        DFService.deregister(TronRunner.this);
                        
                        ACLMessage reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                        reply.setContent("joined battle");
                        reply.addReceiver(commander);
                        TronRunner.this.send(reply);
                        
                        System.out.println("Ready to rock!");
                        
                    } catch (FIPAException ex) {
                        Logger.getLogger(TronRunner.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        );
        sequentialBehaviour.addSubBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage update = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                try {
                    MetaData md = (MetaData) update.getContentObject();                    
                    battlefield = md.battlefield;
                    fieldDimension = md.battlefieldDimension;
                    positions = md.positions;
                    
                    System.out.println("Updated battlefield");
                } catch (UnreadableException ex) {
                    Logger.getLogger(TronRunner.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                String nextMove = getNextMove();
                ACLMessage move = new ACLMessage(ACLMessage.INFORM);
                move.setContent(nextMove);
                move.addReceiver(commander);
                send(move);
            }
        });
        
        this.addBehaviour(sequentialBehaviour);
        
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {                
                ACLMessage message = receive(MessageTemplate.MatchOntology(WallAgent.KILL_MESSAGE));
                if(message != null){// && message.getContent().equals(WallAgent.KILL_MESSAGE)) {
                    printBattlefield();
                    takeDown();                    
                }
            }
        });
    }
    
    protected String getNextMove() {
        Point p = positions.get(this.getAID());
        String move = "";
        int x, y, tries = 0;
        do {
            x = p.x;
            y = p.y;
            int number = 1 + (int)(Math.random() * 4);
            switch(number) {
                case 1:
                    y++;
                    move = "R";
                break;
                case 2:
                    y--;
                    move = "L";
                break;
                case 3:
                    x--;
                    move = "T";
                break;
                case 4:
                    x++;
                    move = "B";
                break;
            }
            tries++;
        } while((hasComponent(x, y) || isOutOfBound(x, y)) && tries <= 8);
        return move;
    }
    
    private boolean isOutOfBound(int x, int y) {
        if(x < 0 || x >= fieldDimension.width)
            return true;
        if(y < 0 || y >= fieldDimension.height)
            return true;
        return false;
    }
    
    private boolean hasComponent(int x, int y) {
        return battlefield[x][y] == CommanderAgent.WALL_CODE || battlefield[x][y] == CommanderAgent.PLAYER_CODE;
    }
    
    public void printBattlefield() {
        String str = "";
        for (int i = 0; i < fieldDimension.width; i++) {
            for (int j = 0; j < fieldDimension.height; j++) {
                str += battlefield[i][j];
            }                        
            str += "\n";
        }
        System.out.println(str);
    }
}
