package scl.handlers;
import scl.util.ProxyMap;

import java.util.*;
import biweekly.component.VEvent;

public class SummaryHandler implements AttributesHandler {
    public void handle(VEvent event, ProxyMap attributes) {
        String input = attributes.get("summary");
        event.setSummary(input);
    }
    public List<String> requires() {
        return Collections.singletonList("summary");
    }
}
