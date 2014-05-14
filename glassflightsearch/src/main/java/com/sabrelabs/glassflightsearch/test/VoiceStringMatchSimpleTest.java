package com.sabrelabs.glassflightsearch.test;

import com.sabrelabs.glassflightsearch.util.VoiceStringMatchSimple;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by barrettclark on 4/25/14.
 */
public class VoiceStringMatchSimpleTest extends TestCase {
    protected VoiceStringMatchSimple vsm;
    protected List<String> originToDestinationStrings;
    protected List<String> destinationForDurationStrings;
    protected List<String> destinationInPeriodStrings;
    protected List<String> destinationInPeriodIATAStrings;
    protected List<String> destinationDateRangeStrings;
    protected List<String> destinationDateRangeIATAStrings;
    protected List<String> categoryStrings;
    protected List<String> categoryIATAStrings;
    protected List<String> destinationStrings;

    protected void setUp() {
        this.vsm = new VoiceStringMatchSimple();

        this.originToDestinationStrings = new ArrayList<String>();
        this.originToDestinationStrings.add("[to SFO]");
        this.originToDestinationStrings.add("[DFW to SFO]");
        this.originToDestinationStrings.add("[from DFW to SFO]");
        this.originToDestinationStrings.add("[2 SFM]");

        this.destinationForDurationStrings = new ArrayList<String>();
        this.destinationForDurationStrings.add("[San Francisco for four days]");
        this.destinationForDurationStrings.add("[San Francisco for one day in May]");
        this.destinationForDurationStrings.add("[San Francisco for four days in May]");
        this.destinationForDurationStrings.add("[San Francisco for four days this spring]");

        this.destinationInPeriodStrings = new ArrayList<String>();
        this.destinationInPeriodStrings.add("[San Francisco this spring]");
        this.destinationInPeriodStrings.add("[Houston this spring]");
        this.destinationInPeriodStrings.add("[San Francisco in May]");

        this.destinationInPeriodIATAStrings = new ArrayList<String>();
        this.destinationInPeriodIATAStrings.add("[from DFW to SFO in July]");

        this.destinationDateRangeStrings = new ArrayList<String>();
        this.destinationDateRangeStrings.add("[San Francisco June first through the fourth]");
        this.destinationDateRangeStrings.add("[Houston July 12th to the 20th]");
        this.destinationDateRangeStrings.add("[Houston July 12th thru the 20th]");
        this.destinationDateRangeStrings.add("[San Francisco June the first through the fourth]");
        this.destinationDateRangeStrings.add("[San Francisco June 1st to the 4th]");
        this.destinationDateRangeStrings.add("[New York from June second to the third]");

        this.destinationDateRangeIATAStrings = new ArrayList<String>();
        this.destinationDateRangeIATAStrings.add("[from MIA to LHR June 6th through the 10th]");

        this.categoryStrings = new ArrayList<String>();
        this.categoryStrings.add("[the Beach]");
        this.categoryStrings.add("[the ANYTHING]");
        this.categoryStrings.add("[to the beach]");

        this.categoryIATAStrings = new ArrayList<String>();
        this.categoryIATAStrings.add("[from ATL to the mountains]");

        this.destinationStrings = new ArrayList<String>();
        this.destinationStrings.add("[San Francisco]");
        this.destinationStrings.add("[SFO]");
    }
    protected void tearDown() { }

