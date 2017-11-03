package net.akehurst.app.dotEditor.computational.dotEditor;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.app.dotEditor.computational.interfaceUser.data.DotGraph;
import net.akehurst.application.framework.common.annotations.declaration.ExternalConnection;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;
import net.akehurst.language.core.analyser.ISemanticAnalyser;
import net.akehurst.language.core.grammar.IGrammar;
import net.akehurst.language.core.grammar.RuleNotFoundException;
import net.akehurst.language.core.parser.ICompletionItem;
import net.akehurst.language.core.parser.IParseTree;
import net.akehurst.language.core.parser.ParseFailedException;
import net.akehurst.language.core.parser.ParseTreeException;
import net.akehurst.language.core.processor.ILanguageProcessor;
import net.akehurst.language.processor.LanguageProcessor;
import net.akehurst.language.processor.OGLanguageProcessor;
import net.akehurst.language.util.ToJsonVisitor;

public class HomeHandler extends AbstractActiveSignalProcessingObject implements IUserHomeRequest {

	public HomeHandler(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IUserHomeNotification userHomeNotification;

	private ILanguageProcessor languageProcessor;

	private ILanguageProcessor getLanguageProcessor() {
		if (null == this.languageProcessor) {
			try {
				final OGLanguageProcessor oglProc = new OGLanguageProcessor();
				final InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("/net/akehurst/language/dot/Dot.ogl"));
				final IGrammar grammar = oglProc.process(reader, "grammarDefinition", IGrammar.class);
				final ISemanticAnalyser semanticAnalyser = new DotAnalyserVisitor();
				this.languageProcessor = new LanguageProcessor(grammar, semanticAnalyser);
			} catch (final Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return this.languageProcessor;
	}

	// --- IUserHomeRequest ---
	@Override
	public Future<String> fetchParseTree(final String initText) {
		return super.submit("fetchParseTree", String.class, () -> {
			try {
				final Reader reader = new StringReader(initText);
				System.out.println("parsing " + initText);
				final IParseTree pt = this.getLanguageProcessor().getParser().parse("graph", reader);
				System.out.println("parse success " + pt.getRoot().getName());
				final ToJsonVisitor visitor = new ToJsonVisitor();
				final JsonBuilderFactory b = Json.createBuilderFactory(null);
				final JsonObject json = visitor.visit(pt, b);

				return json.toString();

			} catch (ParseFailedException | ParseTreeException | RuleNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Json.createObjectBuilder().add("error", "error").build().toString();

		});
	}

	@Override
	public Future<List<Map<String, Object>>> fetchCompletion(final String text, final int position) {
		return super.submit("fetchCompletion", (Class<List<Map<String, Object>>>) (Class<?>) List.class, () -> {
			final Reader reader = new StringReader(text);
			final List<ICompletionItem> expected = this.getLanguageProcessor().expectedAt(reader, "graph", position, 1);
			final List<Map<String, Object>> res = new ArrayList<>();
			for (final ICompletionItem n : expected) {
				final Map<String, Object> m = new HashMap<>();
				m.put("label", n.getText());
				m.put("kind", 0);
				res.add(m);
			}
			return res;
		});
	}

	@Override
	public Future<DotGraph> fetchGraph(final String dotText) {
		return super.submit("fetchGraph", DotGraph.class, () -> {
			final DotGraph res = this.getLanguageProcessor().process(dotText, "graph", DotGraph.class);
			return res;
		});
	}

}
