package helloWorld.engineering.channel.user;

import java.net.URL;

import helloWorld.computational.interfaceUser.IUserNotification;
import helloWorld.computational.interfaceUser.IUserRequest;
import helloWorld.computational.interfaceUser.Message;
import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;
import net.akehurst.application.framework.technology.authentication.TechSession;
import net.akehurst.application.framework.technology.gui.common.AbstractGuiHandler;
import net.akehurst.application.framework.technology.guiInterface.GuiEvent;
import net.akehurst.application.framework.technology.guiInterface.IGuiCallback;
import net.akehurst.application.framework.technology.guiInterface.IGuiNotification;
import net.akehurst.application.framework.technology.guiInterface.IGuiRequest;
import net.akehurst.application.framework.technology.guiInterface.IGuiScene;

public class HelloWorldHandler extends AbstractGuiHandler implements IUserNotification {

	public HelloWorldHandler(String id) {
		super(id);
	}

	IUserRequest userRequest;
	
	IHelloWorldScene scene;
	
	@ConfiguredValue(defaultValue = "/Gui.fxml")
	String urlStr;
	
	// IUserNotification
	@Override
	public void notifyMessage(Message message) {
		this.scene.getOutput().setText(message.asPrimitive());
	}

	// --------- AbstractGuiHandler ---------
	@Override
	public void notifyReady() {
		URL rootUrl = this.getClass().getResource("/hello");
		this.getGuiRequest().createStage("/helloWorld", false, rootUrl);
	}

	protected void onStageCreated(GuiEvent event) {
		URL content = this.getClass().getResource(this.urlStr);
		scene = this.guiRequest.createScene("HelloWorld", "mainWindow", IHelloWorldScene.class, content);
	}
	
	protected void onSceneLoaded(GuiEvent event) {
		this.userRequest.requestStart();
	}

	protected IGuiScene getScene(String sceneId) {
		return this.scene;
	}
	

}
