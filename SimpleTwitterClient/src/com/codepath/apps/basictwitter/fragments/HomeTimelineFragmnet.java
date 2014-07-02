package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.view.View;
import android.widget.AdapterView;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragmnet extends TweetsListFragment {
	
	
	protected void populateTimeline() {
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
				lvTweets.onRefreshComplete();
				if (curr_page == 0) {
					lvTweets.setSelection(0);
				}
			}  

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", "Failure !!-----------------------------------");
				Log.d("debug", e.toString());
				Log.d("debug", s);
				lvTweets.onRefreshComplete();
			}  
		});  
	}

	
}
