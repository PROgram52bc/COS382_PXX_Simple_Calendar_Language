package scl.handlers;
import scl.util.ProxyMap;
import scl.util.Debugger;
import scl.parsers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import biweekly.component.VEvent;
import biweekly.property.RecurrenceRule;
import biweekly.util.Frequency;
import biweekly.util.Recurrence;

public class EveryHandler implements AttributesHandler {

    /**
     * append the interval info onto the builder and return it.
	 * @param intervalValue the string that represents the interval value, if null, default to 1
	 * @param builder the existing builder object.
     *
	 * @return the builder with the interval value added.
     **/
    public Recurrence.Builder handleInterval(String intervalValue, Recurrence.Builder builder) {
        Integer interval = Integer.valueOf(intervalValue == null ? "1" : intervalValue);
        return builder.interval(interval);
    }

    /**
     * append the until info onto the builder and return it.
	 * @param untilValue the string that represents the until value, if null then it is ignored.
	 * @param builder the existing builder object.
     *
	 * @return the builder with the until value added.
     **/
    public Recurrence.Builder handleUntil(String untilValue, Recurrence.Builder builder) {
        LocalDate local = new DateParser().parse(untilValue);
        if (local != null) {
            // TODO: can we get access to the global input timezone? <2021-05-12, David Deng> //
            // Use a context class, see https://stackoverflow.com/questions/4646577/global-variables-in-java
            ZoneId zoneId = ZoneId.systemDefault();
            Date until = Date.from(local.atStartOfDay(zoneId).toInstant());
            builder.until(until, false);
        }
        return builder;
    }

    /**
     * append the count info onto the builder and return it.
	 * @param countValue the string that represents the count value, if null then it is ignored.
	 * @param builder the existing builder object.
     *
	 * @return the builder with the count value added.
     **/
    public Recurrence.Builder handleCount(String countValue, Recurrence.Builder builder) {
        // TODO: Just a thought: have a RegexMatcher class that wraps the following lines? <2021-05-12, David Deng> //
        String regex = "(?<count>\\d+)( times?)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(countValue);
        if (!matcher.matches()) {
            Debugger.log(1, "'for' field does not match regex [" + regex + "]: " + countValue);
            return builder;
        }
        Integer count = Integer.valueOf(matcher.group("count"));
        return builder.count(count);
    }

    // /**
    //  * append the frequency info onto the builder and return it.
	 // * @param frequency the string that represents the frequency value.
	 // * @param builder the existing builder object.
    //  *
	 // * @return the builder with the frequency value added.
    //  **/
    // public Recurrence.Builder handleFrequency(String frequency, Recurrence.Builder builder) {

    //     return builder;
    // }

    @Override
    public void handle(VEvent event, ProxyMap attributes) {
        String every = attributes.get("every");
        String regex = "((?<interval>\\d+) )?(?<frequency>\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(every);
        if (!matcher.matches()) {
            Debugger.log(1, "'every' field does not match regex [" + regex + "]: " + every);
            return;
        }

        // handle frequency ('every' keyword)
        String frequencyValue = matcher.group("frequency");
        Frequency frequency = new FrequencyParser().parse(frequencyValue);
        if (frequency == null) {
            Debugger.log(1, "Failed to parse frequency value: " + frequencyValue);
            return;
        }
        Debugger.log(3, "frequency: " + frequency.name());
        Recurrence.Builder builder = new Recurrence.Builder(frequency);

        // handle interval ('every' keyword)
        String intervalValue = matcher.group("interval");
        builder = handleInterval(intervalValue, builder);

        // TODO: refactor? change the handler's interface so that they take the entire attributes map?
        // Benefits of this:
        // 1. centralized keyword management ('for' corresponds to 'count', but they are not at the same place)
        // 2. have access to the hidden variables.
        // A more elaborate idea:
        // Maybe introduce another interface that builds recurrence rule (similar to the Parser interface) ? <2021-05-12, David Deng> //
        // handle until ('until' keyword)
        String untilValue = attributes.getOrDefault("until", null);
        builder = handleUntil(untilValue, builder);

        // handle count ('for' keyword)
        String countValue = attributes.getOrDefault("for", null);
        if (untilValue == null) {
            // only if 'until' is not specified
            builder = handleCount(countValue, builder);
        } else if (countValue != null && untilValue != null) {
            Debugger.log(1, "'until' and 'for' cannot both be specified, the value of 'for' is ignored: " + countValue);
        }

        // handle by... rules ('scheduled' keyword)

        Recurrence recurrence = builder.build();
        RecurrenceRule rrule = new RecurrenceRule(recurrence);
        event.setRecurrenceRule(rrule);
    }
    @Override
    public List<String> requires() {
        return Collections.singletonList("every");
    }

    @Override
    public List<String> references() {
        return Arrays.asList("scheduled", "until", "for");
    }
}
