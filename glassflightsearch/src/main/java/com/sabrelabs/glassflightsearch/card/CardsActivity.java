package com.sabrelabs.glassflightsearch.card;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.android.glass.app.Card;
import com.google.android.glass.app.Card.ImageLayout;
import com.google.android.glass.widget.CardScrollView;
import com.sabrelabs.glassflightsearch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barrettclark on 4/22/14.
 *
 * Creates a card scroll view with examples of different image layout cards.
 */
public class CardsActivity extends Activity {
    private CardScrollView mCardScroller;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(new CardAdapter(createCards(this)));
        setContentView(mCardScroller);
    }

    /**
     * Create list of cards that showcase different type of {@link Card} API.
     */
    private List<Card> createCards(Context context) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(getImagesCard(context)
                .setImageLayout(ImageLayout.LEFT)
                .setText(R.string.text_left_images_layout_card));
        cards.add(getImagesCard(context)
                .setImageLayout(ImageLayout.FULL)
                .setText(R.string.text_full_images_layout_card));
        return cards;
    }

    private Card getImagesCard(Context context) {
        Card card = new Card(context);
        card.addImage(R.drawable.codemonkey1);
        card.addImage(R.drawable.codemonkey2);
        card.addImage(R.drawable.codemonkey3);
        card.addImage(R.drawable.codemonkey4);
        card.addImage(R.drawable.codemonkey5);
        return card;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }
}
