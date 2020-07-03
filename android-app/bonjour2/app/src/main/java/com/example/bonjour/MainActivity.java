package com.example.bonjour;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback
{
    //private static  boolean running = true;
    private static final int SERVICES_ERROR = 433;
    //private static final float CamZoom = 15;
    //private  Thread t;
    final Context c=this;
    //LocationClient mLocClient;
    Bonjour bonjour;
    private GoogleMap mMap;
    SessionManager session;
    GoogleApiClient mGoogleApiClient;
    boolean updated=false;
    ArrayList<NearbyUser> ulist=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bonjour=new Bonjour(getApplicationContext());
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if(updated==false)
                {
                    int id= Integer.parseInt(user.get(SessionManager.KEY_ID));
                    MainActivity.this.UpdateLocation(id, location,true);
                    updated=true;
                }

                //makeUseOfNewLocation(location);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);

        if(isServicesOK())
        {
            initMap();

        }
        else
        {
        }

    }

    public boolean isServicesOK()
    {
        int isAvailable= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(isAvailable== ConnectionResult.SUCCESS)
        {
            return true;
        }
        else if(GooglePlayServicesUtil.isUserRecoverableError(isAvailable))
        {
            Dialog d=GooglePlayServicesUtil.getErrorDialog(isAvailable, this, SERVICES_ERROR);
            d.show();
        }
        else
        {
            Toast.makeText(this, "Cant Connect", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public boolean initMap()
    {
        if(mMap==null)
        {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            return true;
        }
        return (mMap!=null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        //Location loc=mMap.getMyLocation();
    }

    public void UpdateLocation(final int id,Location loc,boolean check) {
        final User[] temp = {new User()};

        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h = new BonjourServiceHandler();
                try {
                    temp[0] = h.getUser(id);
                } catch (Exception e) {
                    showToastFromBackground(e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            showToastFromBackground(e.getMessage());
        }

        temp[0].setLatitude(loc.getLatitude());
        temp[0].setLongitude(loc.getLongitude());
        final User[] arguser = {new User()};
        final Boolean[] status = new Boolean[1];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h = new BonjourServiceHandler();
                try {
                    arguser[0] = h.updateUser(temp[0]);
                    status[0] = true;
                } catch (Exception e) {
                    status[0] = false;
                    showToastFromBackground(e.getMessage());
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            showToastFromBackground(e.getMessage());
        }

        if (status[0] == true) {
            if (check == true) {
                ShowNearbyPeopleNow(loc,id);
            }
        }

    }

    public void ShowNearbyPeopleNow(Location loc,int id)
    {
        LatLng latlng=new LatLng(loc.getLatitude(),loc.getLongitude());
        findUsers(latlng,id);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!marker.getTitle().equalsIgnoreCase("me"))
                {
                    final int id= Integer.parseInt(marker.getTitle());
                    Intent i =new Intent(MainActivity.this,ProfileView.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(ulist.get(id));
                    i.putExtra("profiledata",myJson);
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });
    }

    public void findUsers(final LatLng latlng,final int id) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    ulist = h.getNearbyUsers(id);
                    if(ulist.size()>0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Me"));
                                builder.include(marker.getPosition());
                                for (int i = 0; i < ulist.size(); i++) {
                                    LatLng ll2 = new LatLng(ulist.get(i).getLatitude(), ulist.get(i).getLongitude());
                                    if(ulist.get(i).isFriend)
                                    {
                                        marker = mMap.addMarker(new MarkerOptions().position(ll2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(String.valueOf(i)));
                                    }
                                    else
                                    {
                                        marker = mMap.addMarker(new MarkerOptions().position(ll2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(String.valueOf(i)));
                                    }

                                    builder.include(marker.getPosition());
                                }
                                LatLngBounds bounds = builder.build();
                                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,16.0f));
                                int padding = 35; // offset from edges of the map in pixels
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                mMap.animateCamera(cu);
                            }
                        });
                    }


                } catch (Exception e) {
                    showToastFromBackground(e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            showToastFromBackground(e.getMessage());
        }
    }
    private LatLng getLastKnownLocation()
    {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
        return userLocation;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

}