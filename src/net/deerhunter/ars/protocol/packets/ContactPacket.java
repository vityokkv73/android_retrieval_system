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

public class ContactPacket extends BasePacket {
	private int id;
	private String displayName;
	private List<Phone> phones = new ArrayList<Phone>();
	private List<Email> emails = new ArrayList<Email>();
	private List<String> notes = new ArrayList<String>();
	private List<Address> addresses = new ArrayList<Address>();
	private List<IM> imAddresses = new ArrayList<IM>();
	private Organization organization;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		if (this.displayName == null)
			this.displayName = "";
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
		if (this.phones == null)
			this.phones = new ArrayList<Phone>();
	}

	public void addPhone(Phone phone) {
		this.phones.add(phone);
	}

	public List<Email> getEmail() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
		if (this.emails == null)
			this.emails = new ArrayList<Email>();
	}

	public void addEmail(Email email) {
		this.emails.add(email);
	}

	public List<String> getNotes() {
		return notes;
	}

	public void setNotes(List<String> notes) {
		this.notes = notes;
		if (this.notes == null)
			this.notes = new ArrayList<String>();
	}

	public void addNote(String note) {
		this.notes.add(note);
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
		if (this.addresses == null)
			this.addresses = new ArrayList<Address>();
	}

	public void addAddress(Address address) {
		this.addresses.add(address);
	}

	public List<IM> getImAddresses() {
		return imAddresses;
	}

	public void setImAddresses(List<IM> imAddresses) {
		this.imAddresses = imAddresses;
		if (this.imAddresses == null)
			this.imAddresses = new ArrayList<IM>();
	}

	public void addImAddress(IM imAddress) {
		this.imAddresses.add(imAddress);
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
		if (this.organization == null)
			this.organization = new Organization();
	}

	/**
	 * Method returns a binary representation of a ContactPacket.
	 * 
	 * @return Binary representation of a CallPacket
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
