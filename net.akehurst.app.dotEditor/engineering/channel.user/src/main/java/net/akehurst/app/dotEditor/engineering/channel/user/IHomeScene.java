package net.akehurst.app.dotEditor.engineering.channel.user;

import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.data.editor.IGuiEditor;
import net.akehurst.application.framework.technology.interfaceGui.data.graph.IGuiGraphViewer;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiElement;
import net.akehurst.application.framework.technology.interfaceGui.elements.IGuiText;

public interface IHomeScene extends IGuiScene {

	IGuiText getTextUsername();

	IGuiElement getActionSignOut();

	IGuiEditor getEditor();

	IGuiGraphViewer getGraph();
}
