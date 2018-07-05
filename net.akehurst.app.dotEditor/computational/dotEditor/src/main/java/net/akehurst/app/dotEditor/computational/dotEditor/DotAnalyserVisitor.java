package net.akehurst.app.dotEditor.computational.dotEditor;

import java.util.List;

import net.akehurst.app.dotEditor.computational.interfaceUser.data.DotGraph;
import net.akehurst.app.dotEditor.computational.interfaceUser.data.Node;
import net.akehurst.language.api.analyser.SemanticAnalyser;
import net.akehurst.language.api.analyser.UnableToAnalyseExeception;
import net.akehurst.language.api.sppt.SPPTBranch;
import net.akehurst.language.api.sppt.SPPTNode;
import net.akehurst.language.util.SemanticAnalyserVisitorBasedAbstract;

public class DotAnalyserVisitor extends SemanticAnalyserVisitorBasedAbstract implements SemanticAnalyser {

	public DotAnalyserVisitor() {
		super.register("graph", this::graph);
		super.register("stmt", this::stmt);
	}

	SPPTNode selectNode(final SPPTBranch start, final int... selectors) {
		SPPTNode node = start;
		for (final int i : selectors) {
			if (node instanceof SPPTBranch) {
				final SPPTBranch b = (SPPTBranch) node;
				node = b.getChild(i);
			} else {
				return null;
			}
		}
		return node;
	}

	SPPTBranch selectBranch(final SPPTBranch start, final int... selectors) {
		final SPPTNode node = this.selectNode(start, selectors);
		if (node instanceof SPPTBranch) {
			return (SPPTBranch) node;
		} else {
			return null;
		}
	}

	public DotGraph graph(final SPPTBranch target, final List<SPPTBranch> children, final Object arg) throws UnableToAnalyseExeception {
		final DotGraph result = new DotGraph("graph");

		for (final SPPTBranch stmtGrpBranch : this.selectBranch(target, 5, 1).getBranchNonSkipChildren()) {
			final SPPTBranch stmtBranch = stmtGrpBranch.getBranchNonSkipChildren().get(0);
			super.analyse(stmtBranch, result);
		}

		return result;
	}

	public Object stmt(final SPPTBranch target, final List<SPPTBranch> children, final Object arg) throws UnableToAnalyseExeception {
		final DotGraph graph = (DotGraph) arg;
		final String id = children.get(0).getBranchNonSkipChildren().get(0).getBranchNonSkipChildren().get(0).getBranchNonSkipChildren().get(0)
				.getNonSkipMatchedText();
		return new Node(graph, id);
	}

}
