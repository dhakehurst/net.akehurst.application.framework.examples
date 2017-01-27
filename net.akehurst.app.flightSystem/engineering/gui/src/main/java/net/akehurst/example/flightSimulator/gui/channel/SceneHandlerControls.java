package net.akehurst.example.flightSimulator.gui.channel;

import java.net.URL;

import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.realisation.AbstractIdentifiableObject;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;

public class SceneHandlerControls extends AbstractIdentifiableObject implements IGuiSceneHandler {

	public SceneHandlerControls(final String afId) {
		super(afId);
		// TODO Auto-generated constructor stub
	}

	@ConfiguredValue(defaultValue = "controls")
	SceneIdentity sceneIdControls;

	IControlsScene scene;

	@Override
	public IGuiScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, this.sceneIdControls, IControlsScene.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		// TODO Auto-generated method stub

	}

}
