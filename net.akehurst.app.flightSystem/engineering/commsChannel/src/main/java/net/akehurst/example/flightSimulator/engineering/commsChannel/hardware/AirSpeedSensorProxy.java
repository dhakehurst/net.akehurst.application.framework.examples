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

import net.akehurst.example.flightSimulator.computational.sensorsInterface.AirSpeedSensorSample;
import net.akehurst.example.flightSimulator.computational.sensorsInterface.IAirSpeedSensor;
import net.akehurst.example.flightSimulator.hardware.IHardwareRegister;

public class AirSpeedSensorProxy implements IAirSpeedSensor {

	public AirSpeedSensorProxy() {
	}
	
	IHardwareRegister hwRegister;
	public IHardwareRegister getHwRegister() {
		return hwRegister;
	}
	public void setHwRegister(IHardwareRegister hwRegister) {
		this.hwRegister = hwRegister;
	}
	
	
	// ---------- IAirSpeedSensor ----------
	@Override
	public AirSpeedSensorSample readValue() {

		// read the hardware register
		// returns an array of 8 bytes that encode a double value
		// the value is in the range 1.0 to 5.0 volts.
		// this represents an air speed 0-500 m/s.
		 
		byte[] bytes = this.getHwRegister().readValue();
		
		//convert the read value into a double
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		double hwValue = bb.getDouble();
		
		//convert the value into an air speed sample
		double value = (hwValue-1) * (500/4); //this needs a calculation.
		
		AirSpeedSensorSample sample = new AirSpeedSensorSample(value);
		return sample;
	}
	
	
}
