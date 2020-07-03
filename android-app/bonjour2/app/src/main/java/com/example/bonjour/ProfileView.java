package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.StringTokenizer;


public class ProfileView extends Activity {

    SessionManager session;
    HashMap<String, String> user;
    NearbyUser ob;
    Button requestBtn;
    Button messageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
        TextView lblName = (TextView) findViewById(R.id.profileViewName);
        TextView lblLName = (TextView) findViewById(R.id.Lastname);
        TextView lblEmail = (TextView) findViewById(R.id.profileViewEmail);
        requestBtn=(Button)findViewById(R.id.requestButton);
        messageBtn=(Button)findViewById(R.id.messageButton);

        Intent i=getIntent();

        Gson gson = new Gson();
        String s=getIntent().getExtras().getString("profiledata");
        ob = gson.fromJson(s,NearbyUser.class);
        // displaying user data
        String s1=ob.getName();
        int spaces = s1.length() - s1.replace(" ", "").length();

        if(spaces>0) {
            String[] str = new String[2];
            StringTokenizer st = new StringTokenizer(s1, " ");
            int nametok = 0;
            while (st.hasMoreTokens()) {
                str[nametok] = st.nextToken();
                nametok++;
            }
            lblName.setText(str[0].toUpperCase());
            lblLName.setText(str[1].toUpperCase());
        }
        else
        {
            lblName.setText(s1);
            lblLName.setVisibility(View.INVISIBLE);
        }
        lblEmail.setText(ob.getEmail());
        if(ob.getIsFriend()==false)
        {
            View friendRequestView = findViewById(R.id.requestButton);
            friendRequestView.setVisibility(View.VISIBLE);
        }
        else
        {
            View messageView = findViewById(R.id.messageButton);
            messageView.setVisibility(View.VISIBLE);
        }


    }

    public void onRequestClickBtn(View v) {
        int id= Integer.parseInt(user.get(SessionManager.KEY_ID));
        final FriendshipRelation relation=new FriendshipRelation();
        relation.setFirstUserId(id);
        relation.setSecondUserId(ob.getId());
        relation.setIsFriend(true);
        final FriendshipRelation[] temp = {new FriendshipRelation()};

        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h = new BonjourServiceHandler();
                try {
                    temp[0] = h.sendFriendRequest(relation);
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
        if(temp[0].getId()>0)
        {
            requestBtn.setText("Request Sent");
        }
        else
        {
            requestBtn.setText("request already sent");
        }

    }


    public void onMessageClickBtn(View v) {
        Intent intent=new Intent(ProfileView.this,Message.class);
        Gson gson = new Gson();
        ob.getId();
        ob.getFriendshipId();
        BonjourFriendRequest r=new BonjourFriendRequest();

        r.setFriendshipId(ob.getFriendshipId());
        r.setUserId(ob.getId());

        String myJson = gson.toJson(r);
        intent.putExtra("chatFriendData",myJson);
        startActivity(intent);
    }
    public void getdirection(View v) {

        String lat = user.get(SessionManager.KEY_LAT);
        String lon = user.get(SessionManager.KEY_LON);
        String a=ob.getName();// friend/nearby name
        String points= lat+","+lon;//your coordinates
        points+=","+ob.getLatitude()+","+ob.getLongitude();//",48.856132, 2.352448";//friends coordinates
        points+=",Me,"+a;

        Intent i=new Intent(ProfileView.this,NavigationActivity.class);
        i.putExtra("latlon",points);
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
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
    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(ProfileView.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }


    }
