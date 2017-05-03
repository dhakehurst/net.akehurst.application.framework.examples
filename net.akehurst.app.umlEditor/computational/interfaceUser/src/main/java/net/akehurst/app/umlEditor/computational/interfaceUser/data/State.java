package net.akehurst.app.umlEditor.computational.interfaceUser.data;

import net.akehurst.application.framework.common.AbstractDataType;
import net.akehurst.application.framework.common.annotations.declaration.DataType;

@DataType
public class State extends AbstractDataType {

	public State(final StateIdentity id) {
		super(id.getValue());
		this.id = id;
	}

	private final StateIdentity id;

	public StateIdentity getId() {
		return this.id;
	}
}
