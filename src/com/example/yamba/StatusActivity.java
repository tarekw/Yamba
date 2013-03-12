package com.example.yamba;

import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements LocationListener {
	static final String TAG = "StatusActivity";
	static final String PROVIDER = LocationManager.GPS_PROVIDER;
	EditText editStatus;
	LocationManager locationManager;
	Location location;
	DialogFragment dialogViewer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		Debug.startMethodTracing("Yamba.trace");
		
		setContentView(R.layout.activity_status);
		
		editStatus = (EditText) findViewById(R.id.edit_status);
		
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation(PROVIDER);
		dialogViewer = DialogViewer.newInstance();
	}

	@Override
	protected void onStop() {
		super.onStop();
		
//		Debug.stopMethodTracing();
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(PROVIDER, 30000, 1000, this);
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
		Log.d(TAG, "onPause");
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
		Intent intentPreferencs = new Intent(this, PrefsActivity.class);
//		Intent intentTimeline = new Intent(this, TimelineActivity.class);
		
		switch (item.getItemId()) {
		case R.id.item_start_service:
			startService(intentUpdater);
			return true;
		case R.id.item_stop_service:
			stopService(intentUpdater);
			return true;
		case R.id.item_refresh_service:
			startService(intentRefresh);
			return true;
		case R.id.item_preferences:
			startActivity(intentPreferencs);
			return true;
/*		case R.id.item_timeline:
			startActivity(intentTimeline);
			return true;
*/		default:
			return false;
		}
	}

	public void onClick(View v) {
		final String statusText = editStatus.getText().toString();
		Log.d("StatusActivity", "Status text" + statusText);
		Log.d("StatusActivity", "onClicked!");
		
//		setContentView(R.layout.progress_indicator);
		showDialog();
		new PostToTwitter().execute(statusText);
	}

	class PostToTwitter extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... params) {
			try {
				((YambaApp) getApplication()).getTwitter().setStatus(params[0]);
				return "Successfully posted " + params[0];
			} catch (TwitterException e) {
				e.printStackTrace();
				return "Failed to post " + params[0];
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//			setContentView(R.layout.activity_status);
			hideDialog();
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
	}

	void showDialog() {

	    // DialogFragment.show() will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

	    // Show the dialog.
	    dialogViewer.show(ft, "dialog");
	}
	
	void hideDialog() {
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    dialogViewer.dismiss();
	}
	
	// LocationListener callbacks
	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		Log.d(TAG, "onLocationChanged: " + location.toString());
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Log.d(TAG, "onProviderDisabled");
	}

	@Override
	public void onProviderEnabled(String arg0) {
		Log.d(TAG, "onProviderEnabled");
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		Log.d(TAG, "onStatusChanged");
	}
}

