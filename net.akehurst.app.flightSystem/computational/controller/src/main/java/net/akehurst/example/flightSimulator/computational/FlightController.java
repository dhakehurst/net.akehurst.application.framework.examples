/*
 * Copyright (c) 2015 D. David H. Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.akehurst.example.flightSimulator.computational;

import net.akehurst.application.framework.common.IPort;
import net.akehurst.application.framework.common.annotations.declaration.Component;
import net.akehurst.application.framework.common.annotations.instance.ActiveObjectInstance;
import net.akehurst.application.framework.common.annotations.instance.PortContract;
import net.akehurst.application.framework.common.annotations.instance.PortInstance;
import net.akehurst.application.framework.realisation.AbstractComponent;
import net.akehurst.example.flightSimulator.computational.engineInterface.IEngineNotification;
import net.akehurst.example.flightSimulator.computational.engineInterface.IEngineRequest;
import net.akehurst.example.flightSimulator.computational.flightSurfaceInterface.IFlightSurfaceRequest;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotNotification;
import net.akehurst.example.flightSimulator.computational.pilotInterface.IPilotRequest;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.ISensorsNotification;

@Component
public class FlightController extends AbstractComponent {

	public FlightController(final String afId) {
		super(afId);
	}

	@ActiveObjectInstance
	PilotRequestHandler pilotRequestHandler;

	@ActiveObjectInstance
	SensorInformationHandler sensorInformationHandler;

	@Override
	public void afConnectParts() {
		this.portPilot().connectInternal(this.pilotRequestHandler);
		this.portPilot().connectInternal(this.sensorInformationHandler);
		this.portPlane().connectInternal(this.pilotRequestHandler);
		this.portPlane().connectInternal(this.sensorInformationHandler);

	}

	@PortInstance
	@PortContract(provides = IPilotRequest.class, requires = IPilotNotification.class)
	private IPort portPilot;

	public IPort portPilot() {
		return this.portPilot;
	}

	@PortInstance
	@PortContract(provides = IFlightSurfaceRequest.class)
	@PortContract(requires = ISensorsNotification.class)
	@PortContract(provides = IEngineRequest.class, requires = IEngineNotification.class)
	private IPort portPlane;

	public IPort portPlane() {
		return this.portPlane;
	}
}
