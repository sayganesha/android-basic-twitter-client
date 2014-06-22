package com.codepath.apps.basictwitter.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.TwitterClientApp;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
     private TwitterClient client;
     private ArrayList<Tweet> tweets;
     private ArrayAdapter<Tweet> adTweets;
     private ListView lvTweets;
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
	
		client = TwitterClientApp.getTwitterClient();
		
		lvTweets = (ListView)findViewById(R.id.lvTweets);       
		tweets = new ArrayList<Tweet>();
		adTweets = new  TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(adTweets);
		
		populateTimeline();
	}    
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.twitter_menu, menu);
        return true;
    }
	
	
	private void populateTimeline() {
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonArray) {
				adTweets.addAll(Tweet.fromJSONArray(jsonArray));
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
