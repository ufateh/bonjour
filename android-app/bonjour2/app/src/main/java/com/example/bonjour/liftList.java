package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class liftList extends Activity{

    SessionManager session;
    User u=new User();
    ArrayList<Lift> array_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lift_list);
        array_list = new ArrayList<>();
        getLiftList();
       // get list
        //id,name,string cotactno,int vacancy, date.tostring, starting lat,lon, ending lat,lon,destination


        /************************************************************************************************/

        //Lift l1=new Lift("Sanjay","Paris","3","1:00","52.37518", "4.895439","48.856132", "2.352448");
        //array_list.add(l1);

        //Lift l2=new Lift("Fateh","London","3","1:00","38.898748", "-77.037684","48.856132", "2.352448");
        //array_list.add(l2);
        /************************************************************************************************/
        LiftArrayAdapter adapter = new LiftArrayAdapter(this, array_list);
        final ListView obj = (ListView)findViewById(R.id.liftList);
        obj.setAdapter(adapter);
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
                profile();
                return true;
            case R.id.action_location:
                location();
                return true;
            case R.id.action_logout:
                Logout();
                return true;
            case R.id.action_post:
                Post();
                return true;
            case R.id.action_lift:
                Lift();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void Lift() {
        //	  Intent i = new Intent(this, MainActivity.class);
        //       startActivity(i);

    }


    private void location() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    /**
     * Launching new activity
     * */

    private void profile() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        finish();
    }

    private void Logout() {
        Intent i = new Intent(liftList.this, Login.class);
        startActivity(i);
        finish();
    }

    private void Post() {
        Intent i = new Intent(liftList.this, Post.class);
        startActivity(i);

    }

    public void givelift (View v) {
        Intent i = new Intent(liftList.this, GiveLift.class);
        startActivity(i);

    }

    public void getLiftList()
    {
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                   array_list = h.getAllLifts();
                    Collections.reverse(array_list);
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
    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(liftList.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}