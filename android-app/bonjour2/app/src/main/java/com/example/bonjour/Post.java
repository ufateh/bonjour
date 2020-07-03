package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Post extends Activity{

    DBHelper mydb;
    SessionManager session;
    User u=new User();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mydb = new DBHelper(this);
        session = new SessionManager(getApplicationContext());

        TextView lblName = (TextView) findViewById(R.id.post_name);

        HashMap<String, String> user = session.getUserDetails();
        User u=new User();
        u.setId(Integer.parseInt(user.get(SessionManager.KEY_ID)));
        u.setName(user.get(SessionManager.KEY_NAME).toUpperCase());
        lblName.setText(u.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);




        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_home:
                Home();
                return true;
            case R.id.action_logout:
                if(setUserOffline(u.getId()))
                    Logout();
                else {
                    Toast.makeText(this, "web service could not set the user offline", Toast.LENGTH_SHORT);
                    //mydb.offlineUser(u.getName(),u.getEmail());
                    Logout();
                }
                return true;
            case R.id.action_location:
                Location();
                return true;
            case R.id.action_post:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void Location() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    private void profile() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        finish();
    }

    private void Home() {
        Intent i = new Intent(Post.this, home.class);
        startActivity(i);
        finish();
    }

    /**
     * Launching new activity
     * */
    private void Logout() {
        Intent intent  = new Intent(getBaseContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void postClick (View v)
    {
        final TextView name = (TextView) findViewById(R.id.post_name);
        final EditText status = (EditText) findViewById(R.id.detail);
        final EditText city = (EditText) findViewById(R.id.city);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-0yy");

        String datestr = sdf.format(new Date());
        SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");
        String timestr = mSDF.format(new Date().getTime());

        String date=datestr+" "+timestr;



        Status data=new Status();

        data.Userstatus=status.getText().toString();
        data.Location=city.getText().toString();
        data.Name=name.getText().toString();
        data.Time=date;
        Status d=exchangeNews(data);
        if(d.Id>0)
        {
            Intent i = new Intent(Post.this, home.class);
            startActivity(i);
            finish();
        }
        else
        {
            Toast.makeText(this,"web service not responding",Toast.LENGTH_LONG);
        }
    }
    public Status exchangeNews(final Status data)
    {
        final Status[] temp={new Status()};
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    temp[0]=h.submitNewsDetails(data);
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
        return temp[0];
    }
    public boolean setUserOffline(final int id)
    {
        final Boolean[] status= new Boolean[1];
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    status[0]=h.setUserOffline(id);
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
        return status[0];
    }


    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(Post.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
/*
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }*/
                strReturnedAddress.append(returnedAddress.getSubLocality()).append("( ");
                strReturnedAddress.append(returnedAddress.getLocality()).append(" )");
                strAdd = strReturnedAddress.toString();

            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
}