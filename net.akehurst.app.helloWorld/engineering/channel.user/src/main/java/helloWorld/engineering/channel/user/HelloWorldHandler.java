package helloWorld.engineering.channel.user;

import java.net.URL;

import helloWorld.computational.interfaceUser.IUserNotification;
import helloWorld.computational.interfaceUser.IUserRequest;
import helloWorld.computational.interfaceUser.Message;
import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.technology.gui.common.AbstractGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;

public class HelloWorldHandler extends AbstractGuiHandler implements IGuiSceneHandler, IUserNotification {

	public HelloWorldHandler(final String id) {
		super(id);
	}

	@ConfiguredValue(defaultValue = "helloWorld")
	StageIdentity stageId;

	@ConfiguredValue(defaultValue = "")
	SceneIdentity sceneId;

	IUserRequest userRequest;

	IHelloWorldScene scene;

	@ConfiguredValue(defaultValue = "/Gui.fxml")
	String urlStr;

	// IUserNotification
	@Override
	public void notifyMessage(final UserSession session, final Message message) {
		this.scene.getOutput().setText(session, message.asPrimitive());
	}

	// --------- AbstractGuiHandler ---------
	@Override
	public void notifyReady() {
		final URL rootUrl = this.getClass().getResource("/hello");
		this.getGuiRequest().createStage(this.stageId, false, rootUrl);
	}

	@Override
	protected void onStageCreated(final GuiEvent event) {
		final URL content = this.getClass().getResource(this.urlStr);
		this.createScene(this, this.stageId, content);
	}

	@Override
	public IGuiScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, this.sceneId, IHelloWorldScene.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		this.userRequest.requestStart(event.getSession());
	}
}
