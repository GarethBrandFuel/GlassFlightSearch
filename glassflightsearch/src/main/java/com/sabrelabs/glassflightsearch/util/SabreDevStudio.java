package com.sabrelabs.glassflightsearch.util;

import android.location.Location;
import android.util.Log;

import com.sabrelabs.glassflightsearch.LocationProvider;
import com.sabrelabs.glassflightsearch.models.ApiResultSet;
import com.sabrelabs.glassflightsearch.models.CityPairApiResultSet;
import com.sabrelabs.glassflightsearch.models.DestinationFinderApiResultSet;
import com.sabrelabs.glassflightsearch.models.InstaFlightApiResultSet;
import com.sabrelabs.glassflightsearch.models.LeadPriceCalendarApiResultSet;
import com.sabrelabs.glassflightsearch.models.ThemeApiResultSet;
import com.sabrelabs.glassflightsearch.models.TravelThemeApiResultSet;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by barrettclark on 4/25/14.
 */
public class SabreDevStudio {
    public String accessToken;
    private final String TAG = getClass().getSimpleName();
    private Credential credential;
    public static final String DEFAULT_LENGTH_OF_STAY = "3";

    private Location getLastLocation() {
      return LocationProvider.getInstance().getLocation();
    }

    public SabreDevStudio() throws IOException {
        credential = new Credential();
    }

    public String getAccessToken() throws Exception {
        String accessToken = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://" + credential.getUri() + "/v1/auth/token");

        post.setHeader("Authorization", "Basic " + credential.getEncodedCredentials());
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpResponse response = client.execute(post);
        int code = response.getStatusLine().getStatusCode();
        if (code == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String json = reader.readLine();
            JSONObject jsonObj = null;

            jsonObj = new JSONObject(json);
            accessToken = (String) jsonObj.get("access_token");
            Integer expiry = (Integer) jsonObj.get("expires_in");
            Log.d(TAG, "Expires: " + expiry);
        }

        this.accessToken = accessToken;
        return accessToken;
    }

    public TravelThemeApiResultSet getTravelThemeLookup() throws Exception {
        String endpoint = "/v1/shop/themes";
        JSONObject response = get(endpoint);
        TravelThemeApiResultSet travelThemeApiResultSet = new TravelThemeApiResultSet(response);
        return travelThemeApiResultSet;
    }

    public ThemeApiResultSet getThemeAirportLookup(String theme) throws Exception {
        String endpoint = "/v1/shop/themes/" + theme;
        JSONObject response = get(endpoint);
        ThemeApiResultSet themeApiResultSet = new ThemeApiResultSet(response);
        return themeApiResultSet;
    }

    public DestinationFinderApiResultSet getDestinationAirShop(HashMap<String, String> options) throws Exception {
        // NOTE: no sort order available for this request
        // NOTE: departuredate / returndate OR earliestdeparturedate / latestdeparturedate are optional timebox parameters
        String endpoint = "/v1/shop/flights/fares";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("origin", getOrigin(options.get("origin"))));
        // NOTE: the theme is not verified against the TravelThemeLookup endpoint
        params.add(new BasicNameValuePair("theme", options.get("category")));
        params.add(new BasicNameValuePair("lengthofstay", DEFAULT_LENGTH_OF_STAY));

        String requestEndpoint = formatEndpointWithQueryString(endpoint, params);
        JSONObject response = get(requestEndpoint);
        DestinationFinderApiResultSet destinationFinderApiResultSet = new DestinationFinderApiResultSet(response);

        destinationFinderApiResultSet.origin = getOrigin(options.get("origin"));

