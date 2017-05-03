package net.akehurst.app.umlEditor.computational.interfaceUser.data;

import java.util.HashSet;
import java.util.Set;

import net.akehurst.application.framework.common.AbstractDataType;
import net.akehurst.application.framework.common.annotations.declaration.DataType;

@DataType
public class StatechartDiagram extends AbstractDataType {

	public StatechartDiagram(final StatechartIdentity id) {
		super(id.getValue());
		this.id = id;
		this.states = new HashSet<>();
		this.transitions = new HashSet<>();
	}

	final private StatechartIdentity id;
	private final Set<State> states;
	private final Set<Transition> transitions;

	public StatechartIdentity getId() {
		return this.id;
	}

	public Set<State> getStates() {
		return this.states;
	}

	public Set<Transition> getTransitions() {
		return this.transitions;
	}
}
