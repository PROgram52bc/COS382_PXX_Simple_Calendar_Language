package scl.handlers;
import scl.util.ProxyMap;

import java.util.*;
import biweekly.component.VEvent;

public class DescriptionHandler implements AttributesHandler {
    public void handle(VEvent event, ProxyMap attributes) {
        String input = attributes.get("description");
        event.setDescription(input);
    }
    public List<String> requires() {
        return Collections.singletonList("description");
    }
}
