package com.codepath.apps.basictwitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "User")
public class User extends Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 782520282314155005L;

	@Column(name = "Name", unique = false)
	private String name;

	@Column(name = "Uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;

	@Column(name = "ScreenName", unique = false)
	private String screenName;

	@Column(name = "ProfileImageUrl", unique = false)
	private String profileImageUrl;
	
	@Column(name = "FollowersCount", unique = false)
	private long followersCount;
	
	public long getStatusesCount() {
		return statusesCount;
	}

	@Column(name = "Status", unique = false)
	private String tagLine;
	
	public long getFollowersCount() {
		return followersCount;
	}

	public long getFollowingCount() {
		return followingCount;
	}

	@Column(name = "FollowingCount", unique = false)
	private long followingCount;
	
	@Column(name = "StatusCount", unique = false)
	private long statusesCount;
	
	
	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public static User fromJSON(JSONObject jsonObject) {
		User user =  new User();
		try {
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
			user.followersCount = jsonObject.getLong("followers_count");
			user.followingCount = jsonObject.getLong("friends_count");
			user.tagLine = jsonObject.getString("description");
			user.statusesCount = jsonObject.getLong("statuses_count");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}

	public String getTagLine() {
		return tagLine;
	}
	
	

}
