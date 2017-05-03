package net.akehurst.app.umlEditor.computational.umlEditor;

import java.util.HashMap;
import java.util.Map;

import net.akehurst.app.umlEditor.computational.interfaceUser.UserException;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.State;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.StateIdentity;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.StatechartDiagram;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.StatechartIdentity;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.Transition;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.TransitionIdentity;
import net.akehurst.language.core.parser.IBranch;
import net.akehurst.language.core.parser.INode;
import net.akehurst.language.core.parser.IParseTree;

public class ParseTreeToStatechart {

	public ParseTreeToStatechart() {
		this.states = new HashMap<>();
	}

	private int nextTransId;
	private final Map<String, State> states;

	public StatechartDiagram transform(final IParseTree pt) throws UserException {
		final IBranch document = (IBranch) pt.getRoot();
		final IBranch statechart = (IBranch) document.getChild(1);
		return this.transform(statechart);
	}

	private <T> T transform(final INode node) throws UserException {
		final String name = node.getName();
		switch (name) {
			case "statechart":
				return (T) this.createStatechartDiagram((IBranch) node);
			case "state":
				return (T) this.createState((IBranch) node);
			case "simpleState":
				return (T) this.createSimpleState((IBranch) node);
			case "transition":
				return (T) this.createTransition((IBranch) node);
			case "qualifiedStateId":
				return (T) this.findState((IBranch) node);
			default:
				return null;
		}
	}

	private StatechartDiagram createStatechartDiagram(final IBranch node) throws UserException {
		final String idStr = node.getChild(1).getMatchedText().trim();

		final StatechartDiagram result = new StatechartDiagram(new StatechartIdentity(idStr));

		final IBranch states = (IBranch) node.getChild(3);
		for (final INode sn : states.getChildren()) {
			final State s = this.transform(sn);
			if (null != s) {
				result.getStates().add(s);
			}
		}

		final IBranch transitions = (IBranch) node.getChild(4);
		for (final INode tn : transitions.getChildren()) {
			final Transition t = this.transform(tn);
			if (null != t) {
				result.getTransitions().add(t);
			}
		}

		return result;
	}

	private State createState(final IBranch node) throws UserException {
		final State s = this.transform(node.getChild(0));
		this.states.put(s.getId().getValue(), s);
		return s;
	}

	private State findState(final String idStr) {
		return this.states.get(idStr);
	}

	private State findState(final IBranch qualifiedStateId) {
		final String idStr = qualifiedStateId.getMatchedText().trim();
		return this.findState(idStr);
	}

	private State createSimpleState(final IBranch node) throws UserException {
		final String idStr = node.getChild(1).getMatchedText().trim();
		final State result = new State(new StateIdentity(idStr));
		return result;
	}

	private Transition createTransition(final IBranch node) throws UserException {
		final IBranch sourceEnd = (IBranch) node.getChild(1);
		final IBranch targetEnd = (IBranch) node.getChild(3);
		final State source = this.transform(sourceEnd.getChild(0));
		final State target = this.transform(targetEnd.getChild(0));
		final TransitionIdentity id = new TransitionIdentity(this.nextTransId());
		final Transition result = new Transition(id, source, target);
		return result;
	}

	private String nextTransId() {
		return "t" + this.nextTransId++;
	}
}
