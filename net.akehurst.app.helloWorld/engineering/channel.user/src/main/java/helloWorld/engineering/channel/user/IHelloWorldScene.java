package helloWorld.engineering.channel.user;

import net.akehurst.application.framework.technology.guiInterface.IGuiScene;
import net.akehurst.application.framework.technology.guiInterface.controls.IText;

public interface IHelloWorldScene extends IGuiScene {


	IText getOutput();
	
}
