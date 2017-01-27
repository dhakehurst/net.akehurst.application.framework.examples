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

import net.akehurst.example.flightSimulator.computational.engineInterface.EngineSpeed;
import net.akehurst.example.flightSimulator.computational.engineInterface.IEngineNotification;
import net.akehurst.example.flightSimulator.computational.engineInterface.IEngineSpeedSensor;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AirPressureSensorSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AirSpeedSensorSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorHeadingSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorPitchSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorRollSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorYawSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.IAirPressureSensor;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.IAirSpeedSensor;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.IAttitudeSensor;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.IRadarAltitudeSensor;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.ISensorsNotification;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.RadarAltitudeSensorSample;


public class SensorSampler implements Runnable {

	public SensorSampler(double sensingRatePerSecond) {
		this.sensingRatePerSecond = sensingRatePerSecond;
	}

	double sensingRatePerSecond;

	IEngineSpeedSensor engineSpeedSensor;
	public IEngineSpeedSensor getEngineSpeedSensor() {
		return engineSpeedSensor;
	}
	public void setEngineSpeedSensor(IEngineSpeedSensor value) {
		this.engineSpeedSensor = value;
	}
	
	IAirSpeedSensor airSpeedSensor;
	public IAirSpeedSensor getAirSpeedSensor() {
		return airSpeedSensor;
	}
	public void setAirSpeedSensor(IAirSpeedSensor value) {
		this.airSpeedSensor = value;
	}
	
	IRadarAltitudeSensor radarAltitudeSensor;
	public IRadarAltitudeSensor getRadarAltitudeSensor() {
		return radarAltitudeSensor;
	}
	public void setRadarAltitudeSensor(IRadarAltitudeSensor value) {
		this.radarAltitudeSensor = value;
	}
	
	IAirPressureSensor airPressureSensor;
	public IAirPressureSensor getAirPressureSensor() {
		return airPressureSensor;
	}
	public void setAirPressureSensor(IAirPressureSensor value) {
		this.airPressureSensor = value;
	}
	
	IAttitudeSensor attitudeSensor;
	public IAttitudeSensor getAttitudeSensor() {
		return attitudeSensor;
	}
	public void setAttitudeSensor(IAttitudeSensor value) {
		this.attitudeSensor = value;
	}
	
	ISensorsNotification sensorNotification;
	public ISensorsNotification getSensorNotification() {
		return sensorNotification;
	}
	public void setSensorNotification(ISensorsNotification value) {
		this.sensorNotification = value;
	}

	IEngineNotification engineNotification;
	public IEngineNotification getEngineNotification() {
		return engineNotification;
	}
	public void setEngineNotification(IEngineNotification value) {
		this.engineNotification = value;
	}
	
	// ---------------- Runnable --------------------
	public void run() {
		try {
			while (true) {

				EngineSpeed engineSpeedSample = this.getEngineSpeedSensor().readValue();
				this.getEngineNotification().notifyEnginePower(engineSpeedSample);
				
				AirSpeedSensorSample speedSample = this.getAirSpeedSensor().readValue();
				this.getSensorNotification().notifyAirSpeed(speedSample);

				RadarAltitudeSensorSample altitudeSample = this.getRadarAltitudeSensor().readValue();
				this.getSensorNotification().notifyRadarAltitude(altitudeSample);
				
				AirPressureSensorSample pressureSample = this.getAirPressureSensor().readValue();
				this.getSensorNotification().notifyAirPressure(pressureSample);
				
				AttitudeSensorHeadingSample headingSample = this.getAttitudeSensor().readHeadingValue();
				AttitudeSensorRollSample rollSample = this.getAttitudeSensor().readRollValue();
				AttitudeSensorPitchSample pitchSample = this.getAttitudeSensor().readPitchValue();			
				AttitudeSensorYawSample yawSample = this.getAttitudeSensor().readYawValue();
				this.getSensorNotification().notifyHeading(headingSample);
				this.getSensorNotification().notifyRoll(rollSample);
				this.getSensorNotification().notifyPitch(pitchSample);
				this.getSensorNotification().notifyYaw(yawSample);
				
				Thread.sleep((long) (1000.0 / this.sensingRatePerSecond));
			}
		} catch (InterruptedException e) {
			// ok thread interrupted
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	Thread thread;

	public void start() {
		this.thread = new Thread(this, this.getClass().getSimpleName());
		this.thread.start();
	}

	public void join() throws InterruptedException {
		this.thread.join();
	}

}
