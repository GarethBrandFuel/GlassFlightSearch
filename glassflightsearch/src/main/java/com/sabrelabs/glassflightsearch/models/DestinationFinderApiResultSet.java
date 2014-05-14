package com.sabrelabs.glassflightsearch.models;

import android.app.Activity;
import android.util.Log;

import com.sabrelabs.glassflightsearch.card.FareCard;
import com.sabrelabs.glassflightsearch.util.DateMaker;
import com.sabrelabs.glassflightsearch.util.LowestFareValueComperator;
import com.sabrelabs.glassflightsearch.util.SabreDevStudio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by barrettclark on 4/30/14.
 */
public class DestinationFinderApiResultSet extends ApiResultSet {
    private final String TAG = getClass().getSimpleName();
    JSONArray fareInfo;

    public DestinationFinderApiResultSet(JSONObject results) {
        this.results = results;
        try {
            if (results != null) {
                this.fareInfo = results.getJSONArray("FareInfo");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<FareCard> displayCards(Activity activity) {
        ArrayList<FareCard> cards = new ArrayList<FareCard>();

        FareCard card;

        ArrayList <JSONObject> uniqueDestinations = new ArrayList();
        HashMap<String, JSONObject> destinations = new HashMap<String, JSONObject>();
        // http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
        LowestFareValueComperator bvc =  new LowestFareValueComperator(destinations);
        TreeMap<String, JSONObject> sortedDestinations = new TreeMap<String, JSONObject>(bvc);

        for (int i = 0; i < this.fareInfo.length(); i++) {
            try {
                JSONObject fareInfo = this.fareInfo.getJSONObject(i);
                String destinationLocation = fareInfo.getString("DestinationLocation");

                uniqueDestinations.add(fareInfo);

                if (destinations.containsKey(destinationLocation)) {
                    // Check to see if this is lower than the one we already have
                    String fareString = fareInfo.getString("LowestFare");
                    if (fareString.equals("N/A")) { continue; }
                    if (fareInfo.getDouble("LowestFare") < destinations.get(destinationLocation).getDouble("LowestFare")) {
                        destinations.put(destinationLocation, fareInfo);
                    }
                } else {
                    // yay new destination!!
                    destinations.put(destinationLocation, fareInfo);
                }
            }
            catch(JSONException e) {
                continue;
            }
        }
        sortedDestinations.putAll(destinations);
        Log.d(TAG, sortedDestinations.toString());
        int count = 0;
        for (Map.Entry<String, JSONObject> entry : sortedDestinations.entrySet()) {
            try {
                count += 1;
                JSONObject fare = entry.getValue();
                Log.d("FARE: ", fare.toString());
                card = new FareCard(activity);

                card.title = "To the " + fare.getString("Theme");
                card.fare = fare.getString("LowestFare") + "+";
                card.origin = this.origin;
                card.destination = fare.getString("DestinationLocation");
                card.departureDateString = ((new DateMaker()).dateStringForCardFromResult(fare.getString("DepartureDateTime")));
                card.footer = "based on " + SabreDevStudio.DEFAULT_LENGTH_OF_STAY + " day stay";
                cards.add(card);
                Log.d(TAG, fare.toString());
                if (count == 5) { break; }
            } catch(JSONException e) {
                Log.d("ERROR: ", e.toString());
                continue;
            }
        }

        return cards;
    }
}
