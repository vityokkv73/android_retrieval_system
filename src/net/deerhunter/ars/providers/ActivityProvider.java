package net.deerhunter.ars.providers;

import static net.deerhunter.ars.providers.ActivityContract.AUTHORITY;
import net.deerhunter.ars.providers.ActivityContract.Calls;
import net.deerhunter.ars.providers.ActivityContract.Locations;
import net.deerhunter.ars.providers.ActivityContract.Thumbnails;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * This class provides all the information and methods to simplify the access to the
 * Activity database.
 * 
 * @author DeerHunter
 */
public class ActivityProvider extends ContentProvider {

	public static final String TAG = "ActivityProvider";

	private static final String DATABASE_NAME = "activities.db";
	private static final int DATABASE_VERSION = 6;
	private static final String SMS_TABLE_NAME = "sms";
	private static final String CALLS_TABLE_NAME = "calls";
	private static final String THUMBNAILS_IMAGE_TABLE_NAME = "image_thumbnails";
	private static final String LOCATION_TABLE_NAME = "locations";

	private static final int SMS = 1;
	private static final int SMS_ID = 2;
	private static final int CALLS = 3;
	private static final int CALLS_ID = 4;
	private static final int THUMBNAILS = 5;
	private static final int THUMBNAILS_ID = 6;
	private static final int LOCATIONS = 7;
	private static final int LOCATIONS_ID = 8;

	private static final UriMatcher sUriMatcher;

