package helloWorld.engineering.channel.user;

import java.net.URL;

import helloWorld.computational.interfaceUser.IUserNotification;
import helloWorld.computational.interfaceUser.IUserRequest;
import helloWorld.computational.interfaceUser.Message;
import net.akehurst.application.framework.common.UserSession;
import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.technology.gui.common.AbstractGuiHandler;
import net.akehurst.application.framework.technology.guiInterface.GuiEvent;
import net.akehurst.application.framework.technology.guiInterface.IGuiScene;
import net.akehurst.application.framework.technology.guiInterface.SceneIdentity;
import net.akehurst.application.framework.technology.guiInterface.StageIdentity;

public class HelloWorldHandler extends AbstractGuiHandler implements IUserNotification {

	public HelloWorldHandler(String id) {
		super(id);
	}

	@ConfiguredValue(defaultValue="/helloWorld")
	StageIdentity stageId;
	
	@ConfiguredValue(defaultValue="helloWorld")
	SceneIdentity sceneId;
	
	IUserRequest userRequest;
	
	IHelloWorldScene scene;
	
	@ConfiguredValue(defaultValue = "/Gui.fxml")
	String urlStr;
	
	// IUserNotification
	@Override
	public void notifyMessage(UserSession session, Message message) {
		this.scene.getOutput().setText(session, message.asPrimitive());
	}

	// --------- AbstractGuiHandler ---------
	@Override
	public void notifyReady() {
		URL rootUrl = this.getClass().getResource("/hello");
		this.getGuiRequest().createStage(this.stageId, false, rootUrl);
	}

	protected void onStageCreated(GuiEvent event) {
		URL content = this.getClass().getResource(this.urlStr);
		scene = this.guiRequest.createScene(this.stageId, this.sceneId, IHelloWorldScene.class, content);
	}
	
	protected void onSceneLoaded(GuiEvent event) {
		this.userRequest.requestStart(event.getSession());
	}

	protected IGuiScene getScene(SceneIdentity sceneId) {
		return this.scene;
	}
	

}
