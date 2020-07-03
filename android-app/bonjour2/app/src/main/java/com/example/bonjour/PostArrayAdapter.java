package com.example.bonjour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PostArrayAdapter extends ArrayAdapter<Status> {
  private final Context context;
  private final ArrayList<Status> values;

  public PostArrayAdapter(Context context, ArrayList<Status> values) {
    super(context, R.layout.row_layout, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.row_layout, parent, false);
      Status data = values.get(position);
    
    TextView title = (TextView) rowView.findViewById(R.id.friendName);
    title.setText(data.Name);
    TextView desc = (TextView) rowView.findViewById(R.id.description);
    desc.setText(data.Userstatus);
    TextView contct = (TextView) rowView.findViewById(R.id.vacancy);
    contct.setText(data.Time);
    TextView city = (TextView) rowView.findViewById(R.id.city);
    city.setText(data.Location);
    
  
    ImageView image=(ImageView) rowView.findViewById(R.id.imageView1);
    image.setImageResource(R.drawable.user);
    
    return rowView;
  }
} 