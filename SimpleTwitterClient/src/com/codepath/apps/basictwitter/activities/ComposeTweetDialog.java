package com.codepath.apps.basictwitter.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ComposeTweetDialog extends DialogFragment {

	private EditText mEditText;

	public ComposeTweetDialog() {
		// Empty constructor required for DialogFragment
	}

	public static ComposeTweetDialog newInstance(String title) {
		ComposeTweetDialog frag = new ComposeTweetDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		View view = inflater.inflate(com.codepath.apps.basictwitter.R.layout.fragment_compose_tweet, container);
		/*mEditText = (EditText) view.findViewById(R.id.e);
		String title = getArguments().getString("title", "Enter Name");
		getDialog().setTitle(title);
		// Show soft keyboard automatically
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);*/
		return view;
	}

}
