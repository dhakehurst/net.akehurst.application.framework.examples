package helloWorld.engineering.channel.user;

import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiText;

public interface IHelloWorldScene extends IGuiScene {

	IGuiText getOutput();

}
