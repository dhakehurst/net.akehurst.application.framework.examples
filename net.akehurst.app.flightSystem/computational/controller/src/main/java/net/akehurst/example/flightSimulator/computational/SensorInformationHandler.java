package net.akehurst.example.flightSimulator.computational;

import net.akehurst.application.framework.common.annotations.declaration.ExternalConnection;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;
import net.akehurst.example.flightSimulator.computational.pilotInterface.AirSpeed;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Elevation;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Heading;
import net.akehurst.example.flightSimulator.computational.pilotInterface.HeightAboveSeaLevel;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Roll;
import net.akehurst.example.flightSimulator.computational.pilotInterface.Yaw;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AirPressureSensorSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AirSpeedSensorSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorHeadingSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorPitchSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorRollSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorYawSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.ISensorsNotification;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.RadarAltitudeSensorSample;

public class SensorInformationHandler extends AbstractActiveSignalProcessingObject implements ISensorsNotification {

	public SensorInformationHandler(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IPilotNotification pilotNotification;

	// ---------- ISensorsNotification ---------
	@Override
	public void notifyAirSpeed(final AirSpeedSensorSample value) {
		final AirSpeed v = new AirSpeed(value.asPrimitive());
		this.pilotNotification.notifyCurrentAirSpeed(v);
	}

	@Override
	public void notifyAirPressure(final AirPressureSensorSample value) {
		// TODO: convert air pressure into height above sea level!
		final double d = value.asPrimitive();
		final HeightAboveSeaLevel v = new HeightAboveSeaLevel(d);
		this.pilotNotification.notifyCurrentHeightAboveSeaLevel(v);
	}

	@Override
	public void notifyRoll(final AttitudeSensorRollSample value) {
		final Roll v = new Roll(value.asPrimitive());
		this.pilotNotification.notifyCurrentRoll(v);
	}

	@Override
	public void notifyPitch(final AttitudeSensorPitchSample value) {
		final Elevation v = new Elevation(value.asPrimitive());
		this.pilotNotification.notifyCurrentElevation(v);
	}

	@Override
	public void notifyYaw(final AttitudeSensorYawSample value) {
		final Yaw v = new Yaw(value.asPrimitive());
		this.pilotNotification.notifyCurrentYaw(v);
	}

	@Override
	public void notifyHeading(final AttitudeSensorHeadingSample value) {
		final Heading h = new Heading(value.asPrimitive());
		this.pilotNotification.notifyCurrentHeading(h);
	}

	@Override
	public void notifyRadarAltitude(final RadarAltitudeSensorSample value) {
		final HeightAboveSeaLevel alt = new HeightAboveSeaLevel(value.asPrimitive());
		this.pilotNotification.notifyCurrentHeightAboveSeaLevel(alt);
	}

}
