package com.codepath.apps.basictwitter.activities;

import com.codepath.apps.basictwitter.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class FragmentComposeTweet extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_timeline);
		Intent i = getIntent();
		String userName = i.getStringExtra("userName");
		String userHandle = i.getStringExtra("userHandle");
		showComposeTweetDialog(userName, userHandle);
	}

	private void showComposeTweetDialog(String userName, String userHandle) {
		/*FragmentManager fm = getSupportFragmentManager();
		ComposeTweetDialog composeTweetDialog = ComposeTweetDialog.newInstance(userName, userHandle);
		composeTweetDialog.show(fm, "fragment_compose_tweet");*/
		
	}

}
