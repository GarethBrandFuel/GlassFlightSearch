package com.sabrelabs.glassflightsearch.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by barrettclark on 4/30/14.
 */
public class WordToNumber {
    Pattern numericPattern = Pattern.compile("(?i)^(\\d+).*$");
    Matcher numericMatcher;

    public Integer translate(String word) {
        Integer value = 0;
        String[] daysArray = {
            null, "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth",
                "ninth", "tenth", "eleventh", "twelfth", "thirteenth", "fourteenth", "fifteenth",
                "sixteenth", "seventeenth", "eighteenth", "nineteenth", "twentieth", "twenty first",
                "twenty second", "twenty third", "twenty fourth", "twenty fifth", "twenty sixth",
                "twenty seventh", "twenty eighth", "twenty ninth", "thirtieth", "thirty first"
        };
        List<String> days = Arrays.asList(daysArray);
        numericMatcher = numericPattern.matcher(word);

        if (numericMatcher.matches()) {
            value = Integer.valueOf(numericMatcher.group(1));
        } else if (days.contains(word)) {
            value = days.indexOf(word);
        }
        return value;
    }
}
