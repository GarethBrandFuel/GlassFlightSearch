package com.sabrelabs.glassflightsearch.models;

import android.app.Activity;

import com.sabrelabs.glassflightsearch.card.FareCard;
import com.sabrelabs.glassflightsearch.util.DateMaker;
import com.sabrelabs.glassflightsearch.util.SabreDevStudio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by barrettclark on 4/29/14.
 */
public class LeadPriceCalendarApiResultSet extends ApiResultSet {
    JSONArray fareInfo;

    public LeadPriceCalendarApiResultSet (JSONObject results) {
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
        HashMap<String, String> lowestFare = this.lowestFare();
        HashMap<String, String> lowestNonstopFare = this.lowestNonstopFare();

        ArrayList<FareCard> cards = new ArrayList<FareCard>();

        FareCard card;

        card = new FareCard(activity);
        card.title = "Lowest Fare";
        card.fare = lowestFare.get("fare");
        card.origin = this.origin;
        card.destination = this.destination;
        card.departureDateString = ((new DateMaker()).dateStringForCardFromResult(lowestFare.get("departureDate")));
        card.footer = "based on " + SabreDevStudio.DEFAULT_LENGTH_OF_STAY + " day stay";
        cards.add(card);

        card = new FareCard(activity);
        card.title = "Lowest NonStop Fare";
        card.fare = lowestNonstopFare.get("fare");
        card.origin = this.origin;
        card.destination = this.destination;
        card.departureDateString = ((new DateMaker()).dateStringForCardFromResult(lowestNonstopFare.get("departureDate")));
        card.footer = "based on " + SabreDevStudio.DEFAULT_LENGTH_OF_STAY + " day stay";
        cards.add(card);

        return cards;
    }

    @Override
    public HashMap<String, String> lowestFare() {
        HashMap<String, String> result = new HashMap<String, String>();

        try {
            JSONObject lowestFareResult = lowFareResultOnAttribute(this.fareInfo, "LowestFare");

            result.put("fare", lowestFareResult.getString("LowestFare"));
            result.put("departureDate", lowestFareResult.getString("DepartureDateTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public HashMap<String, String> lowestNonstopFare() {
        HashMap<String, String> result = new HashMap<String, String>();

        try {
            JSONObject lowestFareResult = lowFareResultOnAttribute(this.fareInfo, "LowestNonStopFare");

            result.put("fare", lowestFareResult.getString("LowestNonStopFare"));
            result.put("departureDate", lowestFareResult.getString("DepartureDateTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
