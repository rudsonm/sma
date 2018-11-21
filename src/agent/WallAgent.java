package agent;

import gui.GroundPanel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.awt.Color;

public class WallAgent extends Agent {
    public static final Color COLOR = Color.MAGENTA;
    public static final String KILL_MESSAGE = "Die";
    private GroundPanel panel;
    @Override
    protected void setup() {        
        Object[] args = getArguments();
        this.panel = (GroundPanel) args[0];
        
        addBehaviour(new TickerBehaviour(WallAgent.this, 500) {
            @Override
            protected void onTick() {
                if(panel.hasTronRunner()) {
                    AID runner = panel.getTronRunner();
                    ACLMessage message = new ACLMessage(ACLMessage.REQUEST);                    
                    message.setContent(WallAgent.KILL_MESSAGE);
                    message.addReceiver(runner);
                    WallAgent.this.send(message);
                    
                    panel.setBackground(WallAgent.COLOR);
                }
            }
        });
    }
    
}