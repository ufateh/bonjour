package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Sanjay Kumar on 4/9/2015.
 */
public class GiveLift extends Activity {

    DatePicker date;
    TimePicker time;
    EditText details;
    EditText phone;
    Button done;
    String email,name;
    String points;
    DBHelper mydb;
    SessionManager session;
    User u=new User();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.givelift);

        done = (Button) findViewById(R.id.routeSet);

        session = new SessionManager(getApplicationContext());
          HashMap<String, String> user = session.getUserDetails();
          email=user.get(SessionManager.KEY_EMAIL);
        name=user.get(SessionManager.KEY_NAME);

        date =(DatePicker) findViewById(R.id.date);
        time = (TimePicker)findViewById(R.id.time);
        details = (EditText)findViewById(R.id.detail);
        phone = (EditText) findViewById(R.id.contact);

    }


    public void setValue (View v)
    {
        String datestr,timestr,detailstr,Destination;
        detailstr=details.getText().toString();

        int   day  = date.getDayOfMonth();
        int   month= date.getMonth();
        int   year = date.getYear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-0yy");
        datestr = sdf.format(new Date(year, month, day));

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, time.getCurrentHour());
        mCalendar.set(Calendar.MINUTE, time.getCurrentMinute());
        SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");
        timestr = mSDF.format(mCalendar.getTime());

        String []str=new String[8];
        StringTokenizer st = new StringTokenizer(points, ",");
        int i=0;
        while (st.hasMoreTokens()) {
            str[i]=st.nextToken();
            i++;
        }

        Destination=str[6];
        Lift l=new Lift();
        l.destination = Destination;
        l.name=name;
        l.contactNo = phone.getText().toString();
        l.time = datestr+" "+timestr;
        l.vacancy = detailstr;
        l.slat=str[0];
        l.slon=str[1];
        l.elat=str[2];
        l.elon=str[3];

        insertLiftInfo(l);
        //save in database(datestr,timestr,detailstr,destination,str[0-1]str[2-3]])


        Intent intnt = new Intent(GiveLift.this, liftList.class);
        startActivity(intnt);

    }

    public void insertLiftInfo(final Lift lift)
    {
        final Lift[] result = new Lift[1];
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    result[0] = h.addLift(lift);
                    showToastFromBackground("successfully posted");
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

    public void getPoints (View v)
    {
        Intent intent=new Intent(GiveLift.this,GetLocationPoints.class);
        startActivityForResult(intent, 2);
         //   Intent i = new Intent(GiveLift.this, GetLocationPoints.class);
          //  startActivity(i);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
             points=data.getStringExtra("points");

        }
    }

    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(GiveLift.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }





}
