package net.akehurst.app.dotEditor.engineering.channel.user;

import java.net.URL;

import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeNotification;
import net.akehurst.app.dotEditor.computational.interfaceUser.IUserWelcomeRequest;
import net.akehurst.application.framework.common.annotations.declaration.ExternalConnection;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.realisation.IdentifiableObjectAbstract;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.GuiEventType;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;

public class SceneHandlerWelcome extends IdentifiableObjectAbstract implements IUserWelcomeNotification, IGuiSceneHandler {

	public SceneHandlerWelcome(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IUserWelcomeRequest userWelcomeRequest;

	IWelcomeScene scene;

	@Override
	public IGuiScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, SceneHandlerCommon.sceneIdWelcome, IWelcomeScene.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		final UserSession session = event.getSession();
		this.scene.getActionSignIn().onEvent(session, GuiEventType.CLICK, (e) -> {
			gui.getScene(SceneHandlerCommon.sceneIdSignIn).switchTo(session);
		});

	}

}
