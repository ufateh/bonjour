package com.example.bonjour;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;


public class FriendList extends Activity {

    SessionManager session;
    HashMap<String, String> user;
    BonjourFriendRequest item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();

        ListView list=(ListView)findViewById(R.id.friendListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                item = (BonjourFriendRequest)parent.getAdapter().getItem(position);
                String s=item.getName();
                //Toast.makeText(FriendList.this,s==null?"error":s,Toast.LENGTH_LONG).show();
                Dialog d=createDialog(FriendList.this,"Chat with "+s+"?","Yes","No");
                d.show();
            }
        });
        getAllFriends();

    }

    public void getAllFriends() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h = new BonjourServiceHandler();
                try {
                    int id = Integer.parseInt(user.get(SessionManager.KEY_ID));
                    ArrayList<BonjourFriendRequest> array_list = h.getFriends(id, true);
                    if (array_list!=null) {
                        FriendListAdapter adapter= new FriendListAdapter(FriendList.this,array_list);
                        final ListView listView = (ListView) findViewById(R.id.friendListView);
                        listView.setAdapter(adapter);
                    } else {
                        showToastFromBackground("You have no Friends Yet!");
                    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
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
                Toast.makeText(FriendList.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }


    public Dialog createDialog(final FriendList friend,String message,String okBtnText,String cancelBtnText) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(friend);
        builder.setMessage(message)
                .setPositiveButton(okBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Intent intent=new Intent(FriendList.this,Message.class);
                        Gson gson = new Gson();
                        String myJson = gson.toJson(item);
                        intent.putExtra("chatFriendData",myJson);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(cancelBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();//dialog.cancel();
                    }
                });
        return builder.create();
    }
}
