package net.akehurst.app.umlEditor.engineering.channel.user;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.akehurst.app.umlEditor.computational.interfaceUser.IUserHomeNotification;
import net.akehurst.app.umlEditor.computational.interfaceUser.IUserHomeRequest;
import net.akehurst.app.umlEditor.computational.interfaceUser.data.Project;
import net.akehurst.application.framework.common.annotations.declaration.ExternalConnection;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationRequest;
import net.akehurst.application.framework.realisation.AbstractIdentifiableObject;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.GuiEventType;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;
import net.akehurst.application.framework.technology.interfaceGui.data.table.AbstractGuiTableData;

public class SceneHandlerHome extends AbstractIdentifiableObject implements IUserHomeNotification, IGuiSceneHandler {

	public SceneHandlerHome(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IUserHomeRequest userHomeRequest;

	@ExternalConnection
	public IUserAuthenticationRequest userAuthenticationRequest;

	private ISceneHome scene;

	@Override
	public IGuiScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, SceneHandlerCommon.sceneIdHome, ISceneHome.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		final UserSession session = event.getSession();
		final SceneIdentity currentSceneId = event.getSignature().getSceneId();

		this.scene.getTextUsername().setText(session, session.getUser().getName());

		this.scene.getActionSignOut().onEvent(session, GuiEventType.CLICK, (ev) -> {
			gui.getScene(SceneHandlerCommon.sceneIdWelcome).switchTo(session);
		});

		this.scene.getActionSelectProject().onEvent(session, GuiEventType.CLICK, (ev) -> {
			final String projectId = (String) ev.getDataItem("projectId");
			final Map<String, String> args = new HashMap<>();
			args.put("id", projectId);
			gui.getScene(SceneHandlerCommon.sceneIdProject).switchTo(ev.getSession(), args);
		});

		this.userHomeRequest.requestSelectHome(session);

	}

	@Override
	public void notifyProjectList(final UserSession session, final List<Project> projectList) {

		this.scene.getTableProjects().setData(session, new AbstractGuiTableData<String, Integer>() {

			@Override
			public Map<String, Object> getRowData(final int index) {
				final Project p = projectList.get(index);
				final Map<String, Object> r = new HashMap<>();

				final String identity = p.getIdentity().getValue();
				final String status = null == p.getStatus() ? "unknown" : p.getStatus().getValue();
				final String name = null == p.getName() ? "unknown" : p.getName();
				final String owner = null == p.getOwner() ? "unknown" : p.getOwner();
				final String description = null == p.getDescription() ? "unknown" : p.getDescription();

				r.put("identity", identity);
				r.put("status", status);
				r.put("name", name);
				r.put("owner", owner);
				r.put("description", description);
				return r;
			}

			@Override
			public int getNumberOfRows() {
				return projectList.size();
			}
		});
	}

}