	/**
	 * This class helps to open, create, and upgrade the database file.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// create SMS table
			db.execSQL("CREATE TABLE " + SMS_TABLE_NAME + " (" + ActivityContract.SMS._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + ActivityContract.SMS.SENDER + " TEXT,"
					+ ActivityContract.SMS.RECIPIENT + " TEXT," + ActivityContract.SMS.SENDER_PHONE_NUMBER + " TEXT,"
					+ ActivityContract.SMS.RECIPIENT_PHONE_NUMBER + " TEXT," + ActivityContract.SMS.TIME + " INTEGER,"
					+ ActivityContract.SMS.SMS_BODY + " TEXT" + ");");

			// create calls table
			db.execSQL("CREATE TABLE " + CALLS_TABLE_NAME + " (" + Calls._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Calls.CALLER + " TEXT," + Calls.RECIPIENT + " TEXT," + Calls.CALLER_PHONE_NUMBER + " TEXT,"
					+ Calls.RECIPIENT_PHONE_NUMBER + " TEXT," + Calls.TIME + " INTEGER" + ");");

			// create thumbnails table
			db.execSQL("CREATE TABLE " + THUMBNAILS_IMAGE_TABLE_NAME + " (" + Thumbnails._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Thumbnails.MEDIASTORE_ID + " INTEGER,"
					+ Thumbnails.DELETED + " BOOLEAN," + Thumbnails.THUMBNAIL_SENT + " BOOLEAN,"
					+ Thumbnails.FULL_IMAGE_SENT + " BOOLEAN" + ");");

			// create locations table
			db.execSQL("CREATE TABLE " + LOCATION_TABLE_NAME + " (" + Locations._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Locations.LATITUDE + " REAL," + Locations.LONGITUDE
					+ " REAL," + Locations.ALTITUDE + " REAL," + Locations.ACCURACY + " REAL," + Locations.PROVIDER
					+ " TEXT," + Locations.TIME + " INTEGER" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + SMS_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + CALLS_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + THUMBNAILS_IMAGE_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);
			onCreate(db);
		}
	}

	private DatabaseHelper mOpenHelper;

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
			case SMS:
				qb.setTables(SMS_TABLE_NAME);
				break;

			case SMS_ID:
				qb.setTables(SMS_TABLE_NAME);
				qb.appendWhere(ActivityContract.SMS._ID + "=" + uri.getPathSegments().get(1));
				break;

			case CALLS:
				qb.setTables(CALLS_TABLE_NAME);
				break;

			case CALLS_ID:
				qb.setTables(CALLS_TABLE_NAME);
				qb.appendWhere(Calls._ID + "=" + uri.getPathSegments().get(1));
				break;

			case THUMBNAILS:
				qb.setTables(THUMBNAILS_IMAGE_TABLE_NAME);
				break;

			case THUMBNAILS_ID:
				qb.setTables(THUMBNAILS_IMAGE_TABLE_NAME);
				qb.appendWhere(Thumbnails._ID + "=" + uri.getPathSegments().get(1));
				break;

			case LOCATIONS:
				qb.setTables(LOCATION_TABLE_NAME);
				break;

			case LOCATIONS_ID:
				qb.setTables(LOCATION_TABLE_NAME);
				qb.appendWhere(Locations._ID + "=" + uri.getPathSegments().get(1));
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = "_id ASC";
		} else {
			orderBy = sortOrder;
		}

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

		// Tell the cursor what uri to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
			case SMS:
				return ActivityContract.SMS.CONTENT_TYPE;

			case SMS_ID:
				return ActivityContract.SMS.CONTENT_ITEM_TYPE;

			case CALLS:
				return Calls.CONTENT_TYPE;

			case CALLS_ID:
				return Calls.CONTENT_ITEM_TYPE;

			case THUMBNAILS:
				return Thumbnails.CONTENT_TYPE;

			case THUMBNAILS_ID:
				return Thumbnails.CONTENT_ITEM_TYPE;

			case LOCATIONS:
				return Locations.CONTENT_TYPE;

			case LOCATIONS_ID:
				return Locations.CONTENT_ITEM_TYPE;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		switch (sUriMatcher.match(uri)) {
			case SMS:
				return insertSMS(uri, initialValues);
			case CALLS:
				return insertCall(uri, initialValues);
			case THUMBNAILS:
				return insertImage(uri, initialValues);
			case LOCATIONS:
				return insertLocation(uri, initialValues);
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private Uri insertLocation(Uri uri, ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// Make sure that the fields are all set
		if (!values.containsKey(Locations.LATITUDE))
			values.put(Locations.LATITUDE, -100000.0);

		if (!values.containsKey(Locations.LONGITUDE))
			values.put(Locations.LONGITUDE, -100000.0);

		if (!values.containsKey(Locations.ALTITUDE))
			values.put(Locations.ALTITUDE, -100000.0);

		if (!values.containsKey(Locations.ACCURACY))
			values.put(Locations.ACCURACY, -1.0);

		if (!values.containsKey(Locations.PROVIDER))
			values.put(Locations.PROVIDER, "Unknown");

		if (!values.containsKey(Locations.TIME))
			values.put(Locations.TIME, System.currentTimeMillis());

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(LOCATION_TABLE_NAME, null, values);
		if (rowId > 0) {
			Uri locationUri = ContentUris.withAppendedId(Locations.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(locationUri, null);
			return locationUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	private Uri insertCall(Uri uri, ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// Make sure that the fields are all set
		if (!values.containsKey(Calls.CALLER))
			values.put(Calls.CALLER, "");

		if (!values.containsKey(Calls.RECIPIENT))
			values.put(Calls.RECIPIENT, "");

		if (!values.containsKey(Calls.CALLER_PHONE_NUMBER))
			values.put(Calls.CALLER_PHONE_NUMBER, "");

		if (!values.containsKey(Calls.RECIPIENT_PHONE_NUMBER))
			values.put(Calls.RECIPIENT_PHONE_NUMBER, "");

		if (!values.containsKey(Calls.TIME))
			values.put(Calls.TIME, System.currentTimeMillis());

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(CALLS_TABLE_NAME, null, values);
		if (rowId > 0) {
			Uri callUri = ContentUris.withAppendedId(Calls.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(callUri, null);
			return callUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	private Uri insertSMS(Uri uri, ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// Make sure that the fields are all set
		if (!values.containsKey(ActivityContract.SMS.SENDER))
			values.put(ActivityContract.SMS.SENDER, "");

		if (!values.containsKey(ActivityContract.SMS.RECIPIENT))
			values.put(ActivityContract.SMS.RECIPIENT, "");

		if (!values.containsKey(ActivityContract.SMS.SENDER_PHONE_NUMBER))
			values.put(ActivityContract.SMS.SENDER_PHONE_NUMBER, "");

		if (!values.containsKey(ActivityContract.SMS.RECIPIENT_PHONE_NUMBER))
			values.put(ActivityContract.SMS.RECIPIENT_PHONE_NUMBER, "");

		if (!values.containsKey(ActivityContract.SMS.TIME))
			values.put(ActivityContract.SMS.TIME, System.currentTimeMillis());

		if (!values.containsKey(ActivityContract.SMS.SMS_BODY))
			values.put(ActivityContract.SMS.SMS_BODY, "");

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(SMS_TABLE_NAME, null, values);
		if (rowId > 0) {
			Uri smsUri = ContentUris.withAppendedId(ActivityContract.SMS.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(smsUri, null);
			return smsUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	private Uri insertImage(Uri uri, ContentValues initialValues) {
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// Make sure that the fields are all set
		if (!values.containsKey(ActivityContract.Thumbnails.MEDIASTORE_ID))
			values.put(ActivityContract.Thumbnails.MEDIASTORE_ID, -1);

		if (!values.containsKey(ActivityContract.Thumbnails.DELETED))
			values.put(ActivityContract.Thumbnails.DELETED, 0);

		if (!values.containsKey(ActivityContract.Thumbnails.THUMBNAIL_SENT))
			values.put(ActivityContract.Thumbnails.THUMBNAIL_SENT, 0);

		if (!values.containsKey(ActivityContract.Thumbnails.FULL_IMAGE_SENT))
			values.put(ActivityContract.Thumbnails.FULL_IMAGE_SENT, 0);

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(THUMBNAILS_IMAGE_TABLE_NAME, null, values);
		if (rowId > 0) {
			Uri imageUri = ContentUris.withAppendedId(ActivityContract.Thumbnails.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(imageUri, null);
			return imageUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
			case SMS:
				count = db.delete(SMS_TABLE_NAME, where, whereArgs);
				break;

			case SMS_ID:
				String smsId = uri.getPathSegments().get(1);
				count = db.delete(SMS_TABLE_NAME, ActivityContract.SMS._ID + "=" + smsId
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
				break;

			case CALLS:
				count = db.delete(CALLS_TABLE_NAME, where, whereArgs);
				break;

			case CALLS_ID:
				String callId = uri.getPathSegments().get(1);
				count = db.delete(CALLS_TABLE_NAME, Calls._ID + "=" + callId
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
				break;

			case THUMBNAILS:
				count = db.delete(THUMBNAILS_IMAGE_TABLE_NAME, where, whereArgs);
				break;

			case THUMBNAILS_ID:
				String imageId = uri.getPathSegments().get(1);
				count = db.delete(THUMBNAILS_IMAGE_TABLE_NAME,
						Thumbnails._ID + "=" + imageId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""),
						whereArgs);
				break;

			case LOCATIONS:
				count = db.delete(LOCATION_TABLE_NAME, where, whereArgs);
				break;

			case LOCATIONS_ID:
				String locationId = uri.getPathSegments().get(1);
				count = db.delete(LOCATION_TABLE_NAME, Locations._ID + "=" + locationId
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
			case SMS:
				count = db.update(SMS_TABLE_NAME, values, where, whereArgs);
				break;

			case SMS_ID:
				String smsId = uri.getPathSegments().get(1);
				count = db.update(SMS_TABLE_NAME, values,
						ActivityContract.SMS._ID + "=" + smsId
								+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
				break;

			case CALLS:
				count = db.update(CALLS_TABLE_NAME, values, where, whereArgs);
				break;

			case CALLS_ID:
				String callId = uri.getPathSegments().get(1);
				count = db.update(CALLS_TABLE_NAME, values, Calls._ID + "=" + callId
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
				break;

			case THUMBNAILS:
				count = db.update(THUMBNAILS_IMAGE_TABLE_NAME, values, where, whereArgs);
				break;

			case THUMBNAILS_ID:
				String imageId = uri.getPathSegments().get(1);
				count = db.update(THUMBNAILS_IMAGE_TABLE_NAME, values,
						Thumbnails._ID + "=" + imageId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""),
						whereArgs);
				break;

			case LOCATIONS:
				count = db.update(LOCATION_TABLE_NAME, values, where, whereArgs);
				break;

			case LOCATIONS_ID:
				String locationId = uri.getPathSegments().get(1);
				count = db.update(LOCATION_TABLE_NAME, values,
						Locations._ID + "=" + locationId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""),
						whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, SMS_TABLE_NAME, SMS);
		sUriMatcher.addURI(AUTHORITY, SMS_TABLE_NAME + "/#", SMS_ID);
		sUriMatcher.addURI(AUTHORITY, CALLS_TABLE_NAME, CALLS);
		sUriMatcher.addURI(AUTHORITY, CALLS_TABLE_NAME + "/#", CALLS_ID);
		sUriMatcher.addURI(AUTHORITY, THUMBNAILS_IMAGE_TABLE_NAME, THUMBNAILS);
		sUriMatcher.addURI(AUTHORITY, THUMBNAILS_IMAGE_TABLE_NAME + "/#", THUMBNAILS_ID);
		sUriMatcher.addURI(AUTHORITY, LOCATION_TABLE_NAME, LOCATIONS);
		sUriMatcher.addURI(AUTHORITY, LOCATION_TABLE_NAME + "/#", LOCATIONS_ID);
	}
}
