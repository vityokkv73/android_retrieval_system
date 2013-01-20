package net.deerhunter.ars.contact_structs;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import android.content.Context;

/**
 * This class represents an email address of a contact.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class Email {
	private String address;
	private int type;

	/**
	 * Returns an address of the email.
	 * @return Address of the email
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address of the email.
	 * @param address Address of the email
	 */
	public void setAddress(String address) {
		this.address = address;
		if (this.address == null)
			this.address = "";
	}

	/**
	 * Returns a type of the email. See {@link net.deerhunter.ars.contact_structs.Email.Type} for more information.
	 * @return Type of the email
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets a type of the email. See {@link net.deerhunter.ars.contact_structs.Email.Type} for more information.
	 * @param type Type of the email
	 */
	public void setType(int type) {
		this.type = type;
		if (this.type < 0 || this.type > 4)
			this.type = 0;
	}

	/**
	 * Creates an instance of the Email class.
	 * @param address Address of the email
	 * @param type Type of the email. See {@link net.deerhunter.ars.contact_structs.Email.Type} for more information.
	 */
	public Email(String address, int type) {
		setAddress(address);
		setType(type);
	}
	
	@Override
	public String toString() {
		if (address.isEmpty())
			return "";
		Context context = ArsApplication.getInstance().getApplicationContext();
		StringBuilder builder = new StringBuilder(address);
		String[] types = context.getResources().getStringArray(R.array.emails_type);
		builder.append(", ");
		builder.append(types[type]);
		return builder.toString();
	}

	/**
	 * Class that consists of email type constants.
	 * Defined constants:
	 * <ul>
	 * <li>{@link #HOME}</li>
	 * <li>{@link #WORK}</li>
	 * <li>{@link #OTHER}</li>
	 * <li>{@link #MOBILE}</li>
	 * <li>{@link #CUSTOM}</li>
	 * </ul>
	 * 
	 * @author DeerHunter (vityokkv73@gmail.com)
	 */
	public class Type {
		/**
		 * Home email address
		 */
		public static final int HOME = 1;
		/**
		 * Work email address
		 */
		public static final int WORK = 2;
		/**
		 * Other email address
		 */
		public static final int OTHER = 3;
		/**
		 * Mobile email address
		 */
		public static final int MOBILE = 4;
		/**
		 * User-defined type of email address
		 */
		public static final int CUSTOM = 0;
	}
}
