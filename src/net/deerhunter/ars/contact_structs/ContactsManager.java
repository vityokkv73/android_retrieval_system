package net.deerhunter.ars.contact_structs;

import java.util.ArrayList;
import java.util.List;

import net.deerhunter.ars.application.ArsApplication;
import net.deerhunter.ars.protocol.packets.ContactPacket;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * This class helps to get all contacts information.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class ContactsManager {
	private static volatile ContactsManager instance;

	private ContactsManager() {
	}

	/**
	 * This method returns an instance of the ContactsManager class.
	 * 
	 * @return Instance of the ContactsManager class
	 */
	public static ContactsManager getInstance() {
		ContactsManager localInstance = instance;
		if (localInstance == null) {
			synchronized (ContactsManager.class) {
				localInstance = instance;
				if (localInstance == null) {
					localInstance = instance = new ContactsManager();
				}
			}
		}
		return localInstance;
	}

	public ContactList newContactList() {
		ContactList contacts = new ContactList();
		ContentResolver cr = ArsApplication.getInstance().getApplicationContext().getContentResolver();
		int id;

		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				ContactPacket c = new ContactPacket();
				id = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts._ID));
				c.setId(id);
				c.setDisplayName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
				if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					c.setPhones(this.getPhoneNumbers(id));
				}
				c.setEmails(this.getEmailAddresses(id));
				c.setNotes(this.getContactNotes(id));
				c.setAddresses(this.getContactAddresses(id));
				c.setImAddresses(this.getIM(id));
				c.setOrganization(this.getContactOrg(id));
				contacts.addContact(c);
			}
		}
		return contacts;
	}

	public List<Phone> getPhoneNumbers(int id) {
		List<Phone> phones = new ArrayList<Phone>();
		ContentResolver cr = ArsApplication.getInstance().getApplicationContext().getContentResolver();

		Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { String.valueOf(id) }, null);
		while (pCur.moveToNext()) {
			phones.add(new Phone(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
					pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))));

		}
		pCur.close();
		return phones;
	}

	public List<Email> getEmailAddresses(int id) {
		List<Email> emails = new ArrayList<Email>();
		ContentResolver cr = ArsApplication.getInstance().getApplicationContext().getContentResolver();
		Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { String.valueOf(id) }, null);
		while (emailCur.moveToNext()) {
			// This would allow you get several email addresses
			Email e = new Email(
					emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)),
					emailCur.getInt(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
			emails.add(e);
		}
		emailCur.close();
		return emails;
	}

	public List<String> getContactNotes(int id) {
		List<String> notes = new ArrayList<String>();
		ContentResolver cr = ArsApplication.getInstance().getApplicationContext().getContentResolver();
		String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { String.valueOf(id),
				ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE };
		Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, where, whereParameters, null);
		if (noteCur.moveToFirst()) {
			String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
			if (note.length() > 0) {
				notes.add(note);
			}
		}
		noteCur.close();
		return notes;
	}

	public List<Address> getContactAddresses(int id) {
		List<Address> addrList = new ArrayList<Address>();
		ContentResolver cr = ArsApplication.getInstance().getApplicationContext().getContentResolver();
		String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { String.valueOf(id),
				ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };

		Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, null, where, whereParameters, null);
		while (addrCur.moveToNext()) {
			String poBox = addrCur.getString(addrCur
					.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
			String street = addrCur.getString(addrCur
					.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
			String city = addrCur.getString(addrCur
					.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
			String state = addrCur.getString(addrCur
					.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
			String postalCode = addrCur.getString(addrCur
					.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
			String country = addrCur.getString(addrCur
					.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
			int type = addrCur.getInt(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
			Address a = new Address(poBox, street, city, state, postalCode, country, type);
			addrList.add(a);
		}
		addrCur.close();
		return addrList;
	}

	public List<IM> getIM(int id) {
		List<IM> imList = new ArrayList<IM>();
		ContentResolver cr = ArsApplication.getInstance().getApplicationContext().getContentResolver();
		String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { String.valueOf(id),
				ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE };

		Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI, null, where, whereParameters, null);
		if (imCur.moveToFirst()) {
			String imName = imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
			int imType = imCur.getInt(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
			if (imName.length() > 0) {
				IM im = new IM(imName, imType);
				imList.add(im);
			}
		}
		imCur.close();
		return imList;
	}

	public Organization getContactOrg(int id) {
		Organization org = new Organization();
		ContentResolver cr = ArsApplication.getInstance().getApplicationContext().getContentResolver();
		String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
		String[] whereParameters = new String[] { String.valueOf(id),
				ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE };

		Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI, null, where, whereParameters, null);

		if (orgCur.moveToFirst()) {
			String orgName = orgCur
					.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
			String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
			if (orgName.length() > 0) {
				org.setOrganization(orgName);
				org.setTitle(title);
			}
		}
		orgCur.close();
		return org;
	}
}