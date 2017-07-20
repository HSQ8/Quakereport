package com.practice.hq.quakereport;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.quakereport.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HQ on 6/13/2017.
 */

public class EarthquakeInstanceAdapter extends ArrayAdapter<Earthquake> {
    // private ArrayList<Earthquake> mList;
    public EarthquakeInstanceAdapter(Context context, ArrayList<Earthquake> _list) {
        super(context, 0, _list);
        //  mList = _list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Earthquake instance = getItem(position);
        View listitemview = convertView;
        if (listitemview == null) {
            listitemview = LayoutInflater.from(getContext()).inflate(R.layout.earthquakeinstance_layout, parent, false);
        }
        TextView magnitude = (TextView) listitemview.findViewById(R.id.magnitude);
        magnitude.setText(formatMagnitude(instance.getmMagnitude()));

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        magnitudeCircle.setColor(getMagnitudeColor(instance.getmMagnitude()));

        Date date = new Date(instance.getmTimeInMillisecond());

        TextView quakedate = (TextView) listitemview.findViewById(R.id.date);
        quakedate.setText(formatDate(date));

        TextView quaketime = (TextView) listitemview.findViewById(R.id.time);
        quaketime.setText(formatTime(date));

        String[] splitLocation;
        if (instance.getmLocation().contains("of")) {
            splitLocation = instance.getmLocation().split("of ");
            splitLocation[0] = splitLocation[0] + "of";
        } else {
            splitLocation = new String[2];
            splitLocation[0] = "near the";
            splitLocation[1] = instance.getmLocation();
        }

        TextView orientation = (TextView) listitemview.findViewById(R.id.orientation);
        orientation.setText(splitLocation[0]);

        TextView city = (TextView) listitemview.findViewById(R.id.city);
        city.setText(splitLocation[1]);
        city.setTypeface(null, Typeface.BOLD);
        city.setTextColor(Color.DKGRAY);


        return listitemview;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude) {
        int color = R.color.colorAccent;
        if (magnitude < 2)
            color = R.color.magnitude1;
        else if (magnitude < 3)
            color = R.color.magnitude2;
        else if (magnitude < 4)
            color = R.color.magnitude3;
        else if (magnitude < 5)
            color = R.color.magnitude4;
        else if (magnitude < 6)
            color = R.color.magnitude5;
        else if (magnitude < 7)
            color = R.color.magnitude6;
        else if (magnitude < 8)
            color = R.color.magnitude7;
        else if (magnitude < 9)
            color = R.color.magnitude8;
        else if (magnitude < 10)
            color = R.color.magnitude9;
        else if (magnitude >= 10)
            color = R.color.magnitude10plus;
        return ContextCompat.getColor(getContext(), color);
    }

}