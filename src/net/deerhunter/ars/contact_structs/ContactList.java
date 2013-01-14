package net.deerhunter.ars.contact_structs;

import java.util.ArrayList;
import java.util.List;

import net.deerhunter.ars.protocol.packets.ContactPacket;

public class ContactList {
	private List<ContactPacket> contacts = new ArrayList<ContactPacket>();

	public List<ContactPacket> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactPacket> contacts) {
		this.contacts = contacts;
		if (this.contacts == null)
			contacts = new ArrayList<ContactPacket>();
	}

	public void addContact(ContactPacket contact) {
		this.contacts.add(contact);
	}
}
