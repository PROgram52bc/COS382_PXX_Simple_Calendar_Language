package scl.handlers;
import scl.util.ProxyMap;
import scl.util.Debugger;
import scl.parsers.*;

import java.util.*;
import java.time.ZoneId;
import biweekly.component.VEvent;
import java.time.LocalDateTime;

public class ToHandler implements AttributesHandler {
    public void handle(VEvent event, ProxyMap attributes) {
        ZoneId zoneId = attributes.containsKey("_timezone") ?
            ZoneId.of(attributes.get("_timezone")) :
            ZoneId.systemDefault();
        Parser<LocalDateTime> parser = new MultiParser<LocalDateTime>()
            .addParser(new DateTimeParser())
            .addParser(new DateTimeParserNoYear());
        String input = attributes.get("to");
        LocalDateTime local = parser.parse(input);
        if (local == null) {
            Debugger.log(1, "Failed to handle 'to' field: " + input);
            return;
        }
        Date end = Date.from(local.atZone(zoneId).toInstant());
        event.setDateEnd(end);
    }
    public List<String> requires() {
        return Collections.singletonList("to");
    }
}
