package net.deerhunter.ars.contact_structs;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import android.content.Context;

public class IM {
	private String name;
	private int type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if (this.name == null)
			this.name = "";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		if (this.type < 0 || this.type > 3)
			this.type = 0;
	}

	public IM(String name, int type) {
		setName(name);
		setType(type);
	}

	@Override
	public String toString() {
		if (name.isEmpty())
			return "";
		Context context = ArsApplication.getInstance().getApplicationContext();
		StringBuilder builder = new StringBuilder(name);
		String[] types = context.getResources().getStringArray(R.array.ims_type);
		builder.append(", ");
		builder.append(types[type]);
		return builder.toString();
	}

	public class Type {
		public static final int HOME = 1;
		public static final int WORK = 2;
		public static final int OTHER = 3;
		public static final int CUSTOM = 0;
	}
}
