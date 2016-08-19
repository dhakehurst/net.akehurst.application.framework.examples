package helloWorld.engineering.channel.user;

import net.akehurst.application.framework.technology.guiInterface.IGuiScene;
import net.akehurst.application.framework.technology.guiInterface.elements.IGuiText;

public interface IHelloWorldScene extends IGuiScene {

	IGuiText getOutput();

}
