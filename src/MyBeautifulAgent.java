import jade.core.Agent;

public class MyBeautifulAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();
        System.out.println(
            "Hello World, I'm a " + this.getClass().getName()
            + ", but everyone calls me " + this.getLocalName()
            + ". Send me a letter on " + this.getName()
        );
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println(this.getLocalName() + ": IM DYING YOU SON OF A BEAUTIFUL AGENT MOTHER");                
    }
    
    
   
}
