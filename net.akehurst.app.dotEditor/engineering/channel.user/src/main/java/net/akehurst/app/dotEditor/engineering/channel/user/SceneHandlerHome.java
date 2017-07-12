package net.akehurst.app.dotEditor.engineering.channel.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.application.framework.common.annotations.declaration.ExternalConnection;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationRequest;
import net.akehurst.application.framework.engineering.gui.common.GuiGraphData;
import net.akehurst.application.framework.realisation.AbstractIdentifiableObject;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.GuiEventType;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;
import net.akehurst.application.framework.technology.interfaceGui.data.editor.IGuiLanguageService;
import net.akehurst.language.core.ILanguageProcessor;

public class SceneHandlerHome extends AbstractIdentifiableObject implements IUserHomeNotification, IGuiSceneHandler {

	public SceneHandlerHome(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IUserHomeRequest userHomeRequest;

	@ExternalConnection
	public IUserAuthenticationRequest userAuthenticationRequest;

	private IHomeScene scene;
	private ILanguageProcessor languageProcessor;
	private IGuiLanguageService languageService;

	@Override
	public IGuiScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, SceneHandlerCommon.sceneIdHome, IHomeScene.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		try {
			final UserSession session = event.getSession();
			final SceneIdentity currentSceneId = event.getSignature().getSceneId();

			this.scene.getTextUsername().setText(session, session.getUser().getName());

			this.scene.getActionSignOut().onEvent(session, GuiEventType.CLICK, (ev) -> {
				gui.getScene(SceneHandlerCommon.sceneIdWelcome).switchTo(session);
			});

			// final IGuiLanguageService ls = this.getLanguageService();
			final String initText = this.getExampleDot();
			this.scene.getEditor().create(session, "dot", initText);
			// TODO: need to use an 'onReady' event/message
			final String jsonParseTreeData = this.userHomeRequest.fetchParseTree(initText).get(); // ls.update(initText);
			this.scene.getEditor().updateParseTree(session, jsonParseTreeData);

			this.scene.getEditor().onEvent(session, GuiEventType.TEXT_CHANGE, (ev) -> {
				try {
					final String newText = ev.getDataItem("editor");
					final String jsonParseTreeData1 = this.userHomeRequest.fetchParseTree(newText).get(); // ls.update(newText);
					this.scene.getEditor().updateParseTree(session, jsonParseTreeData1);
					this.createGraph(session);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			});
			this.scene.getEditor().onProvideCompletionItems(session, (text, position) -> {
				try {
					final List<Map<String, Object>> list = this.userHomeRequest.fetchCompletion(text, position).get();
					return list;
				} catch (final Exception e) {
					e.printStackTrace();
				}
				return Arrays.asList();
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private String getExampleDot() {
		final InputStream is = this.getClass().getResourceAsStream("/example/html-graph.dot");
		try (final BufferedReader sr = new BufferedReader(new InputStreamReader(is))) {
			final String s = sr.lines().collect(Collectors.joining(System.lineSeparator()));
			return s;
		} catch (final IOException e) {
			e.printStackTrace();
			return "error reading file";
		}
	}

	private void createGraph(final UserSession session) {
		try {
			// userHomeRequest.getGraph()

			final Reader styleReader = new StringReader("node { border-color: red; background-color:green;} edge {line-color: black; width: 1;}");
			final GuiGraphData data = new GuiGraphData(styleReader);

			data.getLayout().put("name", "cose");
			data.getLayout().put("randomize", "true");

			data.addNode(null, "s1", "State");
			data.addNode(null, "s2", "State");
			data.addNode(null, "s3", "State");
			data.addNode(null, "s4", "State");

			data.addEdge("t1", null, "s1", "s2", "Transition").getData().put("label", "lbl1");
			data.addEdge("t2", null, "s1", "s3", "Transition").getData().put("label", "lbl2");
			data.addEdge("t3", null, "s1", "s4", "Transition").getData().put("label", "lbl3");
			data.addEdge("t4", null, "s2", "s4", "Transition").getData().put("label", "lbl4");

			this.scene.getGraph().create(session, data);

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	// private ILanguageProcessor getLanguageProcessor() {
	// // if (null == this.languageProcessor) {
	// try {
	// final OGLanguageProcessor oglProc = new OGLanguageProcessor();
	// final InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream("/net/akehurst/language/dot/Dot.ogl"));
	// final IGrammar grammar = oglProc.process(reader, "grammarDefinition", IGrammar.class);
	// final ISemanticAnalyser semanticAnalyser = null;
	// this.languageProcessor = new LanguageProcessor(grammar, semanticAnalyser);
	// } catch (final Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// // }
	// return this.languageProcessor;
	// }
	//
	// private IGuiLanguageService getLanguageService() {
	// // if (null == this.languageService) {
	// final ILanguageProcessor processor = this.getLanguageProcessor();
	// this.languageService = new GuiLanguageServiceFromProcessor(processor, "graph");
	// // }
	// return this.languageService;
	//
	// }
}
