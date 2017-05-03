package net.akehurst.app.umlEditor.computational.umlEditor;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.json.JsonObject;

import net.akehurst.app.umlEditor.computational.interfaceUser.IUserProjectNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserProjectRequest;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.StatechartDiagram;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;
import net.akehurst.language.core.ILanguageProcessor;
import net.akehurst.language.core.analyser.IGrammar;
import net.akehurst.language.core.analyser.ISemanticAnalyser;
import net.akehurst.language.core.parser.IParseTree;
import net.akehurst.language.core.parser.ParseFailedException;
import net.akehurst.language.core.parser.ParseTreeException;
import net.akehurst.language.core.parser.RuleNotFoundException;
import net.akehurst.language.processor.LanguageProcessor;
import net.akehurst.language.processor.OGLanguageProcessor;

public class ProjectHandler extends AbstractActiveSignalProcessingObject implements IUserProjectRequest {

	public ProjectHandler(final String afId) {
		super(afId);
	}

	public IUserProjectNotification userProjectNotification;

	@Override
	public void requestUpdateText(final UserSession session, final String text) {
		super.submit("requestUpdateText", () -> {
			final IParseTree pt = this.parse("document", text);
			final String jsonStr = this.createJsonStr(pt);
			this.userProjectNotification.notifyParseTree(session, jsonStr);
			this.createDiagram(session, pt);
		});
	}

	private String createJsonStr(final IParseTree pt) {
		final ToJsonVisitor visitor = new ToJsonVisitor();
		final JsonObject json = pt.accept(visitor, null);
		return json.toString();
	}

	private void createDiagram(final UserSession session, final IParseTree pt) {
		super.submit("requestUpdateText", () -> {
			final ParseTreeToStatechart trans = new ParseTreeToStatechart();
			final StatechartDiagram diagram = trans.transform(pt);
			this.userProjectNotification.notifyDiagramData(session, diagram);
		});
	}

	private IParseTree parse(final String ruleName, final String text) {
		try {
			final Reader reader = new StringReader(text);
			System.out.println("parsing " + text);
			final IParseTree pt = this.getLanguageProcessor().getParser().parse(ruleName, reader);
			System.out.println("parse success " + pt.getRoot().getName());
			return pt;

		} catch (ParseFailedException | ParseTreeException | RuleNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	ILanguageProcessor languageProcessor;

	ILanguageProcessor getLanguageProcessor() {
		// if (null == this.languageProcessor) {
		try {
			final OGLanguageProcessor oglProc = new OGLanguageProcessor();
			final InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("/net/akehurst/language/uml/Statechart.ogl"));
			final IGrammar grammar = oglProc.process(reader, IGrammar.class);
			final ISemanticAnalyser semanticAnalyser = null;
			this.languageProcessor = new LanguageProcessor(grammar, semanticAnalyser);
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
		// }
		return this.languageProcessor;
	}

}
