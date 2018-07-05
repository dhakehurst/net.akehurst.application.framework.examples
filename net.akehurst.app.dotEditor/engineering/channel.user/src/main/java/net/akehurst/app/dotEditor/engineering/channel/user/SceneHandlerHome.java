package net.akehurst.app.dotEditor.engineering.channel.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.lambda.tuple.Tuple3;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.app.dotEditor.computational.interfaceUser.data.DotGraph;
import net.akehurst.application.framework.common.annotations.declaration.ExternalConnection;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationRequest;
import net.akehurst.application.framework.realisation.ActiveSignalProcessingObjectAbstract;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.GuiEventType;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;
import net.akehurst.application.framework.technology.interfaceGui.data.editor.IGuiLanguageService;
import net.akehurst.application.framework.technology.interfaceGui.data.graph.IGuiGraphViewData;

public class SceneHandlerHome extends ActiveSignalProcessingObjectAbstract implements IUserHomeNotification, IGuiSceneHandler {

	public SceneHandlerHome(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IUserHomeRequest userHomeRequest;

	@ExternalConnection
	public IUserAuthenticationRequest userAuthenticationRequest;

	private IHomeScene scene;
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

			final Map<String, Tuple3<String, String, String>> theme = new HashMap<>();
			theme.put("dot.**", new Tuple3<>(null, null, "dd0000"));
			theme.put("dot.graph.**", new Tuple3<>("00dd00", "normal", null));
			theme.put("**.GRAPH", new Tuple3<>("00dd00", "bold", null));
			theme.put("**.WS", new Tuple3<>(null, null, "dddddd"));
			theme.put("**.node_id.**", new Tuple3<>("dd0000", "bold", null));
			this.scene.getEditor().defineTextColourTheme(session, "myTheme", theme);

			final String initText = this.getExampleDot();
			final String edoptions = "{theme:'myTheme', automaticLayout:true, codeLens:false, minimap:{enabled:false} }";
			this.scene.getEditor().create(session, "dot", initText, edoptions);

			// TODO: need to use an 'onReady' event/message
			final String jsonParseTreeData = this.userHomeRequest.fetchParseTree(initText).get(); // ls.update(initText);
			this.scene.getEditor().updateParseTree(session, jsonParseTreeData);

			this.scene.getEditor().onEvent(session, GuiEventType.CHANGE, (ev) -> {
				try {
					final String newText = ev.getDataItem("editor");
					final String jsonParseTreeData1 = this.userHomeRequest.fetchParseTree(newText).get(); // ls.update(newText);
					this.scene.getEditor().updateParseTree(session, jsonParseTreeData1);
					this.createGraph(session, newText);
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

	private void createGraph(final UserSession session, final String newText) {
		super.submit("createGraph", () -> {
			try {
				final DotGraph dotGraph = this.userHomeRequest.fetchGraph(newText).get();

				final DotGraphToGuiGraphConverter converter = new DotGraphToGuiGraphConverter();
				final IGuiGraphViewData data = converter.convert(dotGraph);

				this.scene.getGraph().create(session, data);

			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
	}

}
