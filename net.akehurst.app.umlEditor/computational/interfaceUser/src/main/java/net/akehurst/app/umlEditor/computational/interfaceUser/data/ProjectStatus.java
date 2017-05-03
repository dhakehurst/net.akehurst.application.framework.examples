package net.akehurst.app.umlEditor.computational.interfaceUser.data;

import net.akehurst.application.framework.common.AbstractDataType;
import net.akehurst.application.framework.common.annotations.declaration.DataType;

@DataType
public class ProjectStatus extends AbstractDataType {
	public ProjectStatus(final String value) {
		super(value);
	}

	public String getValue() {
		return (String) super.getIdentityValues().get(0);
	}

}
