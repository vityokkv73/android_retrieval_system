package net.deerhunter.ars.contact_structs;

import net.deerhunter.ars.R;
import net.deerhunter.ars.application.ArsApplication;
import android.content.Context;

public class Address {
	private String poBox;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		if (this.type < 0 || this.type > 3)
			this.type = 0;
	}

	public String getPoBox() {
		return poBox;
	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
		if (this.poBox == null)
			this.poBox = "";
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
		if (this.street == "")
			this.street = "";
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
		if (this.city == null)
			this.city = "";
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		if (this.state == null)
			this.state = "";
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
		if (this.postalCode == null)
			this.postalCode = "";
	}

	public String getCountry() {
		return country;
	}

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

	public Address(String poBox, String street, String city, String state, String postal, String country, int type) {
		setPoBox(poBox);
		setStreet(street);
		setCity(city);
		setState(state);
		setPostalCode(postal);
		setCountry(country);
		setType(type);
	}
	
	public class Type {
		public static final int HOME = 1;
		public static final int WORK = 2;
		public static final int OTHER = 3;
		public static final int CUSTOM = 0;
	}
}
