package com.example.bonjour;

public final class BonjourApi {
	public final static String API_URL="http://bonjourapi.azurewebsites.net/";
	public final static String USER_GET_API="/api/User/";
	public final static String USER_POST_API="/api/User/";
    public final static String USER_PUT_API="/api/User/";
    public final static String USER_DELETE_API="/api/User/";
    public final static String USER_VALIDATE_API="api/User/Validate";
    public final static String USER_PUT_OFFLINE_API="api/User/Offline/";
    public final static String USER_NEARBY_API_ID="api/User/NearbyAll/";
    public final static String USER_LOGOUT_API_ID="api/User/Offline/";

    public final static String STATUS_GETALL = "api/Status";
    public final static String STATUS_GET = "api/Status/";//  /{id}
    public final static String STATUS_PUT = "api/Status/";//{id}
    public final static String STATUS_POST = "api/Status";
    public final static String STATUS_DELETE = "api/Status/";//{id}

    public final static String GET_FRIENDS_ID="api/FriendshipRelation/GetFriends/";
    public final static String GET_FRIENDSHIP_ID="api/FriendshipRelation/";
    public final static String PUT_FRIENDSHIP_ID="api/FriendshipRelation/";
    public final static String POST_FRIENDSHIP="api/FriendshipRelation";

    public final static String GET_CHAT_FRIENDSHIP_ID="api/Chats/";
    public final static String POST_CHAT="api/Chats";
    public final static String EMERGENCY_MESSAGE_GET="api/EmergencyMessages/";
    public final static String EMERGENCY_MESSAGE_POST="api/EmergencyMessages";



}
