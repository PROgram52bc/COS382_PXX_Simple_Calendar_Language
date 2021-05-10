// package scl;
// import java.util.Date;
// import java.text.SimpleDateFormat;
// import biweekly.component.VEvent;
// import java.text.ParseException;

// public class ToHandler implements AttributesHandler {
//     public VEvent parseAttribute(String attributeName, String attributeValue, VEvent event) {
//         SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mma");
//         try {
//             switch (attributeName) {
//                 case "from":
//                     Date start = df.parse(attributeValue);
//                     event.setDateStart(start);
//                     break;
//                 case "to":
//                     Date end = df.parse(attributeValue);
//                     event.setDateEnd(end);
//                     break;
//                 default:
//                     // TODO: throw a particular exception <2021-04-27, David Deng> //
//                     System.out.println("Unrecognized attributeName: " + attributeName);
//             }
//         } catch (ParseException e) {
//             System.out.println("Parse exception: " + e.toString());
//         }
//         return event;
//     }
// }
