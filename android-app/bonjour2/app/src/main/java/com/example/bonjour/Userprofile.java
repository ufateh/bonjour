package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Userprofile extends Activity {

	
	User u=new User();
	DBHelper db=new DBHelper(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);
        
      
        
        TextView lblName = (TextView) findViewById(R.id.profileViewName);
        TextView lblEmail = (TextView) findViewById(R.id.profileViewEmail);
        TextView lblgender = (TextView) findViewById(R.id.gender);
        TextView lblcity = (TextView) findViewById(R.id.city);

       Intent i=getIntent();

        // name
            u.setName(u.getName().toUpperCase());
            // email
            // displaying user data
            lblName.setText(u.getName());
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
        default:
            return super.onOptionsItemSelected(item);
        }
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
    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(Userprofile.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }
}