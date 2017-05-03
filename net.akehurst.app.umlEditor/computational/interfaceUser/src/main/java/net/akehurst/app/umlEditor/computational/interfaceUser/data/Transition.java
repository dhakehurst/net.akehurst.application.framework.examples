package net.akehurst.app.umlEditor.computational.interfaceUser.data;

import net.akehurst.application.framework.common.AbstractDataType;
import net.akehurst.application.framework.common.annotations.declaration.DataType;

@DataType
public class Transition extends AbstractDataType {

	public Transition(final TransitionIdentity id, final State source, final State target) {
		super(source.getId().getValue(), target.getId().getValue());
		this.id = id;
		this.source = source;
		this.target = target;
	}

	private final TransitionIdentity id;

	public TransitionIdentity getId() {
		return this.id;
	}

	private final State source;
	private final State target;

	public State getSource() {
		return this.source;
	}

	public State getTarget() {
		return this.target;
	}
}
