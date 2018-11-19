package agent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import core.Starter;
import core.Starter;
import gui.Battlefield;
import gui.GroundPanel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;

/**
 *
 * @author 5966868
 */
public class CommanderAgent extends Agent {

    private final int INTERVAL = 1000;
    
    private HashMap<String, Point> playersPositions;
    private HashMap<String, Color> playersColors;
    
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
    
}
