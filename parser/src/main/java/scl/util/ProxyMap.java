package scl.util;
import java.util.HashMap;

/** An intermediate representation of an Event */
public class ProxyMap extends HashMap<String, String> { 
    public ProxyMap() {
        super();
    }
    public ProxyMap(ProxyMap pm) {
        super(pm);
    }
}
