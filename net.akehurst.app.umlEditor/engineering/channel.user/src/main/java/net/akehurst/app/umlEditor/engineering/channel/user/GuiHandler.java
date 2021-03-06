package net.akehurst.app.umlEditor.engineering.channel.user;

import java.net.URL;

import net.akehurst.application.framework.common.annotations.instance.ActiveObjectInstance;
import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.common.annotations.instance.IdentifiableObjectInstance;
import net.akehurst.application.framework.technology.gui.common.AbstractGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.IGuiNotification;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;

public class GuiHandler extends AbstractGuiHandler implements IGuiNotification {

	public GuiHandler(final String id) {
		super(id);
	}

	@IdentifiableObjectInstance
	SceneHandlerWelcome sceneHandlerWelcome;

	@IdentifiableObjectInstance
	SceneHandlerAuthentication sceneHandlerAuthentication;

	@IdentifiableObjectInstance
	SceneHandlerHome sceneHandlerHome;

	@ActiveObjectInstance
	SceneHandlerProject sceneHandlerProject;

	@ConfiguredValue(defaultValue = "css")
	StageIdentity stageIdStyle;

	@ConfiguredValue(defaultValue = "auth")
	StageIdentity authStageId;

	@ConfiguredValue(defaultValue = "")
	StageIdentity unsecureId;

	@ConfiguredValue(defaultValue = "user")
	StageIdentity stageIdUser;

	// --------- AbstractGuiHandler ---------
	@Override
	public void notifyReady() {

		// final URL rootUrl = this.getClass().getResource();
		this.getGuiRequest().createStage(this.stageIdStyle, false, "/css");

		// final URL rootUrl2 = this.getClass().getResource();
		this.getGuiRequest().createStage(this.stageIdUser, true, "/secure");

		// final URL rootUrl3 = this.getClass().getResource();
		this.getGuiRequest().createStage(this.authStageId, false, "/af");

		// Must create this last as the route would override the others given an empty ("") stageId
		// final URL rootUrl4 = this.getClass().getResource();
		this.getGuiRequest().createStage(this.unsecureId, false, "/unsecure");
	}

	@Override
	protected void onStageCreated(final GuiEvent event) {
		final URL content = null;// this.getClass().getResource(this.urlStr);

		final StageIdentity currentStage = event.getSignature().getStageId();
		if (this.stageIdUser.equals(currentStage)) {
			this.sceneHandlerHome.createScene(this, this.stageIdUser, content);
			this.sceneHandlerProject.createScene(this, this.stageIdUser, content);
		} else if (this.authStageId.equals(currentStage)) {
			this.sceneHandlerAuthentication.createScene(this, this.authStageId, content);
		} else if (this.unsecureId.equals(currentStage)) {
			this.sceneHandlerWelcome.createScene(this, this.unsecureId, content);
		}
	}

}
