package net.akehurst.app.dotEditor.computational.interfaceUser.data;

import net.akehurst.application.framework.common.DataTypeAbstract;

public class Node extends DataTypeAbstract {

	private final String identity;

	public Node(final DotGraph owner, final String identity) {
		super(owner.getIdentity(), identity);
		this.identity = identity;
		owner.getNodes().add(this);
	}

	public String getIdentity() {
		return this.identity;
	}

}
