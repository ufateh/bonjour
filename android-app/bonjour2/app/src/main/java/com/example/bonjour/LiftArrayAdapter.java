package com.example.bonjour;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LiftArrayAdapter extends ArrayAdapter<Lift> {
    private final Context context;
    private final ArrayList<Lift> values;


    public LiftArrayAdapter(Context context, ArrayList<Lift> values) {
        super(context, R.layout.lift, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lift, parent, false);
        final Lift data = values.get(position);

        TextView title = (TextView) rowView.findViewById(R.id.friendName);
        title.setText(data.getName());

        TextView time = (TextView) rowView.findViewById(R.id.datetime);
        time.setText(data.getTime());

        final TextView vacancy = (TextView) rowView.findViewById(R.id.vacancy);
        vacancy.setText("Vacancy: "+data.getVacancy());
        final TextView contact = (TextView) rowView.findViewById(R.id.contact);
        contact.setText("Contact: "+data.getContactNo());


        TextView destination = (TextView) rowView.findViewById(R.id.city);
        destination.setText(data.getDestination());
        Button route=(Button)rowView.findViewById(R.id.route);
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String points=data.getSlat()+",";
                points+=data.getSlon()+",";
                points+=data.getElat()+",";
                points+=data.getElon();

                Intent i=new Intent(context,NavigationActivity.class);
                i.putExtra("latlon",points);
                context.startActivity(i);

            }
        });


        Button takelift=(Button)rowView.findViewById(R.id.takelift);
        takelift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vacancystr=data.getVacancy();
                try {

                    int vac = Integer.parseInt(vacancystr);
                    if(vac>0) {
                        vac = vac - 1;
                    }
                    else
                    {
                        vac=0;
                    }
                    vacancystr="Vacancy: "+String.valueOf(vac);
                }catch (Exception ex){
                    vacancystr="Vacancy: "+String.valueOf(0);
                }
                vacancy.setText(vacancystr);
                data.setVacancy(vacancystr);

                //**********************************************8
                //Update data vacancy
                //************************************************88

            }
        });

        ImageView image=(ImageView) rowView.findViewById(R.id.imageView1);
        image.setImageResource(R.drawable.user);

        return rowView;
    }

}