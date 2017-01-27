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
package net.akehurst.example.flightSimulator.engineering.commsChannel.hardware;

import java.nio.ByteBuffer;

import net.akehurst.example.flightSimulator.computational.engineInterface.EngineSpeed;
import net.akehurst.example.flightSimulator.computational.engineInterface.IEngineSpeedSensor;
import net.akehurst.example.flightSimulator.hardware.IHardwareRegister;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EngineSensorProxy implements IEngineSpeedSensor {

	Logger log;
	
	public EngineSensorProxy() {
		this.log = LoggerFactory.getLogger(this.getClass());
	}
	
	IHardwareRegister engineSpeedSensor;
	public IHardwareRegister getHwRegister() {
		return engineSpeedSensor;
	}
	public void setHwRegister(IHardwareRegister value) {
		this.engineSpeedSensor = value;
	}
	
	// --------- IEngineSpeedSensor ---------
	@Override
	public EngineSpeed readValue() {

		// read the hardware register
		byte[] bytes = this.getHwRegister().readValue();
		
		//convert the read value into a double
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		double hwValue = bb.getDouble();
		
		//convert the value into an engine speed sample
		double value = hwValue; //this needs a calculation.
		
		EngineSpeed sample = new EngineSpeed(value);
		return sample;
	}
}
