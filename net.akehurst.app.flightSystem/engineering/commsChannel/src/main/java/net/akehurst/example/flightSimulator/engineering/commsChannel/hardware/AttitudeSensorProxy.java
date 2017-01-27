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

import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorHeadingSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorPitchSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorRollSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.AttitudeSensorYawSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.IAttitudeSensor;
import net.akehurst.example.flightSimulator.hardware.IHardwareRegister;

public class AttitudeSensorProxy implements IAttitudeSensor {

	public AttitudeSensorProxy() {
	}
	
	/**
	 * The Attitude Sensor (Attitude and Heading Reference System - AHRS)
	 * puts 4 double values (each 8 bytes) into the hardware Register.
	 * heading, yaw, pitch, roll.
	 * 
	 */
	IHardwareRegister hwRegister;
	public IHardwareRegister getHwRegister() {
		return hwRegister;
	}
	public void setHwRegister(IHardwareRegister hwRegister) {
		this.hwRegister = hwRegister;
	}
	
	
	// ---------- IAttitudeSensor ----------
	@Override
	public AttitudeSensorHeadingSample readHeadingValue() {
		// read the hardware register
		byte[] bytes = this.getHwRegister().readValue();
		
		//convert the read value into a double
		ByteBuffer bb = ByteBuffer.wrap(bytes,0,8);
		double hwValue = bb.getDouble();
		
		//convert the value into an air speed sample
		double value = hwValue; //this needs a calculation.
		
		AttitudeSensorHeadingSample sample = new AttitudeSensorHeadingSample(value);
		return sample;
	}
	
	@Override
	public AttitudeSensorYawSample readYawValue() {
		// read the hardware register
		byte[] bytes = this.getHwRegister().readValue();
		
		//convert the read value into a double
		ByteBuffer bb = ByteBuffer.wrap(bytes,8,8);
		double hwValue = bb.getDouble();
		
		//convert the value into an air speed sample
		double value = hwValue; //this needs a calculation.
		
		AttitudeSensorYawSample sample = new AttitudeSensorYawSample(value);
		return sample;
	}
	
	@Override
	public AttitudeSensorPitchSample readPitchValue() {
		// read the hardware register
		byte[] bytes = this.getHwRegister().readValue();
		
		//convert the read value into a double
		ByteBuffer bb = ByteBuffer.wrap(bytes,16,8);
		double hwValue = bb.getDouble();
		
		//convert the value into an air speed sample
		double value = hwValue; //this needs a calculation.
		
		AttitudeSensorPitchSample sample = new AttitudeSensorPitchSample(value);
		return sample;
	}

	@Override
	public AttitudeSensorRollSample readRollValue() {
		// read the hardware register
		byte[] bytes = this.getHwRegister().readValue();
		
		//convert the read value into a double
		ByteBuffer bb = ByteBuffer.wrap(bytes,24,8);
		double hwValue = bb.getDouble();
		
		//convert the value into an air speed sample
		double value = hwValue; //this needs a calculation.
		
		AttitudeSensorRollSample sample = new AttitudeSensorRollSample(value);
		return sample;
	}
	
}
