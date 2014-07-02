package com.codepath.apps.basictwitter.models;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;
import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Tweet")
public class Tweet  extends Model {
	
	@Column(name = "Body", unique = false)
	private String body;
	
	@Column(name = "Uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	
	@Column(name = "CreatedAt", unique = false)
	private String createdAt;

	@Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	private User user;
	
	@Column(name = "Retweeted")
	private boolean retweeted;

	@Column(name = "RetweetCount")
	private long retweetCount;
	
	@Column(name = "favorited")
	private boolean favorited;
	
	@Column(name = "FavoriteCount")
	private long favoriteCount;
	
	public String getRetweetCount() {
		return "" + retweetCount;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public String getFavoriteCount() {
		return "" + favoriteCount;
	}

	public boolean isRetweeted() {
		return retweeted;
	}

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
	 
		relativeDate = relativeDate.replace(" ago", "").
						replace("hours", "h").replace("hour", "h")
						.replace("days", "d").replace("day", "e")
						.replace("minutes", "m").replace("minute", "m")
						.replace("weeks", "w").replace("week", "w")
						.replace("seconds", "s").replace("second", "s")
						.replace("years", "y").replace("year", "y").replace(" ", "");
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
			if (jsonObject.has("retweeted")) {
				tweet.retweeted = jsonObject.getBoolean("retweeted");
			} else {
				tweet.retweeted = false;
			}
			if (jsonObject.has("retweet_count")) {
				tweet.retweetCount = jsonObject.getLong("retweet_count");
			} else {
				tweet.retweetCount = 0;
			}
			if (jsonObject.has("favorited")) {
				tweet.favorited = jsonObject.getBoolean("favorited");
			} else {
				tweet.favorited = false;
			}
			if (jsonObject.has("favorite_count")) {
				tweet.favoriteCount = jsonObject.getLong("favorite_count");
			} else {
				tweet.favoriteCount = 0;
			}
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
			tweet.user.save();
			tweet.save();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	
	 public static List<Tweet> getAll() {
	        // This is how you execute a query
	        return new Select().from(Tweet.class).execute();
	  }
}
