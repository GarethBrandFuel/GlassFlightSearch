package com.sabrelabs.glassflightsearch.card;

import android.content.Context;

import com.google.android.glass.app.Card;

/**
 * Created by mark on 5/5/14.
 */
public class FareCard extends Card {

    public String title;
    public String footer;
    public String destination;
    public String origin;
    public String fare;
    public String airline;
    public String departureDateString;

    public FareCard(Context context) {
        super(context);
    }


}
