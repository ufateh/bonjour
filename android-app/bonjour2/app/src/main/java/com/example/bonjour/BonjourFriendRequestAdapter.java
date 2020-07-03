package com.example.bonjour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by FATEHULLAH on 4/8/2015.
 */
public class BonjourFriendRequestAdapter extends ArrayAdapter<BonjourFriendRequest> {

    public BonjourFriendRequestAdapter(Context context, ArrayList<BonjourFriendRequest> requests) {
        super(context, 0, requests);
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        BonjourFriendRequest request = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friendrequest_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        // Populate the data into the template view using the data object
        tvName.setText(request.getName());
        tvEmail.setText(request.getEmail());
        return convertView;
    }
}