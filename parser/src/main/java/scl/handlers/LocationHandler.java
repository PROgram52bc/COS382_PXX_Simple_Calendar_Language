package scl.handlers;
import scl.util.ProxyMap;

import java.util.*;
import biweekly.component.VEvent;

public class LocationHandler implements AttributesHandler {
    public void handle(VEvent event, ProxyMap attributes) {
        String input = attributes.get("location");
        event.setLocation(input);
    }
    public List<String> requires() {
        return Collections.singletonList("location");
    }
}
