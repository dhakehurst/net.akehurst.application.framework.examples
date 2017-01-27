package net.akehurst.example.flightSimulator.computational;

import net.akehurst.application.framework.common.annotations.declaration.ExternalConnection;
import net.akehurst.application.framework.realisation.AbstractActiveSignalProcessingObject;
import net.akehurst.example.flightSimulator.computational.engineInterface.EngineThrottlePosition;
import net.akehurst.example.flightSimulator.computational.engineInterface.IEngineRequest;
import net.akehurst.example.flightSimulator.computational.flightSurfaceInterface.AileronPosition;
import net.akehurst.example.flightSimulator.computational.flightSurfaceInterface.ElevatorPosition;
import net.akehurst.example.flightSimulator.computational.flightSurfaceInterface.IFlightSurfaceRequest;
import net.akehurst.example.flightSimulator.computational.flightSurfaceInterface.RudderPosition;
import net.akehurst.example.flightSimulator.computational.pilotInterface.ElevationRate;
import net.akehurst.example.flightSimulator.computational.pilotInterface.EngineThrust;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotRequest;
import net.akehurst.example.flightSimulator.computational.pilotInterface.RollRate;
import net.akehurst.example.flightSimulator.computational.pilotInterface.YawRate;

public class PilotRequestHandler extends AbstractActiveSignalProcessingObject implements IPilotRequest {

	public PilotRequestHandler(final String afId) {
		super(afId);
	}

	@ExternalConnection
	public IPilotNotification pilotNotification;

	@ExternalConnection
	public IFlightSurfaceRequest flightSurfaceRequest;

	@ExternalConnection
	public IEngineRequest engineRequest;

	// ---------- IPilotRequest ----------
	@Override
	public void requestElevationRate(final ElevationRate value) {
		final ElevatorPosition pos = new ElevatorPosition(value.asPrimitive());
		this.flightSurfaceRequest.requestElevatorPosition(pos);

	}

	@Override
	public void requestRollRate(final RollRate value) {
		final AileronPosition pos = new AileronPosition(value.asPrimitive());
		this.flightSurfaceRequest.requestAileronPosition(pos);
	}

	@Override
	public void requestYawRate(final YawRate value) {
		final RudderPosition pos = new RudderPosition(value.asPrimitive());
		this.flightSurfaceRequest.requestRudderPosition(pos);

	}

	@Override
	public void requestEngineThrust(final EngineThrust value) {
		// TODO: This simply uses the value of the Thrust request as the
		// required throttle position
		// a more realistic calculation would be nice!
		final EngineThrottlePosition pos = new EngineThrottlePosition(value.asPrimitive());
		this.engineRequest.requestPosition(pos);
	}
}
