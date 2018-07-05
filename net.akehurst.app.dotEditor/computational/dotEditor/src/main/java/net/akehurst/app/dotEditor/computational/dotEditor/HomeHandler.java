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
import net.akehurst.application.framework.realisation.ActiveSignalProcessingObjectAbstract;
import net.akehurst.language.api.analyser.SemanticAnalyser;
import net.akehurst.language.api.grammar.Grammar;
import net.akehurst.language.api.processor.CompletionItem;
import net.akehurst.language.api.processor.LanguageProcessor;
import net.akehurst.language.api.sppt.SharedPackedParseTree;
import net.akehurst.language.processor.LanguageProcessorDefault;
import net.akehurst.language.processor.OGLanguageProcessor;
import net.akehurst.language.util.ToJsonVisitor;

public class HomeHandler extends ActiveSignalProcessingObjectAbstract implements IUserHomeRequest {

	public HomeHandler(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IUserHomeNotification userHomeNotification;

	private LanguageProcessor languageProcessor;

	private LanguageProcessor getLanguageProcessor() {
		if (null == this.languageProcessor) {
			try {
				final OGLanguageProcessor oglProc = new OGLanguageProcessor();
				final InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("/net/akehurst/language/dot/Dot.ogl"));
				final Grammar grammar = oglProc.process(reader, "grammarDefinition", Grammar.class);
				final SemanticAnalyser semanticAnalyser = new DotAnalyserVisitor();
				this.languageProcessor = new LanguageProcessorDefault(grammar, semanticAnalyser);
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

			final Reader reader = new StringReader(initText);
			System.out.println("parsing " + initText);
			final SharedPackedParseTree pt = this.getLanguageProcessor().getParser().parse("graph", reader);
			System.out.println("parse success " + pt.getRoot().getName());
			final ToJsonVisitor visitor = new ToJsonVisitor();
			final JsonBuilderFactory b = Json.createBuilderFactory(null);
			final JsonObject json = visitor.visit(pt, b);

			return json.toString();

		});
	}

	@Override
	public Future<List<Map<String, Object>>> fetchCompletion(final String text, final int position) {
		return super.submit("fetchCompletion", (Class<List<Map<String, Object>>>) (Class<?>) List.class, () -> {
			final Reader reader = new StringReader(text);
			final List<CompletionItem> expected = this.getLanguageProcessor().expectedAt(reader, "graph", position, 1);
			final List<Map<String, Object>> res = new ArrayList<>();
			for (final CompletionItem n : expected) {
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
