package com.sabrelabs.glassflightsearch.test;

import android.util.Log;

import com.sabrelabs.glassflightsearch.models.CityPairApiResultSet;
import com.sabrelabs.glassflightsearch.models.DestinationFinderApiResultSet;
import com.sabrelabs.glassflightsearch.models.InstaFlightApiResultSet;
import com.sabrelabs.glassflightsearch.models.LeadPriceCalendarApiResultSet;
import com.sabrelabs.glassflightsearch.models.ThemeApiResultSet;
import com.sabrelabs.glassflightsearch.models.TravelThemeApiResultSet;
import com.sabrelabs.glassflightsearch.util.SabreDevStudio;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by barrettclark on 4/27/14.
 */
public class SabreDevStudioTest extends TestCase {
    SabreDevStudio sabreDevStudio;

    protected void setUp() throws IOException {
        sabreDevStudio = new SabreDevStudio();
    }
    protected void tearDown() { }

    public void testGetAccessToken() throws Exception {
        String accessToken = sabreDevStudio.getAccessToken();
        assertNotNull(accessToken);
    }

    public void testGetTravelThemeLookup() throws Exception {
        TravelThemeApiResultSet travelThemeApiResultSet = sabreDevStudio.getTravelThemeLookup();
        JSONObject results = travelThemeApiResultSet.getResults();
        assertNotNull(results.get("Themes"));
        JSONArray themes = (JSONArray) results.get("Themes");
        assertEquals(11, themes.length());
    }

    public void testCategory() throws Exception {
        ThemeApiResultSet themeApiResultSet = sabreDevStudio.getThemeAirportLookup("beach");
        JSONObject results = themeApiResultSet.getResults();
        assertNotNull(results.get("Destinations"));
        JSONArray destinations = (JSONArray) results.get("Destinations");
        assertEquals(22, destinations.length());
    }

    public void testGetOriginToDestination() throws Exception {
        HashMap<String, String> voiceMatch = new HashMap<String, String>();
        voiceMatch.put("match", "originToDestination");
        voiceMatch.put("origin", "DFW");
        voiceMatch.put("destination", "SFO");
        LeadPriceCalendarApiResultSet leadPriceCalendarApiResultSet = sabreDevStudio.getLeadPriceCalendar(voiceMatch);
        JSONObject results = leadPriceCalendarApiResultSet.getResults();
        assertNotNull(results.get("FareInfo"));
    }

    public void testLeadPriceCalendarWithDepartureDate() throws Exception {
        HashMap<String, String> voiceMatch = new HashMap<String, String>();
        voiceMatch.put("match", "originToDestination");
        voiceMatch.put("origin", "DFW");
        voiceMatch.put("destination", "SFO");
        Calendar cal = Calendar.getInstance();
        String month = new SimpleDateFormat("MMM").format(cal.getTime());
        voiceMatch.put("timeframe", month);
        LeadPriceCalendarApiResultSet leadPriceCalendarApiResultSet = sabreDevStudio.getLeadPriceCalendar(voiceMatch);
        JSONObject results = leadPriceCalendarApiResultSet.getResults();
        assertNotNull(results.get("FareInfo"));
        JSONArray fareInfo = (JSONArray) results.get("FareInfo");
        assertEquals(1, fareInfo.length());
    }

    public void testDestinationDateRange() throws Exception {
        String departureDate = "2014-07-01";
        String returnDate = "2014-07-04";
        HashMap<String, String> voiceMatch = new HashMap<String, String>();
        voiceMatch.put("origin", "DFW");
        voiceMatch.put("destination", "SFO");
        voiceMatch.put("month", "July");
        voiceMatch.put("departureday", "1st");
        voiceMatch.put("returnday", "fourth");
        InstaFlightApiResultSet instaFlightApiResultSet = sabreDevStudio.getInstaflightsSearch(voiceMatch);
        JSONObject results = instaFlightApiResultSet.getResults();
        Log.d("TEST", String.valueOf(results));
        assertEquals(departureDate, results.get("DepartureDateTime"));
        assertEquals(returnDate, results.get("ReturnDateTime"));
        assertEquals("SFO", results.get("DestinationLocation"));
        assertNotNull(results.get("PricedItineraries"));
    }

    public void testDestinationFinder() throws Exception {
        String category = "beach";
        HashMap<String, String> voiceMatch = new HashMap<String, String>();
        voiceMatch.put("category", category);
        DestinationFinderApiResultSet destinationFinderApiResultSet = sabreDevStudio.getDestinationAirShop(voiceMatch);
        JSONObject results = destinationFinderApiResultSet.getResults();
        assertEquals("DFW", results.get("OriginLocation"));
        assertNotNull(results.get("FareInfo"));
    }

    public void testDestinationFinderWithInvalidTheme() throws Exception {
        String category = "invalid-theme";
        HashMap<String, String> voiceMatch = new HashMap<String, String>();
        voiceMatch.put("category", category);
        DestinationFinderApiResultSet destinationFinderApiResultSet = sabreDevStudio.getDestinationAirShop(voiceMatch);
        JSONObject results = destinationFinderApiResultSet.getResults();
        assertNull(results);
    }

    public void testCityPairApiResultSet() throws Exception {
        CityPairApiResultSet cityPairApiResultSet = sabreDevStudio.getCityPairsLookup();
        JSONObject results = cityPairApiResultSet.getResults();
        assertNotNull(results.get("OriginDestinationLocations"));
    }
}
