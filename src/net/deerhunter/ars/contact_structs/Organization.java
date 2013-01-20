package net.deerhunter.ars.contact_structs;

/**
 * This class represents an organization of a contact.
 * 
 * @author DeerHunter (vityokkv73@gmail.com)
 */
public class Organization {
	private String name;
	private String title;

	/**
	 * Return the name of the organization.
	 * @return Name of the organization
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the organization.
	 * @param name Name of the organization
	 */
	public void setName(String name) {
		this.name = name;
		if (this.name == null)
			this.name = "";
	}

	/**
	 * Returns the title of the organization.
	 * @return Title of the organization
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the organization.
	 * @param title Title of the organization
	 */
	public void setTitle(String title) {
		this.title = title;
		if (this.title == null)
			this.title = "";
	}

	/**
	 * Creates an instance of the Organization class.
	 * @param name
	 * @param title
	 */
	public Organization(String name, String title) {
		setName(name);
		setTitle(title);
	}

	/**
	 * Creates an instance of the Organization class.
	 */
	public Organization() {
		this.name = "";
		this.title = "";
	}

	@Override
	public String toString() {
		if (name.isEmpty())
			return "";
		StringBuilder builder = new StringBuilder(name);
		if (!title.isEmpty()) {
			builder.append(", ");
			builder.append(title);
		}
		return builder.toString();
	}
}
