package com.practice.hq.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HQ on 7/18/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {
    private URL mUrl;
    private String magLowerLimit = "4";
    private String EventnumberUpperLimit = "20";
    private final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=" + magLowerLimit + "&limit="
            + EventnumberUpperLimit;

    public EarthquakeLoader(Context _context) {
        super(_context);
    }

    public EarthquakeLoader(Context _context, URL _url) {
        super(_context);
        mUrl = _url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {

        QueryUtils myQuery = new QueryUtils(USGS_REQUEST_URL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = myQuery.makeHttpRequest();
        } catch (IOException e) {
            // TODO Handle the IOException
        }
        // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
        return myQuery.extractFeatureFromJson(jsonResponse);
    }
}