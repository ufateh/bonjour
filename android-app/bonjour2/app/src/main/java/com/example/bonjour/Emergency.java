package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;


public class Emergency extends Activity {

    SessionManager session;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        id=Integer.parseInt(user.get(SessionManager.KEY_ID));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emergency, menu);
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


    public void broadCastClick (View v)
    {
        final EditText status = (EditText) findViewById(R.id.detail);
        String date= new Date().toString();
        EmergencyMessage message=new EmergencyMessage();
        message.senderId= id;
        message.isRead=false;
        message.date=date;
        message.message = status.getText().toString();

        int result= broadCastMessage(message);
        if(result>0) {
            Toast.makeText(Emergency.this, "message sent to " + result + " nearby users!", Toast.LENGTH_LONG);
            finish();
            //Intent i = new Intent(Post.this, home.class);
            //startActivity(i);
        }
        else
        {
            Toast.makeText(Emergency.this, "no nearby user found!", Toast.LENGTH_LONG);
            finish();
        }

    }
    public int broadCastMessage(final EmergencyMessage message)
    {
        final int[] result = new int[1];
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    result[0] = h.broadCastEmergencyMessage(message);
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
        return result[0];
    }

    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(Emergency.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
