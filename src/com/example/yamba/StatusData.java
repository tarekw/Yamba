package com.example.yamba;

//import winterwell.jtwitter.Twitter.Status;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;

public class StatusData {
	static final String TAG = "StatusData";
//	public static final String DB_NAME = "timeline.db";
//	public static final int DB_VERSION = 1;
//	public static final String TABLE = "status";
//	public static final String C_ID = "_id";
//	public static final String C_CREATED_AT = "created_at";
//	public static final String C_USER = "user_name";
//	public static final String C_TEXT = "status_text";

//	Context context;
//	DBHelper dbHelper;
//	SQLiteDatabase db;

/*	public StatusData(Context context) {
		this.context = context;
		dbHelper = new DBHelper(context);
	}
*/
/*	public void insert(Status status) {
		db = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(C_ID, status.id);
		values.put(C_CREATED_AT, status.createdAt.getTime());
		values.put(C_USER, status.user.name);
		values.put(C_TEXT, status.text);

		db.insertWithOnConflict(TABLE, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);
		
		context.getContentResolver().insert(StatusProvider.CONTENT_URI, statusToValues(status));
	}
*/
/*	public Cursor query() {
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE, null, null, null, null, null,
				C_CREATED_AT + " DESC");

		return context.getContentResolver().query(StatusProvider.CONTENT_URI, null, null, null, C_CREATED_AT + " DESC");
	}
*/	
/*	public static ContentValues statusToValues(Status status) {
		ContentValues values = new ContentValues();
		values.put(C_ID, status.id);
		values.put(C_CREATED_AT, status.createdAt.getTime());
		values.put(C_USER, status.user.name);
		values.put(C_TEXT, status.text);
		
		return values;
	}
*/
}
