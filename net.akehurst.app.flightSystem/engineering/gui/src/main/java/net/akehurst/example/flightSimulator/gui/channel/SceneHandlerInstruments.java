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
import net.akehurst.example.flightSimulator.computational.pilotInterface.AirSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Elevation;
import net.akehurst.example.flightSimulator.computational.pilotInterface.GroundSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Heading;
import net.akehurst.example.flightSimulator.computational.pilotInterface.HeightAboveSeaLevel;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Roll;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Yaw;
import net.akehurst.example.maths.AngleDegrees;

public class SceneHandlerInstruments extends AbstractIdentifiableObject implements IPilotNotification, IGuiSceneHandler {

	public SceneHandlerInstruments(final String afId) {
		super(afId);
	}

	// TODO: fix this properly
	UserSession session;

	@ConfiguredValue(defaultValue = "instruments")
	SceneIdentity sceneIdInstruments;

	IInstrumentScene scene;

	@Override
	public IGuiScene createScene(final IGuiHandler gui, final StageIdentity stageId, final URL content) {
		this.scene = gui.createScene(stageId, this.sceneIdInstruments, IInstrumentScene.class, this, content);
		return this.scene;
	}

	@Override
	public void loaded(final IGuiHandler gui, final IGuiScene guiScene, final GuiEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyCurrentHeightAboveSeaLevel(final HeightAboveSeaLevel value) {
		this.scene.getAltimeter().set(this.session, "value", value.asPrimitive());
	}

	@Override
	public void notifyCurrentHeading(final Heading value) {
		this.scene.getAirCompass().set(this.session, "bearing", value.asDegrees().asPrimitive());
	}

	@Override
	public void notifyCurrentElevation(final Elevation value) {
		final AngleDegrees deg = value.asDegrees();
		final double d = deg.asPrimitive();
		this.scene.getHorizon().set(this.session, "pitch", d);
		final boolean upsidedown = d > 90 && d < 270;
	}

	@Override
	public void notifyCurrentRoll(final Roll value) {
		final AngleDegrees deg = value.asDegrees();
		final double d = deg.asPrimitive();
		this.scene.getHorizon().set(this.session, "roll", 360 - d);
	}

	@Override
	public void notifyCurrentYaw(final Yaw value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyCurrentAirSpeed(final AirSpeed value) {
		this.scene.getAirSpeed().set(this.session, "value", value.asPrimitive());
	}

	@Override
	public void notifyCurrentGroundSpeed(final GroundSpeed value) {
		// TODO Auto-generated method stub

	}

}
