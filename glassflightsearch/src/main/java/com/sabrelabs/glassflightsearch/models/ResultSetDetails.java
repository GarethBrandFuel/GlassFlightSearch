package com.sabrelabs.glassflightsearch.models;

import android.app.Activity;

import com.sabrelabs.glassflightsearch.card.FareCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mark on 5/1/14.
 */
public interface ResultSetDetails {
    public HashMap<String, String> lowestFare();
    public HashMap<String, String> lowestNonstopFare();
    public ArrayList<FareCard> displayCards(Activity activity);

}
