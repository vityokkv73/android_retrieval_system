package net.deerhunter.ars.contact_structs;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import android.content.Context;

/**
 * This class represents an IM address of a contact.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class IM {
	private String name;
	private int type;

	/**
	 * Returns the name of the IM address.
	 * @return Name of the IM address
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the IM address.
	 * @param name Name of the IM address.
	 */
	public void setName(String name) {
		this.name = name;
		if (this.name == null)
			this.name = "";
	}

	/**
	 * Returns a type of the IM address. See {@link net.deerhunter.ars.contact_structs.IM.Type} for more information.
	 * @return Type of the IM address
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets a type of the IM address. See {@link net.deerhunter.ars.contact_structs.IM.Type} for more information.
	 * @param type Type of the IM address
	 */
	public void setType(int type) {
		this.type = type;
		if (this.type < 0 || this.type > 3)
			this.type = 0;
	}

	/**
	 * Creates an instance of the IM class.
	 * @param name Name of the IM address
	 * @param type Type of the IM address. See {@link net.deerhunter.ars.contact_structs.IM.Type} for more information.
	 */
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

	/**
	 * Class that consists of IM address type constants.
	 * Defined constants:
	 * <ul>
	 * <li>{@link #HOME}</li>
	 * <li>{@link #WORK}</li>
	 * <li>{@link #OTHER}</li>
	 * <li>{@link #CUSTOM}</li>
	 * </ul>
	 * 
	 * @author DeerHunter (vityokkv73@gmail.com)
	 */
	public class Type {		
		/**
		 * Home IM address
		 */
		public static final int HOME = 1;
		/**
		 * Work IM address
		 */
		public static final int WORK = 2;
		/**
		 * Other IM address
		 */
		public static final int OTHER = 3;
		/**
		 * User-defined type of IM address
		 */
		public static final int CUSTOM = 0;
	}
}
