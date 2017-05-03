package net.akehurst.app.umlEditor.engineering.channel.user;

import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.data.diagram.IGuiDiagram;
import net.akehurst.application.framework.technology.interfaceGui.data.editor.IGuiEditor;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiElement;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiText;

public interface ISceneProject extends IGuiScene {
	IGuiText getTextUsername();

	IGuiElement getActionSignOut();

	IGuiEditor getEditor();

	IGuiDiagram getDiagram();
}
