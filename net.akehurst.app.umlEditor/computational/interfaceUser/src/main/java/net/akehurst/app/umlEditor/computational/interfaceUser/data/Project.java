package net.akehurst.app.umlEditor.computational.interfaceUser.data;

import net.akehurst.application.framework.common.annotations.declaration.DataType;

@DataType
public class Project {
	public Project(final ProjectIdentity identity) {
		this.identity = identity;
	}

	final ProjectIdentity identity;

	public ProjectIdentity getIdentity() {
		return this.identity;
	}

	ProjectStatus status;

	public ProjectStatus getStatus() {
		return this.status;
	}

	public void setStatus(final ProjectStatus value) {
		this.status = value;
	}

	String name;

	public String getName() {
		return this.name;
	}

	public void setName(final String value) {
		this.name = value;
	}

	String owner;

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(final String value) {
		this.owner = value;
	}

	String description;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String value) {
		this.description = value;
	}
}
