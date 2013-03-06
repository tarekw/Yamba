package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshService extends IntentService {
	static final String TAG = "RefreshService";

	public RefreshService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");

		super.onCreate();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			List<Status> timeline = ((YambaApp) getApplication()).twitter.getPublicTimeline();

			for (Status status : timeline) {
				Log.d(TAG, String.format("%s: %s", status.user.name,
						status.text));
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}	
		Log.d(TAG, "onHandleIntent");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}
}
