package com.example.yamba;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class TimelineActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	static final String TAG = "TimelineActivity";
	static final String[] FROM = { StatusProvider.C_USER, StatusProvider.C_TEXT, StatusProvider.C_CREATED_AT };
	static final int[] TO = { R.id.text_user, R.id.text_tweet, R.id.text_created_at };
	static final int STATUS_LOADER = 47;
//	Cursor cursor;
	private SimpleCursorAdapter adapter;
	private TimelineReceiver receiver;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		setContentView(R.layout.timeline);
//		listOut = (ListView) findViewById(R.id.list_out);
		
//		cursor = ((YambaApp)getApplication()).StatusProvider.query();
//		cursor = getContentResolver().query(StatusProvider.CONTENT_URI, null, null, null, StatusProvider.C_CREATED_AT + " DESC");
//		cursor = managedQuery(StatusProvider.CONTENT_URI, null, null, null, StatusProvider.C_CREATED_AT + " DESC");
		
		adapter = new SimpleCursorAdapter(this, R.layout.row, null, FROM, TO);
		adapter.setViewBinder(VIEW_BINDER);
		getLoaderManager().initLoader(STATUS_LOADER, null, this);
		
		setTitle(R.string.timeline);
		setListAdapter(adapter);
		
/*		while (cursor.moveToNext()) {
			String user = cursor.getString(cursor.getColumnIndex(StatusProvider.C_USER));
			String text = cursor.getString(cursor.getColumnIndex(StatusProvider.C_TEXT));
			textOut.append(String.format("%s, %s\n", user, text));
		}
*/	}
	

	@Override
	protected void onResume() {
		super.onResume();
		if (receiver == null)
			receiver = new TimelineReceiver();
		registerReceiver(receiver, new IntentFilter(YambaApp.ACTION_NEW_STATUS));
	}	
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	static final ViewBinder VIEW_BINDER = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() != R.id.text_created_at) return false;
//			if (cursor.getColumnIndex(StatusProvider.C_CREATED_AT) != columnIndex) return false;
			
			long time = cursor.getLong(cursor.getColumnIndex(StatusProvider.C_CREATED_AT));
			CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(time);
			((TextView)view).setText(relativeTime);
			
			return true;
		}
	};

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
		Intent intentStatusUpdate = new Intent(this, StatusActivity.class);
		
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
		case R.id.item_status_update:
			startActivity(intentStatusUpdate);
			return true;
		default:
			return false;
		}
	}
	
	class TimelineReceiver extends BroadcastReceiver {
		static final String TAG = "TimelineReceiver";
		@Override
		public void onReceive(Context context, Intent intent) {			
//			cursor = ((YambaApp)getApplication()).StatusProvider.query();
//			cursor = context.getContentResolver().query(StatusProvider.CONTENT_URI, null, null, null, StatusProvider.C_CREATED_AT + " DESC");
//			adapter.changeCursor(cursor);
			getLoaderManager().restartLoader(STATUS_LOADER, null, TimelineActivity.this);
			Log.d(TAG, "onReceive with count :" + intent.getIntExtra("count", 0));
		}
		
	}

	// LoaderManager.LoaderCallbacks
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, StatusProvider.CONTENT_URI, null, null, null, StatusProvider.C_CREATED_AT + " DESC");
	}


	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		adapter.swapCursor(cursor);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
}
