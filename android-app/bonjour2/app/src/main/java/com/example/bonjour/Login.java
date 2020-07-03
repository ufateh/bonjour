package com.example.bonjour;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

    String userNameText;
    String passwordText;
    DBHelper mydb;
    SessionManager session;
    private NavigationActivity activity;
    private Exception exception;
    private ProgressDialog progressDialog;

    private BonjourServiceHandler handler = new BonjourServiceHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }


    public void onSignUpClick(View v) {
        Intent i = new Intent(Login.this, SignUp.class);
        startActivity(i);
    }

    public void login_clicked(View v) {
        final EditText userName = (EditText) findViewById(R.id.signInUserName);
        final EditText password = (EditText) findViewById(R.id.signInPassword);

        userNameText = userName.getText().toString();
        passwordText = password.getText().toString();

        final User u = new User();
        u.setEmail(userNameText);
        if (!passwordText.equals(""))
            u.setPassword(passwordText);
        else
            u.setPassword(null);

        final User[] thisUser = {new User()};
        final Boolean[] check = new Boolean[1];
        final Exception[] ex={new Exception()};
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h = new BonjourServiceHandler();
                try {
                    thisUser[0] = h.validateUser(u);
                    check[0] = true;
                } catch (Exception e){
                   showToastFromBackground(e.getMessage());
                }
            }
        });

        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            showToastFromBackground(e.getMessage());
        }

        if (check[0]!=null && check[0] == true) {
            User temp = new User();
            temp = thisUser[0];
            try {
                String id = String.valueOf(temp.getId());
                String age = String.valueOf(temp.getAge());
                String lat = String.valueOf(temp.getLatitude());
                String lon = String.valueOf(temp.getLongitude());
                session.createLoginSession(id, temp.getName(), temp.getEmail(), temp.getCity(), temp.getGender(), age, lat, lon);
                Intent i = new Intent(Login.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }catch (Exception e){
                showToastFromBackground(e.getMessage());
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();  // optional depending on your needs

    }
    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}