package com.sabrelabs.glassflightsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.sabrelabs.glassflightsearch.card.FareCard;
import com.sabrelabs.glassflightsearch.card.FareCardAdapter;
import com.sabrelabs.glassflightsearch.models.ApiResultSet;
import com.sabrelabs.glassflightsearch.util.AsyncRequest;
import com.sabrelabs.glassflightsearch.util.VoiceStringMatchSimple;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    private final String TAG = getClass().getSimpleName();
    private ArrayList<String> voiceResults;

    private ArrayList<FareCard> mCards =  new ArrayList<FareCard>();
    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    LocationProvider locationProvider;

    // Visible for testing.
    CardScrollView getScroller() {
        return mCardScroller;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationProvider = LocationProvider.getInstance();

        HashMap<String, String> matchResults = handleCommand(getIntent());
        try {
            ApiResultSet results = new AsyncRequest().execute(matchResults).get();
            Log.d(TAG, String.valueOf(results.getResults()));

            JSONObject jsonResults = results.getResults();
            if (jsonResults != null) {
                mCards = results.displayCards(this);

                activateCards();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCardScroller.activate();
    }

    private HashMap<String, String> handleCommand(Intent intent) {
        voiceResults = intent.getExtras().getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
        String voiceResultsString = voiceResults.toString();
        Log.d(TAG, voiceResultsString);

        // This is where you do some magic regex voodoo
        VoiceStringMatchSimple vsm = new VoiceStringMatchSimple();
        vsm.setVoiceResults(voiceResultsString);
        HashMap<String, String> matchResults = vsm.findMatch();

//        mCards.add(CARDS, new Card(this).setText("Searching..."));
//        activateCards();

        return matchResults;
    }

    private void activateCards() {
        mAdapter = new FareCardAdapter(mCards, this);

        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(mAdapter);
        setContentView(mCardScroller);
        setCardScrollerListener();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }

    /**
     * Different type of activities can be shown, when tapped on a card.
     */
    private void setCardScrollerListener() {

        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "Clicked view at position " + position + ", row-id " + id);
                    int soundEffect = Sounds.TAP;

                    //startActivity(new Intent(MainActivity.this, CardsActivity.class));

                    // Play sound.
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    am.playSoundEffect(soundEffect);
                }
        });
    }

//    private class ExampleCardScrollAdapter extends CardScrollAdapter {
//
//        @Override
//        public int getPosition(Object item) {
//            return mCards.indexOf(item);
//        }
//
//        @Override
//        public int getCount() {
//            return mCards.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mCards.get(position);
//        }
//
//        /**
//         * Returns the amount of view types.
//         */
//        @Override
//        public int getViewTypeCount() {
//            return Card.getViewTypeCount();
//        }
//
//        /**
//         * Returns the view type of this card so the system can figure out
//         * if it can be recycled.
//         */
//        @Override
//        public int getItemViewType(int position){
//            return mCards.get(position).getItemViewType();
//        }
//
//        @Override
//        public View getView(int position, View convertView,
//                            ViewGroup parent) {
//            return  mCards.get(position).getView(convertView, parent);
//        }
//    }
}
