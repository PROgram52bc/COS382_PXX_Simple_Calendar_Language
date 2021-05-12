package scl.handlers;
import scl.util.ProxyMap;
import scl.util.Debugger;
import scl.parsers.*;

import java.util.*;
import java.time.ZoneId;
import biweekly.component.VEvent;
import java.time.LocalDateTime;

public class FromHandler implements AttributesHandler {
    public void handle(VEvent event, ProxyMap attributes) {
        ZoneId zoneId = attributes.containsKey("_timezone") ?
            ZoneId.of(attributes.get("_timezone")) :
            ZoneId.systemDefault();
        Parser<LocalDateTime> parser = new MultiParser<LocalDateTime>()
            .addParser(new DateTimeParser())
            .addParser(new DateTimeParserNoYear());
        String input = attributes.get("from");
        LocalDateTime local = parser.parse(input);
        if (local == null) {
            Debugger.log(1, "Failed to handle 'from' field: " + input);
            return;
        }
        Date start = Date.from(local.atZone(zoneId).toInstant());
        event.setDateStart(start);
    }
    public List<String> requires() {
        return Collections.singletonList("from");
    }
}
