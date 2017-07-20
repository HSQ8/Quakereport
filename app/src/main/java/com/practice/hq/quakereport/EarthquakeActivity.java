/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.practice.hq.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.quakereport.R;

import java.util.ArrayList;


public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>>{
    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.v("oncreate","oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Create a new {@link ArrayAdapter} of earthquakes
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        getLoaderManager().initLoader(1, null, this);

    }

    private void UpdateUi(final ArrayList<Earthquake> _earthquake) {



        Log.v("updateUI","updateUI");
        final ListView earthquakeListView = (ListView) findViewById(R.id.list);
        EarthquakeInstanceAdapter adapter = new EarthquakeInstanceAdapter(getBaseContext(), _earthquake);
        earthquakeListView.setAdapter(adapter);

        if(_earthquake.isEmpty()) {
            TextView emptyView = (TextView) findViewById(R.id.emptyView);
            emptyView.setText("no earthquakes found");
            earthquakeListView.setEmptyView(emptyView);
        }

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            // If no connectivity, cancel task and update Callback with null data.

            TextView emptyView = (TextView) findViewById(R.id.emptyView);
            emptyView.setText("no network found");
            earthquakeListView.setEmptyView(emptyView);
        }


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = _earthquake.get(i).getmUrl();
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                urlIntent.setData(Uri.parse(url));

                if (urlIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(urlIntent);
                } else {
                    Log.v("Intent", "No Intent available to handle action");
                }
            }
        };
        earthquakeListView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.v("onCreateLoader","onCreateLoader");
        return new EarthquakeLoader(EarthquakeActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {
        Log.v("onLoadFinished","onLoadFinished");

        ProgressBar progressCircle = (ProgressBar)findViewById(R.id.indeterminateBar);
        progressCircle.setVisibility(View.GONE);
        UpdateUi(earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {
        Log.v("onLoaderReset","onLoaderReset");
        UpdateUi(new ArrayList<Earthquake>());
    }
}