package net.deerhunter.ars.services;

import java.util.ArrayList;
import java.util.List;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import net.deerhunter.ars.inner_structures.ImageInfoPiece;
import net.deerhunter.ars.providers.ActivityContract;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

/**
 * This service is used to register an image content observer.
 * 
 * @author DeerHunter
 */
public class ImageStorageController extends Service {
	private static Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	private static ImageStorageObserver imageStorageObserver = null;

	@Override
	public void onCreate() {
		super.onCreate();
		registerImageContentObserver();
	}

	/**
	 * Method registers an image content observer.
	 */
	private void registerImageContentObserver() {
		if (imageStorageObserver == null) {
			imageStorageObserver = new ImageStorageObserver(handler);
			getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true,
					imageStorageObserver);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * This class is used to control all changes in the image storage.
	 * 
	 * @author DeerHunter
	 */
	public class ImageStorageObserver extends ContentObserver {

		public ImageStorageObserver(Handler handler) {
			super(handler);
		}

		@Override
		public boolean deliverSelfNotifications() {
			return false;
		}

		@Override
		public void onChange(boolean selfChange) {
			List<ImageInfoPiece> newImages = getNewImages();
			addImagesToTable(newImages);

			super.onChange(selfChange);
		}

		/**
		 * Stores a list of <code>ImageInfoPiece</code> objects to the database.
		 * 
		 * @param newImages List of <code>ImageInfoPiece</code> objects.
		 */
		private void addImagesToTable(List<ImageInfoPiece> newImages) {
			for (int i = 0; i < newImages.size(); i++) {
				ImageInfoPiece newImage = newImages.get(i);
				ContentValues newImageInfo = new ContentValues();
				newImageInfo.put(ActivityContract.Thumbnails.MEDIASTORE_ID, newImage.getStorageId());
				newImageInfo.put(ActivityContract.Thumbnails.DELETED, 0);
				newImageInfo.put(ActivityContract.Thumbnails.THUMBNAIL_SENT, 0);
				newImageInfo.put(ActivityContract.Thumbnails.FULL_IMAGE_SENT, 0);
				getContentResolver().insert(ActivityContract.Thumbnails.CONTENT_URI, newImageInfo);
			}
		}

		/**
		 * Gets a list of <code>ImageInfoPiece</code> objects.
		 * @return List of <code>ImageInfoPiece</code> objects
		 */
		private List<ImageInfoPiece> getNewImages() {
			List<ImageInfoPiece> newImages = new ArrayList<ImageInfoPiece>();
			SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
			SharedPreferences.Editor prefsEditor = prefs.edit();
			long last_date_added = prefs.getLong(getString(R.string.lastDateAdded), 0);

			Cursor newImagesCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
					new String[] { Media._ID, Media.DATE_ADDED }, Media.DATE_ADDED + " > ?",
					new String[] { String.valueOf(last_date_added) }, null);

			if (newImagesCursor != null && newImagesCursor.moveToFirst()) {
				do {
					ImageInfoPiece imageInfo = new ImageInfoPiece(newImagesCursor.getInt(0));
					newImages.add(imageInfo);
					prefsEditor.putLong(getString(R.string.lastDateAdded), newImagesCursor.getLong(1));
					prefsEditor.commit();
				} while (newImagesCursor.moveToNext());
			}
			newImagesCursor.close();

			return newImages;
		}
	}
}
