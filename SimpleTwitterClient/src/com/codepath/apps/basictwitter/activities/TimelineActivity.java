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
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.TwitterClientApp;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter.OnTweetActivityListener;
import com.codepath.apps.basictwitter.fragments.ComposeTweetDialog;
import com.codepath.apps.basictwitter.fragments.ComposeTweetDialog.ComposeTweetDialogListener;
import com.codepath.apps.basictwitter.fragments.HomeTimelineFragmnet;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.fragments.listeners.SupportFragmentTabListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends ActionBarActivity  implements ComposeTweetDialogListener, OnMenuItemClickListener, OnTweetActivityListener{
	private TwitterClient client;
	private String user_id;
	// The logged in user
	private User user;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);


		client = TwitterClientApp.getTwitterClient();



		getUserName();
		setupTabs();

		//populateTimeline();
	}      

	private void setupTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setBackgroundDrawable(new 
				ColorDrawable(Color.parseColor("#5cb3ff")));  
		actionBar.setTitle("Twitter");


		android.support.v7.app.ActionBar.Tab tab1 = actionBar
				.newTab()
				.setText("Home")
				.setIcon(R.drawable.ic_tab_home)
				.setTag("HomeTimelineFragment")
				.setTabListener(new SupportFragmentTabListener<HomeTimelineFragmnet>(R.id.flContainer, this,
						"home", HomeTimelineFragmnet.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		android.support.v7.app.ActionBar.Tab tab2 = actionBar
				.newTab()
				.setText("Mentions")
				.setIcon(R.drawable.ic_tab_mentions)
				.setTag("MentionsTimelineFragment")
				.setTabListener(new SupportFragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this,
						"mentions", MentionsTimelineFragment.class));
		actionBar.addTab(tab2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.twitter_menu, menu);

		MenuItem mi_compose = menu.findItem(R.id.miCompose);
		mi_compose.setOnMenuItemClickListener(this);

		MenuItem mi_profile = menu.findItem(R.id.miProfile);
		mi_profile.setOnMenuItemClickListener(this);
		return true;
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


	public boolean onMenuItemClick(MenuItem mi) {
		switch (mi.getItemId()) {
		case R.id.miCompose :
			showComposeTweetDialog(0, "");
			break;
		case R.id.miProfile:
			launchUserProfile();
			break;
		default:
			break;
		}   
		return false;
	} 


	private void launchUserProfile() {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("user_id", user_id);
		i.putExtra("orig_user", user);
		startActivity(i);
	}
	
	public void onProfileSelected(long tweet_uid, String userScreenName)
	{
		showComposeTweetDialog(tweet_uid, userScreenName);
	}

	private void showComposeTweetDialog(long tweet_uid, String userScreenName) {
		FragmentManager fm = getSupportFragmentManager();
		ComposeTweetDialog composeTweetDialog = ComposeTweetDialog.newInstance(user, "userName", "userHandle", tweet_uid, userScreenName);
		composeTweetDialog.setMenuVisibility(false);
		composeTweetDialog.show(fm, "fragment_compose_tweet");  
	}

	private void getUserName() {
		client.getMyInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonObject) {

				user = User.fromJSON(jsonObject);
				if (user != null) {
					user_id = "" + user.getUid();
				}

				Log.d("debug", jsonObject.toString());
				Log.d("debug", "---------------------------------------------");				
			}  

			@Override
			public void onSuccess(int arg0, JSONArray arg1) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1);
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", "Could not find out username !!-----------------------------------");
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}     
		}); 
	}

	@Override
	public void onTweetReply(long tweet_uid, String userScreenName) {
		showComposeTweetDialog(tweet_uid, userScreenName);
	}

	@Override
	public void onProfileSelected(long userId) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("user_id", "" +  userId);
		i.putExtra("orig_user", user);
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

}
