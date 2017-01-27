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
package net.akehurst.example.flightSimulator.hardware.actuators;

import java.nio.ByteBuffer;

import net.akehurst.example.flightSimulator.hardware.IHardwareRegister;
import net.akehurst.example.flightSimulator.simulation.BankRate;
import net.akehurst.example.flightSimulator.simulation.EnvironmentReference;
import net.akehurst.example.flightSimulator.simulation.Speed;


public class EngineThrottleActuator implements IHardwareRegister {

	public EngineThrottleActuator() {
		this.environment = new EnvironmentReference();
		this.currentValue = new byte[8];
	}
	
	byte[] currentValue;
	
	EnvironmentReference environment;
	
	@Override
	public byte[] readValue() {
		return this.currentValue;
	}

	@Override
	public void writeValue(byte[] bytes) {
		this.currentValue = bytes;
		this.updateSimulation(bytes);
	}

//	// ---------- IEngineRequest ----------
//	@Override
//	public void requestPosition(EngineThrottlePosition value) {
//		// TODO: Engine power currently simulated as direct correlation to throttle position
//		// a better calculation would be nice
//		this.currentEngineSpeed = new EngineSpeed(value.asPrimitive()*100);
//		this.getNotification().notifyEnginePower(this.currentEngineSpeed);
//		//TODO: be nice to have a proper relationship between engine speed and air speed
//		// airSpeed = airSpeed.Max / engineSpeed.Max * engineSpeed
//		double asp = (AirSpeed.MAX / EngineSpeed.MAX) * this.currentEngineSpeed.asPrimitive();
//		this.environment.setAirSpeed( new Speed(asp) );
//	}
	
	void updateSimulation(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		double v = bb.getDouble();
		
		this.environment.setEngineThrottlePosition(v);
	}
	
}
