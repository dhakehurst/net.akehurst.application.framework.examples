package net.akehurst.app.umlEditor.engineering.channel.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import net.akehurst.app.umlEditor.computational.interfaceUser.IUserProjectNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserProjectRequest;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.State;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.StatechartDiagram;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.Transition;
import net.akehurst.application.framework.common.annotations.declaration.ExternalConnection;
import net.akehurst.application.framework.common.annotations.instance.ServiceReference;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.engineering.common.gui.GuiDiagramData;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.GuiEventType;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;
import net.akehurst.application.framework.technology.interfaceGui.data.diagram.IGuiDiagramData;
import net.akehurst.application.framework.technology.interfaceGui.data.diagram.IGuiGraphEdge;
import net.akehurst.application.framework.technology.interfaceGui.data.diagram.IGuiGraphNode;
import net.akehurst.application.framework.technology.interfaceLogging.ILogger;
import net.akehurst.application.framework.technology.interfaceLogging.LogLevel;

public class SceneHandlerProject extends AbstractActiveSignalProcessingObject implements IUserProjectNotification, IGuiSceneHandler {

	@ServiceReference
	ILogger logger;

	public SceneHandlerProject(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IUserProjectRequest userProjectRequest;

	private ISceneProject scene;

	@Override
	public IGuiScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, SceneHandlerCommon.sceneIdProject, ISceneProject.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		super.submit("loaded", () -> {
			final UserSession session = event.getSession();
			final SceneIdentity currentSceneId = event.getSignature().getSceneId();

			this.scene.getTextUsername().setText(session, session.getUser().getName());

			this.scene.getActionSignOut().onEvent(session, GuiEventType.CLICK, (ev) -> {
				gui.getScene(SceneHandlerCommon.sceneIdWelcome).switchTo(session);
			});

			final String initText = this.getExampleText();
			this.scene.getEditor().onEvent(session, GuiEventType.TEXT_CHANGE, (e) -> {
				final String text = (String) e.getDataItem("editor");
				this.userProjectRequest.requestUpdateText(session, text);
			});
			this.scene.getEditor().create(session, "uml.Statechart", initText);

			final IGuiDiagramData initContent = new GuiDiagramData();
			this.scene.getDiagram().create(session, initContent);
		});
	}

	@Override
	public void notifyParseTree(final UserSession session, final String jsonParseTreeStr) {
		this.scene.getEditor().updateParseTree(session, jsonParseTreeStr);
	}

	@Override
	public void notifyDiagramData(final UserSession session, final StatechartDiagram diagram) {
		super.submit("", () -> {
			final IGuiDiagramData newContent = this.createDiagramContent(diagram);
			this.scene.getDiagram().update(session, newContent);
		});
	}

	String getExampleText() {
		final InputStream is = this.getClass().getResourceAsStream("/example/demo.uml-sc");
		try (final BufferedReader sr = new BufferedReader(new InputStreamReader(is))) {
			final String s = sr.lines().collect(Collectors.joining(System.lineSeparator()));
			return s;
		} catch (final IOException e) {
			e.printStackTrace();
			return "error reading file";
		}
	}

	private IGuiDiagramData createDiagramContent(final StatechartDiagram diagram) {
		final GuiDiagramData data = new GuiDiagramData();

		for (final State s : diagram.getStates()) {
			final IGuiGraphNode n = data.addNode(null, s.getId().getValue(), "State");
			n.getData().put("shape", "shapes.uml.State");
			n.getData().put("name", s.getId().getValue());
			n.getData().put("name", s.getId().getValue());
			final Map<String, Integer> size = new HashMap<>();
			size.put("height", 40);
			size.put("width", 70);
			n.getData().put("size", size);
		}

		for (final Transition t : diagram.getTransitions()) {
			final IGuiGraphEdge e = data.addEdge(t.getId().getValue(), null, t.getSource().getId().getValue(), t.getTarget().getId().getValue(), "Transition");
			e.getData().put("shape", "shapes.uml.Transition");
		}

		return data;
	}

	private IGuiDiagramData createDummyContent() {
		final String filename = "/diagram-styles/statechart.css";
		final URL url = this.getClass().getResource(filename);
		if (url == null) {
			this.logger.log(LogLevel.ERROR, "no such file %s", filename);
		} else {
			try {
				final InputStreamReader r = new InputStreamReader(url.openStream());
				final GuiDiagramData data = new GuiDiagramData(r);
				data.getLayout().put("name", "dagre");
				// data.getLayout().put("fit", "false");

				final IGuiGraphNode n1 = data.addNode(null, "s1", "State");
				n1.getData().put("shape", "shapes.uml.State");
				// data.addNode(n1, "s1_content", "StateContent");
				data.addNode(null, "s2", "State").getData().put("shape", "shapes.uml.State");
				data.addNode(null, "s3", "State").getData().put("shape", "shapes.uml.State");
				data.addNode(null, "s4", "State").getData().put("shape", "shapes.uml.State");

				final IGuiGraphEdge e1 = data.addEdge("t1", null, "s1", "s2", "Transition");
				e1.getData().put("shape", "shapes.uml.Transition");
				e1.getData().put("label", "lbl1");

				data.addEdge("t2", null, "s1", "s3", "Transition").getData().put("label", "lbl2");
				data.addEdge("t3", null, "s1", "s4", "Transition").getData().put("label", "lbl3");
				data.addEdge("t4", null, "s2", "s4", "Transition").getData().put("label", "lbl4");

				return data;
			} catch (final IOException e) {
				this.logger.log(LogLevel.ERROR, "Error reading file %s", filename);
			}
		}
		return null;
	}

}
