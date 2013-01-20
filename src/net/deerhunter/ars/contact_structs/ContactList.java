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
