package com.sabrelabs.glassflightsearch.models;

import android.app.Activity;
import android.util.Log;

import com.sabrelabs.glassflightsearch.card.FareCard;
import com.sabrelabs.glassflightsearch.util.DateMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by barrettclark on 4/29/14.
 */
public class InstaFlightApiResultSet extends ApiResultSet {
    private final String TAG = getClass().getSimpleName();
    JSONArray pricedItineraries;

    public InstaFlightApiResultSet(JSONObject results) {
        this.results = results;
        try {
            if (results != null) {
                this.pricedItineraries = results.getJSONArray("PricedItineraries");
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

        Log.d(TAG, "Lowest Fare " + lowestFare.toString() );
        Log.d(TAG, "Lowest Nonstop Fare " + lowestNonstopFare.toString() );

        card = new FareCard(activity);
        card.setText("Lowest Fare: " + lowestFare.get("fare") + "\nDeparture Date: " + lowestFare.get("outboundDepartureDateTime"));
        card.setFootnote("Length of Stay or Dates go here");
        card.title = "Lowest Fare";
        card.fare = lowestFare.get("fare");
        card.origin = this.origin;
        card.destination = this.destination;

        String outbound = ((new DateMaker()).dateStringForCardFromResult(lowestFare.get("outboundDepartureDateTime")));
        String inbound = ((new DateMaker()).dateStringForCardFromResult(lowestFare.get("returnDepartureDateTime")));
        card.departureDateString = outbound + " to " + inbound;

        cards.add(card);

        Log.d(TAG, "Lowest Fare Card " + card.toString() );

        card = new FareCard(activity);
        card.setText("First Lowest NonStop Fare: " + lowestNonstopFare.get("fare") + "\nDeparture Date: " + lowestNonstopFare.get("outboundDepartureDateTime"));
        card.setFootnote("Length of Stay or Dates go here");
        card.title = "Lowest NonStop Fare";
        card.fare = lowestNonstopFare.get("fare");
        card.origin = this.origin;
        card.destination = this.destination;

        outbound = ((new DateMaker()).dateStringForCardFromResult(lowestNonstopFare.get("outboundDepartureDateTime")));
        inbound = ((new DateMaker()).dateStringForCardFromResult(lowestNonstopFare.get("returnDepartureDateTime")));
        card.departureDateString = outbound + " to " + inbound;

        cards.add(card);

        return cards;
    }

    @Override
    public HashMap<String, String> lowestFare() {
        // results are sorted by totalfare, so we can just grab the first one
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            JSONObject pricedItinerary = pricedItineraries.getJSONObject(0);
            result = parseInstaPricedItinerary(pricedItinerary);
            Log.d(TAG, result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public HashMap<String, String> lowestNonstopFare() {
        HashMap<String, String> result = new HashMap<String, String>();
        JSONObject pricedItinerary;
        for (int i = 0; i < pricedItineraries.length(); i++) {
            try {
                pricedItinerary = pricedItineraries.getJSONObject(i);
                result = parseInstaPricedItinerary(pricedItinerary);
                if (result.get("direct") == "true") { break; } else { result = null; }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, result.toString());
        return result;
    }

    private HashMap<String, String> parseInstaPricedItinerary(JSONObject pricedItinerary) throws JSONException {
        HashMap<String, String> result = new HashMap<String, String>();
        JSONObject airItineraryPricingInfo = pricedItinerary.getJSONObject("AirItineraryPricingInfo");
        JSONObject itinTotalFare = airItineraryPricingInfo.getJSONObject("ItinTotalFare");
        JSONObject totalFare = itinTotalFare.getJSONObject("TotalFare");
        Double fare = totalFare.getDouble("Amount");

        JSONObject airItinerary = pricedItinerary.getJSONObject("AirItinerary");
        JSONObject originDestinationOptions = airItinerary.getJSONObject("OriginDestinationOptions");
        JSONArray originDestinationOption = originDestinationOptions.getJSONArray("OriginDestinationOption");
        JSONObject outboundOriginDestinationOption = originDestinationOption.getJSONObject(0);
        JSONObject inboundOriginDestinationOption = originDestinationOption.getJSONObject(1);
        JSONArray outboundFlightSegments = outboundOriginDestinationOption.getJSONArray("FlightSegment");
        JSONObject initialOutboundFlightSegment = outboundFlightSegments.getJSONObject(0);
        JSONObject finalOutboundFlightSegment = outboundFlightSegments.getJSONObject(outboundFlightSegments.length()-1);
        JSONArray inboundFlightSegments = inboundOriginDestinationOption.getJSONArray("FlightSegment");
        JSONObject initialInboundFlightSegment = inboundFlightSegments.getJSONObject(0);
        JSONObject finalInboundFlightSegment = inboundFlightSegments.getJSONObject(inboundFlightSegments.length()-1);

        // TODO: parse these into a Date
        String outboundDepartureDateTime = initialOutboundFlightSegment.getString("DepartureDateTime");
        String outboundArrivalDateTime = finalOutboundFlightSegment.getString("ArrivalDateTime");
        String returnDepartureDateTime = initialInboundFlightSegment.getString("DepartureDateTime");
        String returnArrivalDateTime = finalInboundFlightSegment.getString("ArrivalDateTime");

        String airline = initialOutboundFlightSegment.getJSONObject("MarketingAirline").getString("Code");
        Integer flightDuration = outboundOriginDestinationOption.getInt("ElapsedTime");

        // Direct (without connections) == 0
        Integer connectionCount;
        if (outboundFlightSegments.length() > inboundFlightSegments.length()) {
            connectionCount = outboundFlightSegments.length() - 1;
        } else {
            connectionCount = inboundFlightSegments.length() - 1;
        }
        Boolean direct = connectionCount == 0;

        result.put("outboundDepartureDateTime", outboundDepartureDateTime);
        result.put("outboundArrivalDateTime", outboundArrivalDateTime);
        result.put("returnDepartureDateTime", returnDepartureDateTime);
        result.put("returnArrivalDateTime", returnArrivalDateTime);
        // NOTE: there could be multiple airlines given connecting flights - just taking 1 here
        result.put("airline", airline);
        result.put("flightDuration", String.valueOf(flightDuration));
        result.put("connectionCount", String.valueOf(connectionCount));
        result.put("direct", String.valueOf(direct));
        result.put("fare", String.valueOf(fare));
        return result;
    }
}
