package net.deerhunter.ars.contact_structs;

import android.content.Context;
import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;

public class Phone {
	private String phoneNumber;
	private int type;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		if (this.phoneNumber == null)
			this.phoneNumber = "";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		if (this.type < 0 || this.type > 20)
			this.type = 0;
	}

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
