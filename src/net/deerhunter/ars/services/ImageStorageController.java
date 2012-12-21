package net.deerhunter.ars.services;

import java.util.ArrayList;
import java.util.List;

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

	private ImageStorageObserver imageStorageObserver = null;

	public void onCreate() {
		super.onCreate();
		imageStorageObserver = new ImageStorageObserver(handler);
		getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true,
				imageStorageObserver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (imageStorageObserver != null) {
			this.getContentResolver().unregisterContentObserver(imageStorageObserver);
		}
		super.onDestroy();
	}

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

		private List<ImageInfoPiece> getNewImages() {
			List<ImageInfoPiece> newImages = new ArrayList<ImageInfoPiece>();
			SharedPreferences prefs = getSharedPreferences(getApplication().getPackageName(), MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = prefs.edit();
			long last_date_added = prefs.getLong("lastDateAdded", 0);

			Cursor newImagesCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
					new String[] { Media._ID, Media.DATE_ADDED }, Media.DATE_ADDED + " > ?",
					new String[] { String.valueOf(last_date_added) }, null);

			if (newImagesCursor.moveToFirst()) {
				do {
					ImageInfoPiece imageInfo = new ImageInfoPiece(newImagesCursor.getInt(0));
					newImages.add(imageInfo);
					prefEditor.putLong("lastDateAdded", newImagesCursor.getLong(1));
					prefEditor.commit();
				} while (newImagesCursor.moveToNext());
			}

			return newImages;
		}
	}
}