    public void testOriginToDestinationMatches() {
        boolean itMatches;
        for (String s: this.originToDestinationStrings) {
            this.vsm.setVoiceResults(s);
            itMatches = this.vsm.originToDestinationMatches();
            assertEquals(true, itMatches);
            assertEquals(4, this.vsm.getOriginToDestinationMatcher().groupCount());
        }
    }
    public void testDestinationForDurationMatches() {
        boolean itMatches;
        for (String s: this.destinationForDurationStrings) {
            this.vsm.setVoiceResults(s);
            itMatches = this.vsm.destinationForDurationMatches();
            assertEquals(true, itMatches);
            assertEquals(4, this.vsm.getDestinationForDurationMatcher().groupCount());
        }
    }
    public void testDestinationInPeriodMatches() {
        boolean itMatches;
        for (String s: this.destinationInPeriodStrings) {
            this.vsm.setVoiceResults(s);
            itMatches = this.vsm.destinationInPeriodMatches();
            assertEquals(true, itMatches);
            assertEquals(3, this.vsm.getDestinationInPeriodMatcher().groupCount());
        }
    }
    public void testDestinationInPeriodIATAMatches() {
        boolean itMatches;
        for (String s: this.destinationInPeriodIATAStrings) {
            this.vsm.setVoiceResults(s);
            itMatches = this.vsm.destinationInPeriodIATAMatches();
            assertEquals(true, itMatches);
            assertEquals(6, this.vsm.getDestinationInPeriodIATAMatcher().groupCount());
        }
    }
    public void testDestinationDateRangeMatches() {
        boolean itMatches;
        for (String s: this.destinationDateRangeStrings) {
            this.vsm.setVoiceResults(s);
            itMatches = this.vsm.destinationDateRangeMatches();
            assertEquals(true, itMatches);
            assertEquals(7, this.vsm.getDestinationDateRangeMatcher().groupCount());
        }
    }
    public void testDestinationDateRangeIATAMatches() {
        boolean itMatches;
        for (String s: this.destinationDateRangeIATAStrings) {
            this.vsm.setVoiceResults(s);
            itMatches = this.vsm.destinationDateRangeIATAMatches();
            assertEquals(true, itMatches);
            assertEquals(10, this.vsm.getDestinationDateRangeIATAMatcher().groupCount());
        }
    }
    public void testCategoryMatches() {
        boolean itMatches;
        for (String s: this.categoryStrings) {
            this.vsm.setVoiceResults(s);
            itMatches = this.vsm.categoryMatches();
            assertEquals(true, itMatches);
            assertEquals(2, this.vsm.getCategoryMatcher().groupCount());
        }
    }
    public void testCategoryIATAMatches() {
        boolean itMatches;
        for (String s: this.categoryIATAStrings) {
            this.vsm.setVoiceResults(s);
            itMatches = this.vsm.categoryIATAMatches();
            assertEquals(true, itMatches);
            assertEquals(3, this.vsm.getCategoryIATAMatcher().groupCount());
        }
    }
    public void testDestinationMatches() {
        boolean itMatches;
        for (String s: this.destinationStrings) {
            this.vsm.setVoiceResults(s);
            itMatches = this.vsm.destinationMatches();
            assertEquals(true, itMatches);
            assertEquals(1, this.vsm.getDestinationMatcher().groupCount());
        }
    }

    public void testFindMatch() {
        HashMap<String, String> results;
        HashMap<String, List<String>> strings = new HashMap<String, List<String>>();
        strings.put("originToDestination", this.originToDestinationStrings);
        strings.put("destinationForDuration", this.destinationForDurationStrings);
        strings.put("destinationInPeriod", this.destinationInPeriodStrings);
        strings.put("destinationInPeriodIATA", this.destinationInPeriodIATAStrings);
        strings.put("destinationDateRange", this.destinationDateRangeStrings);
        strings.put("destinationDateRangeIATA", this.destinationDateRangeIATAStrings);
        strings.put("category", this.categoryStrings);
        strings.put("categoryIATA", this.categoryIATAStrings);
        strings.put("destination", this.destinationStrings);
        Iterator keysIterator = strings.keySet().iterator();
        while (keysIterator.hasNext()) {
            String key = (String)keysIterator.next();
            List<String> values = strings.get(key);
            for (String s: values) {
                this.vsm.setVoiceResults(s);
                results = this.vsm.findMatch();
                assertEquals(key, results.get("match"));
            }
        }
    }

    public void testOriginToDestinationMatchesGroups() {
        HashMap<String, String> results;
        this.vsm.setVoiceResults(this.originToDestinationStrings.get(0));
        results = this.vsm.findMatch();
        assertEquals("", results.get("origin"));
        assertEquals("SFO", results.get("destination"));

        this.vsm.setVoiceResults(this.originToDestinationStrings.get(1));
        results = this.vsm.findMatch();
        assertEquals("DFW", results.get("origin"));
        assertEquals("SFO", results.get("destination"));

        this.vsm.setVoiceResults(this.originToDestinationStrings.get(2));
        results = this.vsm.findMatch();
        assertEquals("DFW", results.get("origin"));
        assertEquals("SFO", results.get("destination"));

        this.vsm.setVoiceResults(this.originToDestinationStrings.get(3));
        results = this.vsm.findMatch();
        assertEquals("", results.get("origin"));
        assertEquals("SFM", results.get("destination"));
    }

    public void testDestinationForDurationMatchesGroups() {
        HashMap<String, String> results;
        this.vsm.setVoiceResults(this.destinationForDurationStrings.get(0));
        results = this.vsm.findMatch();
        assertEquals("", results.get("timeframe"));
        assertEquals("four", results.get("duration"));
        assertEquals("San Francisco", results.get("destination"));

        this.vsm.setVoiceResults(this.destinationForDurationStrings.get(1));
        results = this.vsm.findMatch();
        assertEquals("May", results.get("timeframe"));
        assertEquals("one", results.get("duration"));
        assertEquals("San Francisco", results.get("destination"));

        this.vsm.setVoiceResults(this.destinationForDurationStrings.get(2));
        results = this.vsm.findMatch();
        assertEquals("May", results.get("timeframe"));
        assertEquals("four", results.get("duration"));
        assertEquals("San Francisco", results.get("destination"));

        this.vsm.setVoiceResults(this.destinationForDurationStrings.get(3));
        results = this.vsm.findMatch();
        assertEquals("spring", results.get("timeframe"));
        assertEquals("four", results.get("duration"));
        assertEquals("San Francisco", results.get("destination"));
    }

