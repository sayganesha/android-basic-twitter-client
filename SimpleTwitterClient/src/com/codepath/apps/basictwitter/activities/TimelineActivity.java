package com.codepath.apps.basictwitter.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.TwitterClientApp;
import com.codepath.apps.basictwitter.activities.ComposeTweetDialog.ComposeTweetDialogListener;
import com.codepath.apps.basictwitter.adapters.EndlessScrollListener;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity  implements ComposeTweetDialogListener{
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> adTweets;
	private ListView lvTweets;

	private String userScreenName;
	private Long latest_tweet_id = 1L;
	// The logged in user
	private User user;
	private int curr_page = 0;
	private final int MAX_PAGE = 8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
   
		client = TwitterClientApp.getTwitterClient();

		lvTweets = (ListView)findViewById(R.id.lvTweets);       
		tweets = new ArrayList<Tweet>();
		adTweets = new  TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(adTweets);       
		// Attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
	    @Override
	    public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
	        if (page < MAX_PAGE) {
	        	curr_page++;
	        	populateTimeline();
	        }
                // or customLoadMoreDataFromApi(totalItemsCount); 
	    } 
        });
		
		
		getActionBar().setBackgroundDrawable(new 
	               ColorDrawable(Color.parseColor("#5cb3ff")));  
		getActionBar().setTitle("Twitter");
		
		getUserName();
		populateTimeline();
	}    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.twitter_menu, menu);
		return true;
	}  

	@Override
    public void onFinishEditDialog(String newStatus) {
       client.postStatusUpdate(newStatus, new JsonHttpResponseHandler() {
    	   @Override
    	public void onSuccess(int arg0, JSONArray arg1) {
    		Toast.makeText(getApplicationContext(), "Successfully posted!", Toast.LENGTH_SHORT).show();
    		
    	} 
       });
       populateTimeline();
    }
	
	public void onMenuItemClick(MenuItem mi) {
		switch (mi.getItemId()) {
		case R.id.miCompose :
			showComposeTweetDialog();
			break;
		case R.id.miRefresh:
			// start from the beginning
			curr_page = 0;
			latest_tweet_id = 1L;
			populateTimeline();
			break;
		default:
			return;
		}  
	} 

	private void showComposeTweetDialog() {
		Intent i = new Intent(getBaseContext(), FragmentComposeTweet.class);
		i.putExtra("userName", "User");
		i.putExtra("userHandle", "@userHandle");
		i.putExtra("user", user);
		FragmentManager fm = getSupportFragmentManager();
		ComposeTweetDialog composeTweetDialog = ComposeTweetDialog.newInstance(user, "userName", "userHandle");
		composeTweetDialog.setMenuVisibility(false);

		composeTweetDialog.show(fm, "fragment_compose_tweet");
		//startActivity(i);
	}

	private void getProfileInfo() {
		client.getUserLookup(userScreenName, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray jsonArray) {
				try {
					if (jsonArray.length() > 0) {
						JSONObject jsonObject = jsonArray.getJSONObject(0);
						user = User.fromJSON(jsonObject);
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

	private void getUserName() {
		client.getUserSetttings(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				try {
					userScreenName = jsonObject.getString("screen_name");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (userScreenName != null) {
					getProfileInfo();
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

	private void populateTimeline() {
		client.getHomeTimeline(latest_tweet_id, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonArray) {
				if (curr_page == 0) {
					adTweets.clear();
				}
				ArrayList<Tweet> tweets = Tweet.fromJSONArray(jsonArray);
				adTweets.addAll(tweets);
				if (latest_tweet_id == 1) {
					latest_tweet_id = tweets.get(0).getUid();
				}
				for (Tweet tweet : tweets) {
					if (tweet.getUid() < latest_tweet_id   ) {
						latest_tweet_id = tweet.getUid();
					}
				}
				//adTweets.notifyDataSetChanged();
				Log.d("debug", jsonArray.toString());
				Log.d("debug", "---------------------------------------------");
			}  

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", "Failure !!-----------------------------------");
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}  
		});  
	}
}
