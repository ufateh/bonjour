package com.example.bonjour;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Message extends Activity {

    SessionManager session;
    HashMap<String, String> user;
    BonjourFriendRequest chatInfoObject;
    MessageAdapter adapter;
    ArrayList<Chat> array_list;
    ListView listView;//=(ListView) findViewById(R.id.messageListView);
    int senderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        listView=(ListView) findViewById(R.id.messageListView);
        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
        senderId = Integer.parseInt(user.get(SessionManager.KEY_ID));

        Button sendButton=(Button) findViewById(R.id.sendButton);
        final TextView messageText=(TextView) findViewById(R.id.messageText);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=messageText.getText().toString();
                if(s.trim().length()>0)
                {
                    Chat c = new Chat();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-0yy");

                    String datestr = sdf.format(new Date());
                    SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");
                    String timestr = mSDF.format(new Date().getTime());

                    c.setDate(datestr+" "+timestr);

                    c.setFriendshipId(chatInfoObject.getFriendshipId());
                    c.setIsRead(false);
                    c.setMessage(s);
                    c.setReceiverId(chatInfoObject.userId);
                    c.setSenderId(senderId);
                    if(array_list==null)
                        array_list=new ArrayList<>();
                    array_list.add(c);
                    if(adapter==null)
                        adapter = new MessageAdapter(Message.this, array_list);

                    listView.setAdapter(adapter);
                    pushNewMessageToService(c);
                }
                else {
                    Toast.makeText(Message.this, "empty message !", Toast.LENGTH_SHORT).show();
                }
                messageText.setText("");
            }
        });

        Intent i=getIntent();
        Gson gson = new Gson();
        String s=getIntent().getExtras().getString("chatFriendData");
        chatInfoObject = gson.fromJson(s,BonjourFriendRequest.class);
        if(chatInfoObject==null)
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        importChatHistoryFromService();

    }

    private void importChatHistoryFromService() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h = new BonjourServiceHandler();
                try {
                    array_list = h.getChatWithFriendshipId(chatInfoObject.getFriendshipId());
                    if (array_list!=null && array_list.size()>0) {
                        adapter= new MessageAdapter(Message.this,array_list);
                        listView.setAdapter(adapter);
                        listView.setSelection(array_list.size());
                    } else {
                        showToastFromBackground("No Chat History!");
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
        getMenuInflater().inflate(R.menu.menu_message, menu);
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
                Toast.makeText(Message.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void pushNewMessageToService(final Chat chat)
    {
        Thread t = new Thread(new Runnable() {
            public void run() {
                BonjourServiceHandler h = new BonjourServiceHandler();
                try {
                    boolean a= h.addChatMessage(chat);
                    if (a==true) {
                        showToastFromBackground("sent");
                    } else {
                        showToastFromBackground("error");
                    }

                } catch (Exception e) {
                    showToastFromBackground(e.getMessage());
                    Log.d("Post Chat: ",e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            showToastFromBackground(e.getMessage());
            Log.d("Post Chat: ",e.getMessage());
        }
    }

}