    public void testDestinationInPeriodMatchesGroups() {
        HashMap<String, String> results;

        this.vsm.setVoiceResults(this.destinationInPeriodStrings.get(0));
        results = this.vsm.findMatch();
        assertEquals("San Francisco", results.get("destination"));
        assertEquals("spring", results.get("timeframe"));

        this.vsm.setVoiceResults(this.destinationInPeriodStrings.get(1));
        results = this.vsm.findMatch();
        assertEquals("Houston", results.get("destination"));
        assertEquals("spring", results.get("timeframe"));

        this.vsm.setVoiceResults(this.destinationInPeriodStrings.get(2));
        results = this.vsm.findMatch();
        assertEquals("San Francisco", results.get("destination"));
        assertEquals("May", results.get("timeframe"));
    }

    public void testDestinationInPeriodIATAMatchesGroups() {
        HashMap<String, String> results;

        this.vsm.setVoiceResults(this.destinationInPeriodIATAStrings.get(0));
        results = this.vsm.findMatch();
        assertEquals("DFW", results.get("origin"));
        assertEquals("SFO", results.get("destination"));
        assertEquals("July", results.get("timeframe"));
    }

    public void testDestinationDateRangeIATAMatchesGroups() {
        HashMap<String, String> results;
        this.vsm.setVoiceResults(this.destinationDateRangeIATAStrings.get(0));
        results = this.vsm.findMatch();
        assertEquals("June", results.get("month"));
        assertEquals("6th", results.get("departureday"));
        assertEquals("10th", results.get("returnday"));
        assertEquals("MIA", results.get("origin"));
        assertEquals("LHR", results.get("destination"));
    }

    public void testDestinationDateRangeMatchesGroups() {
        HashMap<String, String> results;
        this.vsm.setVoiceResults(this.destinationDateRangeStrings.get(0));
        results = this.vsm.findMatch();
        assertEquals("June", results.get("month"));
        assertEquals("first", results.get("departureday"));
        assertEquals("fourth", results.get("returnday"));
        assertEquals("San Francisco", results.get("destination"));

        this.vsm.setVoiceResults(this.destinationDateRangeStrings.get(1));
        results = this.vsm.findMatch();
        assertEquals("July", results.get("month"));
        assertEquals("12th", results.get("departureday"));
        assertEquals("20th", results.get("returnday"));
        assertEquals("Houston", results.get("destination"));

        this.vsm.setVoiceResults(this.destinationDateRangeStrings.get(2));
        results = this.vsm.findMatch();
        assertEquals("July", results.get("month"));
        assertEquals("12th", results.get("departureday"));
        assertEquals("20th", results.get("returnday"));
        assertEquals("Houston", results.get("destination"));

        this.vsm.setVoiceResults(this.destinationDateRangeStrings.get(3));
        results = this.vsm.findMatch();
        assertEquals("June", results.get("month"));
        assertEquals("first", results.get("departureday"));
        assertEquals("fourth", results.get("returnday"));
        assertEquals("San Francisco", results.get("destination"));

        this.vsm.setVoiceResults(this.destinationDateRangeStrings.get(4));
        results = this.vsm.findMatch();
        assertEquals("June", results.get("month"));
        assertEquals("1st", results.get("departureday"));
        assertEquals("4th", results.get("returnday"));
        assertEquals("San Francisco", results.get("destination"));

        this.vsm.setVoiceResults(this.destinationDateRangeStrings.get(5));
        results = this.vsm.findMatch();
        assertEquals("June", results.get("month"));
        assertEquals("second", results.get("departureday"));
        assertEquals("third", results.get("returnday"));
        assertEquals("New York", results.get("destination"));
    }

    public void testCategoryMatchesGroups() {
        HashMap<String, String> results;

        this.vsm.setVoiceResults(this.categoryStrings.get(0));
        results = this.vsm.findMatch();
        assertEquals("Beach", results.get("category"));

        this.vsm.setVoiceResults(this.categoryStrings.get(1));
        results = this.vsm.findMatch();
        assertEquals("ANYTHING", results.get("category"));

        this.vsm.setVoiceResults(this.categoryStrings.get(2));
        results = this.vsm.findMatch();
        assertEquals("beach", results.get("category"));
    }

    public void testCategoryIATAMatchesGroups() {
        HashMap<String, String> results;

        this.vsm.setVoiceResults(this.categoryIATAStrings.get(0));
        results = this.vsm.findMatch();
        assertEquals("ATL", results.get("origin"));
        assertEquals("mountains", results.get("category"));
    }

    public void testDestinationMatchesGroups() {
        HashMap<String, String> results;

        this.vsm.setVoiceResults(this.destinationStrings.get(0));
        results = this.vsm.findMatch();
        assertEquals("San Francisco", results.get("destination"));

        this.vsm.setVoiceResults(this.destinationStrings.get(1));
        results = this.vsm.findMatch();
        assertEquals("SFO", results.get("destination"));
    }

}
