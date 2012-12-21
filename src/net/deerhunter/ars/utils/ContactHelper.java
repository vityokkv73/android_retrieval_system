package net.deerhunter.ars.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

public class ContactHelper {
	private ContactHelper() {
	}

	public static String getContactDisplayNameByNumber(Context context, String phoneNumber) {
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		String name = phoneNumber;

		ContentResolver contentResolver = context.getContentResolver();
		Cursor contactLookup = contentResolver.query(uri, new String[] { BaseColumns._ID,
				ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

		try {
			if (contactLookup != null && contactLookup.getCount() > 0) {
				contactLookup.moveToNext();
				name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
			}
		} finally {
			if (contactLookup != null) {
				contactLookup.close();
			}
		}

		return name;
	}
}
