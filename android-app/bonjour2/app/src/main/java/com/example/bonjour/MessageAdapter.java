package com.example.bonjour;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.BaseAdapter;
    import android.widget.Button;
    import android.widget.LinearLayout;
    import android.widget.ListAdapter;
    import android.widget.TextView;
    import android.widget.Toast;

    import java.util.ArrayList;
    import java.util.HashMap;

/**
 * Created by FATEHULLAH on 4/8/2015.
 */
public class MessageAdapter extends ArrayAdapter<Chat> {

    SessionManager session;
    HashMap<String, String> user;
    int id;
    public MessageAdapter(Context context, ArrayList<Chat> requests) {
        super(context, 0, requests);
        session = new SessionManager(context.getApplicationContext());
        user = session.getUserDetails();
        id= Integer.parseInt(user.get(SessionManager.KEY_ID));
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        Chat request = getItem(position);
        TextView tvName;
        TextView tvMessage;

        if (convertView == null) {
            if(request.getSenderId()==id)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_layout_2, parent, false);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_layout, parent, false);
        }
            tvName = (TextView) convertView.findViewById(R.id.userNameTextView);
            tvMessage = (TextView) convertView.findViewById(R.id.messageTextView);
            tvName.setText(request.getDate());
            tvMessage.setText(request.getMessage());

        return convertView;
    }
}