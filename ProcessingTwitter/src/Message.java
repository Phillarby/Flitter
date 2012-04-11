
import java.awt.Color;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.*;

public class Message {
	private Date _date;
	private String _text;
	private ArrayList<String> _hashTags;
	private String _userLanguage;
	private String _userScreenName;
	private boolean _retweeted;
	String _userProfileImageURL;
	int _retweetedCount;
	String _place;
	String _geo;
	String _source;
	String _coordinates;
	String _userTimeZone;
	String _userLocation;
	
	 
	/*
	 * Potential fields not yet implemented
	String _in_reply_to_status_id_str;
	String id_str;
	String in_reply_to_user_id;
	ArrayList<String> _user_mentions;
	ArrayList<String> _uRLs;
	boolean _userIsTranslator;
	int _userStatusesCount;
	boolean _userProfileBackgroundTile;
	int _userListedCount;
	int _userFavouritesCount;
	Color _userProfileLinkColour;
	String _userURL;
	int favourites_count;
	Color profile_link_color;
	boolean contributors_enabled;
	int following;
	Color profile_sidebar_border_color;
	String _userDefaultProfileImage;
	boolean _userDefaultProfile;
	boolean profile_use_background_image;
	String _UserProfileBackgroundImageUrlHttps;
	Color _userProfileBackgroundColor;
	String _userNotifications;
	String _userProfileBackgroundImageUrl;
	boolean _userProtected;
	boolean _userShowAllInlineMedia;
	boolean _userGeoEnabled;
	Date _userCreatedAt;
	String _userName;
	boolean _userVerified;
	Color _userProfileTextColour;
	int _userFollowersCount;
	String _userProfileImageUrlHttps;
	String _userId;
	int _userUTCOffset;
	int _userFriendsCount;
	Color _userProfileSidebarFillColour;
	BigInteger _id;
	boolean _favourited;
	boolean _truncated;
	
	*/
	
	public Date getDate(){ return _date;}
	public void setDate(Date d){_date = d;}
	
	public String getText(){return _text;}
	public void setText(String t){_text = t.replace("\\/", "/");}
	
	public ArrayList<String> getHashTags(){return _hashTags;}
	public void setHashTags(ArrayList<String> t){_hashTags = t;}
	
	public String getUserLanguage(){return _userLanguage;}
	public void setUserLanguage(String language){_userLanguage = language;}
	
	public String getUserScreenName(){return _userScreenName;}
	public void setUserScreenName(String sn){_userScreenName = sn;}
	
	public boolean getRetweeted(){return _retweeted;}
	public void setRetweeted(boolean r){_retweeted = r;}
	
	public String getUserProfileImageUrl() {return _userProfileImageURL;}
	public void setUserProfileImageUrl(String upi){_userProfileImageURL = upi;}
	
	public int getRetweetedCount(){return _retweetedCount;}
	public void setRetweetedCount(int r){_retweetedCount = r;}
	
	public String getPlace() {return _place;}
	public void setPlace(String p){_place = p;}
	
	public String getGeo() {return _geo;}
	public void setGeo(String g){_geo = g;}
	
	public String getSource() {return _source;}
	public void setSource(String s){_source = s;}
	
	public String getCoordinates() {return _coordinates;}
	public void setCoordinates(String c){_coordinates = c;}
	
	public String getUserTimezone() {return _userTimeZone;}
	public void setUserTimezone(String t){_userTimeZone = t;}
	
	public String getUserLocation() {return _userLocation;}
	public void setUserLocation(String l){_userLocation = l;}
	
	public Message()
	{
		Random r = new Random();
		_userProfileImageURL = "";
		_text =  r.nextDouble() + " No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text No Text";
		_userScreenName = "Unknown User";
	}
}
