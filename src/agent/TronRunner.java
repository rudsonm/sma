package agent;

import gui.Battlefield;
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
import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TronRunner extends Agent {
    protected Battlefield battlefield;
    public Color color;
    private Point2D position;
    
    private String nextMove = null;
    
//    public TronRunner(Battlefield battlefield, Color color) {
//        this.color = color;
//        this.battlefield = battlefield;        
//    }
    
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
                    TronRunner.this.battlefield = (Battlefield) update.getContentObject();
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
        
//        sequentialBehaviour.addSubBehaviour(
//            new OneShotBehaviour() {
//                @Override public void action() {
//                    System.out.println("Joining battle (" + getLocalName() + "):");
//                    String nextMove = getNextMove();
//                    System.out.println("Started execution of TronRunner");
//                    System.out.println("Battlefield: " + commander.getLocalName());
//                }
//            }
//        );
        
        this.addBehaviour(sequentialBehaviour);
        
//        addBehaviour(new );
//        addBehaviour(new CyclicBehaviour() {
//            @Override
//            public void action() {
//                ACLMessage message = TronRunner.this.receive();
//                if(message != null) {                    
//                    // recebe mensagem de atualizacao
//                }
//                ACLMessage reply = message.createReply();
//                reply.addReceiver(new AID("Commander", AID.ISLOCALNAME));
//                reply.setOntology("Action");
//                reply.setContent(TronRunner.this.getNextMove()
//                );
//                TronRunner.this.send(message);
//            }
//        });
    }
    
    private String getNextMove() {
        return "R";
    }
    
    public void die() {
        this.takeDown();
    }
}
