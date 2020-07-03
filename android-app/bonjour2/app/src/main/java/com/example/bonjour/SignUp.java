package com.example.bonjour;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SignUp extends Activity{

	SessionManager session;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
	}

	public void onLognInClick (View v)
	{
		Intent i = new Intent(SignUp.this, Login.class);
        startActivity(i);
	}
	
	public void signUpClick(View v)
	{
    	final EditText UserName = (EditText) findViewById(R.id.signUpUserName);
    	final EditText Password = (EditText) findViewById(R.id.signUpPassword);
    	final EditText email = (EditText) findViewById(R.id.signUpEmail);
    	final EditText city = (EditText) findViewById(R.id.signUpCity);
    	final EditText gender = (EditText) findViewById(R.id.signUpAdress);
    	final EditText age=(EditText) findViewById(R.id.signUpage);
    	User u=new User();
    	
    	u.setName(UserName.getText().toString());
    	u.setEmail(email.getText().toString());
    	u.setCity(city.getText().toString());
    	u.setGender(gender.getText().toString());
    	u.setPassword(Password.getText().toString());
    	int ag=Integer.parseInt(age.getText().toString());
        u.setAge(ag);
        final User user= u;
        final User[] temp={new User()};
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    temp[0]=h.addUser(user);
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

        if (temp[0].getId()>0) {
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
            Toast.makeText(getApplicationContext(), "Account Has been created", Toast.LENGTH_LONG).show();
        }

    }
    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(SignUp.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
