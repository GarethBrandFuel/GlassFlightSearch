package com.sabrelabs.glassflightsearch.models;

import android.app.Activity;

import com.sabrelabs.glassflightsearch.card.FareCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by barrettclark on 4/29/14.
 */
public class ApiResultSet implements ResultSetDetails {
    protected JSONObject results;
    public String origin;
    public String destination;

    public JSONObject getResults() {
        return this.results;
    }

    @Override
    public HashMap<String, String> lowestFare() {
        return null;
    }

    @Override
    public HashMap<String, String> lowestNonstopFare() {
        return null;
    }

    @Override
    public ArrayList<FareCard> displayCards(Activity activity) {
        return null;
    }

    protected JSONObject lowFareResultOnAttribute(JSONArray fares, String attributeName) throws JSONException {
        JSONObject lowFareResult = fares.getJSONObject(0);

        for (int i = 0; i < fares.length(); i++) {
            JSONObject fareResult = fares.getJSONObject(i);
            if (fareResult.getString(attributeName).equals("N/A")) { continue; }
            if (fareResult.getDouble(attributeName) < lowFareResult.getDouble(attributeName)) {
                lowFareResult = fareResult;
            }
        }

        return lowFareResult;
    }
}
