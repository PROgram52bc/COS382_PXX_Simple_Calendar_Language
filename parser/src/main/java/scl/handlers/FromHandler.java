package scl.handlers;
import scl.util.ProxyMap;
import scl.util.Debugger;

import java.util.*;
import java.text.SimpleDateFormat;
import biweekly.component.VEvent;
import java.text.ParseException;

public class FromHandler implements AttributesHandler {
    public void handle(VEvent event, ProxyMap attributes) {
        SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mma");
        try {
            Date start = df.parse(attributes.get("from"));
            event.setDateStart(start);
        } catch (ParseException e) {
            Debugger.log(1, "Parse Exception: " + e);
        }
    }
    public List<String> requires() {
        return Collections.singletonList("from");
    }
}
