package com.example.bonjour;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

   public static final String DATABASE_NAME = "MyDBName.db";
   public static final String USERS_TABLE_NAME = "users";
   public static final String USERS_COLUMN_ID = "id";
   public static final String USERS_COLUMN_USERNAME = "username";
   public static final String USERS_COLUMN_EMAIL = "email";
   public static final String USERS_COLUMN_GENDER = "gender";
   public static final String USERS_COLUMN_CITY = "city";
   public static final String USERS_COLUMN_PASSWORD = "password";
   public static final String USERS_COLUMN_AGE = "age";
	private static final String USERS_COLUMN_IsOnline = "isonline";
	private static final String USERS_COLUMN_VISIBILITY = "visibility";
	private static final String USERS_COLUMN_Longitude = "longitude";
	private static final String USERS_COLUMN_Latitude = "latitude";

   public static final String POSTS_TABLE_NAME = "Status";
   public static final String POSTS_COLUMN_ID = "id";
   public static final String POSTS_COLUMN_Name = "name";
   public static final String POSTS_COLUMN_STATUS = "status";
   public static final String POSTS_COLUMN_TIME = "time";
   public static final String POSTS_COLUMN_CITY = "city";
   
   
   private HashMap hp;

   public DBHelper(Context context)
   {
      super(context, DATABASE_NAME , null, 10);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
            
      db.execSQL(
    	      "create table posts " +
    	      "(id integer primary key, name text, status text, city text, time text)"
    	      );
      
      db.execSQL(
    	      "create table users " +
    	      "(id integer primary key, username text,email text, gender text,city text, password text,age integer,isonline integer,visibility integer, latitude real,longitude real)"
    	      );
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      db.execSQL("DROP TABLE IF EXISTS users");
      db.execSQL("DROP TABLE IF EXISTS posts");
      onCreate(db);
   }

   public long insertUser  (String username, String email, String gender, String usercity,String password,int age)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();

      contentValues.put("username", username);
      contentValues.put("email", email);
      contentValues.put("gender", gender);	
      contentValues.put("city", usercity);
      contentValues.put("password", password);
      contentValues.put("age", age);
      
      
      return db.insert("users", null, contentValues);
      
   }
   
   void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(USERS_COLUMN_USERNAME, user.getName()); // user Name
		values.put(USERS_COLUMN_EMAIL, user.getEmail()); // 
		values.put(USERS_COLUMN_PASSWORD, user.getPassword());
		
		values.put(USERS_COLUMN_CITY, user.getCity());
		values.put(USERS_COLUMN_AGE, user.getAge());
		values.put(USERS_COLUMN_IsOnline, user.getIsOnline());
		values.put(USERS_COLUMN_VISIBILITY, user.getVisibility());
		values.put(USERS_COLUMN_Latitude, user.getLatitude());
		values.put(USERS_COLUMN_Longitude, user.getLongitude());
		// Inserting Row

		db.insert(USERS_TABLE_NAME, null, values);
		db.close(); // Closing database connection
	}
   public ArrayList<User> getAllUsers() {
		ArrayList<User> userList = new ArrayList<User>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + USERS_TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				User user = new User();
				user.setId(cursor.getInt(0));
				user.setName(cursor.getString(1));
				user.setEmail(cursor.getString(2));
				user.setGender(cursor.getString(3));
				user.setPassword(cursor.getString(4));
				user.setCity(cursor.getString(5));
				user.setAge((cursor.getInt(6)));
				user.setIsOnline(cursor.getInt(7));
				user.setVisibility(cursor.getInt(8));
				user.setLatitude(((cursor.getDouble(9))));
				user.setLongitude(((cursor.getDouble(10))));
				// Adding user to list
				userList.add(user);
			} while (cursor.moveToNext());
		}

		// return user list
		return userList;
	}
   public long insertPost  (String name, String status, String city, String time)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("name", name);
      contentValues.put("status", status);
  //    contentValues.put("image", image);	
 //     contentValues.put("auther", auther);
  //    contentValues.put("publisher", publisher);
      contentValues.put("city", city);
      contentValues.put("time", time);

      return db.insert("posts", null, contentValues);
   }

   
   public User offlineUser(String username, String email){
	      ArrayList array_list = new ArrayList();
	      User u=new User();
	      u.setId(-1);
	      //hp = new HashMap();
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from users", null );
	      res.moveToFirst();
	      while(res.isAfterLast() == false){
	    	  u.setId(res.getInt(res.getColumnIndex(USERS_COLUMN_ID)));
	    	  u.setName(res.getString(res.getColumnIndex(USERS_COLUMN_USERNAME)));
	    	  u.setPassword(res.getString(res.getColumnIndex(USERS_COLUMN_PASSWORD)));
	    	  u.setEmail(res.getString(res.getColumnIndex(USERS_COLUMN_EMAIL)));
	    	  u.setGender(res.getString(res.getColumnIndex(USERS_COLUMN_GENDER)));
	    	  u.setAge(res.getInt(res.getColumnIndex(USERS_COLUMN_AGE)));
	    	  u.setCity(res.getString(res.getColumnIndex(USERS_COLUMN_CITY)));
	    	
	    	  if(u.getName().equals(username) && u.getEmail().equals(email))
	    	  {   SQLiteDatabase db2 = this.getWritableDatabase();
	          ContentValues contentValues = new ContentValues();
	          contentValues.put("isonline", 0);

	          db2.update("users", contentValues, "id = ? ", new String[] { Integer.toString(u.getId()) } );
	         
	    		  
	    		  
	    		  
	    		  
	    		  return u;
	    	  
	    	  }
	    	  
	    	  res.moveToNext();
	      }
	      u.setId(-1);
	      return u;
	   }
   
   
   
   
   
   
   
   public User validateUser(String username, String password){
	      ArrayList array_list = new ArrayList();
	      User u=new User();
	      u.setId(-1);
	      //hp = new HashMap();
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from users", null );
	      res.moveToFirst();
	      while(res.isAfterLast() == false){
	    	  u.setId(res.getInt(res.getColumnIndex(USERS_COLUMN_ID)));
	    	  u.setName(res.getString(res.getColumnIndex(USERS_COLUMN_USERNAME)));
	    	  u.setPassword(res.getString(res.getColumnIndex(USERS_COLUMN_PASSWORD)));
	    	  u.setEmail(res.getString(res.getColumnIndex(USERS_COLUMN_EMAIL)));
	    	  u.setGender(res.getString(res.getColumnIndex(USERS_COLUMN_GENDER)));
	    	  u.setAge(res.getInt(res.getColumnIndex(USERS_COLUMN_AGE)));
	    	  u.setCity(res.getString(res.getColumnIndex(USERS_COLUMN_CITY)));
	    	
	    	  if(u.getName().equals(username) && u.getPassword().equals(password))
	    	  {   SQLiteDatabase db2 = this.getWritableDatabase();
	          ContentValues contentValues = new ContentValues();
	          contentValues.put("isonline", 1);

	          db2.update("users", contentValues, "id = ? ", new String[] { Integer.toString(u.getId()) } );
	         
	    		  
	    		  
	    		  
	    		  
	    		  return u;
	    	  
	    	  }
	    	  
	    	  res.moveToNext();
	      }
	      u.setId(-1);
	      return u;
	   }
   
   
   
   public User getcUser(String email){
	      ArrayList array_list = new ArrayList();
	      User u=new User();
	      u.setId(-1);
	      //hp = new HashMap();
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from users", null );
	      res.moveToFirst();
	      while(res.isAfterLast() == false){
	    	  u.setId(res.getInt(res.getColumnIndex(USERS_COLUMN_ID)));
	    	  u.setName(res.getString(res.getColumnIndex(USERS_COLUMN_USERNAME)));
	    	  u.setPassword(res.getString(res.getColumnIndex(USERS_COLUMN_PASSWORD)));
	    	  u.setEmail(res.getString(res.getColumnIndex(USERS_COLUMN_EMAIL)));
	    	  u.setGender(res.getString(res.getColumnIndex(USERS_COLUMN_GENDER)));
	    	  u.setAge(res.getInt(res.getColumnIndex(USERS_COLUMN_AGE)));
	    	  u.setCity(res.getString(res.getColumnIndex(USERS_COLUMN_CITY)));
	    	
	    	  if(u.getEmail().equals(email))
	    	  {   SQLiteDatabase db2 = this.getWritableDatabase();
	          	    		  return u;
	    	  
	    	  }
	    	  
	    	  res.moveToNext();
	      }
	      u.setId(-1);
	      return u;
	   }

   
   public User getUser(String username, String password){
	   User u=new User();
	   u.setId(-1);   
	   SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from users where username="+username+"And password="+password+"", null );
	      
	      u.setName(res.getString(res.getColumnIndex(USERS_COLUMN_USERNAME)));
    	  u.setPassword(res.getString(res.getColumnIndex(USERS_COLUMN_PASSWORD)));
    	  u.setEmail(res.getString(res.getColumnIndex(USERS_COLUMN_EMAIL)));
    	  u.setGender(res.getString(res.getColumnIndex(USERS_COLUMN_EMAIL)));
    	  u.setAge(res.getInt(res.getColumnIndex(USERS_COLUMN_EMAIL)));
    	  u.setCity(res.getString(res.getColumnIndex(USERS_COLUMN_EMAIL)));
    	
	      
	      return u;
	   }
   
   public Cursor getData(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from users where id="+id+"", null );
      return res;
   }
   
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, USERS_TABLE_NAME);
      return numRows;
   }
   
   public boolean updateUser (Integer id, String username, String email, String usercity,String gender)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("username", username);
      contentValues.put("email", email);
      contentValues.put("city", usercity);
      contentValues.put("gender", gender);

      db.update("users", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
      return true;
   }

   public Integer deleteContact (Integer id)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      return db.delete("users", 
      "id = ? ", 
      new String[] { Integer.toString(id) });
   }
   
   public ArrayList getAllPosts()
   {
      ArrayList<Status> array_list = new ArrayList();
      
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from posts", null );
      res.moveToFirst();
      while(res.isAfterLast() == false){
          Status data = new Status();
    	  data.Name = res.getString(res.getColumnIndex(POSTS_COLUMN_Name));
    	  data.Userstatus = res.getString(res.getColumnIndex(POSTS_COLUMN_STATUS));
    //	  data.edition = res.getString(res.getColumnIndex(POSTS_COLUMN_STATUS));
   // 	  data.auther = res.getString(res.getColumnIndex(POSTS_COLUMN_AUTHER));
    //	  data.publisher = res.getString(res.getColumnIndex(POSTS_COLUMN_PUBLISHER));
    	  data.Location = res.getString(res.getColumnIndex(POSTS_COLUMN_CITY));
    	  data.Time = res.getString(res.getColumnIndex(POSTS_COLUMN_TIME));
    	  
    	  array_list.add(data);
    	  res.moveToNext();
      }
   return array_list;
   }

