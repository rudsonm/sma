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
import jade.lang.acl.ACLMessage;
import java.awt.Color;

/**
 *
 * @author 5966868
 */
public class RunnerAgent extends Agent {
    public Color color;
    Battlefield battlefield;
    
    private String nextMove = null;
    
    public RunnerAgent(Battlefield battlefield, Color color) {
        this.color = color;
        this.battlefield = battlefield;        
    }

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message = RunnerAgent.this.receive();
                if(message != null) {                    
                    // recebe mensagem de atualizacao
                }
                ACLMessage reply = message.createReply();
                reply.addReceiver(new AID("Commander", AID.ISLOCALNAME));
                reply.setOntology("Action");
                reply.setContent(
                    RunnerAgent.this.getNextMove()
                );
                RunnerAgent.this.send(message);
            }
        });
    }
    
    private String getNextMove() {
        return "R";
    }
    
    public void die() {
        this.takeDown();
    }
}
