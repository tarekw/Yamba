package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements OnSharedPreferenceChangeListener {
	static final String TAG = "YambaApp";
	public static final String ACTION_NEW_STATUS = "com.example.yamba.NEW_STATUS";
	private Twitter twitter;
	SharedPreferences prefs;	// sharing only within the context of the app not with other apps
	StatusData statusData;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// prefs stuff
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		statusData = new StatusData(this);
		
		Log.d(TAG, "onCreated");
	}
	
	public Twitter getTwitter() {
		if (twitter == null) {
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			String url = prefs.getString("server", "");
			
			// twitter stuff
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(url);
		}
		return twitter;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key) {
		twitter = null;
		Log.d(TAG, "onSharedPreferencesChanged for key: " + key);
		
	}
	
	// TODO: the count logic is broken, but the update works
	long lastTimeStampSeen = -1;
	public int pullAndInsert() {
		int count = 0;
		try {
			List<Status> timeline = getTwitter().getPublicTimeline();

			for (Status status : timeline) {
				statusData.insert(status);
				if (status.createdAt.getTime()>lastTimeStampSeen) {
					count++;
					lastTimeStampSeen = status.createdAt.getTime();
				}
				Log.d(TAG, String.format("%s: %s", status.user.name,
						status.text));
			}
		} catch (TwitterException e) {
			Log.d(TAG, "failed to getPublicTimeline");
			e.printStackTrace();
		}
		
		if (count>0) {
			sendBroadcast(new Intent(ACTION_NEW_STATUS).putExtra("count", count));
		}
		
		return count;
	}
}
