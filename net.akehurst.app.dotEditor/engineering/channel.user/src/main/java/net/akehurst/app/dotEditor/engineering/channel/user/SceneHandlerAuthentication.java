package net.akehurst.app.dotEditor.engineering.channel.user;

import java.net.URL;

import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationNotification;
import net.akehurst.application.framework.computational.interfaceUser.authentication.IUserAuthenticationRequest;
import net.akehurst.application.framework.engineering.gui.common.ISignInScene;
import net.akehurst.application.framework.realisation.AbstractIdentifiableObject;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.GuiEventType;
import net.akehurst.application.framework.technology.interfaceGui.GuiException;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;

public class SceneHandlerAuthentication extends AbstractIdentifiableObject implements IUserAuthenticationNotification, IGuiSceneHandler {

	public SceneHandlerAuthentication(final String afId) {
		super(afId);
	}

	ISignInScene scene;
	IGuiHandler gui;
	public IUserAuthenticationRequest userAuthenticationRequest;

	@Override
	public ISignInScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, SceneHandlerCommon.sceneIdSignIn, ISignInScene.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		this.gui = gui;

		this.scene.getActionSignIn().onEvent(event.getSession(), GuiEventType.CLICK, (e) -> {
			final String username = (String) e.getDataItem("inputEmail");
			final String password = (String) e.getDataItem("inputPassword");

			this.userAuthenticationRequest.requestLogin(e.getSession(), username, password);

		});
	}

	@Override
	public void notifyAuthenticationSuccess(final UserSession session) {
		try {
			this.gui.addAuthentication(session);
		} catch (final GuiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.gui.getScene(SceneHandlerCommon.sceneIdHome).switchTo(session);
	}

	@Override
	public void notifyAuthenticationFailure(final UserSession session, final String message) {
		this.scene.getMessageBox().set(session, "hidden", false);
		this.scene.getMessageText().setText(session, "Sign In Failed, please try again.");
	}

	@Override
	public void notifyAuthenticationCleared(final UserSession session) {
		this.gui.getScene(SceneHandlerCommon.sceneIdSignIn).switchTo(session);
	}

}
