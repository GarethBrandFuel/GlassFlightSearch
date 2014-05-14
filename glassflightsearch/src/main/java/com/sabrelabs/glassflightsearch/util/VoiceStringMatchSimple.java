package com.sabrelabs.glassflightsearch.util;

import android.util.Log;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by barrettclark on 4/25/14.
 */
public class VoiceStringMatchSimple {
    private final String TAG = getClass().getSimpleName();
    String voiceResults;

    /*
     * [to SFO]
     * [DFW to SFO]
     * [from DFW to SFO]
     * [2 SFM]
     */
    Pattern originToDestinationPattern = Pattern.compile("(?i)^\\[(from\\s)?(.*)?\\s?(to|2)\\s(?!the\\s)(.*)\\]$");

    /*
     * [San Francisco for four days]
     * [San Francisco for one day in May]
     * [San Francisco for four days in May]
     * [San Francisco for four days this spring]  NOTE: the season is not converted to a date
     */
    Pattern destinationForDurationPattern = Pattern.compile("(?i)^\\[(.*)\\sfor\\s(\\w*)\\sday[s]?(\\sin\\s|\\sthis\\s)?(\\w*)?\\]$");

    /*
     * [San Francisco this spring]  NOTE: the season is not converted to a date
     * [Houston this spring]  NOTE: the season is not converted to a date
     * [San Francisco in May]
     */
    Pattern destinationInPeriodPattern = Pattern.compile("(?i)^\\[(.*)(\\sin\\s|\\sthis\\s)(\\w*)?\\]$");

    /*
     * [from DFW to SFO in July]
     */
    Pattern destinationInPeriodIATAPattern = Pattern.compile("(?i)^\\[(from\\s)?(\\w{3})(\\sto\\s)?(\\w{3})(\\sin\\s|\\sthis\\s)(\\w*)?\\]$");

    /*
     * [San Francisco June first through the fourth]
     * [Houston July 12th to the 20th]
     * [Houston July 12th thru the 20th]
     * [San Francisco June the first through the fourth]
     * [San Francisco June 1st to the 4th]
     * [New York from June second to the third]
     */
    Pattern destinationDateRangePattern = Pattern.compile("(?i)^\\[(\\D+?)(from)?(\\s\\w*)(\\sthe)?(\\s\\w*)(\\sto|\\sthrough|\\sthru)\\sthe\\s(\\w*)\\]$");

    /*
     * [from MIA to LHR June 6th through the 10th]
     */
    Pattern destinationDateRangeIATAPattern = Pattern.compile("(?i)^\\[(from\\s)?(\\w{3})(\\sto\\s)?(\\w{3})(from)?(\\s\\w*)(\\sthe)?(\\s\\w*)(\\sto|\\sthrough|\\sthru)\\sthe\\s(\\w*)\\]$");

    /*
     * [the Beach]
     * [the ANYTHING]
     * [to the beach]
     */
    Pattern categoryPattern = Pattern.compile("(?i)^\\[(to\\s)?the\\s(\\w*)\\]$");

    /*
     * [from ATL to the mountains
     */
    Pattern categoryIATAPattern = Pattern.compile("(?i)^\\[(from\\s)?(\\w{3})\\sto\\sthe\\s(\\w*)\\]$");

    /*
     * [San Francisco]
     * [SFO]
     * NOTE: this will be greedy and just grab the entire string, so it's a catch-all
     */
    Pattern destinationPattern = Pattern.compile("(?i)^\\[(.*)\\]$");

    Matcher originToDestinationMatcher;
    Matcher destinationForDurationMatcher;
    Matcher destinationInPeriodMatcher;
    Matcher destinationInPeriodIATAMatcher;
    Matcher destinationDateRangeMatcher;
    Matcher destinationDateRangeIATAMatcher;
    Matcher categoryMatcher;
    Matcher categoryIATAMatcher;
    Matcher destinationMatcher;

    public Matcher getOriginToDestinationMatcher() { return this.originToDestinationMatcher; }
    public Matcher getDestinationForDurationMatcher() { return this.destinationForDurationMatcher; }
    public Matcher getDestinationInPeriodMatcher() { return this.destinationInPeriodMatcher; }
    public Matcher getDestinationInPeriodIATAMatcher() { return this.destinationInPeriodIATAMatcher; }
    public Matcher getDestinationDateRangeMatcher() { return this.destinationDateRangeMatcher; }
    public Matcher getDestinationDateRangeIATAMatcher() { return this.destinationDateRangeIATAMatcher; }
    public Matcher getCategoryMatcher() { return this.categoryMatcher; }
    public Matcher getCategoryIATAMatcher() { return this.categoryIATAMatcher; }
    public Matcher getDestinationMatcher() { return this.destinationMatcher; }

    public VoiceStringMatchSimple() { }

    public void setVoiceResults(String voiceResults) {
        this.voiceResults = voiceResults;
    }

