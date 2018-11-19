package gui;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.introspection.ACLMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BattlefieldAgent extends Agent {

    private final DFAgentDescription description = new DFAgentDescription();
    
    private final Scanner scanner = new Scanner(System.in);
    
    public BattlefieldAgent() throws FIPAException {
        
    }

    @Override
    protected void setup() {
        super.setup();
        description.setName(getAID());
        
        try {
            
//            DFService.register(this, this.description);
            Map<String, AID> runners = lookForRunners();
            
            System.out.println("Available Runners:");
            for (String runner : runners.keySet()) {
                System.out.println(runner);
            }
            
            int nRunners = 2;
            System.out.println("Choose the " + nRunners + " runners:");
            
            AID[] chosenRunners = new AID[nRunners];
            for (int i = 0; i < nRunners; ++i){
                chosenRunners[i] = runners.get(scanner.next());
            }
            
            for (AID runner : chosenRunners) {
                ACLMessage message = new ACLMessage();
                message.setPayload("Join Battlefield");
                
                
            }
            
            
            
        } catch (FIPAException ex) {
            Logger.getLogger(BattlefieldAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    private Map<String, AID> lookForRunners() throws FIPAException{
        DFAgentDescription runnerDescription = new DFAgentDescription();
        ServiceDescription runnerService = new ServiceDescription();
        runnerService.setType("Runner");
        runnerDescription.addServices(runnerService);
                
        DFAgentDescription[] result = DFService.search(this, runnerDescription);
        
        Map<String, AID> runners = new HashMap<>();
        
        for (DFAgentDescription desc : result) {
            runners.put(desc.getName().getLocalName(), desc.getName());
        }
        
        return runners;
    }
    
}
