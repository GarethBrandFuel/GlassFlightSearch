package com.sabrelabs.glassflightsearch.test;

import com.sabrelabs.glassflightsearch.models.InstaFlightApiResultSet;
import com.sabrelabs.glassflightsearch.util.SabreDevStudio;
import com.sabrelabs.glassflightsearch.util.VoiceStringMatchSimple;

import junit.framework.TestCase;

import java.util.HashMap;

/**
 * Created by barrettclark on 5/5/14.
 */
public class InstaFlightApiRestulsSetTest extends TestCase {
    InstaFlightApiResultSet instaFlightApiResultSet;

    protected void setUp() throws Exception {
        String voiceResultsString = "[from MIA to LHR June 6th through the 10th]";
//        String voiceResultsString = "[from DFW to SFO June 6th through the 10th]";
        VoiceStringMatchSimple vsm = new VoiceStringMatchSimple();
        vsm.setVoiceResults(voiceResultsString);
        HashMap<String, String> matchResults = vsm.findMatch();
        SabreDevStudio sabreDevStudio = new SabreDevStudio();
        instaFlightApiResultSet = sabreDevStudio.getInstaflightsSearch(matchResults);
    }

    public void testResultsAreNotNull() {
        assertNotNull(instaFlightApiResultSet);
    }

    public void testLowestFare() {
        HashMap<String, String> lowestFare = instaFlightApiResultSet.lowestFare();
        assertNotNull(lowestFare);
        assertNotNull(lowestFare.get("outboundDepartureDateTime"));
        assertNotNull(lowestFare.get("outboundArrivalDateTime"));
        assertNotNull(lowestFare.get("returnDepartureDateTime"));
        assertNotNull(lowestFare.get("returnArrivalDateTime"));
        assertNotNull(lowestFare.get("airline"));
        assertNotNull(lowestFare.get("flightDuration"));
        assertNotNull(lowestFare.get("connectionCount"));
        assertNotNull(lowestFare.get("direct"));
        assertNotNull(lowestFare.get("fare"));
    }

    public void testLowestNonstopFare() {
        HashMap<String, String> lowestNonstopFare = instaFlightApiResultSet.lowestNonstopFare();
        assertNotNull(lowestNonstopFare);
        assertNotNull(lowestNonstopFare.get("outboundDepartureDateTime"));
        assertNotNull(lowestNonstopFare.get("outboundArrivalDateTime"));
        assertNotNull(lowestNonstopFare.get("returnDepartureDateTime"));
        assertNotNull(lowestNonstopFare.get("returnArrivalDateTime"));
        assertNotNull(lowestNonstopFare.get("airline"));
        assertNotNull(lowestNonstopFare.get("flightDuration"));
        assertEquals("0", lowestNonstopFare.get("connectionCount"));
        assertEquals("true", lowestNonstopFare.get("direct"));
        assertNotNull(lowestNonstopFare.get("fare"));
    }
}
