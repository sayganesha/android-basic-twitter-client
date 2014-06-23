package com.codepath.apps.basictwitter.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeTweetDialog extends DialogFragment implements TextWatcher, OnEditorActionListener  {

	private EditText etComposeTweet;
	private TextView tvComposeUserName;
	private TextView tvComposeUserHandle;
	private TextView tvComposeRemainingChars;
	private ImageButton btCancelTweet;
	private Button btSendTweet;
	private final int MAX_CHARS = 140;

	public interface ComposeTweetDialogListener {
	    void onFinishEditDialog(String inputText);
	}
	
	
	public ComposeTweetDialog() {
		// Empty constructor required for DialogFragment
	}

	public static ComposeTweetDialog newInstance(User user, String userName, String userHandle) {
		ComposeTweetDialog frag = new ComposeTweetDialog();
		Bundle args = new Bundle();
		args.putString("userName", userName);
		args.putString("userHandle", userHandle);
		args.putSerializable("user", user);
		frag.setArguments(args);
		return frag;
	}


	public void onCancelTweet(View w) {
		// end the activity
		dismiss();
	}

	public void onSendTweet(View w) {
		// end the activity
		dismiss();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View view = inflater.inflate(com.codepath.apps.basictwitter.R.layout.fragment_compose_tweet, container);

		//String title = getArguments().getString("title", "Compose Tweet");
		//getDialog().setTitle(title);


		btCancelTweet = (ImageButton) view.findViewById(com.codepath.apps.basictwitter.R.id.btCancelTweet);
		btCancelTweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View w) {
				// TODO Auto-generated method stub
				dismiss();

			}
		});
		btSendTweet = (Button) view.findViewById(com.codepath.apps.basictwitter.R.id.btSendTweet);
		btSendTweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View w) {
				// TODO Auto-generated method stub
				ComposeTweetDialogListener activity = (ComposeTweetDialogListener) getActivity();
				activity.onFinishEditDialog(etComposeTweet.getText().toString());
				dismiss();
			}
		});

		User user = (User) getArguments().getSerializable("user");
		if(user != null) {
			ImageLoader imageLoader = ImageLoader.getInstance();
			ImageView ivProfileImage = (ImageView) view.findViewById(R.id.ivComposeUserProfileImg);
			ivProfileImage.setImageResource(android.R.color.transparent);
			imageLoader.displayImage(user.getProfileImageUrl(), ivProfileImage);
		}

		tvComposeUserName = (TextView) view.findViewById(com.codepath.apps.basictwitter.R.id.tvComposeUserName);
		tvComposeUserName.setText(user.getName());

		tvComposeUserHandle = (TextView) view.findViewById(com.codepath.apps.basictwitter.R.id.tvComposeUserHandle);
		tvComposeUserHandle.setText(user.getScreenName());

		tvComposeRemainingChars = (TextView) view.findViewById(com.codepath.apps.basictwitter.R.id.tvRemainingChars);
		tvComposeRemainingChars.setText("" + MAX_CHARS);

		/* Not showing any title
		String title = getArguments().getString("title", "Enter Name");
		getDialog().setTitle(title);*/

		etComposeTweet = (EditText) view.findViewById(com.codepath.apps.basictwitter.R.id.etComposeTweet);
		etComposeTweet.requestFocus();
		etComposeTweet.addTextChangedListener(this);


		//getDialog().getWindow().setSoftInputMode(
		//		WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return view;
	}


	private void updateRemainingChars() {
		if ( etComposeTweet.getText().toString().length() > MAX_CHARS) {
			etComposeTweet.setText(etComposeTweet.getText().toString().substring(0, MAX_CHARS));
		}
		int remaining_chars = MAX_CHARS - etComposeTweet.getText().toString().length();
		tvComposeRemainingChars.setText("" + remaining_chars);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		updateRemainingChars();

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
