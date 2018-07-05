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
package net.akehurst.example.flightSimulator.application;

import net.akehurst.application.framework.common.annotations.instance.ComponentInstance;
import net.akehurst.application.framework.common.annotations.instance.ServiceInstance;
import net.akehurst.application.framework.realisation.AbstractApplication;
import net.akehurst.application.framework.service.configuration.file.HJsonConfigurationService;
import net.akehurst.application.framework.technology.filesystem.StandardFilesystem;
import net.akehurst.application.framework.technology.gui.jfx.JfxWindow;
import net.akehurst.application.framework.technology.log4j.Log4JLogger;
import net.akehurst.example.flightSimulator.computational.FlightController;
import net.akehurst.example.flightSimulator.gui.channel.PilotToGui;
import net.akehurst.example.flightSimulator.technology.network.SimulatedNetworkBus;

public class FlightSystemApplication extends AbstractApplication {

	final double SENSING_RATE = 10;

	public FlightSystemApplication(final String id) {
		super(id);

		// --- Connections ---
		// this.flightController.setEngineRequest(this.flightControllerEngineRequestPublisher);
		// this.flightController.setFlightSurfaceRequest(this.flightControllerFlightSurfaceRequestPublisher);
		// this.flightController.setPilotNotification(this.flightControllerPilotNotificationPublisher);
		// this.flightControllerPilotRequestSubscriber.setPilotRequest(this.flightController);
		// this.flightControllerSensorsNotificationSubscriber.setSensorsNotification(this.flightController);
		// this.flightControllerPilotRequestSubscriber.setNetwork(this.network);
		// this.flightControllerEngineRequestPublisher.setNetwork(this.network);
		// this.flightControllerFlightSurfaceRequestPublisher.setNetwork(this.network);
		// this.flightControllerSensorsNotificationSubscriber.setNetwork(this.network);
		// this.flightControllerPilotNotificationPublisher.setNetwork(this.network);
		//
		// this.sensorSampler.setEngineNotification(this.enginePublisher);
		// this.sensorSampler.setSensorNotification(this.sensorsPublisher);
		// this.sensorSampler.setAirSpeedSensor(this.airSpeedSensorProxy);
		// this.sensorSampler.setAirPressureSensor(this.airPressureSensorProxy);
		// this.sensorSampler.setAttitudeSensor(this.attitudeSensorProxy);
		// this.sensorSampler.setRadarAltitudeSensor(this.radarAltitudeSensorProxy);
		// this.sensorSampler.setEngineSpeedSensor(this.engineSensorProxy);
		//
		// this.controls.setRequest(this.controlsPilotRequestPublisher);
		// this.controlsPilotRequestPublisher.setNetwork( this.network );
		//
		// this.enginePublisher.setNetwork(this.network);
		//
		// this.instrumentPanelEngineNotificationSubscriber.setEngineNotification(this.instrumentPanel);
		// this.instrumentPanelEngineNotificationSubscriber.setNetwork(this.network);
		// this.instrumentPanelPilotNotificationSubscriber.setPilotNotification(this.instrumentPanel);
		// this.instrumentPanelPilotNotificationSubscriber.setNetwork(this.network);
		//
		// this.engineSensorProxy.setHwRegister(this.engineSpeedSensor);
		// this.airSpeedSensorProxy.setHwRegister(this.airSpeedSensor);
		// this.airPressureSensorProxy.setHwRegister(this.airPressureSensor);
		// this.radarAltitudeSensorProxy.setHwRegister(this.radarAltitudeSensor);
		// this.attitudeSensorProxy.setHwRegister(this.attitudeSensor);
		// this.sensorsPublisher.setNetwork(this.network);
		//
		// this.flightSurfaceProxy.setAileronActuator(this.aileronActuator);
		// this.flightSurfaceProxy.setElevatorActuator(this.elevatorActuator);
		// this.flightSurfaceProxy.setNetwork(this.network);
		//
		// this.engineRequestProxy.setEngineThrottleActuator(this.engineThrottleActuator);
		// this.engineRequestProxy.setNetwork(this.network);
	}

	@ServiceInstance
	StandardFilesystem fs;

	@ServiceInstance
	HJsonConfigurationService configuration;

	@ServiceInstance
	Log4JLogger logger;

	// --- Computational ---
	@ComponentInstance
	FlightController flightController;
	//
	// @ComponentInstance
	// SensorSampler sensorSampler;
	//

	//
	// // --- Engineering ---
	PilotRequestPublisher controlsPilotRequestPublisher;
	// PilotRequestSubscriber flightControllerPilotRequestSubscriber;
	//
	// EngineRequestPublisher flightControllerEngineRequestPublisher;
	// FlightSurfaceRequestPublisher flightControllerFlightSurfaceRequestPublisher;
	// SensorsNotificationSubscriber flightControllerSensorsNotificationSubscriber;
	// PilotNotificationPublisher flightControllerPilotNotificationPublisher;
	//
	//
	// EngineNotificationPublisher enginePublisher;
	//
	// EngineNotificationSubscriber instrumentPanelEngineNotificationSubscriber;
	// PilotNotificationSubscriber instrumentPanelPilotNotificationSubscriber;
	//
	// // --- Hardware Proxys ---
	// FlightSurfaceProxy flightSurfaceProxy; //does network subscribing & Proxy for h/w
	// EngineRequestProxy engineRequestProxy; //does network subscribing & Proxy for h/w
	//
	// EngineSensorProxy engineSensorProxy;
	// AirSpeedSensorProxy airSpeedSensorProxy;
	// RadarAltitudeSensorProxy radarAltitudeSensorProxy;
	// AirPressureSensorProxy airPressureSensorProxy;
	// AttitudeSensorProxy attitudeSensorProxy;
	//
	// SensorsNotificationPublisher sensorsPublisher;
	//
	// // --- Technoology ---
	SimulatedNetworkBus network;
	//
	// AirSpeedSensor airSpeedSensor;
	// RadarAltitudeSensor radarAltitudeSensor;
	// AirPressureSensor airPressureSensor;
	// AttitudeSensor attitudeSensor;
	// EngineSpeedSensor engineSpeedSensor;
	//
	// EngineThrottleActuator engineThrottleActuator;
	// AileronActuator aileronActuator;
	// ElevatorActuator elevatorActuator;

	@ComponentInstance
	PilotToGui pilotToGui;

	@ComponentInstance
	JfxWindow gui;

	@Override
	public void afConnectParts() {
		// computational <--> engineering

		// engineering <--> technology
		this.pilotToGui.portGui().connect(this.gui.portGui());
	}

}
