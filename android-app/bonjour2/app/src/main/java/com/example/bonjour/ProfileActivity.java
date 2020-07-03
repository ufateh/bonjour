package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.StringTokenizer;

public class ProfileActivity extends Activity {
	
	SessionManager session;
	User u=new User();
	DBHelper db=new DBHelper(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

      session = new SessionManager(getApplicationContext());
        
        TextView lblName = (TextView) findViewById(R.id.profileViewName);
        TextView lblLName = (TextView) findViewById(R.id.Lastname);
        TextView lblEmail = (TextView) findViewById(R.id.profileViewEmail);
        TextView lblgender = (TextView) findViewById(R.id.gender);
        TextView lblcity = (TextView) findViewById(R.id.city);
        
        HashMap<String, String> user = session.getUserDetails();
        
        // name


        u.setName(user.get(SessionManager.KEY_NAME).toUpperCase());
        u.setEmail(user.get(SessionManager.KEY_EMAIL));
        u.setGender(user.get(SessionManager.KEY_GENDER));
        u.setCity(user.get(SessionManager.KEY_CITY));
        u.setAge(Integer.parseInt(user.get(SessionManager.KEY_AGE)));
        // email
        
        

        // displaying user data
        String s=u.getName();
        int spaces = s.length() - s.replace(" ", "").length();

        if(spaces>0) {
            String[] str = new String[2];
            StringTokenizer st = new StringTokenizer(s, " ");
            int nametok = 0;
            while (st.hasMoreTokens()) {
                str[nametok] = st.nextToken();
                nametok++;
            }
            lblName.setText(str[0]);
            lblLName.setText(str[1]);
        }
        else
        {
            lblName.setText(s);
            lblLName.setVisibility(View.INVISIBLE);
        }
            lblEmail.setText(u.getEmail());
        lblgender.setText(u.getGender());
        lblcity.setText(u.getCity());
        
            
    }
    
    public void edit(View v)
    {
 		Intent i = new Intent(this, editpro.class);
        startActivity(i);
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
        case R.id.action_location:
        	location();
            return true;
        case R.id.action_logout:
        	db.offlineUser(u.getName(), u.getEmail());
            Logout();
            return true;
        case R.id.action_post:
            Post();
            return true;
            case R.id.friend_request_action:
                friendreq();
                return true;
            case R.id.friend_list_action:
                friendlist();
                return true;
            case R.id.action_lift:
                liftlist();
                return true;
            case R.id.action_emergency:
                emergencyActivity();
                return true;
            case R.id.action_emergency_list:
                emergencyFeedActivity();
                return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void liftlist() {
        Intent i = new Intent(this, liftList.class);
        startActivity(i);
    }

    private void friendlist() {
        Intent i = new Intent(this, FriendList.class);
        startActivity(i);
    }

    private void friendreq() {
        Intent i = new Intent(this, FriendRequest.class);
        startActivity(i);
    }
    private void emergencyActivity() {
        Intent i = new Intent(this, Emergency.class);
        startActivity(i);
    }
    private void emergencyFeedActivity() {
        Intent i = new Intent(this, emergencyFeed.class);
        startActivity(i);
    }

    private void location() {
		// TODO Auto-generated method stub
    	 Intent i = new Intent(this, MainActivity.class);
         startActivity(i);
		
	}

	/**
     * Launching new activity
     * */
    private void Logout() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }
    
    
    private void Post() {
        Intent i = new Intent(this, Post.class);
        startActivity(i);
	}
    
    private void Home() {
        Intent i = new Intent(this, home.class);
        startActivity(i);
	}
}
