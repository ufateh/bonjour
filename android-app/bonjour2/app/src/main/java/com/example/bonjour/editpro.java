package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class editpro extends Activity {
DBHelper db;
SessionManager session;
User u=new User();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        
        db=new DBHelper(this);
        session = new SessionManager(getApplicationContext());
        
        EditText lblName = (EditText) findViewById(R.id.EditName);
        EditText lblEmail = (EditText) findViewById(R.id.Editemail);
        EditText lblgender = (EditText) findViewById(R.id.Editgender);
        EditText lblcity = (EditText) findViewById(R.id.editcity);
        
        HashMap<String, String> user = session.getUserDetails();
        
        // name
        u.setName(user.get(SessionManager.KEY_NAME));// .lowercase
        u.setEmail(user.get(SessionManager.KEY_EMAIL));
        u.setGender(user.get(SessionManager.KEY_GENDER));
        u.setCity(user.get(SessionManager.KEY_CITY));
        u.setId(Integer.parseInt(user.get(SessionManager.KEY_ID)));
        //u.setAge(Integer.parseInt(user.get(SessionManager.KEY_AGE)));
        // displaying user data
        lblName.setText(u.getName());
        lblEmail.setText(u.getEmail());
        lblgender.setText(u.getGender());
        lblcity.setText(u.getCity());
    }
    
    public void saveEdit(View v)
    {
    	final EditText userName = (EditText) findViewById(R.id.EditName);
    	final EditText email = (EditText) findViewById(R.id.Editemail);
    	final EditText city = (EditText) findViewById(R.id.editcity);
    	final EditText gender = (EditText) findViewById(R.id.Editgender);
    	String userNameText = userName.getText().toString();
    	String emailText = email.getText().toString();
    	String cityText = city.getText().toString();
    	String genderText = gender.getText().toString();

        final User[] temp={new User()};

        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    temp[0]=h.getUser(u.getId());
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

        temp[0].setName(userNameText);
        temp[0].setEmail(emailText);
        temp[0].setCity(cityText);
        temp[0].setGender(genderText);
        final User[] arguser={new User()};
        final Boolean[] status=new Boolean[1];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    arguser[0]=h.updateUser(temp[0]);
                    status[0]=true;
                } catch (Exception e) {
                   status[0]=false;
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

        if(status[0]==true)
        {
        	Toast.makeText(getApplicationContext(), "User updated", Toast.LENGTH_SHORT).show();
        	String a=String.valueOf(u.getId());
        	session.createLoginSession(String.valueOf(arguser[0].getId()),arguser[0].getName(),arguser[0].getEmail(),arguser[0].getCity(),arguser[0].getGender(),String.valueOf(arguser[0].getAge()),String.valueOf(arguser[0].getLatitude()),String.valueOf(arguser[0].getLongitude()));

        	  Intent i = new Intent(this, ProfileActivity.class);
              startActivity(i);
              finish();
        }
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
                Toast.makeText(editpro.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
 
}
