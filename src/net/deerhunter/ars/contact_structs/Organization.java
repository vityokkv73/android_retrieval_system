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
