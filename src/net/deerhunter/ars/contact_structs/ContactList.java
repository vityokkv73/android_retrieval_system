package net.deerhunter.ars.contact_structs;

import java.util.ArrayList;
import java.util.List;

import net.deerhunter.ars.protocol.packets.ContactPacket;

/**
 * Class that is used as wrapper on the list of contacts.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class ContactList {
	private List<ContactPacket> contacts = new ArrayList<ContactPacket>();

	/**
	 * Returns a list of contacts.
	 * @return
	 */
	public List<ContactPacket> getContacts() {
		return contacts;
	}

	/**
	 * Sets a list of the contacts.
	 * @param contacts List of the contacts
	 */
	public void setContacts(List<ContactPacket> contacts) {
		this.contacts = contacts;
		if (this.contacts == null)
			contacts = new ArrayList<ContactPacket>();
	}

	/**
	 * Adds a contact to the list of contacts.
	 * @param contact Contact
	 */
	public void addContact(ContactPacket contact) {
		this.contacts.add(contact);
	}
}
