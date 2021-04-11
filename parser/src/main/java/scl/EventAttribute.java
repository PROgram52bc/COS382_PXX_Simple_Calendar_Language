package scl;
// label: value
interface EventAttribute<T> {
    public String getLabel();
    public T getValue();
}

/**
 * represents an attribute of an event.
 *
 * For example, an event with an eventattr of
 * on: Nov. 31, 1999
 * corresponds to an EventAttribute with its label equal to 'on', and its value equal to "Nov. 31, 1999".
 * Modules can implement specific EventAttribute subclass to represent various kinds of values.
 **/
// public class EventAttributeBase<T> implements EventAttribute<T> {
//     public String label;
//     public T value;
//     public EventAttribute() { }
//     public EventAttribute(String l, T v) {
//         label = l;
//         value = v;
//     }
//     @Override
//     public String getLabel() {
//         return label;
//     }
//     @Override
//     public T getValue() {
//         return value;
//     }
// }


