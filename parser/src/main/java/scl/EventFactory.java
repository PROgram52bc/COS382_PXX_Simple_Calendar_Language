package scl;
import scl.util.*;

import java.util.HashMap;
import java.util.HashSet;

import biweekly.component.VEvent;
import scl.handlers.AttributesHandler;
import scl.exceptions.ConflictingHandlerException;
import scl.util.ProxyMap;

public class EventFactory {
    private final HashMap<String, AttributesHandler> keywords;
    private final HashSet<String> optionals;

    public EventFactory() {
        keywords = new HashMap<>();
        optionals = new HashSet<>();
    }

    /**
     * Register a handler to be used in the current factory object.
     * @param handler the handler to be attached.
     *
     * All registered handler must not have conflicting
     * {@link AttributesHandler#requires required attributes}.
     *
     * @throws {@link ConflictingHandlerException}
     *
     **/
    public void registerHandler(AttributesHandler handler) throws ConflictingHandlerException {
        for (String keyword: handler.requires()) {
            if (keywords.containsKey(keyword)) {
                throw new ConflictingHandlerException("keyword " + keyword + " has already been registered.");
            } else {
                keywords.put(keyword, handler);
                Debugger.log(1, "registering required keyword: " + keyword);
            }
        }
        for (String optional: handler.references()) {
            if (optionals.add(optional)) {
                // if added successfully
                Debugger.log(1, "registering optional keyword: " + optional);
            }
        }
    }

    /**
     * Make an event with the handlers registered in this current factory.
     * @param eventAttributes the attributes that describe the event.
     * @param initialEvent an initial event object to be built upon.
     *
     * @return the event produced.
     **/
    public VEvent makeEvent(ProxyMap eventAttributes, VEvent initialEvent) {
        for (String keyword: eventAttributes.keySet()) {
            if (keyword.startsWith("_")) continue; // skip hidden attributes
            if (keywords.containsKey(keyword)) {
                // the given keyword has a corresponding handler
                AttributesHandler handler = keywords.get(keyword);
                handler.handle(initialEvent, eventAttributes);
            } else if (optionals.contains(keyword)) {
                Debugger.log(3, "Non-required keyword: " + keyword);
            } else {
                Debugger.log(3, "Unhandled keyword: " + keyword);
            }
        }
        return initialEvent;
    }

    /**
     * Make an event with the handlers registered in this current factory, starting with an empty {@link VEvent} object.
     * @param eventAttributes the attributes that describe the event.
     *
     * @return the event produced.
     **/
    public VEvent makeEvent(ProxyMap eventAttributes) {
        return makeEvent(eventAttributes, new VEvent());
    }


}
