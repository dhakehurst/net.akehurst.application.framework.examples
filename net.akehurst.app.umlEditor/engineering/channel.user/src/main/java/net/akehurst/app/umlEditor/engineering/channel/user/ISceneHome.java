package net.akehurst.app.umlEditor.engineering.channel.user;

import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.data.table.IGuiTable;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiElement;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiText;

public interface ISceneHome extends IGuiScene {

	IGuiText getTextUsername();

	IGuiElement getActionSignOut();

	IGuiTable getTableProjects();

	IGuiElement getActionSelectProject();

}