        return destinationFinderApiResultSet;
    }

    public LeadPriceCalendarApiResultSet getLeadPriceCalendar(HashMap<String, String> options) throws Exception {
        // NOTE: no sort order available for this request
        String endpoint = "/v1/shop/flights/fares";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("origin", getOrigin(options.get("origin"))));
        params.add(new BasicNameValuePair("destination", options.get("destination")));
        String lengthOfStay = options.get("duration");
        if (lengthOfStay == null) { lengthOfStay = DEFAULT_LENGTH_OF_STAY; }
        params.add(new BasicNameValuePair("lengthofstay", lengthOfStay));

        String month = options.get("timeframe");
        if (month != null) {
            DateMaker dateMaker = new DateMaker();
            Calendar cal = Calendar.getInstance();
            Integer day = cal.get(Calendar.DAY_OF_MONTH);
            Calendar departureDate = dateMaker.translate(month, day);
            params.add(new BasicNameValuePair("departuredate", dateMaker.stringify(departureDate)));
        }

        String requestEndpoint = formatEndpointWithQueryString(endpoint, params);
        JSONObject response = get(requestEndpoint);
        LeadPriceCalendarApiResultSet leadPriceCalendarApiResultSet = new LeadPriceCalendarApiResultSet(response);

        leadPriceCalendarApiResultSet.origin = getOrigin(options.get("origin"));
        leadPriceCalendarApiResultSet.destination = options.get("destination");

        return leadPriceCalendarApiResultSet;
    }

    public InstaFlightApiResultSet getInstaflightsSearch(HashMap<String, String> options) throws Exception {
        String endpoint = "/v1/shop/flights";
        WordToNumber wordToNumber = new WordToNumber();
        DateMaker dateMaker = new DateMaker();
        Integer departureDay = wordToNumber.translate(options.get("departureday"));
        Integer returnDay = wordToNumber.translate(options.get("returnday"));
        Calendar departureDate = dateMaker.translate(options.get("month"), departureDay);
        Calendar returnDate = dateMaker.translate(options.get("month"), returnDay);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("origin", getOrigin(options.get("origin"))));
        params.add(new BasicNameValuePair("destination", options.get("destination")));
        params.add(new BasicNameValuePair("departuredate", dateMaker.stringify(departureDate)));
        params.add(new BasicNameValuePair("returndate", dateMaker.stringify(returnDate)));
        params.add(new BasicNameValuePair("limit", "200"));
        params.add(new BasicNameValuePair("sortby", "totalfare"));
        params.add(new BasicNameValuePair("order", "asc"));
        params.add(new BasicNameValuePair("sortby2", "departuretime"));
        params.add(new BasicNameValuePair("order2", "dsc"));

        String requestEndpoint = formatEndpointWithQueryString(endpoint, params);
        JSONObject response = get(requestEndpoint);
        InstaFlightApiResultSet instaFlightResultSet = new InstaFlightApiResultSet(response);

        instaFlightResultSet.origin = getOrigin(options.get("origin"));
        instaFlightResultSet.destination = options.get("destination");

        return instaFlightResultSet;
    }

    public ApiResultSet getLowFareForecast(HashMap<String, String> options) throws Exception {
        String endpoint = "/v1/forecast/flights/fares";
        return null;
    }

    public ApiResultSet getFareRange(HashMap<String, String> options) throws Exception {
        String endpoint = "/v1/historical/flights/fares";
        return null;
    }

    public ApiResultSet getTravelSeasonality(String destination) throws Exception {
        String endpoint = "/v1/historical/flights/" + destination + "/seasonality";
        return null;
    }

    public CityPairApiResultSet getCityPairsLookup() throws Exception {
        String endpoint = "/v1/lists/airports/supported/origins-destinations";
        JSONObject response = get(endpoint);
        CityPairApiResultSet cityPairApiResultSet = new CityPairApiResultSet(response);
        return cityPairApiResultSet;
    }

    public ApiResultSet getMultiAirportCityLookup(HashMap<String, String> options) throws Exception {
        String endpoint = "/v1/lists/cities";
        return null;
    }

    public ApiResultSet getAirportsAtCitiesLookup(HashMap<String, String> options) throws Exception {
        String endpoint = "/v1/lists/airports";
        return null;
    }

    private String formatEndpointWithQueryString(String endpoint, List<NameValuePair> params) {
        String paramsString = URLEncodedUtils.format(params, "UTF-8");
        return endpoint + "?" + paramsString;
    }

    private String getOrigin(String origin) {
        if (origin == null) {
            // TODO: current location -- get airport code
            // NOTE: parking this for now -- http://airports.pidgets.com/v1/airports?near=32.9698367,-97.0136393&format=json
            Location location = getLastLocation();
            Log.d(TAG, String.valueOf(location.getLatitude()));
            origin = "DFW";
        }
        return origin;
    }

    private JSONObject get(String endpoint) throws Exception {
        JSONObject jsonObject = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("https://" + credential.getUri() + endpoint);
        Log.d(TAG, String.valueOf(get.getURI()));
        // NOTE: access token refresh not handled -- fresh access token for each request
        get.setHeader("Authorization", "Bearer " + getAccessToken());
        get.setHeader("Accept-Encoding", "gzip");
        HttpResponse response = client.execute(get);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            Header encoding = entity.getContentEncoding();
            InputStream inputStream;
            BufferedReader reader;
            if (encoding != null) {
                if (encoding.getValue().equals("gzip") ||
                        encoding.getValue().equals("zip") ||
                        encoding.getValue().equals("application/x-gzip-compressed")) {
                    inputStream = new GZIPInputStream(entity.getContent());
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                } else {
                    inputStream = entity.getContent();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                }
            } else {
                inputStream = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(inputStream));
            }
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            jsonObject = new JSONObject(sb.toString());
        } else {
            Log.e(TAG, "HTTP ERROR: " + String.valueOf(statusCode));
        }
        return jsonObject;
    }
}
