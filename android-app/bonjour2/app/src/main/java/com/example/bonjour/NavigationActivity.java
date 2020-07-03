package com.example.bonjour;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static com.example.bonjour.GMapV2Direction.MODE_DRIVING;

public class NavigationActivity extends FragmentActivity {

    private static LatLng Start = new LatLng(52.37518, 4.895439);
    private static LatLng End = new LatLng(48.856132, 2.352448);
    private GoogleMap map;
    private SupportMapFragment fragment;
    private LatLngBounds latlngBounds;
    private Button bNavigation;
    private Polyline newPolyline;
    private int width, height;


 /*
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        getSreenDimanstions();
        fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        map = fragment.getMap();

        map.setMyLocationEnabled(true);


        map.getUiSettings().setMyLocationButtonEnabled(true);

        String s=getIntent().getExtras().getString("latlon");
        String []str=new String[7];
        StringTokenizer st = new StringTokenizer(s, ",");
        int i=0;
        while (st.hasMoreTokens()) {
            str[i]=st.nextToken();
            i++;
        }

        this.setPosition(Double.parseDouble(str[0]),Double.parseDouble(str[1]),Double.parseDouble(str[2]),Double.parseDouble(str[3]));
        MarkerOptions marker = new MarkerOptions().position(new LatLng(Start.latitude, Start.longitude)).title(str[4]);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        map.addMarker(marker);

        MarkerOptions marker2 = new MarkerOptions().position(new LatLng(End.latitude, End.longitude)).title(str[5]);
        marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        map.addMarker(marker2);

        findDirections( Start.latitude, Start.longitude, End.latitude, End.longitude, MODE_DRIVING );



    }



    public void setPosition(double slat,double slon,double elat,double elon)
    {
        Start= new LatLng(slat, slon);
        End=new LatLng(elat,elon);
    }

    @Override
    protected void onResume() {

        super.onResume();
        latlngBounds = createLatLngBoundsObject(Start, End);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));

    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

        for(int i = 0 ; i < directionPoints.size() ; i++)
        {
            rectLine.add(directionPoints.get(i));
        }
        if (newPolyline != null)
        {
            newPolyline.remove();
        }
        newPolyline = map.addPolyline(rectLine);
        latlngBounds = createLatLngBoundsObject(Start, End);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));


    }

    private void getSreenDimanstions()
    {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }

    private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation)
    {
        if (firstLocation != null && secondLocation != null)
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(firstLocation).include(secondLocation);

            return builder.build();
        }
        return null;
    }

    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
        asyncTask.execute(map);
    }

    public void back(View view)
    {
        Intent intent=new Intent();
        //   intent.putExtra("Marker","marker");
        setResult(3,intent);
        finish();
    }

}
