package scl;
import biweekly.component.VEvent;
import java.util.List;

// TODO: learn and add Javadoc <2021-04-27, David Deng> //
public interface AttributesHandler {
    // TODO: implement a 'reset' method or a factory pattern to allow storing information <2021-04-27, David Deng> //
    // parses the attribute and modifies the event object
    public VEvent parseAttribute(String attributeName, String attributeValue, VEvent eventObject);
}
