package com.sabrelabs.glassflightsearch.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by barrettclark on 5/6/14.
 */
public class LowestFareValueComperator implements Comparator<String> {
    Map<String, JSONObject> base;
    public LowestFareValueComperator(Map<String, JSONObject> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(String a, String b) {
        try {
            if (base.get(a).getDouble("LowestFare") <= base.get(b).getDouble("LowestFare")) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
