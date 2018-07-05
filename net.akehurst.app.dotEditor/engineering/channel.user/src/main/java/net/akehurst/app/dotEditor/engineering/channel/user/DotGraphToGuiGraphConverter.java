package net.akehurst.app.dotEditor.engineering.channel.user;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import net.akehurst.app.dotEditor.computational.interfaceUser.data.DotGraph;
import net.akehurst.app.dotEditor.computational.interfaceUser.data.Edge;
import net.akehurst.app.dotEditor.computational.interfaceUser.data.Node;
import net.akehurst.application.framework.engineering.gui.common.GuiGraphData;
import net.akehurst.application.framework.technology.interfaceGui.data.graph.IGuiGraphEdge;
import net.akehurst.application.framework.technology.interfaceGui.data.graph.IGuiGraphNode;
import net.akehurst.application.framework.technology.interfaceGui.data.graph.IGuiGraphViewData;

public class DotGraphToGuiGraphConverter {

	public DotGraphToGuiGraphConverter() throws IOException {
		final Reader styleReader = new InputStreamReader(this.getClass().getResourceAsStream("/graph-style/graph.css"));
		this.graph = new GuiGraphData(styleReader);
		this.graph.getLayout().put("name", "dagre");
		this.graph.getLayout().put("rankDir", "BT");
	}

	private final GuiGraphData graph;

	private String createNodeId(final Node node) {
		final String id = node.getIdentity();
		return id;
	}

	private IGuiGraphNode createNode(final String nodeId, final String label, final String colour, final String... classes) {
		final String identity = nodeId;
		final IGuiGraphNode n = this.graph.addNode(null, identity, classes);
		n.getData().put("label", label);
		n.getData().put("bg", null == colour || colour.isEmpty() ? "gray" : colour);
		return n;
	}

	public IGuiGraphViewData convert(final DotGraph dotGraph) {
		for (final Node node : dotGraph.getNodes()) {
			final String nid = this.createNodeId(node);
			final String nlabel = node.getIdentity();
			this.createNode(nid, nlabel, "green");
		}
		for (final Edge edge : dotGraph.getEdges()) {
			final String fromId = this.createNodeId(edge.getSource());
			final String toId = this.createNodeId(edge.getTarget());
			final IGuiGraphEdge e = this.graph.addEdge(edge.getIdentity(), null, fromId, toId);
			final String label = edge.getIdentity();
			e.getData().put("label", label);
		}
		return this.graph;
	}

}
