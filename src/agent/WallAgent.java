package agent;

import gui.GroundPanel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.awt.Color;

public class WallAgent extends Agent {
    public static final Color COLOR = Color.MAGENTA;
    private GroundPanel panel;
    
    @Override
    protected void setup() {
        System.out.println(this.getAID().getName() + " estou pronto para aniquilar voce!");
        Object[] args = getArguments();
        
        this.panel = (GroundPanel) args[0];
//        panel.setBackground(WallAgent.COLOR);
        
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if(panel.hasTronRunner()) {
                    AID runner = panel.getTronRunner();
                    ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                    message.setContent("Die");
                    message.addReceiver(runner);
                    WallAgent.this.send(message);
                    
                    panel.setBackground(WallAgent.COLOR);
                }
            }
        });
    }
    
}