public ArrayList<Status> getResultantPosts(int selectedItemPosition,String Query)
{
    ArrayList<Status> array_list = new ArrayList();
    
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res =  db.rawQuery( "select * from posts", null );
    res.moveToFirst();
    while(res.isAfterLast() == false){

        Status data = new Status();
  	  data.Name = res.getString(res.getColumnIndex(POSTS_COLUMN_Name));
  	  data.Userstatus = res.getString(res.getColumnIndex(POSTS_COLUMN_STATUS));
 // 	  data.edition = res.getString(res.getColumnIndex(POSTS_COLUMN_STATUS));
//  	  data.auther = res.getString(res.getColumnIndex(POSTS_COLUMN_AUTHER));
  //	  data.publisher = res.getString(res.getColumnIndex(POSTS_COLUMN_PUBLISHER));
  	  data.Location = res.getString(res.getColumnIndex(POSTS_COLUMN_CITY));
	  data.Time = res.getString(res.getColumnIndex(POSTS_COLUMN_TIME));
	  
  	  switch(selectedItemPosition)
  	  {
  	  case 0:
  		  if(data.Name.equals(Query))
  		  {
  			array_list.add(data);
  			break;
  		  }
  	  }
  	  
  	  res.moveToNext();
    }

    return array_list;
}
}