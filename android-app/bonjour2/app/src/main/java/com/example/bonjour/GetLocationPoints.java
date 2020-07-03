package com.example.bonjour;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;
import java.util.Locale;

/**
 * Created by Sanjay Kumar on 4/9/2015.
 */
public class GetLocationPoints extends FragmentActivity {
    private GoogleMap map;
    private SupportMapFragment fragment;
    private LatLngBounds latlngBounds;
    private Button routeSet;
    private Polyline newPolyline;
    private boolean isTravelingToParis = false;
    private int width, height;
    private locationAddress start=new locationAddress(),end=new locationAddress();
    private RadioGroup radioGroup;
    private int flag=1;
    String points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getlocationpoints);

        getSreenDimanstions();
        fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        map = fragment.getMap();

        map.setMyLocationEnabled(true);


        map.getUiSettings().setMyLocationButtonEnabled(true);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
//***********************Marker
        final Marker[] marker = new Marker[1];
        marker[0]=null;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {

                if (flag == 1) {
                    start.setAddress(arg0.latitude,arg0.longitude);
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(start.getLat(), start.getLon())).title("Start ");
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    map.addMarker(marker);

                }
               else
                {
                    end.setAddress(arg0.latitude,arg0.longitude);
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(end.getLat(), end.getLon())).title("End ");
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    map.addMarker(marker);

                }


            }
        });
//***********************Marker
        routeSet = (Button) findViewById(R.id.routeSet);
        routeSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                points=String.valueOf(start.getLat())+",";
                points+=String.valueOf(start.getLon())+",";
                points+=String.valueOf(end.getLat())+",";
                points+=String.valueOf(end.getLon());
                points+=",Start,Stop";
                String dest=getCompleteAddressString(end.getLat(),end.getLon());

                points+=","+dest;

                Intent i=new Intent(GetLocationPoints.this,NavigationActivity.class);
                i.putExtra("latlon",points);
                startActivityForResult(i, 3);

            }
        });
    }

    private void getSreenDimanstions()
    {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_start:
                if (checked)
                    flag=1;
                    break;
            case R.id.radio_end:
                if (checked)
                    flag=0;
                    break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==3)
        {
         //   String points=data.getStringExtra("Marker");
            Intent intent=new Intent();
            intent.putExtra("points",points);
            setResult(2,intent);
            finish();

        }

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
/*
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }*/
                if(returnedAddress.getSubLocality().toString()!=null) {
                    strReturnedAddress.append(returnedAddress.getSubLocality()).append("( ");
                }
                else
                {
                    strReturnedAddress.append("( ");
                }
                strReturnedAddress.append(returnedAddress.getLocality()).append(" )");
                strAdd = strReturnedAddress.toString();

            } else {
                Log.w("My Current loction address", "No Address returned!");
                strAdd="Islamabad";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
            strAdd="Islamabad";
        }
        return strAdd;
    }
}
