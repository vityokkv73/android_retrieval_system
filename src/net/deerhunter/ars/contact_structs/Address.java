package net.deerhunter.ars.contact_structs;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import android.content.Context;

/**
 * This class represents an address of a contact.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class Address {
	private String poBox;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private int type;

	/**
	 * Returns the type of the address.
	 * @return Type of the address
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type of the address.
	 * @param type Type of the address
	 */
	public void setType(int type) {
		this.type = type;
		if (this.type < 0 || this.type > 3)
			this.type = 0;
	}

	/**
	 * Returns the postal box of the address.
	 * @return Postal box of the address
	 */
	public String getPoBox() {
		return poBox;
	}

	/**
	 * Sets the postal box of the address.
	 * @param poBox Postal box of the address
	 */
	public void setPoBox(String poBox) {
		this.poBox = poBox;
		if (this.poBox == null)
			this.poBox = "";
	}

	/**
	 * Returns the street of the address.
	 * @return Street of the address
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Sets the street of the address.
	 * @param street Street of the address
	 */
	public void setStreet(String street) {
		this.street = street;
		if (this.street == "")
			this.street = "";
	}

	/**
	 * Returns the city of the address.
	 * @return City of the address
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city of the address.
	 * @param city City of the address
	 */
	public void setCity(String city) {
		this.city = city;
		if (this.city == null)
			this.city = "";
	}

	/**
	 * Returns the state of the address.
	 * @return State of the address
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state of the address.
	 * @param state State of the address
	 */
	public void setState(String state) {
		this.state = state;
		if (this.state == null)
			this.state = "";
	}

	/**
	 * Returns the postal code of the address.
	 * @return Postal code of the address
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Sets the postal code of the address.
	 * @param postalCode Postal code of the address
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
		if (this.postalCode == null)
			this.postalCode = "";
	}

	/**
	 * Returns the country of the address.
	 * @return Country of the address
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets the country of the address.
	 * @param country Country of the address
	 */
	public void setCountry(String country) {
		this.country = country;
		if (this.country == null)
			this.country = "";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(120);
		if (!street.isEmpty()){
			builder.append(street);
			builder.append(", ");
		}
		if (!city.isEmpty()){
			builder.append(city);
			builder.append(", ");
		}
		if (!state.isEmpty()){
			builder.append(state);
			builder.append(", ");
		}
		if (!country.isEmpty()){
			builder.append(country);
			builder.append(", ");
		}
		if (!poBox.isEmpty()){
			builder.append(poBox);
			builder.append(", ");
		}
		if (!postalCode.isEmpty()){
			builder.append(postalCode);
			builder.append(", ");
		}
		Context context = ArsApplication.getInstance().getApplicationContext();
		String[] types = context.getResources().getStringArray(R.array.addresses_type);
		builder.append(types[type]);		
		
		return builder.toString();
	}

	/**
	 * Creates an address object.
	 * 
	 * @param poBox Postal box
	 * @param street Street
	 * @param city City
	 * @param state State
	 * @param postal Postal code
	 * @param country Country
	 * @param type Type of the address. See {@link net.deerhunter.ars.contact_structs.Address.Type} for more information
	 */
	public Address(String poBox, String street, String city, String state, String postal, String country, int type) {
		setPoBox(poBox);
		setStreet(street);
		setCity(city);
		setState(state);
		setPostalCode(postal);
		setCountry(country);
		setType(type);
	}
	
	/**
	 * Class that consists of address type constants.
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
		 * home address
		 */
		public static final int HOME = 1;
		/**
		 * Work address
		 */
		public static final int WORK = 2;
		/**
		 * Other address
		 */
		public static final int OTHER = 3;
		/**
		 * User-defined address
		 */
		public static final int CUSTOM = 0;
	}
}
