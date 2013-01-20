package net.deerhunter.ars.protocol.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.deerhunter.ars.contact_structs.Address;
import net.deerhunter.ars.contact_structs.Email;
import net.deerhunter.ars.contact_structs.IM;
import net.deerhunter.ars.contact_structs.Organization;
import net.deerhunter.ars.contact_structs.Phone;

/**
 * This class contains all the information and methods needed to send the
 * information about the contacts to the server.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class ContactPacket extends BasePacket {
	private int id;
	private String displayName;
	private List<Phone> phones = new ArrayList<Phone>();
	private List<Email> emails = new ArrayList<Email>();
	private List<String> notes = new ArrayList<String>();
	private List<Address> addresses = new ArrayList<Address>();
	private List<IM> imAddresses = new ArrayList<IM>();
	private Organization organization;

	/**
	 * Returns the ID of the contact.
	 * 
	 * @return ID of the contact
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets ID of the contact.
	 * 
	 * @param id ID of the contact
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns name of the contact.
	 * 
	 * @return Name of the contact
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the name of the contact.
	 * 
	 * @param displayName Name of the contact
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		if (this.displayName == null)
			this.displayName = "";
	}

	/**
	 * Return a list of contact phone numbers.
	 * 
	 * @return List of contact phone numbers
	 */
	public List<Phone> getPhones() {
		return phones;
	}

	/**
	 * Sets a list of contact phone numbers.
	 * 
	 * @param phones List of contact phone numbers.
	 */
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
		if (this.phones == null)
			this.phones = new ArrayList<Phone>();
	}

	/**
	 * Adds the new phone number to the list of phone numbers.
	 * 
	 * @param phone Phone number.
	 */
	public void addPhone(Phone phone) {
		this.phones.add(phone);
	}

	/**
	 * Return a list of email addresses.
	 * 
	 * @return List of email addresses
	 */
	public List<Email> getEmail() {
		return emails;
	}

	/**
	 * Sets the list of email addresses.
	 * 
	 * @param emails List of email addresses
	 */
	public void setEmails(List<Email> emails) {
		this.emails = emails;
		if (this.emails == null)
			this.emails = new ArrayList<Email>();
	}

	/**
	 * Adds new email address to the list of email addresses.
	 * 
	 * @param email New email address
	 */
	public void addEmail(Email email) {
		this.emails.add(email);
	}

	/**
	 * Return a list of notes.
	 * 
	 * @return List of notes
	 */
	public List<String> getNotes() {
		return notes;
	}

	/**
	 * Sets the list of notes.
	 * 
	 * @param notes List of notes
	 */
	public void setNotes(List<String> notes) {
		this.notes = notes;
		if (this.notes == null)
			this.notes = new ArrayList<String>();
	}

	/**
	 * Adds a new note to the list of notes
	 * 
	 * @param note Note
	 */
	public void addNote(String note) {
		this.notes.add(note);
	}

	/**
	 * Return a list of addresses.
	 * 
	 * @return List of addresses
	 */
	public List<Address> getAddresses() {
		return addresses;
	}

	/**
	 * Sets the list of addresses.
	 * 
	 * @param addresses List of addresses
	 */
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
		if (this.addresses == null)
			this.addresses = new ArrayList<Address>();
	}

	/**
	 * Adds new address to the list of addresses.
	 * 
	 * @param address Address
	 */
	public void addAddress(Address address) {
		this.addresses.add(address);
	}

	/**
	 * Return a list of IM addresses.
	 * 
	 * @return List of IM addresses
	 */
	public List<IM> getImAddresses() {
		return imAddresses;
	}

	/**
	 * Sets the list of IM addresses.
	 * 
	 * @param addresses List of IM addresses
	 */
	public void setImAddresses(List<IM> imAddresses) {
		this.imAddresses = imAddresses;
		if (this.imAddresses == null)
			this.imAddresses = new ArrayList<IM>();
	}

	/**
	 * Adds new IM address to the list of addresses.
	 * 
	 * @param imAddress IM address
	 */
	public void addImAddress(IM imAddress) {
		this.imAddresses.add(imAddress);
	}

	/**
	 * Returns an organization.
	 * 
	 * @return Organization
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * Sets an organization
	 * 
	 * @param organization Organization
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
		if (this.organization == null)
			this.organization = new Organization();
	}

	/**
	 * Method returns a binary representation of a ContactPacket.
	 * 
	 * @return Binary representation of a ContactPacket
	 */
	public byte[] generateBinaryPacket() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(500);
		try {
			Charset utf8charset = Charset.forName("UTF-8");
			writeIntToArray(outputStream, id);
			formatWriteStringToArray(outputStream, displayName, utf8charset);
			formatWriteStringToArray(outputStream, getPhonesString(), utf8charset);
			formatWriteStringToArray(outputStream, getEmailsString(), utf8charset);
			formatWriteStringToArray(outputStream, getNotesString(), utf8charset);
			formatWriteStringToArray(outputStream, getAddressesString(), utf8charset);
			formatWriteStringToArray(outputStream, getIMAddressesString(), utf8charset);
			formatWriteStringToArray(outputStream, organization.toString(), utf8charset);
		} catch (IOException e) {}

		return outputStream.toByteArray();
	}

	/**
	 * Returns a string representation of the list of the phones.
	 * 
	 * @return String representation of the list of the phones
	 */
	private String getPhonesString() {
		StringBuilder builder = new StringBuilder(100);
		for (Phone phone : phones) {
			builder.append(phone);
			builder.append(", ");
		}
		int length = builder.length();
		if (length == 0)
			return "";
		if (builder.charAt(length - 2) == ',')
			builder.delete(length - 2, length);
		return builder.toString();
	}

	/**
	 * Returns a string representation of the list of the email addresses.
	 * 
	 * @return String representation of the list of the email addresses
	 */
	private String getEmailsString() {
		StringBuilder builder = new StringBuilder(100);
		for (Email email : emails) {
			builder.append(email);
			builder.append(", ");
		}
		int length = builder.length();
		if (length == 0)
			return "";
		if (builder.charAt(length - 2) == ',')
			builder.delete(length - 2, length);
		return builder.toString();
	}

	/**
	 * Returns a string representation of the list of the notes.
	 * 
	 * @return String representation of the list of the notes
	 */
	private String getNotesString() {
		StringBuilder builder = new StringBuilder(100);
		for (String note : notes) {
			builder.append(note);
			builder.append(", ");
		}
		int length = builder.length();
		if (length == 0)
			return "";
		if (builder.charAt(length - 2) == ',')
			builder.delete(length - 2, length);
		return builder.toString();
	}

	/**
	 * Returns a string representation of the list of the addresses.
	 * 
	 * @return String representation of the list of the addresses
	 */
	private String getAddressesString() {
		StringBuilder builder = new StringBuilder(100);
		for (Address address : addresses) {
			builder.append(address);
			builder.append(", ");
		}
		int length = builder.length();
		if (length == 0)
			return "";
		if (builder.charAt(length - 2) == ',')
			builder.delete(length - 2, length);
		return builder.toString();
	}

	/**
	 * Returns a string representation of the list of the IM addresses.
	 * 
	 * @return String representation of the list of the IM addresses
	 */
	private String getIMAddressesString() {
		StringBuilder builder = new StringBuilder(100);
		for (IM im : imAddresses) {
			builder.append(im);
			builder.append(", ");
		}
		int length = builder.length();
		if (length == 0)
			return "";
		if (builder.charAt(length - 2) == ',')
			builder.delete(length - 2, length);
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(500);
		builder.append("ContactPacket { ");
		builder.append("id = " + id);
		builder.append(", displayName = " + displayName);

		builder.append(", Phones { ");
		builder.append(getPhonesString());
		builder.append(" }");

		builder.append(", Emails { ");
		builder.append(getEmailsString());
		builder.append(" }");

		builder.append(", Notes { ");
		builder.append(getNotesString());
		builder.append(" }");

		builder.append(", Addresses { ");
		builder.append(getAddressesString());
		builder.append(" }");

		builder.append(", IMAddresses { ");
		builder.append(getIMAddressesString());
		builder.append(" }");

		builder.append(", Organization { ");
		builder.append(organization);
		builder.append(" }");

		builder.append(" }");
		return builder.toString();
	}
}
