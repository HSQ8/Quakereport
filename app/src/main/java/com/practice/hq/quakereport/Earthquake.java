package com.practice.hq.quakereport;

/**
 * Created by HQ on 6/13/2017.
 */

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private long mTimeInMillisecond;
    private String mUrl;

    public Earthquake(double _magnitude, String _Location, long _Time, String _url) {
        mMagnitude = _magnitude;
        mLocation = _Location;
        mTimeInMillisecond = _Time;
        mUrl = _url;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public long getmTimeInMillisecond() {
        return mTimeInMillisecond;
    }

    public String getmUrl(){return mUrl;}
}