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

import android.content.Context;
import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;

/**
 *  This class represents a phone of a contact.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class Phone {
	private String phoneNumber;
	private int type;

	/**
	 * Returns the phone number.
	 * @return Phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the phone number.
	 * @param phoneNumber Phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		if (this.phoneNumber == null)
			this.phoneNumber = "";
	}

	/**
	 * Return the type of the phone. See {@link net.deerhunter.ars.contact_structs.Phone.Type} for more information.
	 * @return Type of the phone. 
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type of the phone.
	 * @param type Type of the phone. See {@link net.deerhunter.ars.contact_structs.Phone.Type} for more information.
	 */
	public void setType(int type) {
		this.type = type;
		if (this.type < 0 || this.type > 20)
			this.type = 0;
	}

	/**
	 * Creates an instance of the Phone class.
	 * @param phoneNumber Phone number
	 * @param type Type of the phone. See {@link net.deerhunter.ars.contact_structs.Phone.Type} for more information.
	 */
	public Phone(String phoneNumber, int type) {
		setPhoneNumber(phoneNumber);
		setType(type);
	}

	@Override
	public String toString() {
		if (phoneNumber.isEmpty())
			return "";
		Context context = ArsApplication.getInstance().getApplicationContext();
		StringBuilder builder = new StringBuilder(phoneNumber);
		String[] types = context.getResources().getStringArray(R.array.phones_type);
		builder.append(", ");
		builder.append(types[type]);
		return builder.toString();
	}

	/**
	 * Class that consists of phone type constants.
	 * Defined constants:
	 * <ul>
	 * <li>{@link #HOME}</li>
	 * <li>{@link #MOBILE}</li>
	 * <li>{@link #WORK}</li>
	 * <li>{@link #FAX_WORK}</li>
	 * <li>{@link #FAX_HOME}</li>
	 * <li>{@link #PAGER}</li>
	 * <li>{@link #OTHER}</li>
	 * <li>{@link #CALLBACK}</li>
	 * <li>{@link #CAR}</li>
	 * <li>{@link #COMPANY_MAIN}</li>
	 * <li>{@link #ISDN}</li>
	 * <li>{@link #MAIN}</li>
	 * <li>{@link #OTHER_FAX}</li>
	 * <li>{@link #RADIO}</li>
	 * <li>{@link #TELEX}</li>
	 * <li>{@link #TTY_TDD}</li>
	 * <li>{@link #WORK_MOBILE}</li>
	 * <li>{@link #WORK_PAGER}</li>
	 * <li>{@link #ASSISTANT}</li>
	 * <li>{@link #MMS}</li>
	 * <li>{@link #CUSTOM}</li> 
	 * </ul>
	 * 
	 * @author DeerHunter (vityokkv73@gmail.com)
	 */
	public class Type {
		public static final int ASSISTANT = 19;
		public static final int CALLBACK = 8;
		public static final int CAR = 9;
		public static final int COMPANY_MAIN = 10;
		public static final int FAX_HOME = 5;
		public static final int FAX_WORK = 4;
		public static final int HOME = 1;
		public static final int ISDN = 11;
		public static final int MAIN = 12;
		public static final int MMS = 20;
		public static final int MOBILE = 2;
		public static final int OTHER = 7;
		public static final int OTHER_FAX = 13;
		public static final int PAGER = 6;
		public static final int RADIO = 14;
		public static final int TELEX = 15;
		public static final int TTY_TDD = 16;
		public static final int WORK = 3;
		public static final int WORK_MOBILE = 17;
		public static final int WORK_PAGER = 18;
		public static final int CUSTOM = 0;
	}
}
