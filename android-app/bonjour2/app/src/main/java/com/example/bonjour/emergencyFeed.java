package com.example.bonjour;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class emergencyFeed extends Activity {

    ListView list;
    public static ArrayList<String> emergencyFeedList;
    SessionManager session;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_feed);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        id=Integer.parseInt(user.get(SessionManager.KEY_ID));

        list = (ListView) findViewById(R.id.listView);
        emergencyFeedList=new ArrayList<>();
        LoadEmergencyFeed();
        Collections.reverse(emergencyFeedList);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, emergencyFeedList);
        list.setAdapter(adapter);
    }

    private void LoadEmergencyFeed() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    ArrayList<EmergencyMessage> list=h.getEmergencyMessages(id);
                    if(list!=null && list.size()>0)
                    {
                        for(EmergencyMessage m :list)
                        {
                            emergencyFeedList.add(m.message+"\n"+m.date);
                        }
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
    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(emergencyFeed.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emergency_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
