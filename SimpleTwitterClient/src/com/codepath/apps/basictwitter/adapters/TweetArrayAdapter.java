package com.codepath.apps.basictwitter.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> implements OnClickListener {

	// get it once since every item needs it (saves just a function call btw)
	ImageLoader imageLoader = ImageLoader.getInstance();
	OnTweetActivityListener listener;

	public interface OnTweetActivityListener {
		public void onProfileSelected(long userId);
		public void onTweetReply(long tweet_uid, String userScreenName);
		public void onTweetRetweet(long tweet_uid);
		public void onTweetFavourited(long tweet_uid);
	}

	public TweetArrayAdapter(Context context, List<Tweet> tweets, OnTweetActivityListener listener) {
		super(context, 0, tweets);
		this.listener = listener;
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
		ivProfileImage.setTag(tweet);
		ivProfileImage.setOnClickListener(this);

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

		// handle the reply button
		ImageButton ivReply = (ImageButton) v.findViewById(R.id.btTweetReply);
		ivReply.setTag(tweet);
		ivReply.setOnClickListener(this);

		// handle the reply button
		ImageButton ivRetweet = (ImageButton) v.findViewById(R.id.btTweetRetweet);
		ivRetweet.setTag(tweet);
		ivRetweet.setOnClickListener(this);
		if (tweet.isRetweeted()) {
			ivRetweet.setBackgroundResource(R.drawable.ic_action_retweeted);
		} else {
			ivRetweet.setBackgroundResource(R.drawable.ic_action_tweet_retweet);
		}

		ImageButton ivFavorite = (ImageButton) v.findViewById(R.id.btTweetLike);
		ivFavorite.setTag(tweet);
		ivFavorite.setOnClickListener(this);
		if (tweet.isFavorited()) {  
			ivFavorite.setBackgroundResource(R.drawable.ic_action_favorited);
		} else {
			ivFavorite.setBackgroundResource(R.drawable.ic_action_tweet_like);
		} 
		
		return v;
	}


	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
		case R.id.ivProfileImage: 
			onProfileImageClick(view);
			break;
		case R.id.btTweetReply:
			onTweetReply(view);
			break;
		case R.id.btTweetRetweet:
			onTweetRetweet((ImageButton)view);
			break;
		case R.id.btTweetLike:
			onTweetFavourited((ImageButton)view);
			break;		
		default:
			break;
		}
	}

	public void onProfileImageClick(View view) {  
		if (listener != null) {
			Tweet tweet = (Tweet) view.getTag();
			listener.onProfileSelected(tweet.getUser().getUid());
		}
	}

	public void onTweetReply(View view) {
		if (listener != null) {
			Tweet tweet = (Tweet) view.getTag();
			listener.onTweetReply(tweet.getUid(), tweet.getUser().getScreenName());
		}
	}

	public void onTweetRetweet(ImageButton view) {
		Tweet tweet = (Tweet) view.getTag();
		if (listener != null) {
			listener.onTweetRetweet(tweet.getUid());
		}
		if (!tweet.isRetweeted()) {
			view.setBackgroundResource(R.drawable.ic_action_retweeted);
		} else {
			view.setBackgroundResource(R.drawable.ic_action_tweet_retweet);
		}
	}
	
	public void onTweetFavourited(ImageButton view) {
		Tweet tweet = (Tweet) view.getTag();
		if (listener != null) {
			listener.onTweetFavourited(tweet.getUid());
		}
		if (!tweet.isFavorited()) {  
			view.setBackgroundResource(R.drawable.ic_action_favorited);
		} else {
			view.setBackgroundResource(R.drawable.ic_action_tweet_like);
		} 
	}
}
