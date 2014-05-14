package com.sabrelabs.glassflightsearch.models;

import org.json.JSONObject;

/**
 * Created by barrettclark on 4/29/14.
 */
public class ThemeApiResultSet extends ApiResultSet {
    public ThemeApiResultSet(JSONObject results) {
        this.results = results;
    }
}
