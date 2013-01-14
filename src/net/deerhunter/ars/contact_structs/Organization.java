package net.deerhunter.ars.contact_structs;

public class Organization {
	private String organization;
	private String title;

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
		if (this.organization == null)
			this.organization = "";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if (this.title == null)
			this.title = "";
	}

	public Organization(String org, String title) {
		setOrganization(org);
		setTitle(title);
	}

	public Organization() {
		this.organization = "";
		this.title = "";
	}

	@Override
	public String toString() {
		if (organization.isEmpty())
			return "";
		StringBuilder builder = new StringBuilder(organization);
		if (!title.isEmpty()) {
			builder.append(", ");
			builder.append(title);
		}
		return builder.toString();
	}
}
