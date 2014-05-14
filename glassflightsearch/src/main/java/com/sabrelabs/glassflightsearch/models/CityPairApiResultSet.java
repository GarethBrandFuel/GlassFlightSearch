package com.sabrelabs.glassflightsearch.models;

import org.json.JSONObject;

/**
 * Created by barrettclark on 5/3/14.
 */
public class CityPairApiResultSet extends ApiResultSet {
    public CityPairApiResultSet(JSONObject results) {
        this.results = results;
    }
}
