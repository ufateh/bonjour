package com.example.bonjour;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
public class Bonjour
{
UserCatalog userList;
public Bonjour(Context c)
{
	userList=new UserCatalog(c);
}
public ArrayList<User> findUsers(LatLng l1,double distanceInKm)
{
	ArrayList<User> tempList=userList.getUser_list();
	ArrayList<User> refinedList=new ArrayList<User>();
	for(int i=0;i<tempList.size();i++)
	{

		if(tempList.get(i).getVisibility() == 1 && tempList.get(i).getIsOnline()== 1)
		{
			//LatLng l2=new LatLng(tempList.get(i).getLatitude(), tempList.get(i).getLongitude());
			//if(distFrom(l1.latitude,l1.longitude,l2.latitude, l2.longitude)<=distanceInKm)
			
				refinedList.add(tempList.get(i));
			
		}
	}
	return refinedList;
}
public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
    double earthRadius = 6371; //kilometers
    double dLat = Math.toRadians(lat2-lat1);
    double dLng = Math.toRadians(lng2-lng1);
    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLng/2) * Math.sin(dLng/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    double dist = (double) (earthRadius * c);

    return dist*1000;
    }
}