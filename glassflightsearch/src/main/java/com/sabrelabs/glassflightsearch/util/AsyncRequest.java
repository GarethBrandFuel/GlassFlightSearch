package com.sabrelabs.glassflightsearch.util;

import android.os.AsyncTask;

import com.sabrelabs.glassflightsearch.models.ApiResultSet;

import java.util.HashMap;

/**
 * Created by barrettclark on 4/29/14.
 */
public class AsyncRequest extends AsyncTask<HashMap<String, String>, String, ApiResultSet> {
    @Override
    protected ApiResultSet doInBackground(HashMap<String, String>... params) {
        ApiResultSet results = null;
        HashMap<String, String> options = params[0];
        try {
            SabreDevStudio sabreDevStudio = new SabreDevStudio();
            if (options.get("match") == "category" || options.get("match") == "categoryIATA") {
                results = sabreDevStudio.getDestinationAirShop(options);
            } else if (options.get("match") == "destinationDateRange" || options.get("match") == "destinationDateRangeIATA") {
                results = sabreDevStudio.getInstaflightsSearch(options);
            } else {
                results = sabreDevStudio.getLeadPriceCalendar(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
