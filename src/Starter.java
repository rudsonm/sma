import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;

public class Starter {
    public static void main(String[] args) throws ControllerException, InterruptedException {
        Runtime rt = Runtime.instance();
        ProfileImpl p = new ProfileImpl();
        ContainerController cc = rt.createMainContainer(p);
        AgentController ac = cc.createNewAgent("Tirulipa", "MyBeautifulAgent", args);
        AgentController a2 = cc.createNewAgent("Rudini", "MyBeautifulAgent", args);
        cc.createNewAgent("Mariscleide", "MyBeautifulAgent", args).start();
        a2.start();
        ac.start();
        a2.kill();
    }
}
