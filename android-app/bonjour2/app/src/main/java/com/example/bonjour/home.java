package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class home extends Activity{
	
	DBHelper mydb;
	
	User u=new User();
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_home);
		  getAllPosts();
			
		//mydb = new DBHelper(this);
	      /*ArrayList<PostData> array_list = mydb.getAllPosts();

	      //adding it to the list view.

		PostArrayAdapter adapter = new PostArrayAdapter(this, array_list);
	      final ListView obj = (ListView)findViewById(R.id.PostList);
	      obj.setAdapter(adapter);*/
	      
	    
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
            /*case R.id.nearby_user:
                NearbyUser();
                return true;*/
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void NearbyUser() {
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
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
        Intent i = new Intent(home.this, Login.class);
        startActivity(i);
        finish();
    }
    
    private void Post() {
        Intent i = new Intent(home.this, Post.class);
        startActivity(i);
      
	}

    public void getAllPosts()
    {
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                        ArrayList<Status> array_list = h.getAllPosts();
                    Collections.reverse(array_list);
                    PostArrayAdapter adapter = new PostArrayAdapter(home.this, array_list);
                        final ListView obj = (ListView)findViewById(R.id.PostList);
                        obj.setAdapter(adapter);
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
                Toast.makeText(home.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

        
   
}