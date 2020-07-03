package com.example.bonjour;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;
import java.util.ArrayList;
import java.lang.reflect.Type;

public final class BonjourServiceHandler {

	private final String URL= BonjourApi.API_URL;
	//String URL="http://localhost:6305/";
	//User u= new User("Fateh Ullah", "ufateh@outlook.com","fateh",22,"Islamabad",1, 1,12.0, 17.9,"Male");

	private HttpClient client = null;
	Utility utility = null;
	public BonjourServiceHandler() {
		client=new DefaultHttpClient();
		utility=new Utility();
	}

	public User getUser(int id) throws Exception {

			HttpGet request = new HttpGet();
			request.setURI(new URI(URL+BonjourApi.USER_GET_API+id));
			HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode()==200) {
                String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
                Gson gson = new Gson();
                User user = gson.fromJson(jsonString, User.class);
                return user;
            }
            else
		       return null;
	}

    public Status getPost(int id) throws Exception {

        HttpGet request = new HttpGet();
        request.setURI(new URI(URL+BonjourApi.STATUS_GET+id));
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode()==200) {
            String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
            Gson gson = new Gson();
            Status post = gson.fromJson(jsonString, Status.class);
            return post;
        }
        else
            return null;
    }

    public ArrayList<Status> getAllPosts() throws Exception {

        HttpGet request = new HttpGet();
        request.setURI(new URI(URL+BonjourApi.STATUS_GETALL));
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode()==200) {
            String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
            Gson gson = new Gson();
            Type type = (Type) new TypeToken<ArrayList<Status>>(){}.getType();
            ArrayList<Status> post=new ArrayList<>();
            post = gson.fromJson(jsonString,type);
            return post;
        }
        else
            return null;
    }


    public ArrayList<Lift> getAllLifts() throws Exception {

        HttpGet request = new HttpGet();
        request.setURI(new URI(URL+"api/Lifts"));
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode()==200) {
            String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
            Gson gson = new Gson();
            Type type = (Type) new TypeToken<ArrayList<Lift>>(){}.getType();
            ArrayList<Lift> post=new ArrayList<>();
            post = gson.fromJson(jsonString,type);
            return post;
        }
        else
            return null;
    }

    public Lift addLift(Lift u) throws Exception{

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL + "api/Lifts");

        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        Gson gson= new Gson();
        StringEntity  postingString =new StringEntity(gson.toJson(u));
        post.setEntity(postingString);

        HttpResponse response = client.execute(post);

        String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
        //System.out.println(page);
        Lift user = gson.fromJson(jsonString, Lift.class);
        return user;
    }

	public User addUser(User u) throws Exception{

			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(URL + BonjourApi.USER_POST_API);

			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");
			Gson gson= new Gson();
			StringEntity  postingString =new StringEntity(gson.toJson(u));
			post.setEntity(postingString);

            HttpResponse response = client.execute(post);

            String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
            //System.out.println(page);
            User user = gson.fromJson(jsonString, User.class);
            return user;
	}

    public Status submitNewsDetails(Status u) throws Exception{

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL + BonjourApi.STATUS_POST);

        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        Gson gson= new Gson();
        StringEntity  postingString =new StringEntity(gson.toJson(u));
        post.setEntity(postingString);
        HttpResponse response = client.execute(post);

        String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
        Status p = gson.fromJson(jsonString, Status.class);
        return p;
    }

    public User validateUser(User user) throws Exception{
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(URL + BonjourApi.USER_VALIDATE_API);

            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            Gson gson= new Gson();
            StringEntity  postingString =new StringEntity(gson.toJson(user));
            post.setEntity(postingString);

            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode()==200) {
                String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
                //System.out.println(page);
                User u = gson.fromJson(jsonString, User.class);
                User a=u;
                return u;
            }
            else
                return null;
    }

    public User updateUser(User user) throws Exception{
            HttpClient client = new DefaultHttpClient();
            HttpPut put = new HttpPut(URL + BonjourApi.USER_PUT_API+user.getId());

            put.setHeader("Accept", "application/json");
            put.setHeader("Content-type", "application/json");
            Gson gson= new Gson();
            StringEntity  postingString =new StringEntity(gson.toJson(user));
            put.setEntity(postingString);

            HttpResponse response = client.execute(put);
        if(response.getStatusLine().getStatusCode()==204) {
            return user;
        }else
            return null;
    }

    public Boolean setUserOffline(int id) throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut(URL + BonjourApi.USER_PUT_OFFLINE_API+id);
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-type", "application/json");
        //Gson gson= new Gson();
        //StringEntity  postingString =new StringEntity(gson.toJson(i));
        //put.setEntity(postingString);
        HttpResponse response = client.execute(put);
        if(response.getStatusLine().getStatusCode()==200) {
            return true;
        }else
            return false;
    }

    public ArrayList<NearbyUser> getNearbyUsers(int id) throws Exception {

        HttpGet request = new HttpGet();
        request.setURI(new URI(URL+BonjourApi.USER_NEARBY_API_ID+id));

        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode()==200) {
            String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
            Gson gson = new Gson();
            Type type = (Type) new TypeToken<ArrayList<NearbyUser>>(){}.getType();
            ArrayList<NearbyUser> nearbyUsersList=new ArrayList<>();
        nearbyUsersList = gson.fromJson(jsonString,type);
            return nearbyUsersList;
        }
        else
            return null;
    }
    public ArrayList<BonjourFriendRequest> getFriends(int id,boolean isFriend) throws Exception {

        HttpGet request = new HttpGet();
        request.setURI(new URI(URL+BonjourApi.GET_FRIENDS_ID+id+"/"+isFriend));
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode()==200) {
        String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
        Gson gson = new Gson();
        Type type = (Type) new TypeToken<ArrayList<BonjourFriendRequest>>(){}.getType();
        ArrayList<BonjourFriendRequest> fRequests=new ArrayList<>();
        fRequests = gson.fromJson(jsonString,type);
        return fRequests;
        }
        else
            return null;
    }

    public Boolean confirmFriendRequest(int id) throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut(URL + BonjourApi.PUT_FRIENDSHIP_ID+id);
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(put);
        if(response.getStatusLine().getReasonPhrase()=="OK")
            return true;
        else
            return false;
    }

    public FriendshipRelation sendFriendRequest(FriendshipRelation relation) throws Exception{

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL + BonjourApi.POST_FRIENDSHIP);

        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        Gson gson= new Gson();
        StringEntity  postingString =new StringEntity(gson.toJson(relation));
        post.setEntity(postingString);
        HttpResponse response = client.execute(post);

        String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
        FriendshipRelation f = gson.fromJson(jsonString, FriendshipRelation.class);
        return f;
    }
    public FriendshipRelation getFriendship(int id) throws Exception {

        HttpGet request = new HttpGet();
        request.setURI(new URI(URL+BonjourApi.GET_FRIENDSHIP_ID+id));
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode()==200) {
            String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
            Gson gson = new Gson();
            FriendshipRelation f = gson.fromJson(jsonString, FriendshipRelation.class);
            return f;
        }
        else
            return null;
    }
    public ArrayList<Chat> getChatWithFriendshipId(int id) throws Exception {

        HttpGet request = new HttpGet();
        request.setURI(new URI(URL+BonjourApi.GET_CHAT_FRIENDSHIP_ID+id));
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode()==200) {
        String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
        Gson gson = new Gson();
        Type type = (Type) new TypeToken<ArrayList<Chat>>(){}.getType();
        ArrayList<Chat> chats=new ArrayList<>();
        chats = gson.fromJson(jsonString,type);
        return chats;
        }
        else
            return null;
    }
    public boolean addChatMessage(Chat chat) throws Exception{

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL + BonjourApi.POST_CHAT);

        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        Gson gson= new Gson();
        StringEntity  postingString =new StringEntity(gson.toJson(chat));
        post.setEntity(postingString);
        HttpResponse response = client.execute(post);
        if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
        {
            return true;
        }
        else
        {
            return false;
        }
        /*String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
        Chat p = gson.fromJson(jsonString, Chat.class);
        return p;*/
    }

    public ArrayList<EmergencyMessage> getEmergencyMessages(int userId) throws Exception {
        HttpGet request = new HttpGet();
        request.setURI(new URI(URL+BonjourApi.EMERGENCY_MESSAGE_GET+userId));
        HttpResponse response = client.execute(request);
        if(response.getStatusLine().getStatusCode()==200) {
            String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
            Gson gson = new Gson();
            Type type = (Type) new TypeToken<ArrayList<EmergencyMessage>>(){}.getType();
            ArrayList<EmergencyMessage> messages=new ArrayList<>();
            messages = gson.fromJson(jsonString,type);
            return messages;
        }
        else
            return null;
    }

    public int broadCastEmergencyMessage(EmergencyMessage emergencyMessage) throws Exception{

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL + BonjourApi.EMERGENCY_MESSAGE_POST);

        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        Gson gson= new Gson();
        StringEntity  postingString =new StringEntity(gson.toJson(emergencyMessage));
        post.setEntity(postingString);
        HttpResponse response = client.execute(post);
        if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
        {
            String jsonString = utility.getContentFromResponse(response.getEntity().getContent());
            return Integer.getInteger(jsonString,0);
        }
        else
        {
            return -1;
        }
    }
}
