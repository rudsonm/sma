/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import java.awt.Color;

/**
 *
 * @author 5966868
 */
public class RunnerAgent extends Agent {
    public Color cor;
    
    public RunnerAgent(Color cor) {
        this.cor = cor;
        
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                
            }
        });
    }
}