    public HashMap<String, String> findMatch() {
        HashMap<String, String> results = new HashMap<String, String>();
        if (destinationDateRangeIATAMatches()) {
            // InstaFlights
            String origin = getDestinationDateRangeIATAMatcher().group(2).trim();
            String destination = getDestinationDateRangeIATAMatcher().group(4).trim();
            String month = getDestinationDateRangeIATAMatcher().group(6).trim();
            String departuredate = getDestinationDateRangeIATAMatcher().group(8).trim();
            String returndate = getDestinationDateRangeIATAMatcher().group(10).trim();
            results.put("match", "destinationDateRangeIATA");
            results.put("origin", origin);
            results.put("destination", destination);
            results.put("month", month);
            results.put("departureday", departuredate);
            results.put("returnday", returndate);
        } else if (categoryIATAMatches()) {
            // Destination Finder
            String origin = getCategoryIATAMatcher().group(2).trim();
            String category = getCategoryIATAMatcher().group(3).trim();
            results.put("match", "categoryIATA");
            results.put("origin", origin);
            results.put("category", category);
        } else if (destinationInPeriodIATAMatches()) {
            String origin = getDestinationInPeriodIATAMatcher().group(2).trim();
            String destination = getDestinationInPeriodIATAMatcher().group(4).trim();
            String timeframe = getDestinationInPeriodIATAMatcher().group(6).trim();
            results.put("match", "destinationInPeriodIATA");
            results.put("origin", origin);
            results.put("destination", destination);
            results.put("timeframe", timeframe);
        } else if (originToDestinationMatches()) {
            String origin = getOriginToDestinationMatcher().group(2).trim();
            String destination = getOriginToDestinationMatcher().group(4).trim();
            results.put("match", "originToDestination");
            results.put("origin", origin);
            results.put("destination", destination);
        } else if (destinationForDurationMatches()) {
            String destination = getDestinationForDurationMatcher().group(1).trim();
            String duration = getDestinationForDurationMatcher().group(2).trim();
            String timeframe = getDestinationForDurationMatcher().group(4).trim();
            results.put("match", "destinationForDuration");
            results.put("destination", destination);
            results.put("duration", duration);
            results.put("timeframe", timeframe);
        } else if (destinationInPeriodMatches()) {
            String destination = getDestinationInPeriodMatcher().group(1).trim();
            String timeframe = getDestinationInPeriodMatcher().group(3).trim();
            results.put("match", "destinationInPeriod");
            results.put("destination", destination);
            results.put("timeframe", timeframe);
        } else if (destinationDateRangeMatches()) {
            // InstaFlights
            String destination = getDestinationDateRangeMatcher().group(1).trim();
            String month = getDestinationDateRangeMatcher().group(3).trim();
            String departuredate = getDestinationDateRangeMatcher().group(5).trim();
            String returndate = getDestinationDateRangeMatcher().group(7).trim();
            results.put("match", "destinationDateRange");
            results.put("destination", destination);
            results.put("month", month);
            results.put("departureday", departuredate);
            results.put("returnday", returndate);
        } else if (categoryMatches()) {
            // Destination Finder
            String category = getCategoryMatcher().group(2).trim();
            results.put("match", "category");
            results.put("category", category);
        } else if (destinationMatches()) {
            String destination = getDestinationMatcher().group(1).trim();
            results.put("match", "destination");
            results.put("destination", destination);
        } else {
            results.put("match", "none");
        }
        Log.d(TAG, results.toString());
        return results;
    }

    public boolean originToDestinationMatches() {
        this.originToDestinationMatcher = originToDestinationPattern.matcher(this.voiceResults);
        return this.originToDestinationMatcher.matches();
    }

    public boolean destinationForDurationMatches() {
        this.destinationForDurationMatcher = destinationForDurationPattern.matcher(this.voiceResults);
        return this.destinationForDurationMatcher.matches();
    }

    public boolean destinationInPeriodMatches() {
        this.destinationInPeriodMatcher = destinationInPeriodPattern.matcher(this.voiceResults);
        return this.destinationInPeriodMatcher.matches();
    }

    public boolean destinationInPeriodIATAMatches() {
        this.destinationInPeriodIATAMatcher = destinationInPeriodIATAPattern.matcher(this.voiceResults);
        return this.destinationInPeriodIATAMatcher.matches();
    }

    public boolean destinationDateRangeMatches() {
        this.destinationDateRangeMatcher = destinationDateRangePattern.matcher(this.voiceResults);
        return this.destinationDateRangeMatcher.matches();
    }

    public boolean destinationDateRangeIATAMatches() {
        this.destinationDateRangeIATAMatcher = destinationDateRangeIATAPattern.matcher(this.voiceResults);
        return this.destinationDateRangeIATAMatcher.matches();
    }

    public boolean categoryMatches() {
        this.categoryMatcher = categoryPattern.matcher(this.voiceResults);
        return this.categoryMatcher.matches();
    }

    public boolean categoryIATAMatches() {
        this.categoryIATAMatcher = categoryIATAPattern.matcher(this.voiceResults);
        return this.categoryIATAMatcher.matches();
    }

    public boolean destinationMatches() {
        this.destinationMatcher = destinationPattern.matcher(this.voiceResults);
        return this.destinationMatcher.matches();
    }
}
