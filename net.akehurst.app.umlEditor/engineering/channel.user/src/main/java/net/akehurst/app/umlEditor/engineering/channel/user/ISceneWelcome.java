package net.akehurst.app.umlEditor.engineering.channel.user;

import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiElement;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiText;

public interface ISceneWelcome extends IGuiScene {

	IGuiText getWelcomeMessage();

	IGuiElement getActionSignIn();
}
