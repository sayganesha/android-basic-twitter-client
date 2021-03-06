package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	String userId = new String();
	
	public void setUserId(String userId)
	{ 
		this.userId = userId;
		// fetch the new user information
		populateTimeline();
	}
	
	@Override
	protected void populateTimeline() {
		if (userId.length() == 0) {
			// user id not set, nothing to do
			return;
		}
		client.getUserTimeline(userId, latest_tweet_id, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonArray) {
				if (curr_page == 0) {
					adTweets.clear();
				}
				ArrayList<Tweet> tweets = Tweet.fromJSONArray(jsonArray);
				adTweets.addAll(tweets);
				if (latest_tweet_id == 1 && tweets.size() > 0) {
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
				lvTweets.setSelection(0);
			}  
 
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", "Failure !!-----------------------------------");
				Log.d("debug", e.toString());
				Log.d("debug", s);
				lvTweets.onRefreshComplete();
				lvTweets.setSelection(0);
			}  
		}); 

	}

}
