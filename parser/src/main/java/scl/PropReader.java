package scl;

import java.util.Hashtable;
import java.util.LinkedList;

public class PropReader extends SCLBaseListener {
    public Hashtable<String, LinkedList<EventAttribute>> events;
    protected String currentEventName;
    public PropReader() {
        events = new Hashtable<String, LinkedList<EventAttribute>>();
    }
    @Override
    public void enterFile(SCLParser.FileContext ctx) {
        System.out.println("Entering file!");
    }
    @Override
    public void enterEventhead(SCLParser.EventheadContext ctx) {
        System.out.println("Entering Eventhead!" + ctx.ID().getText());
        currentEventName = ctx.ID().getText();
        events.put(currentEventName, new LinkedList<EventAttribute>());
    }
    @Override
    public void exitEventattr(SCLParser.EventattrContext ctx) {
        System.out.println("Exiting EventAttribute!");
        System.out.println(ctx.getText());
    }
}
