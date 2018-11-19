/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import gui.GroundPanel;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import java.awt.Color;

/**
 *
 * @author 5966868
 */
public class WallAgent extends Agent {
    public static final Color COLOR = Color.MAGENTA;
    private GroundPanel panel;
    
    public WallAgent(GroundPanel panel) {
        this.panel = panel;
        panel.setBackground(WallAgent.COLOR);
    }
    
    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if(panel.hasRunnerAgent()) {
                    panel.getRunnerAgent().die();
                    panel.setBackground(WallAgent.COLOR);
                }
            }
        });
    }
    
}
