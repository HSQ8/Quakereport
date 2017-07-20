
package com.practice.hq.quakereport;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.practice.hq.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public class QueryUtils {
    private URL mURL;

    public QueryUtils(String _url) {
        mURL = createUrl(_url);
    }

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        Log.v("create URl", url.toString());
        return url;
    }

    public String makeHttpRequest() throws IOException {

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) mURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        Log.v("return Json Response",jsonResponse);
        return jsonResponse;
    }

    @NonNull
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        Log.v("ReadfromStream",output.toString());
        return output.toString();
    }

    public ArrayList<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONObject jsonObject = new JSONObject(earthquakeJSON);
            JSONArray featureArray = jsonObject.getJSONArray("features");
            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject properties = featureArray.getJSONObject(i).getJSONObject("properties");
                earthquakes.add(new Earthquake(properties.getDouble("mag"),
                        properties.getString("place"),
                        properties.getLong("time"),
                        properties.getString("url")));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        SystemClock.sleep(10000);
        return earthquakes;
    }

}