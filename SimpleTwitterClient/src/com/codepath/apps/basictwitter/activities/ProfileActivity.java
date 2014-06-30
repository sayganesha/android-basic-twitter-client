package com.codepath.apps.basictwitter.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.TwitterClientApp;
import com.codepath.apps.basictwitter.fragments.UserTimelineFragment;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private TwitterClient client;
	UserTimelineFragment userTimelineFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		client = TwitterClientApp.getTwitterClient();
	
		// Get the user id for which we are showing this profile info
		String userId = getIntent().getStringExtra("user_id");
		
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
						User u = User.fromJSON(jsonObject);
						getActionBar().setTitle("@" + u.getScreenName());
						populateUserDetails(u);
						userTimelineFragment.setUserId("" + u.getUid());
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
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		
		tvName.setText(u.getName());
		tvtagLine.setText(u.getTagLine());
		tvFollowers.setText("" + u.getFollowersCount());
		tvFollowing.setText("" + u.getFollowingCount());
		
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
	}
}
