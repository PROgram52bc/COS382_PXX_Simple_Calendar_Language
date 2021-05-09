package scl.handlers;

import biweekly.component.VEvent;
import java.util.List;

/** 
 * The interface of a handler that parses a specific attribute in an event.
 *
 * For example, we can have a handler that handles the "from" attribute,
 * where its {@link requires} method should return ["from"], 
 * and the {@link handle} method should modify the {@link VEvent} object passed to it
 * according to all the attributes specified in the event, especially the "from" attribute.
 *
 * Implementing classes of this interface should follow the name convention "XyzHandler", 
 * where "xyz" is the attribute name to be handled.
 **/
public interface AttributesHandler {
    /**
     * Modifies an existing event based on a map of values
	 * @param vEvent an existing {@link VEvent} object
	 * @param proxyMap the {@link ProxyMap} object that supplies all needed information of an event
     *
	 * @throws 
     **/
    public void handle(VEvent vEvent, ProxyMap proxyMap);   

    /**
     * Describes the keywords that this handler would require to be present in
     * the {@link ProxyMap} supplied to the {@link handle} method.
     *
     * Typically, a handler shouldn't need to require multiple keywords, so the return value is usually
     * just a list of one string value.
     *
     * One scenario where multiple keywords can be specified is for keywords that have aliases.
     *
	 * @return a list of required keywords
     **/
    public List<String> requires();

    /**
     * Describes the keywords that this handler would reference in the {@link
     * ProxyMap} supplied to the {@link handle} method.
     *
     * @return a list of optional keywords
     **/
    public List<String> references();
}
