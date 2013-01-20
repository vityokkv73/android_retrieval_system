/*
 * This file is part of Android retrieval system project.
 * 
 * Android retrieval system is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. 
 * 
 * Android retrieval system is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Android retrieval system. If not, see <http://www.gnu.org/licenses/>.
 */

package net.deerhunter.ars.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Class contains methods that simplify the access to contacts information.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class ContactHelper {
	private ContactHelper() {
	}

	/**
	 * Methods returns a name for the contact with the given phone number. If no
	 * such phone number in the contacts, the method returns a phone number.
	 * 
	 * @param context Context of the application component
	 * @param phoneNumber Phone number of the contact
	 * @return Name of the contact or the phone number if no such contact in
	 *         phone contacts.
	 */
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
			if (contactLookup != null)
				contactLookup.close();
		}

		return name;
	}
}
