package com.stevensadler.android.blocspot.api.yelp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.stevensadler.android.blocspot.ui.BlocspotApplication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Steven on 3/17/2016.
 */
public class YelpQueryIntentService extends IntentService {

    private static String TAG = YelpQueryIntentService.class.getSimpleName();

    public static final String BROADCAST_FILTER = "YelpQueryIntentService.BROADCAST_FILTER";

    public YelpQueryIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String dataString = intent.getDataString();


        // do something based on the dataString
        Log.v(TAG, "onHandleIntent need to start a query with data " + dataString);
        Log.v(TAG, "onHandleIntent " + intent.getStringExtra("term"));
        Log.v(TAG, "onHandleIntent " + intent.getStringExtra("ll"));
        Log.v(TAG, "onHandleIntent " + intent.getDoubleExtra("latitude", 0d));
        Log.v(TAG, "onHandleIntent " + intent.getDoubleExtra("longitude", 0d));

        String queryResult = BlocspotApplication.getSharedYelpAPI().searchForBusinessesByLatLong(
                intent.getStringExtra("term"),
                intent.getDoubleExtra("latitude", 0d),
                intent.getDoubleExtra("longitude", 0d)
        );

        Log.v(TAG, "queryResult " + queryResult);
        intent.putExtra("queryResult", queryResult);
        //sendBroadcast(intent, YelpQueryIntentService.BROADCAST_FILTER);

        Log.v(TAG, "queryResult 2 ");

        // for testing, parse the queryResult JSON to a java object
        // then move this code to DataSource?
        //parseJSONtoJava(queryResult);

        BlocspotApplication.getSharedDataSource().setYelpPointsOfInterest(queryResult);
        this.stopSelf();

    }

    public void parseJSONtoJava(String jsonString) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(jsonString);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray businesses = (JSONArray) jsonObject.get("businesses");

            for (Object object : businesses) {
                JSONObject business = (JSONObject) object;
                String name = (String) business.get("name");
                JSONObject location = (JSONObject) business.get("location");
                JSONObject coordinate = (JSONObject) location.get("coordinate");
                double latitude = (double) coordinate.get("latitude");
                double longitude = (double) coordinate.get("longitude");

                Log.v(TAG, "business name : " + name + " " + latitude + " " + longitude);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
