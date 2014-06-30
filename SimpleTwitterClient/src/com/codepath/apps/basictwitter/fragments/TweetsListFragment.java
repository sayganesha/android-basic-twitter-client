package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.TwitterClientApp;
import com.codepath.apps.basictwitter.adapters.EndlessScrollListener;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetsListFragment extends Fragment implements OnItemClickListener {
	protected TwitterClient client;
	protected ArrayList<Tweet> tweets;
	protected ArrayAdapter<Tweet> adTweets;
	protected PullToRefreshListView lvTweets;
	protected Long latest_tweet_id = 1L;
	protected int curr_page = 0;
	protected final int MAX_PAGE = 8;

	public interface OnTweetActivityListener {
		public void onProfileSelected(long tweet_uid, String userScreenName);
	}
	
	protected OnTweetActivityListener listener;
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
		// get the tweet on which the user clicked
		Tweet tweet = adTweets.getItem(position);  
		if (listener == null) {
			// no listener is interested in the click
			return;
		}
		listener.onProfileSelected(tweet.getUid(), tweet.getUser().getScreenName());
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		adTweets = new  TweetArrayAdapter(getActivity(), tweets);
		client = TwitterClientApp.getTwitterClient();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnTweetActivityListener) {
			listener = (OnTweetActivityListener) activity;
		}
	}
	 
  
	protected abstract void populateTimeline();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		
		// Attach the listener to the AdapterView onCreate
		lvTweets = (PullToRefreshListView)v.findViewById(R.id.lvTweets);       
		lvTweets.setAdapter(adTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
 			public void onLoadMore(int page, int totalItemsCount) {
				// If new page is less then MAX_PAGE, only
				// then fetch some more data
				if (page < MAX_PAGE) {
					curr_page++;
					populateTimeline();
				} 
			} 
		});

		// Set a listener to be invoked when the list should be refreshed.
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateTimeline();
			}
		});
		
		lvTweets.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				// get the tweet on which the user clicked
				Tweet tweet = adTweets.getItem(position);  
				if (listener == null) {
					// no listener is interested in the click
					return;
				}
				listener.onProfileSelected(tweet.getUid(), tweet.getUser().getScreenName());
				
			}
		});

		// Return the view layout
		return v;
	}

	private void refreshTimeline() 
	{
		curr_page = 0;
		latest_tweet_id = 1L;
		populateTimeline();
	}


}
