package com.example.bonjour;

/**
 * Created by Sanjay Kumar on 4/9/2015.
 */
public class locationAddress {
    double lat;
    double lon;

    public locationAddress() {
        this.lat = 0;
        this.lon=0;
    }

    public locationAddress(double lat,double lon) {
        this.lat = lat;
        this.lon=lon;
    }

    public void setAddress(double lat,double lon) {
        this.lat = lat;
        this.lon=lon;
    }

    public double getLat() {
        return this.lat;
    }
    public double getLon() {
        return this.lon;
    }


}
