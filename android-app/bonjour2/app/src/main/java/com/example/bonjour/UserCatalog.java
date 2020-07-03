package com.example.bonjour;

import android.content.Context;

import java.util.ArrayList;

public class UserCatalog
{
ArrayList<User> user_list=new ArrayList<User>();
DBHelper db ;

public UserCatalog(Context c) {
	super();
	db=new DBHelper(c);
	User user=new User(1,"Umer", "umer@gmail.com", "jhg", 21,
			"pindi", 1, 1,
			33.670052, 72.999782,"male");
	User user1=new User(2,"Sanjay", "sanjay@gmail.com", "fds", 21,
			"dadu", 0, 1,
			33.687648, 73.033694,"male");
	db.addUser(user);
	db.addUser(user1);
	
	//user_list.size();
}

public UserCatalog(ArrayList<User> user_list) {
	super();
	this.user_list = user_list;
}

public ArrayList<User> getUser_list() {
	user_list=db.getAllUsers();
	return user_list;
}

public void setUser_list(ArrayList<User> user_list) {
	this.user_list = user_list;
}

}
