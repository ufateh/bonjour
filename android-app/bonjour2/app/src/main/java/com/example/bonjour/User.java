package com.example.bonjour;

public class User {

	int Id;
	String name;
	String email;
	String password;
	int age;
	String city;
	int visibility;
	int isOnline;
	double latitude;
	double longitude;
	String gender;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public User()
	{
		
	}
	public User(int id,String name, String email, String password, int age,
			String city, int visibility, int isOnline,
			double latitude, double longitude,String gender)
	{
		super();
		this.Id=id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.age = age;
		this.city = city;
		this.visibility = visibility;
		this.isOnline = isOnline;
		this.latitude = latitude;
		this.longitude = longitude;
		this.gender=gender;
	}
	public User(String name, String email, String password, int age,
			String city, int visibility, int isOnline,
			double latitude, double longitude,String gender)
	{
		super();
	
		this.name = name;
		this.email = email;
		this.password = password;
		this.age = age;
		this.city = city;
		this.visibility = visibility;
		this.isOnline = isOnline;
		this.latitude = latitude;
		this.longitude = longitude;
		this.gender=gender;
	}
	
	public User(int id, String name, String email, String password)
	{
		this.Id=id;
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		this.Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getVisibility() {
		return visibility;
	}
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	public int getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}
	
}