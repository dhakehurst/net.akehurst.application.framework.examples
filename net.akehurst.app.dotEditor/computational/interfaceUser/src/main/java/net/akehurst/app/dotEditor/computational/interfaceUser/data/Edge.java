package net.akehurst.app.dotEditor.computational.interfaceUser.data;

import net.akehurst.application.framework.common.DataTypeAbstract;

public class Edge extends DataTypeAbstract {

	private final String identity;
	private final Node source;
	private final Node target;

	public Edge(final DotGraph owner, final String identity, final Node source, final Node target) {
		super(owner.getIdentity(), identity);
		this.identity = identity;
		this.source = source;
		this.target = target;
	}

	public String getIdentity() {
		return this.identity;
	}

	public Node getSource() {
		return this.source;
	}

	public Node getTarget() {
		return this.target;
	}
}
