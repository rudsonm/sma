/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import gui.Battlefield;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.io.Serializable;
import javax.swing.JFrame;

/**
 *
 * @author 5966868
 */
public class CommanderAgent extends Agent {

    private final int INTERVAL = 1000;
    
    private Battlefield battlefield;
    
    public CommanderAgent(Battlefield battlefield) {
        this.battlefield = battlefield;
    }
    
    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Action");
                
            }
        });
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
                String direction = message.getContent();                               
            }
        });
    }
    
}
