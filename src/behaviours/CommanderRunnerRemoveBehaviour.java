/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agent.CommanderAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author 09952410905
 */
public class CommanderRunnerRemoveBehaviour extends CyclicBehaviour {
    private CommanderAgent commander;
    public CommanderRunnerRemoveBehaviour(CommanderAgent commander) {
        this.commander = commander;
    }

    @Override
    public void action() {
        try {
            ACLMessage message = commander.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
            if(message != null) {
                AID agent = (AID) message.getContentObject();
                commander.nRunners--;
                commander.playersColors.remove(agent);
                commander.playersPositions.remove(agent);
                commander.runners.remove(agent);

                if(commander.nRunners == 1)
                    commander.die();
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
