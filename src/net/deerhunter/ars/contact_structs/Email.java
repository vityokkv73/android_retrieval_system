package net.deerhunter.ars.contact_structs;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import android.content.Context;

public class Email {
	private String address;
	private int type;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		if (this.address == null)
			this.address = "";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		if (this.type < 0 || this.type > 4)
			this.type = 0;
	}

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

	public class Type {
		public static final int HOME = 1;
		public static final int WORK = 2;
		public static final int OTHER = 3;
		public static final int MOBILE = 4;
		public static final int CUSTOM = 0;
	}
}
