package com.example.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity {
	
	EditText editStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		Debug.startMethodTracing("Yamba.trace");
		
		setContentView(R.layout.activity_status);
		
		editStatus = (EditText) findViewById(R.id.edit_status);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
//		Debug.stopMethodTracing();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intentUpdater = new Intent(this, UpdaterService.class);
		Intent intentRefresh = new Intent(this, RefreshService.class);
		
		switch (item.getItemId()) {
		case R.id.item_start_service:
			startService(intentUpdater);
			return true;
		case R.id.item_stop_service:
			stopService(intentUpdater);
			return true;
		case R.id.item_refresh_service:
			startService(intentRefresh);
		default:
			return false;
		}
	}

	public void onClick(View v) {
		final String statusText = editStatus.getText().toString();
		Log.d("StatusActivity", "Status text" + statusText);
		Log.d("StatusActivity", "onClicked!");
		
		new PostToTwitter().execute(statusText);
	}

	class PostToTwitter extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... params) {
			try {
				Twitter twitter = new Twitter("student", "password");
				twitter.setAPIRootUrl("http://yamba.marakana.com/api");
				twitter.setStatus(params[0]);
				return "Successfully posted " + params[0];
			} catch (TwitterException e) {
				e.printStackTrace();
				return "Failed to post " + params[0];
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
	}
}

