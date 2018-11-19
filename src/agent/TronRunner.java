package agent;

import gui.Battlefield;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TronRunner extends Agent {
    public Color cor;
    Battlefield battlefield;
    private Color color;
    private Point2D position;
    
    private String nextMove = null;
    
//    public TronRunner(Battlefield battlefield, Color cor) {
//        this.cor = cor;
//        this.battlefield = battlefield;        
//    }
    
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
}
