package net.akehurst.app.dotEditor.computational.interfaceUser.data;

import java.util.HashSet;
import java.util.Set;

import net.akehurst.application.framework.common.DataTypeAbstract;

public class DotGraph extends DataTypeAbstract {

	private final String identity;
	private final Set<Node> nodes;
	private final Set<Edge> edges;

	public DotGraph(final String identity) {
		super(identity);
		this.identity = identity;
		this.nodes = new HashSet<>();
		this.edges = new HashSet<>();
	}

	public String getIdentity() {
		return this.identity;
	}

	public Set<Node> getNodes() {
		return this.nodes;
	}

	public Set<Edge> getEdges() {
		return this.edges;
	}

}
