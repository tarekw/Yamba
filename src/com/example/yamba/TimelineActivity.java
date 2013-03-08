package com.example.yamba;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class TimelineActivity extends ListActivity {
	static final String[] FROM = { StatusData.C_USER, StatusData.C_TEXT, StatusData.C_CREATED_AT };
	static final int[] TO = { R.id.text_user, R.id.text_tweet, R.id.text_created_at };
	Cursor cursor;
	SimpleCursorAdapter adapter;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.timeline);
//		listOut = (ListView) findViewById(R.id.list_out);
		
		cursor = ((YambaApp)getApplication()).statusData.query();
		
		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
		adapter.setViewBinder(VIEW_BINDER);
		
		setTitle(R.string.timeline);
		setListAdapter(adapter);
		
/*		while (cursor.moveToNext()) {
			String user = cursor.getString(cursor.getColumnIndex(StatusData.C_USER));
			String text = cursor.getString(cursor.getColumnIndex(StatusData.C_TEXT));
			textOut.append(String.format("%s, %s\n", user, text));
		}
*/	}
	
	static final ViewBinder VIEW_BINDER = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() != R.id.text_created_at) return false;
//			if (cursor.getColumnIndex(StatusData.C_CREATED_AT) != columnIndex) return false;
			
			long time = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
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
}
