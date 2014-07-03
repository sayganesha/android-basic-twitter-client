package com.codepath.apps.basictwitter.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.TwitterClientApp;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter.OnTweetActivityListener;
import com.codepath.apps.basictwitter.fragments.ComposeTweetDialog;
import com.codepath.apps.basictwitter.fragments.UserTimelineFragment;
import com.codepath.apps.basictwitter.fragments.ComposeTweetDialog.ComposeTweetDialogListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity implements ComposeTweetDialogListener, OnTweetActivityListener{
	private TwitterClient client;
	UserTimelineFragment userTimelineFragment;
	User user;
	User origUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		client = TwitterClientApp.getTwitterClient();
	
		// Get the user id for which we are showing this profile info
		String userId = getIntent().getStringExtra("user_id");
		origUser = (User) getIntent().getSerializableExtra("orig_user");
		
		userTimelineFragment = (UserTimelineFragment) getSupportFragmentManager()
										.findFragmentById(R.id.fragment_user_timeline);
		userTimelineFragment.setUserId(userId);
		
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5cb3ff")));  
		
		loadProfileInfo(userId);
	}
	
	private void loadProfileInfo(String user_id)
	{		
		client.getUserLookup(user_id, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray jsonArray) {
				try {
					if (jsonArray.length() > 0) {
						JSONObject jsonObject = jsonArray.getJSONObject(0);
						user = User.fromJSON(jsonObject);
						getActionBar().setTitle("@" + user.getScreenName());
						populateUserDetails(user);
						userTimelineFragment.setUserId("" + user.getUid());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("debug", jsonArray.toString());
				Log.d("debug", "---------------------------------------------");				
			}  
 
  
			@Override 
			public void onFailure(Throwable e, String s) {
				Log.d("debug", "Could not find out username !!-----------------------------------");
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}  
		});
	}
	
	private void populateUserDetails(User u) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvtagLine = (TextView) findViewById(R.id.tvTag);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		TextView tvNumTweets = (TextView) findViewById(R.id.tvNumTweets);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivDetailProfileImage);
		
		tvName.setText(u.getName());
		tvtagLine.setText(u.getTagLine());
		tvFollowers.setText("" + u.getFollowersCount());
		tvFollowing.setText("" + u.getFollowingCount());
		tvNumTweets.setText("" + u.getStatusesCount());
		
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
	}
	
	
	@Override
	public void onTweetReply(long tweet_uid, String userScreenName) {
		FragmentManager fm = getSupportFragmentManager();
		ComposeTweetDialog composeTweetDialog = ComposeTweetDialog.newInstance(origUser, "userName", "userHandle", tweet_uid, userScreenName);
		composeTweetDialog.setMenuVisibility(false);
		composeTweetDialog.show(fm, "fragment_compose_tweet");  
	}

	@Override
	public void onProfileSelected(long userId) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("user_id", "" +  userId);
	    startActivity(i);		
	}

	@Override
	public void onTweetRetweet(long tweet_uid) {
		// do nothing
	}
	
	@Override
	public void onTweetFavourited(long tweet_uid) {
		// do nothing
	}
	
	@Override
	public void onFinishEditDialog(String newStatus, long tweet_id) {
		JSONObject tweetObj = new JSONObject();
		Tweet t = null;
		try {
			tweetObj.put("text", newStatus);
			tweetObj.put("id", "12345");

			String date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").format(new Date());
			tweetObj.put("created_at", date);
			JSONObject userJson = new JSONObject();
			userJson.put("name", user.getName());
			userJson.put("id", user.getUid());
			userJson.put("screen_name", user.getScreenName());
			userJson.put("profile_image_url", user.getProfileImageUrl());
			userJson.put("followers_count", user.getFollowersCount());
			userJson.put("friends_count", user.getFollowingCount());
			userJson.put("description", user.getTagLine());
			userJson.put("statuses_count", user.getStatusesCount());
			tweetObj.put("user", userJson);
			t = Tweet.fromJSON(tweetObj);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		/*if (t != null) {
			adTweets.insert(t, 0);  
			adTweets.notifyDataSetChanged();
			lvTweets.setSelection(0);
		}*/
		client.postStatusUpdate(newStatus, tweet_id,  new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, JSONArray arg1) {
				Toast.makeText(getApplicationContext(), "Successfully posted!", Toast.LENGTH_SHORT).show();

			} 
		});
		//populateTimeline();*/
	}  

}
