package com.codepath.apps.basictwitter.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	// get it once since every item needs it (saves just a function call btw)
	ImageLoader imageLoader = ImageLoader.getInstance();
	
	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		
		View v = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			v = inflater.inflate(com.codepath.apps.basictwitter.R.layout.tweet_item, parent, false);
		} else {
			v = convertView;
		}
		
		// Within the view, find the required items and set them up
		
		// Handle the profile image
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		ivProfileImage.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		
		// handle the user name
		TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
		tvUserName.setText(tweet.getUser().getName());
		
		// handle the user handle
		TextView tvUserHandle = (TextView) v.findViewById(R.id.tvUserHandle);
		tvUserHandle.setText("@" + tweet.getUser().getScreenName());
		
		// handle the tweet body
		TextView tvTweetBody = (TextView) v.findViewById(R.id.tvTweetBody);
		tvTweetBody.setText(tweet.getBody());
		
		// handle the time since
		TextView tvTweetedSince = (TextView) v.findViewById(R.id.tvTweetedSince);
		tvTweetedSince.setText(tweet.getRelativeTime());
		return v;
	}
}
