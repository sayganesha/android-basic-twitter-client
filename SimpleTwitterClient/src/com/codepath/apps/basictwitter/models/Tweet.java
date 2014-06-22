package com.codepath.apps.basictwitter.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.text.format.DateUtils;

public class Tweet {
	private String body;
	private long uid;
	private String createdAt;
	private User user;
	
	public String getRelativeTime() {
		return getRelativeTimeAgo(createdAt);
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	@Override
	public String toString() {
		return user.getScreenName() + " : " + body;
	}

	public String getCreatedAt() {
		return createdAt;  
	}

	public User getUser() {
		return user;
	}

	private String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	
	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
		for (int i = 0; i < jsonArray.length(); ++i ) {
			JSONObject jsonTweet = null;
			try {
				jsonTweet = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// could not extract tweet, move on to next json object
				continue;
			}
			Tweet tweet = Tweet.fromJSON(jsonTweet);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		
		return tweets;
	}
	
	public static Tweet fromJSON(JSONObject jsonObject)
	{
		Tweet tweet = new Tweet();
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

}
