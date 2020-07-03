package com.example.bonjour;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class FriendRequest extends Activity {

    SessionManager session;
    HashMap<String, String> user;
    BonjourFriendRequest request;
    ArrayList<BonjourFriendRequest> array_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();

        ListView list=(ListView)findViewById(R.id.friendRequestListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                BonjourFriendRequest item = (BonjourFriendRequest)parent.getAdapter().getItem(position);
                String s=item.getName();
                request=item;
                Dialog d= createDialog(FriendRequest.this,s,"Accept","Reject");
                d.show();
            }
        });
        getAllFriendRequests();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_request, menu);
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
    public void getAllFriendRequests()
    {
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h=new BonjourServiceHandler();
                try {
                    int id= Integer.parseInt(user.get(SessionManager.KEY_ID));
                    array_list = h.getFriends(id,false);
                    if(array_list!=null) {
                        BonjourFriendRequestAdapter adapter = new BonjourFriendRequestAdapter(FriendRequest.this, array_list);
                        final ListView listView = (ListView) findViewById(R.id.friendRequestListView);
                        listView.setAdapter(adapter);
                    }else {
                        showToastFromBackground("No Friend Request!");
                    }

                } catch (Exception e) {
                    Log.d("Error: ",e.getMessage());
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

    private Boolean acceptFriendRequest(final BonjourFriendRequest request) {
        final Boolean[] temp = new Boolean[1];
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h = new BonjourServiceHandler();
                try {
                        temp[0] = h.confirmFriendRequest(request.getFriendshipId());
                    int a=5;
                } catch (Exception e) {
                    Log.d("Error: ",e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.d("Error: ",e.getMessage());
            e.printStackTrace();
        }
        return temp[0];
    }
    public Dialog createDialog(final FriendRequest friend,String message,String okBtnText,String cancelBtnText) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(friend);
        builder.setMessage(message)
                .setPositiveButton(okBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Boolean check=acceptFriendRequest(request);
                        if(check==true)
                        {
                            showToastFromBackground("You are now friends!");
                            array_list.remove(request);
                            array_list.notify();
                        }
                        else
                        {
                            showToastFromBackground("Accepted");
                        }
                    }
                })
                .setNegativeButton(cancelBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();//dialog.cancel();
                        //Toast.makeText(friend,"Cancel pressed",Toast.LENGTH_SHORT);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(FriendRequest.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
