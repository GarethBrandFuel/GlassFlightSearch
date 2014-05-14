package com.sabrelabs.glassflightsearch.card;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.sabrelabs.glassflightsearch.R;

import java.util.List;

/**
 * Created by mark on 5/5/14.
 * Adapter class that handles list of fare cards.
 */
public class FareCardAdapter extends CardScrollAdapter {
    private final List<FareCard> mCards;
    private LayoutInflater mInflater;

    public FareCardAdapter(List<FareCard> cards, Context context) {
        mInflater = LayoutInflater.from(context);
        mCards = cards;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public Object getItem(int position) {
        return mCards.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FareCard fCard = mCards.get(position);

        View view = null;
        view = mInflater.inflate(R.layout.fare_card, null);

//        TextView title = (TextView) view.findViewById(R.id.text_title);
//        title.setText(fCard.title);

        ((TextView) view.findViewById(R.id.text_title)).setText(fCard.title);
        ((TextView) view.findViewById(R.id.text_fare)).setText("$" + fCard.fare);
        ((TextView) view.findViewById(R.id.text_origin)).setText(fCard.origin);
        ((TextView) view.findViewById(R.id.text_destination)).setText(fCard.destination);
        ((TextView) view.findViewById(R.id.text_departureDate)).setText(fCard.departureDateString);
        ((TextView) view.findViewById(R.id.footer)).setText(fCard.footer);

        Log.d("Card Adapter", "VIEW " + view.toString());

        return view;

        //return mCards.get(position).getView(convertView, parent);
    }

    @Override
    public int getViewTypeCount() {
        return Card.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position){
        return mCards.get(position).getItemViewType();
    }

    @Override
    public int getPosition(Object item) {
        for (int i = 0; i < mCards.size(); i++) {
            if (getItem(i).equals(item)) {
                return i;
            }
        }
        return AdapterView.INVALID_POSITION;
    }
}

