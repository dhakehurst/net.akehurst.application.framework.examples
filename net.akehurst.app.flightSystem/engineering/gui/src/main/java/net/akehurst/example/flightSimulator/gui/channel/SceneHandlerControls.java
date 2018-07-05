package net.akehurst.example.flightSimulator.gui.channel;

import java.net.URL;

import net.akehurst.application.framework.common.annotations.instance.ConfiguredValue;
import net.akehurst.application.framework.common.interfaceUser.UserSession;
import net.akehurst.application.framework.realisation.AbstractIdentifiableObject;
import net.akehurst.application.framework.technology.interfaceGui.GuiEvent;
import net.akehurst.application.framework.technology.interfaceGui.IGuiHandler;
import net.akehurst.application.framework.technology.interfaceGui.IGuiScene;
import net.akehurst.application.framework.technology.interfaceGui.IGuiSceneHandler;
import net.akehurst.application.framework.technology.interfaceGui.SceneIdentity;
import net.akehurst.application.framework.technology.interfaceGui.StageIdentity;
import net.akehurst.example.flightSimulator.computational.pilotInterface.ElevationRate;
import net.akehurst.example.flightSimulator.computational.pilotInterface.EngineThrust;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotRequest;
import net.akehurst.example.flightSimulator.computational.pilotInterface.RollRate;

public class SceneHandlerControls extends AbstractIdentifiableObject implements IGuiSceneHandler {

	public SceneHandlerControls(final String afId) {
		super(afId);
		// TODO Auto-generated constructor stub
	}

	// TODO: fix this properly
	UserSession session;

	IPilotRequest pilotRequest;

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
		// TODO: what is event name from thrust leaver?
		this.scene.getThrotle().onEvent(this.session, "", (e) -> {
			// TODO: get data from event args
			final double value = 0;
			final EngineThrust thrust = new EngineThrust(value);
			this.pilotRequest.requestEngineThrust(thrust);
		});

		// TODO: what is event name from joystick?
		this.scene.getJoystick().onEvent(this.session, "", (e) -> {
			// TODO: get data from event args

			final ElevationRate elValue;
			this.pilotRequest.requestElevationRate(elValue);

			final RollRate rlValue;
			this.pilotRequest.requestRollRate(rlValue);
		});
	}

}
