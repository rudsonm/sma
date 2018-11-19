
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;


public class TronStarter {
    public static void main(String[] args) throws StaleProxyException {
        
        Runtime jadeRuntime = jade.core.Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        
        AgentContainer container = jadeRuntime.createMainContainer(profile);
        
        container.createNewAgent("p1", "agent.TronRunner", null).start();
        container.createNewAgent("p2", "agent.TronRunner", null).start();
        
        
//        container.createNewAgent("rma", "jade.tools.rma.rma", null).start();
        
        

        container.createNewAgent("battlefieldAgent", "gui.BattlefieldAgent", null).start();
        
    }
}
