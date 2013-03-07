package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	static final String TAG = "UpdaterService";
	static final int DELAY = 30;	// in seconds
	boolean running = false;
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		running = true;
	
		new Thread() {
			public void run() {
				try {
					while (running) {
						List<Status> timeline = ((YambaApp) getApplication()).getTwitter().getPublicTimeline();

						for (Status status : timeline) {
							Log.d(TAG, String.format("%s: %s", status.user.name,
									status.text));
						}
						
						int delay = Integer.parseInt(((YambaApp) getApplication()).prefs.getString("delay", "30"));
						Thread.sleep(delay * 1000);
					}
				} catch (TwitterException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		Log.d(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		running = false;
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
